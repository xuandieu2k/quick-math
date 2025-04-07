package com.dhug.example.data.repository

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.dhug.example.data.local.dao.PurchasedItemDao
import com.dhug.example.data.local.entities.PurchasedItem
import com.dhug.example.domain.repository.BillingRepository
import com.dhug.example.utils.BillingUtils
import com.dhug.example.utils.MMKVUtils
import com.dhug.example.utils.PayWallConstants
import com.dhug.example.utils.SecurityUtils
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingRepositoryImpl @Inject constructor(
    private val billingClient: BillingClient,
    private val purchasedItemDao: PurchasedItemDao
) : BillingRepository {

    override fun startConnection(onConnected: () -> Unit, onError: (Throwable) -> Unit) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    onConnected()
                } else {
                    onError(Exception("Billing setup failed: ${billingResult.debugMessage}"))
                }
            }

            override fun onBillingServiceDisconnected() {
                onError(Exception("Billing service disconnected"))
            }
        })
    }

    override fun queryAvailableProducts(
        productIds: List<String>,
        productType: String,
        onResult: (List<ProductDetails>) -> Unit
    ) {
        val productParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                productIds.map {
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(it)
                        .setProductType(productType)
                        .build()
                }
            ).build()

        billingClient.queryProductDetailsAsync(productParams) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                onResult(productDetailsList)
            } else {
                onResult(emptyList())
            }
        }
    }

    override fun purchase(activity: Activity, productDetails: ProductDetails) {
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .setOfferToken(getToken(productDetails))
                        .build()
                )
            )
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    private fun getToken(productDetails: ProductDetails): String {
        val offerToken = if (productDetails.productType == BillingClient.ProductType.INAPP) {
            productDetails.oneTimePurchaseOfferDetails?.zza() ?: ""
        } else {
            productDetails.subscriptionOfferDetails?.get(0)?.offerToken ?: ""
        }
        Timber.tag("Offer Token: ").d(offerToken)
        return offerToken
    }

    override suspend fun savePurchase(purchase: Purchase, productDetails: ProductDetails) {
        when (purchase.purchaseState) {
            Purchase.PurchaseState.PURCHASED -> {
                // continue
            }

            Purchase.PurchaseState.PENDING -> {
                return
            }

            Purchase.PurchaseState.UNSPECIFIED_STATE -> {
                return
            }
        }
        val accountId = purchase.accountIdentifiers?.obfuscatedAccountId
        val profileId = purchase.accountIdentifiers?.obfuscatedProfileId

        // Lấy chu kỳ thanh toán từ ProductDetails
        val billingPeriod = BillingUtils.getBillingPeriod(productDetails)

        // Tính thời gian hết hạn
        val expiryTime = if (purchase.isAutoRenewing && billingPeriod != null) {
            BillingUtils.calculateExpiryTime(purchase.purchaseTime, billingPeriod)
        } else {
            null
        }

        val entity = PurchasedItem(
            purchaseToken = purchase.purchaseToken,
            productId = purchase.products.firstOrNull() ?: "",
            productType = if (purchase.isAutoRenewing) PayWallConstants.SUBS else PayWallConstants.INAPP,
            purchaseTime = purchase.purchaseTime,
            expiryTime = expiryTime,
            purchaseState = purchase.purchaseState,
            accountId = accountId,
            profileId = profileId,
            isAcknowledged = purchase.isAcknowledged,
            isAutoRenewing = purchase.isAutoRenewing
        )

        purchasedItemDao.insertOne(entity)
    }


    override suspend fun refreshPurchases(onDone: () -> Unit, onError: (Throwable) -> Unit) {
        val iapParams = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()


        val subsParams = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        var isHasAnyIAP = false
        // Query gói IAP
        billingClient.queryPurchasesAsync(iapParams) { billingResult, iapPurchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (iapPurchases.isNotEmpty()) {
                        handlePurchases(iapPurchases, BillingClient.ProductType.INAPP)
                        Timber.tag("RefreshPurchases")
                            .d("List iapPurchases ${iapPurchases.size}")
                        isHasAnyIAP = true
                    } else {
                        Timber.tag("RefreshPurchases")
                            .d("Empty list iapPurchases")
                    }
                }
            } else {
                Timber.tag("RefreshPurchases")
                    .e("Failed to query IAP purchases: ${billingResult.debugMessage}")
                onError(Exception("Failed to query IAP purchases: ${billingResult.debugMessage}"))
            }
        }

        // Query gói SUBS
        billingClient.queryPurchasesAsync(subsParams) { billingResult, subsPurchases ->
            MMKVUtils.clearExpiredPremiums()
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (subsPurchases.isNotEmpty()) {
                        Timber.tag("RefreshPurchases")
                            .d("List subsPurchases ${subsPurchases.size}")
                        handlePurchases(subsPurchases, BillingClient.ProductType.SUBS)
                    } else {
                        Timber.tag("RefreshPurchases")
                            .d("Empty list subsPurchases")
                        if (!isHasAnyIAP) {
                            MMKVUtils.clearAllActivePremiums()
                            Timber.tag("RefreshPurchases")
                                .d("Reset subsPurchases")
                        }
                    }
                    withContext(Dispatchers.Main){
                        onDone()
                    }
                }
            } else {
                Timber.tag("RefreshPurchases")
                    .e("Failed to query SUBS purchases: ${billingResult.debugMessage}")
                onError(Exception("Failed to query SUBS purchases: ${billingResult.debugMessage}"))
            }
        }
    }

    private fun handlePurchases(purchases: List<Purchase>, productType: String) {
        val productIds = purchases.flatMap { it.products }

        // Query thông tin chi tiết về sản phẩm
        val productDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                productIds.map { productId ->
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(productType)
                        .build()
                }
            ).build()

        billingClient.queryProductDetailsAsync(productDetailsParams) { result, productDetailsList ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                purchases.forEach { purchase ->
                    if (!SecurityUtils.isSignatureValid(purchase = purchase)) {
                        Timber.tag("Log Purchase: ").d(
                            "Signature Is Not Valid ${
                                GsonBuilder().setPrettyPrinting().create().toJson(purchase)
                            }"
                        )
                        return@queryProductDetailsAsync
                    }
                    val productDetails =
                        productDetailsList.find { it.productId == purchase.products.firstOrNull() }
                    if (productDetails != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            Timber.tag("BillingRepository")
                                .d(
                                    "Purchase: ${
                                        GsonBuilder().setPrettyPrinting().create().toJson(purchase)
                                    }"
                                )
//                            savePurchase(purchase, productDetails)
                            BillingUtils.savePremiumData(purchase, productDetails)
                        }
                    }
                }
            } else {
                Timber.tag("BillingRepository")
                    .e("Failed to query product details: ${result.debugMessage}")
            }
        }
    }


    override suspend fun queryAllProductDetails(productIds: List<String>): List<ProductDetails> {
        val iapProducts = queryProductDetails(productIds, BillingClient.ProductType.INAPP)
        val subsProducts = queryProductDetails(productIds, BillingClient.ProductType.SUBS)
        return iapProducts + subsProducts
    }

    override suspend fun queryAllProductDetails(products: Map<String, String>): List<ProductDetails> {
        Timber.tag("LOG BILLING products")
            .d(GsonBuilder().setPrettyPrinting().create().toJson(products))
        val listProduct = mutableListOf<ProductDetails>()
        for (product in products) {
            val productData = queryProductDetails(mutableListOf(product.key), product.value)
            listProduct.addAll(productData)
        }
        return listProduct
    }

    private suspend fun queryProductDetails(
        productIds: List<String>,
        productType: String
    ): List<ProductDetails> {
        Timber.tag("LOG BILLING productIds")
            .d(GsonBuilder().setPrettyPrinting().create().toJson(productIds))
        val productParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                productIds.map {
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(it)
                        .setProductType(productType)
                        .build()
                }
            ).build()

        val deferredResult = CompletableDeferred<List<ProductDetails>>()

        billingClient.queryProductDetailsAsync(productParams) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                deferredResult.complete(productDetailsList)
                Timber.tag("LOG BILLING $productType")
                    .d(GsonBuilder().setPrettyPrinting().create().toJson(productDetailsList))
            } else {
                deferredResult.complete(emptyList())
                Timber.tag("LOG BILLING $productType")
                    .d("Empty ${GsonBuilder().setPrettyPrinting().create().toJson(billingResult)}")
            }
        }

        return deferredResult.await()
    }

    override fun getAllPurchasedItems(): Flow<List<PurchasedItem>> {
        return purchasedItemDao.getAllPurchasedItems()
    }


    override fun acknowledgedPurchase(purchase: Purchase, onResult: (billingResult: BillingResult) -> Unit) {
        billingClient.acknowledgePurchase(
            AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
        ) { billingResult ->
            onResult(billingResult)
        }
    }
}

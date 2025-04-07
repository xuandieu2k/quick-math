package com.dhug.quick_math.domain.usecase

import android.app.Activity
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.dhug.quick_math.data.local.entities.PurchasedItem
import com.dhug.quick_math.domain.repository.BillingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PurchaseUseCase @Inject constructor(
    private val repository: BillingRepository
) {

    fun startConnection(onConnected: () -> Unit, onError: (Throwable) -> Unit) {
        repository.startConnection(onConnected, onError)
    }

    fun getAllPurchasedItems(): Flow<List<PurchasedItem>> {
        return repository.getAllPurchasedItems()
    }

    suspend fun refreshPurchases(onDone: () -> Unit, onError: (Throwable) -> Unit) {
        repository.refreshPurchases(onDone, onError)
    }

    suspend fun savePurchase(purchase: Purchase, productDetails: ProductDetails) {
        repository.savePurchase(purchase, productDetails)
    }

    fun getAvailableProducts(
        productIds: List<String>, productType: String, onResult: (List<ProductDetails>) -> Unit
    ) {
        repository.queryAvailableProducts(productIds, productType) {
            onResult(it)
        }
    }

    fun purchaseProduct(activity: Activity, productDetails: ProductDetails) {
        repository.purchase(activity, productDetails)
    }

    suspend fun queryAllProductDetails(productIds: List<String>): List<ProductDetails> =
        repository.queryAllProductDetails(productIds)

    suspend fun queryAllProductDetails(products: Map<String, String>): List<ProductDetails> =
        repository.queryAllProductDetails(products)


    fun acknowledgedPurchase(purchase: Purchase, onResult: (billingResult: BillingResult) -> Unit) {
        repository.acknowledgedPurchase(purchase, onResult)
    }
}

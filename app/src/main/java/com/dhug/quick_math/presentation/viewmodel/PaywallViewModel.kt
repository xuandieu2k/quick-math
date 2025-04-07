package com.dhug.quick_math.presentation.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.dhug.quick_math.R
import dagger.hilt.android.lifecycle.HiltViewModel
import com.dhug.quick_math.data.local.entities.PaywallItem
import com.dhug.quick_math.domain.usecase.PurchaseUseCase
import com.dhug.quick_math.interfaces.BillingUpdateListener
import com.dhug.quick_math.utils.AppConstants
import com.dhug.quick_math.utils.BillingUtils
import com.dhug.quick_math.utils.MMKVUtils
import com.dhug.quick_math.utils.PayWallConstants
import com.dhug.quick_math.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.collections.toMutableList
import kotlin.to


@HiltViewModel
class PaywallViewModel @Inject constructor(
    private val purchaseUseCase: PurchaseUseCase,
    private val billingUpdateListener: BillingUpdateListener,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val purchaseResult = billingUpdateListener.purchaseResultFlow
    private val _listPaywall: MutableLiveData<Resource<List<Pair<PaywallItem, ProductDetails?>>>> =
        MutableLiveData(
            Resource.Success(
                mutableListOf(
                    Pair(
                        PaywallItem(AppConstants.PaymentType.FREE_TRIAL, BigDecimal("230000")),
                        null
                    ),
                    Pair(
                        PaywallItem(
                            AppConstants.PaymentType.MONTHLY,
                            BigDecimal("230000"),
                            title = context.getString(
                                R.string.monthly
                            ),
                            subtitle = context.getString(R.string.renew_monthly)
                        ), null
                    ),
                    Pair(
                        PaywallItem(
                            AppConstants.PaymentType.LIFETIME, BigDecimal("1990000"),
                            title = context.getString(
                                R.string.lifetime
                            ), subtitle = context.getString(R.string.onetime_payment)
                        ), null
                    )
                )
            )
        )
    val listPaywall: LiveData<Resource<List<Pair<PaywallItem, ProductDetails?>>>> = _listPaywall

    private val _products = MutableLiveData<List<ProductDetails>>()
    val products: LiveData<List<ProductDetails>> = _products

    fun fetchAvailableProducts(productIds: List<String>, productType: String) {
        purchaseUseCase.getAvailableProducts(productIds, productType) { products ->
            _products.postValue(products)
        }
    }

    fun queryAllProductDetails() {
        viewModelScope.launch {
            val products = purchaseUseCase.queryAllProductDetails(BillingUtils.initMapAllProduct())
            _products.postValue(products)
        }
    }


    private fun initMapProduct(): Map<String, String> {
        val mapProduct: Map<String, String> =
            when (MMKVUtils.getRemoteConfig().isVersionPaywall) {
                PayWallConstants.VersionPaywall.WEEK.ordinal.toLong() -> {
                    mapOf(
                        PayWallConstants.WEEKLY_STANDARD to BillingClient.ProductType.SUBS,
                        PayWallConstants.MONTHLY to BillingClient.ProductType.SUBS,
                        PayWallConstants.LIFETIME to BillingClient.ProductType.INAPP,
                    )
                }

                PayWallConstants.VersionPaywall.YEARLY_NON_TRIAL.ordinal.toLong() -> {
                    mapOf(
                        PayWallConstants.MONTHLY to BillingClient.ProductType.SUBS,
                        PayWallConstants.YEARLY to BillingClient.ProductType.SUBS,
                        PayWallConstants.LIFETIME to BillingClient.ProductType.INAPP,
                    )
                }

                // include default
                else -> {
                    mapOf(
                        PayWallConstants.FREE_TRIAL to BillingClient.ProductType.SUBS,
                        PayWallConstants.MONTHLY to BillingClient.ProductType.SUBS,
                        PayWallConstants.LIFETIME to BillingClient.ProductType.INAPP,
                    )
                }
            }

        return mapProduct
    }

    fun purchase(activity: Activity, productDetails: ProductDetails) {
        purchaseUseCase.purchaseProduct(activity, productDetails)
    }


    fun getDataPaywall(): MutableList<Pair<PaywallItem, ProductDetails?>> {
        return _listPaywall.value?.data?.toMutableList() ?: mutableListOf()
    }

    fun updateListPaywall(pairList: MutableList<Pair<PaywallItem, ProductDetails>>) {
        _listPaywall.value = Resource.Success(pairList)
    }

    fun refreshPurchase() {
        viewModelScope.launch {
            purchaseUseCase.refreshPurchases(
                onDone = {
                    //
                },
                onError = {
                    //
                }
            )
        }
    }

    fun acknowledgedPurchase(purchase: Purchase, onResult: (billingResult: BillingResult) -> Unit) {
        purchaseUseCase.acknowledgedPurchase(purchase, onResult)
    }
}
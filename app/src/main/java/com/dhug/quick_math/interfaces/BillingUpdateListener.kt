package com.dhug.quick_math.interfaces

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.dhug.quick_math.data.local.entities.PurchaseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingUpdateListener @Inject constructor() : PurchasesUpdatedListener {

    private val _purchaseResultFlow = MutableStateFlow<PurchaseResult?>(null)
    val purchaseResultFlow: StateFlow<PurchaseResult?> = _purchaseResultFlow

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        _purchaseResultFlow.value = PurchaseResult.Success(purchase)
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                _purchaseResultFlow.value = PurchaseResult.Canceled
            }
            else -> {
                _purchaseResultFlow.value = PurchaseResult.Failure(
                    billingResult.responseCode,
                    billingResult.debugMessage
                )
            }
        }
    }
}

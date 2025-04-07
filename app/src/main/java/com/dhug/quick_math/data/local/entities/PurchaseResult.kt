package com.dhug.quick_math.data.local.entities

import com.android.billingclient.api.Purchase

sealed class PurchaseResult {
    data class Success(val purchase: Purchase) : PurchaseResult()
    data class Failure(val errorCode: Int, val errorMessage: String) : PurchaseResult()
    data object Canceled : PurchaseResult()
}
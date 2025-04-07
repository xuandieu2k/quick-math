package com.dhug.quick_math.domain.repository

import android.app.Activity
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.dhug.quick_math.data.local.entities.PurchasedItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface BillingRepository {
    fun startConnection(onConnected: () -> Unit, onError: (Throwable) -> Unit)

    fun queryAvailableProducts(
        productIds: List<String>,
        productType: String,
        onResult: (List<ProductDetails>) -> Unit
    )

    fun purchase(activity: Activity, productDetails: ProductDetails)

    suspend fun savePurchase(purchase: Purchase, productDetails: ProductDetails)

    suspend fun refreshPurchases(onDone: () -> Unit, onError: (Throwable) -> Unit)

    suspend fun queryAllProductDetails(productIds: List<String>): List<ProductDetails>
    suspend fun queryAllProductDetails(products: Map<String, String>): List<ProductDetails>

    fun getAllPurchasedItems(): Flow<List<PurchasedItem>>

    fun acknowledgedPurchase(purchase: Purchase, onResult: (billingResult: BillingResult) -> Unit)
}
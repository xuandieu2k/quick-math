package com.dhug.quick_math.utils

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import timber.log.Timber
import java.util.Calendar
import javax.inject.Singleton

@Singleton
object BillingUtils {
    fun initMapAllProduct(): Map<String, String> {
        val mapProduct: Map<String, String> =
            mapOf(
                PayWallConstants.FREE_TRIAL to BillingClient.ProductType.SUBS,
                PayWallConstants.WEEKLY_STANDARD to BillingClient.ProductType.SUBS,
                PayWallConstants.MONTHLY to BillingClient.ProductType.SUBS,
                PayWallConstants.YEARLY to BillingClient.ProductType.SUBS,
                PayWallConstants.LIFETIME to BillingClient.ProductType.INAPP,
            )

        return mapProduct
    }

    fun calculateExpiryTime(purchaseTime: Long, billingPeriod: String): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = purchaseTime

        when {
            billingPeriod.startsWith("P") && billingPeriod.endsWith("D") -> {
                val days = billingPeriod.removePrefix("P").removeSuffix("D").toInt()
                calendar.add(Calendar.DAY_OF_WEEK, days)
            }

            billingPeriod.startsWith("P") && billingPeriod.endsWith("W") -> {
                val weeks = billingPeriod.removePrefix("P").removeSuffix("W").toInt()
                calendar.add(Calendar.WEEK_OF_YEAR, weeks)
            }

            billingPeriod.startsWith("P") && billingPeriod.endsWith("M") -> {
                val months = billingPeriod.removePrefix("P").removeSuffix("M").toInt()
                calendar.add(Calendar.MONTH, months)
            }

            billingPeriod.startsWith("P") && billingPeriod.endsWith("Y") -> {
                val years = billingPeriod.removePrefix("P").removeSuffix("Y").toInt()
                calendar.add(Calendar.YEAR, years)
            }

            else -> throw IllegalArgumentException("Unsupported billing period: $billingPeriod")
        }

        return calendar.timeInMillis
    }


    fun getBillingPeriod(productDetails: ProductDetails): String? {
        return productDetails.subscriptionOfferDetails
            ?.get(0)?.pricingPhases?.pricingPhaseList?.get(0)?.billingPeriod
    }

    fun savePremiumData(purchase: Purchase, productDetails: ProductDetails) {
        // Lấy chu kỳ thanh toán từ ProductDetails
        val billingPeriod = getBillingPeriod(productDetails)
        var expriryTime = if (billingPeriod == null) {
            0L
        } else {
            calculateExpiryTime(
                purchase.purchaseTime,
                billingPeriod
            )
        }
        if (MMKVUtils.isLifetime(productDetails.productId)) {
            expriryTime = MMKVUtils.MAX_EXPIRY_TIME
        }
        when (purchase.purchaseState) {
            Purchase.PurchaseState.PURCHASED -> {
                Timber.tag("Save Purchase").d("PURCHASED")
                MMKVUtils.savePremiumStatus(productDetails.productId, expriryTime)
            }

            Purchase.PurchaseState.PENDING -> {
                Timber.tag("Save Purchase").d("PENDING")
                MMKVUtils.clearPremiumStatus(productDetails.productId)
            }

            Purchase.PurchaseState.UNSPECIFIED_STATE -> {
                Timber.tag("Save Purchase").d("UNSPECIFIED_STATE")
                MMKVUtils.clearPremiumStatus(productDetails.productId)
            }
        }
    }
}
package com.dhug.quick_math.utils

import com.android.billingclient.api.BillingClient
import javax.inject.Singleton

@Singleton
object PayWallConstants {

    /**
     * Lifetime - product_id: lifetime
     * Monthly- product_id: monthly
     * Yearly Freetrial_product_id: yearly_freetrial
     * Yearly Standard_product_id: yearly_standard
     * Weekly Free-trial_product_id: weekly_freetrial
     * Weekly Standard_product_id: weekly_standard
     */
    const val LIFETIME = "lifetime"
    const val WEEKLY_STANDARD = "weekly_standard"
    const val WEEKLY_FREE_TRIAL = "weekly_freetrial"
    const val MONTHLY = "monthly"
    const val YEARLY = "yearly_standard"
    const val FREE_TRIAL = "yearly_freetrial"

    /**
     * Biến cho phép config UI Paywall, trong đó: 0 = Default, 1: Promote Yearly Non-trial, 2: Promote Weekly Non-trial
     */
    enum class VersionPaywall{
        DEFAULT,
        YEARLY_NON_TRIAL,
        WEEK,
    }

    // Yearly
    const val FREE_TRIAL_TITLE = "{FREE_TRIAL_TITLE}"
    const val FREE_TRIAL_DURATION = "{FREE_TRIAL_DURATION}"
    const val FREE_TRIAL_PRICE = "{FREE_TRIAL_PRICE}"
    const val FREE_TRIAL_PERIOD = "{FREE_TRIAL_PERIOD}"

    // Monthly
    const val MONTHLY_TITLE = "{MONTHLY_TITLE}"
    const val MONTHLY_PRICE = "{MONTHLY_PRICE}"
    const val MONTHLY_PERIOD = "{MONTHLY_PERIOD}"

    // Weekly
    const val WEEKLY_TITLE = "{WEEKLY_TITLE}"
    const val WEEKLY_PRICE = "{WEEKLY_PRICE}"
    const val WEEKLY_PERIOD = "{WEEKLY_PERIOD}"

    // Lifetime
    const val LIFETIME_TITLE = "{LIFETIME_TITLE}"
    const val LIFETIME_PRICE = "{LIFETIME_PRICE}"

    // Product Type
    const val SUBS = BillingClient.ProductType.SUBS
    const val INAPP = BillingClient.ProductType.INAPP

}
package com.dhug.example.data.local.entities

import com.dhug.example.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.Locale

data class PaywallItem(
    @SerializedName("purchaseToken")
    var type: AppConstants.PaymentType = AppConstants.PaymentType.FREE_TRIAL,

    @SerializedName("price")
    var price: BigDecimal = BigDecimal.ZERO,

    @SerializedName("priceFormat")
    var priceFormat: String = "",

    @SerializedName("title")
    var title: String = "",

    @SerializedName("subtitle")
    var subtitle: String = "",

    @SerializedName("locale")
    var locale: Locale = Locale.getDefault()
)
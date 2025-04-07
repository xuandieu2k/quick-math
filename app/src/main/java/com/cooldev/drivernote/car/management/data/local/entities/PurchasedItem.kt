package com.dhug.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "purchased_items")
data class PurchasedItem(
    @SerializedName("purchaseToken")
    @PrimaryKey val purchaseToken: String,

    @SerializedName("productId")
    val productId: String,

    @SerializedName("productType")
    val productType: String,       // INAPP hoặc SUBS

    @SerializedName("purchaseTime")
    val purchaseTime: Long,        // Thời gian mua

    @SerializedName("expiryTime")
    val expiryTime: Long?,         // Thời gian hết hạn (chỉ dùng cho SUBS)

    @SerializedName("purchaseState")
    val purchaseState: Int,        // Trạng thái giao dịch

    @SerializedName("isAcknowledged")
    val isAcknowledged: Boolean,   // Giao dịch đã xác nhận chưa

    @SerializedName("isAutoRenewing")
    val isAutoRenewing: Boolean?,  // Gói có tự động gia hạn không (chỉ dùng cho SUBS)

    @SerializedName("accountId")
    val accountId: String?,        // ID tài khoản người dùng

    @SerializedName("profileId")
    val profileId: String?         // ID hồ sơ người dùng
)
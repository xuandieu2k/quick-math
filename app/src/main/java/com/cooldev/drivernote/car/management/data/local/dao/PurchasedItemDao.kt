package com.dhug.example.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dhug.example.data.local.entities.PurchasedItem
import com.dhug.example.interfaces.CRUDBaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchasedItemDao : CRUDBaseDao<PurchasedItem> {

    @Query("SELECT * FROM purchased_items")
    fun getAllPurchasedItems(): Flow<List<PurchasedItem>>

    @Query("DELETE FROM purchased_items WHERE purchaseToken = :purchaseToken")
    suspend fun deletePurchasedItem(purchaseToken: String)
}

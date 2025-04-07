package com.dhug.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dhug.example.data.local.dao.PurchasedItemDao
import com.dhug.example.data.local.entities.PurchasedItem

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 31 / 10 / 2024
 */
@Database(
    entities = [
        PurchasedItem::class
    ],
    version = 1
)

//@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun purchasedItemDao(): PurchasedItemDao
}
package com.dhug.example.interfaces

import androidx.room.*
/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 04 / 11 / 2024
 */

interface CRUDBaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(entity: T): Long

    @Update
    suspend fun update(entity: T)

    @Delete
    suspend fun delete(entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(entities: List<T>): List<Long>
}

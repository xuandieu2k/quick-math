package com.dhug.example.interfaces

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 04 / 11 / 2024
 */
interface CRUDRepositoryBase<T, ID> {
    suspend fun insert(entity: T): ID
    suspend fun update(entity: T)
    suspend fun delete(entity: T)
    suspend fun findById(id: ID): T?
    suspend fun findAll(): List<T>
    suspend fun insertMany(entities: List<T>): List<ID>
}
package com.dhug.quick_math.interfaces

import com.dhug.quick_math.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    CRUDBaseUseCase.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 12/4/25 at 09:28
 * Description: File CRUDBaseUseCase.kt created by admin - 12/4/25 at 09:28
 */
abstract class CRUDBaseUseCase<T, ID>(
    private val repository: CRUDRepositoryBase<T, ID>
) {
    fun insert(entity: T): Flow<Resource<ID>> = flow {
        emit(Resource.Loading())
        try {
            val id = repository.insert(entity)
            emit(Resource.Success(id))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to insert entity", null))
            e.printStackTrace()
        }
    }

    fun update(entity: T): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            repository.update(entity)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to update entity", null))
            e.printStackTrace()
        }
    }

    fun delete(entity: T): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            repository.delete(entity)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to delete entity", null))
            e.printStackTrace()
        }
    }

    fun findById(id: ID): Flow<Resource<T?>> = flow {
        emit(Resource.Loading())
        try {
            val result = repository.findById(id)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to find entity by ID", null))
            e.printStackTrace()
        }
    }

    fun findAll(): Flow<Resource<List<T>>> = flow {
        emit(Resource.Loading())
        try {
            val results = repository.findAll()
            emit(Resource.Success(results))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to retrieve all entities", emptyList()))
            e.printStackTrace()
        }
    }

    fun insertMany(entities: List<T>): Flow<Resource<List<ID>>> = flow {
        emit(Resource.Loading())
        try {
            val ids = repository.insertMany(entities)
            emit(Resource.Success(ids))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to insert multiple entities", null))
            e.printStackTrace()
        }
    }

}
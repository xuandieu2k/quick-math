package com.dhug.quick_math.domain.usecase

import androidx.paging.PagingData
import com.dhug.quick_math.data.local.entities.Score
import com.dhug.quick_math.domain.repository.ScoreRepository
import com.dhug.quick_math.interfaces.CRUDBaseUseCase
import com.dhug.quick_math.utils.EnumConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    ScoreUseCase.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 12/4/25 at 09:17
 * Description: File ScoreUseCase.kt created by admin - 12/4/25 at 09:17
 */
@Singleton
class ScoreUseCase constructor(private val repository: ScoreRepository) :
    CRUDBaseUseCase<Score, Long>(repository) {

    fun paging(limit: Int, type: EnumConstants.PlayType): Flow<PagingData<Score>> =
        flow {
            try {
                repository.paging(limit, type).collect { pagingData ->
                    emit(pagingData)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    fun getHighestScore(): Flow<Int> = flow {
        try {
            repository.getHighestScore()
                .collect { data ->
                    emit(data)
                }
        } catch (e: Exception) {
            emit(0)
            e.printStackTrace()
        }
    }
}
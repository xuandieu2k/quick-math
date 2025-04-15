package com.dhug.quick_math.domain.repository

import androidx.paging.PagingData
import com.dhug.quick_math.data.local.entities.Score
import com.dhug.quick_math.interfaces.CRUDRepositoryBase
import com.dhug.quick_math.utils.EnumConstants
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface ScoreRepository : CRUDRepositoryBase<Score, Long> {

    fun paging(limit: Int, type: EnumConstants.PlayType): Flow<PagingData<Score>>

    fun getHighestScore(): Flow<Int>
}
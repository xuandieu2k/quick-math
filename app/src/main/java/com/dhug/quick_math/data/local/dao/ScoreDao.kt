package com.dhug.quick_math.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.dhug.quick_math.data.local.entities.Score
import com.dhug.quick_math.interfaces.CRUDBaseDao
import com.dhug.quick_math.utils.EnumConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao : CRUDBaseDao<Score> {

    @Query("SELECT * FROM score")
    fun findAll(): List<Score>

    @Query("SELECT * FROM score WHERE id = :id")
    fun findOne(id: Long): Score?

    @Query("SELECT * FROM score WHERE type = :type ORDER BY highestAnswer DESC")
    fun paging(type: EnumConstants.PlayType): PagingSource<Int, Score>

    @Query("SELECT COALESCE(MAX(highestAnswer), 0) FROM score")
    fun getHighestScore(): Flow<Int>
}
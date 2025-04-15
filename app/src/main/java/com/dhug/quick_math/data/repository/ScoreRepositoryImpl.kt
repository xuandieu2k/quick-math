package com.dhug.quick_math.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dhug.quick_math.data.local.dao.ScoreDao
import com.dhug.quick_math.data.local.entities.Score
import com.dhug.quick_math.domain.repository.ScoreRepository
import com.dhug.quick_math.utils.EnumConstants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScoreRepositoryImpl @Inject constructor(private val dao: ScoreDao) : ScoreRepository {
    override suspend fun insert(entity: Score): Long {
        return dao.insertOne(entity)
    }

    override suspend fun update(entity: Score) {
        dao.update(entity)
    }

    override suspend fun delete(entity: Score) {
        dao.delete(entity)
    }

    override suspend fun findById(id: Long): Score? {
        return dao.findOne(id)
    }

    override suspend fun findAll(): List<Score> {
        return dao.findAll()
    }

    override suspend fun insertMany(entities: List<Score>): List<Long> {
        return dao.insertMany(entities)
    }

    override fun paging(
        limit: Int,
        type: EnumConstants.PlayType
    ): Flow<PagingData<Score>> {
        return Pager(
            config = PagingConfig(
                pageSize = limit,
                prefetchDistance = 3,
                enablePlaceholders = false,
                initialLoadSize = limit
            ),
            pagingSourceFactory = { dao.paging(type) }
        ).flow
    }

    override fun getHighestScore(): Flow<Int> = dao.getHighestScore()
}
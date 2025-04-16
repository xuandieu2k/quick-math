package com.dhug.quick_math.di

import com.android.billingclient.api.BillingClient
import com.dhug.quick_math.data.local.dao.PurchasedItemDao
import com.dhug.quick_math.data.local.dao.ScoreDao
import com.dhug.quick_math.data.repository.BillingRepositoryImpl
import com.dhug.quick_math.data.repository.ScoreRepositoryImpl
import com.dhug.quick_math.domain.repository.BillingRepository
import com.dhug.quick_math.domain.repository.ScoreRepository
import com.dhug.quick_math.domain.usecase.LanguageUseCase
import com.dhug.quick_math.domain.usecase.PurchaseUseCase
import com.dhug.quick_math.domain.usecase.ScoreUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 31 / 10 / 2024
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // MODULE LIBRARY
    @Provides
    @Singleton
    fun providePurchaseUseCase(repository: BillingRepository): PurchaseUseCase {
        return PurchaseUseCase(repository)
    }


    @Provides
    @Singleton
    fun providePurchaseRepository(
        billingClient: BillingClient, dao: PurchasedItemDao
    ): BillingRepository {
        return BillingRepositoryImpl(billingClient, dao)
    }

    @Provides
    @Singleton
    fun provideScoreUseCase(repository: ScoreRepository): ScoreUseCase {
        return ScoreUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideScoreRepository(
        dao: ScoreDao
    ): ScoreRepository {
        return ScoreRepositoryImpl(dao)
    }


    @Provides
    @Singleton
    fun provideLanguageUseCase(): LanguageUseCase {
        return LanguageUseCase()
    }


}
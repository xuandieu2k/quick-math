package com.dhug.quick_math.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.android.billingclient.api.BillingClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.dhug.quick_math.data.local.dao.PurchasedItemDao
import com.dhug.quick_math.data.repository.BillingRepositoryImpl
import com.dhug.quick_math.domain.repository.BillingRepository
import com.dhug.quick_math.domain.usecase.PurchaseUseCase
import com.dhug.quick_math.helper.NotificationHelper
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory
import dagger.hilt.android.qualifiers.ApplicationContext
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


}
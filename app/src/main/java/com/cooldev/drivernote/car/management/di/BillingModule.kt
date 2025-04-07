package com.dhug.example.di

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.dhug.example.interfaces.BillingUpdateListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillingModule {

    @Provides
    @Singleton
    fun provideBillingUpdateListener(): BillingUpdateListener {
        return BillingUpdateListener()
    }

    @Provides
    @Singleton
    fun provideBillingClient(
        @ApplicationContext context: Context,
        listener: BillingUpdateListener
    ): BillingClient {
        @Suppress("DEPRECATION")
        return BillingClient.newBuilder(context)
            .setListener(listener)
            .enablePendingPurchases()
            .build()
    }
}

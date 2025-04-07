package com.dhug.quick_math.di

import com.dhug.quick_math.domain.usecase.GetRemoteConfigValueUseCase
import com.dhug.quick_math.firebase.service.RemoteConfigService
import com.dhug.quick_math.utils.RemoteConfigManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigModule {

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance()
    }

    @Provides
    @Singleton
    fun provideRemoteConfigService(
        remoteConfig: FirebaseRemoteConfig
    ): RemoteConfigService {
        return RemoteConfigService(remoteConfig)
    }

    @Provides
    @Singleton
    fun provideRemoteConfigManager(
        getRemoteConfigValueUseCase: GetRemoteConfigValueUseCase
    ): RemoteConfigManager {
        return RemoteConfigManager(getRemoteConfigValueUseCase)
    }
}

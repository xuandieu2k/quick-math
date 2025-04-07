package com.dhug.example.di

import com.dhug.example.domain.usecase.GetRemoteConfigValueUseCase
import com.dhug.example.firebase.service.RemoteConfigService
import com.dhug.example.utils.RemoteConfigManager
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

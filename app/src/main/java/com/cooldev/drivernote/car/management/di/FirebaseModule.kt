package com.dhug.example.di

import com.google.firebase.FirebaseApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 21 / 01 / 2025
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseApp(): FirebaseApp {
        return FirebaseApp.getInstance()
    }
}
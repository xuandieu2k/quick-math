package com.dhug.quick_math.di

import android.content.Context
import com.dhug.quick_math.presentation.adapter.AnswerAdapter
import com.dhug.quick_math.presentation.adapter.HistoryAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AdapterModule {

    // ########  ADAPTER ######## //

    @Provides
    fun provideAnswerAdapter(@ApplicationContext context: Context): AnswerAdapter = AnswerAdapter(context)


    @Provides
    fun provideHistoryAdapter(@ApplicationContext context: Context): HistoryAdapter = HistoryAdapter(context)
}
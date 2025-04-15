package com.dhug.quick_math.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dhug.quick_math.data.local.AppDatabase
import com.dhug.quick_math.data.local.dao.PurchasedItemDao
import com.dhug.quick_math.data.local.dao.ScoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Singleton

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 31 / 10 / 2024
 */
@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        val database = Room.databaseBuilder(
            context, AppDatabase::class.java, "quick_math_database"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
//                if (MMKVUtils.isFirstTimeCreateDb()) {
//                    executeSqlFile(context, db, "data.sql")
//                    MMKVUtils.setFirstTimeCreateDb(false)
//                }
            }
        }).build()

        return database
    }

    private fun executeSqlFile(context: Context, db: SupportSQLiteDatabase, fileName: String) {
        try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String? = reader.readLine()
            val stringBuilder = StringBuilder()
            while (line != null) {
                stringBuilder.append(line).append("\n")
                if (line.trim().endsWith(";")) {
                    db.execSQL(stringBuilder.toString())
                    stringBuilder.clear()
                }
                line = reader.readLine()
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Provides
    @Singleton
    fun providePurchasedItemDao(database: AppDatabase): PurchasedItemDao {
        return database.purchasedItemDao()
    }


    @Provides
    @Singleton
    fun provideScoreDao(database: AppDatabase): ScoreDao {
        return database.scoreDao()
    }

}
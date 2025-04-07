package com.dhug.example.utils

import androidx.paging.PagingData
import androidx.paging.map
import com.google.gson.GsonBuilder
import timber.log.Timber
import java.util.Calendar
import javax.inject.Singleton


@Singleton
object PagingDataUtils {

    inline fun <reified T : Any> PagingData<T>.mapChecked(
        listIdsSelected: List<Long>,
        crossinline idSelector: (T) -> Long,
        crossinline isCheckedSetter: (T, Boolean) -> Unit
    ): PagingData<T> {
        return this.map { item ->
            Timber.tag("Log Data PG").d(
                GsonBuilder().setPrettyPrinting().create().toJson(listIdsSelected)
            )
            item.apply { isCheckedSetter(this, idSelector(this) in listIdsSelected) }
        }
    }

    suspend fun <T : Any> pagingDataToList(pagingData: PagingData<T>): List<T> {
        val list = mutableListOf<T>()
        pagingData.map { list.add(it) }
        return list
    }
}
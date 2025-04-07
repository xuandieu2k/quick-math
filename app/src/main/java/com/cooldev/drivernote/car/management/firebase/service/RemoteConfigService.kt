package com.dhug.example.firebase.service

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteConfigService @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {

    init {
        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600 // Cập nhật tối thiểu mỗi giờ
        }
        remoteConfig.setConfigSettingsAsync(settings)
    }

    suspend fun fetchAndActivate(): Boolean {
        return remoteConfig.fetchAndActivate().await()
    }

    fun getString(key: String): String = remoteConfig.getString(key)

    fun getBoolean(key: String): Boolean = remoteConfig.getBoolean(key)

    fun getLong(key: String): Long = remoteConfig.getLong(key)
}

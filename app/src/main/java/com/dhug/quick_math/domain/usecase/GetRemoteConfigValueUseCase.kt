package com.dhug.quick_math.domain.usecase

import com.dhug.quick_math.firebase.service.RemoteConfigService
import javax.inject.Inject

class GetRemoteConfigValueUseCase @Inject constructor(
    private val remoteConfigService: RemoteConfigService
) {

    suspend fun fetchAndActivate(): Boolean {
        return remoteConfigService.fetchAndActivate()
    }

    fun getString(key: String): String {
        return remoteConfigService.getString(key)
    }

    fun getBoolean(key: String): Boolean {
        return remoteConfigService.getBoolean(key)
    }

    fun getLong(key: String): Long {
        return remoteConfigService.getLong(key)
    }
}
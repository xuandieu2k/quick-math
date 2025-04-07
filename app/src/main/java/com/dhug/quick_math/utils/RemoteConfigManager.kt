package com.dhug.quick_math.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhug.quick_math.domain.usecase.GetRemoteConfigValueUseCase
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 11 / 01 / 2025
 */
@Singleton
class RemoteConfigManager @Inject constructor(
    private val getRemoteConfigValueUseCase: GetRemoteConfigValueUseCase
) {

    private val _configData = MutableLiveData<Map<String, Any>>()
    val configData: LiveData<Map<String, Any>> get() = _configData

    private val _isConfigFetched = MutableLiveData<Boolean>()
    val isConfigFetched: LiveData<Boolean> get() = _isConfigFetched

    // Fetch Remote Config once in Application
    suspend fun fetchRemoteConfig() {
        try {
            val success = getRemoteConfigValueUseCase.fetchAndActivate()
            Timber.tag("Log Fetch Config").d("Success: $success")

            if (success) {
                val data = mapOf(
                    RemoteConfigConstants.DIRECT_STORE_DESCRIPTION to getRemoteConfigValueUseCase.getString(
                        RemoteConfigConstants.DIRECT_STORE_DESCRIPTION
                    ),
                    RemoteConfigConstants.IS_SHOW_YEARLY_PACKAGE to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_SHOW_YEARLY_PACKAGE
                    ),
                    RemoteConfigConstants.IS_SHOW_ADS_INTERSTITIAL to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_SHOW_ADS_INTERSTITIAL
                    ),
                    RemoteConfigConstants.IS_SHOW_ADS_BANNER to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_SHOW_ADS_BANNER
                    ),
                    RemoteConfigConstants.IS_SHOW_ADS_BANNER_ONBOARDING to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_SHOW_ADS_BANNER_ONBOARDING
                    ),
                    RemoteConfigConstants.IS_ONBOARDING_RATING_DIALOG to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_ONBOARDING_RATING_DIALOG
                    ),

                    RemoteConfigConstants.IS_VERSION_PAYWALL to getRemoteConfigValueUseCase.getLong(
                        RemoteConfigConstants.IS_VERSION_PAYWALL
                    ),
                    RemoteConfigConstants.INTERSTITIAL_INTERVAL to getRemoteConfigValueUseCase.getLong(
                        RemoteConfigConstants.INTERSTITIAL_INTERVAL
                    ),
                    RemoteConfigConstants.MAX_INTER_ADS_COUNT to getRemoteConfigValueUseCase.getLong(
                        RemoteConfigConstants.MAX_INTER_ADS_COUNT
                    ),
                    RemoteConfigConstants.IS_SHOW_SPLASH to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_SHOW_SPLASH
                    ),

                    //
                    RemoteConfigConstants.IS_LOCK_FEATURE_REPORT to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_LOCK_FEATURE_REPORT
                    ),

                    RemoteConfigConstants.IS_LIMIT_ADD_CAR to getRemoteConfigValueUseCase.getLong(
                        RemoteConfigConstants.IS_LIMIT_ADD_CAR
                    ),
                    RemoteConfigConstants.IS_LIMIT_ADD_NOTE to getRemoteConfigValueUseCase.getLong(
                        RemoteConfigConstants.IS_LIMIT_ADD_NOTE
                    ),

                    )

                // Save config data to local storage
                val dataMap = RemoteConfigConstants.getRemoteConfigByHashmap(data)
                MMKVUtils.saveRemoteConfig(dataMap)

                _configData.postValue(data)
                _isConfigFetched.postValue(true)

                Timber.tag("Log Fetch Config: Data")
                    .d(GsonBuilder().setPrettyPrinting().create().toJson(dataMap))
            }
        } catch (ex: Exception) {
            Timber.e(ex, "Failed to fetch Remote Config")
            _isConfigFetched.postValue(false)
        }
    }

    // Fetch Remote Config once in Application
    suspend fun fetchFistRemoteConfig(onFetchResult: (isDone: Boolean) -> Unit) {
        try {
            val success = getRemoteConfigValueUseCase.fetchAndActivate()
            Timber.tag("Log Fetch Config").d("Success: $success")

            if (success) {
                val data = mapOf(
                    RemoteConfigConstants.DIRECT_STORE_DESCRIPTION to getRemoteConfigValueUseCase.getString(
                        RemoteConfigConstants.DIRECT_STORE_DESCRIPTION
                    ),
                    RemoteConfigConstants.IS_SHOW_YEARLY_PACKAGE to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_SHOW_YEARLY_PACKAGE
                    ),
                    RemoteConfigConstants.IS_SHOW_ADS_INTERSTITIAL to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_SHOW_ADS_INTERSTITIAL
                    ),
                    RemoteConfigConstants.IS_SHOW_ADS_BANNER to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_SHOW_ADS_BANNER
                    ),
                    RemoteConfigConstants.IS_SHOW_ADS_BANNER_ONBOARDING to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_SHOW_ADS_BANNER_ONBOARDING
                    ),
                    RemoteConfigConstants.IS_ONBOARDING_RATING_DIALOG to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_ONBOARDING_RATING_DIALOG
                    ),

                    RemoteConfigConstants.IS_VERSION_PAYWALL to getRemoteConfigValueUseCase.getLong(
                        RemoteConfigConstants.IS_VERSION_PAYWALL
                    ),
                    RemoteConfigConstants.INTERSTITIAL_INTERVAL to getRemoteConfigValueUseCase.getLong(
                        RemoteConfigConstants.INTERSTITIAL_INTERVAL
                    ),
                    RemoteConfigConstants.MAX_INTER_ADS_COUNT to getRemoteConfigValueUseCase.getLong(
                        RemoteConfigConstants.MAX_INTER_ADS_COUNT
                    ),
                    RemoteConfigConstants.IS_SHOW_SPLASH to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_SHOW_SPLASH
                    ),

                    //
                    RemoteConfigConstants.IS_LOCK_FEATURE_REPORT to getRemoteConfigValueUseCase.getBoolean(
                        RemoteConfigConstants.IS_LOCK_FEATURE_REPORT
                    ),

                    RemoteConfigConstants.IS_LIMIT_ADD_CAR to getRemoteConfigValueUseCase.getLong(
                        RemoteConfigConstants.IS_LIMIT_ADD_CAR
                    ),
                    RemoteConfigConstants.IS_LIMIT_ADD_NOTE to getRemoteConfigValueUseCase.getLong(
                        RemoteConfigConstants.IS_LIMIT_ADD_NOTE
                    ),

                    RemoteConfigConstants.IS_API_RATING_INAPP to getRemoteConfigValueUseCase.getString(
                        RemoteConfigConstants.IS_API_RATING_INAPP
                    ),

                    )

                // Save config data to local storage
                val dataMap = RemoteConfigConstants.getRemoteConfigByHashmap(data)
                MMKVUtils.saveRemoteConfig(dataMap)

                _configData.postValue(data)
                _isConfigFetched.postValue(true)

                Timber.tag("Log Fetch Config: Data")
                    .d(GsonBuilder().setPrettyPrinting().create().toJson(dataMap))
                withContext(Dispatchers.Main){
                    onFetchResult(true)
                }
            }else{
                withContext(Dispatchers.Main){
                    onFetchResult(false)
                }
            }
        } catch (ex: Exception) {
            withContext(Dispatchers.Main){
                onFetchResult(false)
            }
            Timber.e(ex, "Failed to fetch Remote Config")
            _isConfigFetched.postValue(false)
        }
    }
}
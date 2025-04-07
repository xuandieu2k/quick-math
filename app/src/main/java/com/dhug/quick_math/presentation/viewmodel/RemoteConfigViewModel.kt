package com.dhug.quick_math.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhug.quick_math.utils.RemoteConfigManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteConfigViewModel @Inject constructor(
    private val remoteConfigManager: RemoteConfigManager
) : ViewModel() {

    val configData: LiveData<Map<String, Any>> = remoteConfigManager.configData

    fun refreshConfig() {
        viewModelScope.launch {
            remoteConfigManager.fetchRemoteConfig()
        }
    }
}
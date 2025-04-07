package com.dhug.example.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhug.example.utils.RemoteConfigManager
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
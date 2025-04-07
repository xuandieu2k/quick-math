package com.dhug.example.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhug.example.domain.usecase.PurchaseUseCase
import com.dhug.example.utils.MMKVUtils
import com.dhug.example.utils.Resource
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val purchaseUseCase: PurchaseUseCase
) : ViewModel() {
    private val _isPremium: MutableLiveData<Resource<Pair<Boolean, Boolean>>> =
        MutableLiveData(Resource.Success(Pair(MMKVUtils.areAnyPremiumsActive(), false)))
    val isPremium: LiveData<Resource<Pair<Boolean, Boolean>>> = _isPremium

    fun refreshPurchase(onDone: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            purchaseUseCase.refreshPurchases(
                onDone = {
                    _isPremium.value = Resource.Success(Pair(MMKVUtils.areAnyPremiumsActive(), true))
                    onDone()
                    Timber.tag("Log Refresh Purchase:").d("Data: ${GsonBuilder().create().toJson(Pair(
                        MMKVUtils.areAnyPremiumsActive(), true))}")
                },
                onError = {
                    onError(it)
                    Timber.tag("Log Refresh Purchase:").e(it)
                }
            )
        }
    }
}
package com.dhug.quick_math.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhug.quick_math.data.local.entities.QuickMath
import com.dhug.quick_math.data.local.entities.QuickMath.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor() : ViewModel() {
    private val _question: MutableStateFlow<Question?> = MutableStateFlow(null)
    val question: StateFlow<Question?> get() = _question.asStateFlow()

    init {
        updateQuestion()
    }

    fun updateQuestion() {
        viewModelScope.launch(Dispatchers.IO) {
            val question = QuickMath.generateQuestion()
            _question.emit(question)
        }
    }

}
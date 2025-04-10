package com.dhug.quick_math.presentation.viewmodel

import android.os.CountDownTimer
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

    private val _nextQuestion: MutableStateFlow<Question?> = MutableStateFlow(null)
    val nextQuestion: StateFlow<Question?> get() = _nextQuestion.asStateFlow()


    private val _sumOfQuestion: MutableStateFlow<Int> = MutableStateFlow(0)
    val sumOfQuestion: StateFlow<Int> get() = _sumOfQuestion.asStateFlow()

    private val _highestQuestion: MutableStateFlow<Int> = MutableStateFlow(100)
    val highestQuestion: StateFlow<Int> get() = _highestQuestion.asStateFlow()

    private val _timeLeftMillis = MutableStateFlow(30 * 1000L)
    val timeLeftMillis: StateFlow<Long> = _timeLeftMillis

    private var countDownTimer: CountDownTimer? = null
    private val _totalTimeMillis = MutableStateFlow(30 * 1000L)
    val totalTimeMillis: StateFlow<Long> = _totalTimeMillis

    private val _sumInCorrectAnswer: MutableStateFlow<Int> = MutableStateFlow(0)
    val sumInCorrectAnswer: StateFlow<Int> get() = _sumInCorrectAnswer.asStateFlow()

    private val _sumCorrectAnswer: MutableStateFlow<Int> = MutableStateFlow(0)
    val sumCorrectAnswer: StateFlow<Int> get() = _sumCorrectAnswer.asStateFlow()

    fun startTimer() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(_totalTimeMillis.value, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeftMillis.value = millisUntilFinished
            }

            override fun onFinish() {
                _timeLeftMillis.value = 0L
            }
        }.start()
    }

    init {
        updateQuestion()
    }

    fun updateSumCorrectAnswer() {
        _sumCorrectAnswer.value = _sumCorrectAnswer.value.plus(1)
    }

    fun updateSumInCorrectAnswer() {
        _sumInCorrectAnswer.value = _sumInCorrectAnswer.value.plus(1)
    }

    fun updateQuestion() {
        viewModelScope.launch(Dispatchers.IO) {
            if (nextQuestion.value == null) {
                val question = QuickMath.generateQuestion()
                _question.emit(question)
                val nextQuestion = QuickMath.generateQuestion()
                _nextQuestion.emit(nextQuestion)
            } else {
                _question.value = _nextQuestion.value
                val nextQuestion = QuickMath.generateQuestion()
                _nextQuestion.emit(nextQuestion)
            }
            _sumOfQuestion.value = _sumOfQuestion.value.plus(1)
        }
    }

    override fun onCleared() {
        countDownTimer?.cancel()
        super.onCleared()
    }

    fun resetData() {
        _sumOfQuestion.value = 0
        _sumCorrectAnswer.value = 0
        _sumInCorrectAnswer.value = 0
        updateQuestion()
        startTimer()
    }

}
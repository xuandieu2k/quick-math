package com.dhug.quick_math.presentation.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhug.quick_math.data.local.entities.QuickMath
import com.dhug.quick_math.data.local.entities.QuickMath.Question
import com.dhug.quick_math.data.local.entities.Score
import com.dhug.quick_math.domain.usecase.ScoreUseCase
import com.dhug.quick_math.utils.EnumConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val scoreUseCase: ScoreUseCase
) : ViewModel() {
    private val _question: MutableStateFlow<Question?> = MutableStateFlow(null)
    val question: StateFlow<Question?> get() = _question.asStateFlow()

    private val _nextQuestion: MutableStateFlow<Question?> = MutableStateFlow(null)
    val nextQuestion: StateFlow<Question?> get() = _nextQuestion.asStateFlow()


    private val _sumOfQuestion: MutableStateFlow<Int> = MutableStateFlow(0)
    val sumOfQuestion: StateFlow<Int> get() = _sumOfQuestion.asStateFlow()

    val highestQuestion =
        scoreUseCase.getHighestScore().stateIn(viewModelScope, SharingStarted.Lazily, 0)

    private val _timeLeftMillis = MutableStateFlow(30 * 1000L)
    val timeLeftMillis: StateFlow<Long> = _timeLeftMillis

    private var countDownTimer: CountDownTimer? = null
    private val _totalTimeMillis = MutableStateFlow(30 * 1000L)
    val totalTimeMillis: StateFlow<Long> = _totalTimeMillis

    private val _sumInCorrectAnswer: MutableStateFlow<Int> = MutableStateFlow(0)
    val sumInCorrectAnswer: StateFlow<Int> get() = _sumInCorrectAnswer.asStateFlow()

    private val _sumCorrectAnswer: MutableStateFlow<Int> = MutableStateFlow(0)
    val sumCorrectAnswer: StateFlow<Int> get() = _sumCorrectAnswer.asStateFlow()

    private val _totalSpentTimeMillis = MutableStateFlow(0L)
    val totalSpentTimeMillis: StateFlow<Long> = _totalSpentTimeMillis

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
        viewModelScope.launch {
            highestQuestion.collectLatest {  }
        }
    }

    fun updateTimeLeftMillisWhenOverGame() {
        countDownTimer?.cancel()
        countDownTimer = null
//        _timeLeftMillis.value = 0L
    }


    fun updateSumCorrectAnswer() {
        _sumCorrectAnswer.value = _sumCorrectAnswer.value.plus(1)
    }

    fun updateSumInCorrectAnswer() {
        _sumInCorrectAnswer.value = _sumInCorrectAnswer.value.plus(1)
    }

    fun updateQuestion() {
        viewModelScope.launch {
            val newQuestion: Question
            val nextQ: Question

            if (_nextQuestion.value == null) {
                newQuestion = QuickMath.generateQuestion()
                nextQ = QuickMath.generateQuestion()
            } else {
                newQuestion = _nextQuestion.value!!
                nextQ = QuickMath.generateQuestion()
            }

            withContext(Dispatchers.Main) {
                _question.value = newQuestion
                _nextQuestion.value = nextQ
                _sumOfQuestion.value = _sumOfQuestion.value + 1
            }
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

    fun insertData(type: EnumConstants.PlayType = EnumConstants.PlayType.TRAINING) {
        viewModelScope.launch {
            scoreUseCase.insert(createScore(type)).collectLatest { }
        }
    }

    private fun createScore(type: EnumConstants.PlayType = EnumConstants.PlayType.TRAINING): Score =
        Score().apply {
            this.highestAnswer = sumOfQuestion.value
            this.answerCorrect =
                if (type == EnumConstants.PlayType.TRAINING) sumCorrectAnswer.value else 0
            this.answerIncorrect =
                if (type == EnumConstants.PlayType.TRAINING) sumInCorrectAnswer.value else 0

            this.totalSpentTime = totalSpentTimeMillis.value

            this.type = type
        }

}
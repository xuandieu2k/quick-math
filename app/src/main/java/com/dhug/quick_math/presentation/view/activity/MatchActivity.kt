package com.dhug.quick_math.presentation.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.dhug.quick_math.R
import com.dhug.quick_math.base.AppAdsActivity
import com.dhug.quick_math.data.local.entities.QuickMath
import com.dhug.quick_math.databinding.ActivityMatchBinding
import com.dhug.quick_math.presentation.adapter.AnswerAdapter
import com.dhug.quick_math.presentation.dialog.GameOverDialog
import com.dhug.quick_math.presentation.viewmodel.MatchViewModel
import com.dhug.quick_math.utils.AppUtils
import com.dhug.quick_math.utils.DialogUtils
import com.dhug.quick_math.utils.MoneyUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MatchActivity : AppAdsActivity() {
    private lateinit var binding: ActivityMatchBinding
    private val matchViewModel: MatchViewModel by viewModels()

    @Inject
    lateinit var answerAdapter: AnswerAdapter
    override fun onClickAfterAd(view: View) {
        //
    }

    override fun getLayoutView(): View {
        binding = ActivityMatchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {
        initRecycleView()
        setUpView()
    }

    private fun setUpView() {
        setOnClickListener(binding.btnBack)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialogWarning()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        setUpSeekBar()
    }

    private fun initRecycleView() {
        binding.rvAnswer.adapter = answerAdapter
        AppUtils.initRecyclerViewHorizontal(binding.rvAnswer, answerAdapter, 2)
        answerAdapter.setOnListener(object : AnswerAdapter.OnClickAnswer {
            override fun onAnswer(position: Int, item: String) {
                if (matchViewModel.timeLeftMillis.value == 0L) return
                if (position == matchViewModel.question.value?.correctIndex) {
                    matchViewModel.updateQuestion()
                } else {
                    showDialogGameOver()
                }
            }

        })
    }

    override fun initData() {
        //
    }

    override fun observerData() {
        super.observerData()
        lifecycleScope.launch {
            matchViewModel.question.collectLatest {
                it?.let {
                    updateUIWithQuestion(it)
                    matchViewModel.startTimer()
                }
            }
        }

        lifecycleScope.launch {
            matchViewModel.sumOfQuestion.collectLatest {
                updateViewSumOfQuestion(it)
            }
        }

        lifecycleScope.launch {
            matchViewModel.timeLeftMillis.collectLatest {
                updateSeekBarTimer(it)
                if (it == 0L) {
                    showDialogGameOver()
                }
            }
        }
    }

    private fun showDialogGameOver() {
        binding.lavGlassHour.cancelAnimation()
        binding.lavGlassHour.progress = 0f

        DialogUtils.showDialogGameOver(
            this@MatchActivity,
            matchViewModel.sumOfQuestion.value,
            matchViewModel.highestQuestion.value
        ) {
            when (it) {
                GameOverDialog.Builder.Companion.TypeAction.HOME -> finish()
                GameOverDialog.Builder.Companion.TypeAction.AGAIN -> resetGame()
                GameOverDialog.Builder.Companion.TypeAction.TRAINING -> {
                    startActivity(Intent(this, TrainingActivity::class.java))
                    finish()
                }
            }
        }
    }


    private fun showDialogWarning() {
        DialogUtils.showDialogWarning(this) {
            if (it) {
                finish()
            }
        }
    }

    private fun resetGame() {
        binding.lavGlassHour.cancelAnimation()
        binding.lavGlassHour.progress = 0f
        binding.lavGlassHour.playAnimation()
        matchViewModel.resetData()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun lockSeekBarInteraction() {
        binding.sbTimer.setOnTouchListener { _, _ -> true }

    }


    private fun setUpSeekBar() {
        binding.sbTimer.max = 100
        lockSeekBarInteraction()

    }

    private fun updateSeekBarTimer(
        millisUntilFinished: Long,
        totalTimeMillis: Long = matchViewModel.totalTimeMillis.value
    ) {
        val percent = (millisUntilFinished.toFloat() / totalTimeMillis * 100).toInt()
        binding.sbTimer.progress = percent
        binding.tvTimer.text = formatTime(millisUntilFinished)
        binding.tvTimer.setTextColor(
            AppCompatResources.getColorStateList(
                this,
                if (millisUntilFinished >= 10000L) R.color.colorTextPrimary else R.color.red_primary
            )
        )
    }

    @SuppressLint("DefaultLocale")
    private fun formatTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun updateUIWithQuestion(question: QuickMath.Question) {
        binding.tvQuestion.text = question.questionText

        answerAdapter.setData(question.options.toMutableList())
    }

    @SuppressLint("SetTextI18n")
    private fun updateViewSumOfQuestion(sum: Int) {
        binding.tvSumQuestion.text =
            "${getString(R.string.question)} ${MoneyUtils.formatBigDecimal(sum.toBigDecimal())}"
    }

    override fun setAdPosition(): Companion.AdPosition = Companion.AdPosition.BOTTOM

    override fun onClickNormal(view: View) {
        when (view) {
            binding.btnBack -> showDialogWarning()
        }
    }
}
package com.dhug.quick_math.presentation.view.activity

import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dhug.quick_math.R
import com.dhug.quick_math.base.AppAdsActivity
import com.dhug.quick_math.base.wiget.AppToast
import com.dhug.quick_math.data.local.entities.QuickMath
import com.dhug.quick_math.databinding.ActivityMatchBinding
import com.dhug.quick_math.presentation.adapter.AnswerAdapter
import com.dhug.quick_math.presentation.viewmodel.MatchViewModel
import com.dhug.quick_math.utils.AppUtils
import com.hjq.toast.ToastUtils
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
    }

    private fun initRecycleView() {
        binding.rvAnswer.adapter = answerAdapter
        AppUtils.initRecyclerViewHorizontal(binding.rvAnswer, answerAdapter, 2)
        answerAdapter.setOnListener(object : AnswerAdapter.OnClickAnswer {
            override fun onAnswer(position: Int, item: String) {
                if (position == matchViewModel.question.value?.correctIndex) {
                    matchViewModel.updateQuestion()
                } else {
                    AppToast(this@MatchActivity, getString(R.string.wrong), Toast.LENGTH_SHORT).show()
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
                }
            }
        }
    }

    private fun updateUIWithQuestion(question: QuickMath.Question) {
        binding.tvQuestion.text = question.questionText

        answerAdapter.setData(question.options.toMutableList())
    }

    override fun setAdPosition(): Companion.AdPosition = Companion.AdPosition.BOTTOM

    override fun onClickNormal(view: View) {
        when (view) {
            binding.btnBack -> finish()
        }
    }
}
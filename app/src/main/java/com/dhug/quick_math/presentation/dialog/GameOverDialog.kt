package com.dhug.quick_math.presentation.dialog

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import com.dhug.base.BaseDialog
import com.dhug.base.action.AnimAction
import com.dhug.quick_math.R
import com.dhug.quick_math.databinding.DialogGameOverBinding
import com.dhug.quick_math.utils.MoneyUtils

class GameOverDialog {
    class Builder(
        private val context: Context,
        private val sumOfQuestion: Int,
        private val highestQuestion: Int
    ) :
        BaseDialog.Builder<Builder>(context) {
        private val binding: DialogGameOverBinding by lazy {
            DialogGameOverBinding.inflate(
                LayoutInflater.from(context)
            )
        }

        private var listener: OnActionGameOver? = null

        fun setListenerAction(listener: OnActionGameOver): Builder {
            this.listener = listener
            return this
        }

        companion object {
            enum class TypeAction {
                HOME,
                AGAIN,
                TRAINING
            }
        }

        init {
            setContentView(binding.root)
            setAnimStyle(AnimAction.ANIM_TOAST)
            setWidth(Resources.getSystem().displayMetrics.widthPixels * 5 / 6)
            setCancelable(false)
            setActionView()
            setUpView()
        }

        private fun setUpView() {
            binding.tvHighestQuestion.text = if (sumOfQuestion < highestQuestion) getString(R.string.highest_question_) else getString(R.string.new_highest_question_)
                binding.tvSumQuestion.text =
                    MoneyUtils.formatBigDecimal(sumOfQuestion.toBigDecimal())
            binding.tvHighestQuestion.text =
                if (sumOfQuestion < highestQuestion) MoneyUtils.formatBigDecimal(highestQuestion.toBigDecimal()) else MoneyUtils.formatBigDecimal(sumOfQuestion.toBigDecimal())
        }

        private fun setActionView() {
            setOnClickListener(
                binding.btnHome,
                binding.btnTraining,
                binding.btnPlayAgain,
            )
        }

        override fun onClickNormal(view: View) {
            super.onClickNormal(view)
            when (view) {
                binding.btnHome -> {
                    listener?.onFishActionGameOver(typeActionGameOver = TypeAction.HOME)
                    dismiss()
                }

                binding.btnTraining -> {
                    listener?.onFishActionGameOver(typeActionGameOver = TypeAction.TRAINING)
                    dismiss()
                }

                binding.btnPlayAgain -> {
                    listener?.onFishActionGameOver(typeActionGameOver = TypeAction.AGAIN)
                    dismiss()
                }
            }
        }

        interface OnActionGameOver {
            fun onFishActionGameOver(typeActionGameOver: TypeAction)
        }
    }
}
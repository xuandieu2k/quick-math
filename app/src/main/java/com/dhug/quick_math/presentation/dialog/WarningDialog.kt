package com.dhug.quick_math.presentation.dialog

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import com.dhug.base.BaseDialog
import com.dhug.base.action.AnimAction
import com.dhug.quick_math.databinding.DialogWarningBinding

class WarningDialog {
    class Builder(private val context: Context) : BaseDialog.Builder<Builder>(context) {
        private val binding: DialogWarningBinding by lazy {
            DialogWarningBinding.inflate(
                LayoutInflater.from(context)
            )
        }

        private var listener: OnActionDone? = null

        fun setListenerAction(listener: OnActionDone): Builder {
            this.listener = listener
            return this
        }

        init {
            setContentView(binding.root)
            setAnimStyle(AnimAction.ANIM_TOAST)
            setWidth(Resources.getSystem().displayMetrics.widthPixels * 5 / 6)
            setCancelable(false)
            setActionView()
        }

        private fun setActionView() {
            setOnClickListener(
                binding.btnOke,
                binding.btnCancel
            )
        }

        override fun onClickNormal(view: View) {
            super.onClickNormal(view)
            when (view) {
                binding.btnOke -> {
                    listener?.onFinishAction(true)
                    dismiss()
                }

                binding.btnCancel -> {
                    listener?.onFinishAction(false)
                    dismiss()
                }
            }
        }

        interface OnActionDone {
            fun onFinishAction(isConfirm: Boolean)
        }
    }
}
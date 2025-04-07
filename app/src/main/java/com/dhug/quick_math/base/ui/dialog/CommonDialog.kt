package com.dhug.quick_math.base.ui.dialog

import android.content.*
import android.view.*
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.cooldev.base.action.AnimAction
import com.cooldev.base.BaseDialog
import com.dhug.quick_math.R
import com.dhug.quick_math.utils.ExtensionUtils

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
class CommonDialog {

    @Suppress("UNCHECKED_CAST", "LeakingThis")
    open class Builder<B : Builder<B>>(context: Context) : BaseDialog.Builder<B>(context) {

        private var autoDismiss = true
        private var shouldDismissOnCancel = true
        private var shouldDismissOnConfirm = true

        private val containerLayout: ViewGroup? by lazy { findViewById(R.id.ll_ui_container) }
        private val titleView: TextView? by lazy { findViewById(R.id.tv_ui_title) }
        private val cancelView: TextView? by lazy { findViewById(R.id.tv_ui_cancel) }
        private val lineView: View? by lazy { findViewById(R.id.v_ui_line) }
        private val confirmView: TextView? by lazy { findViewById(R.id.tv_ui_confirm) }

        private lateinit var onCommonAction: OnCommonAction

        init {
            setContentView(R.layout.dialog_ui)
            setAnimStyle(AnimAction.ANIM_IOS)
            setGravity(Gravity.CENTER)
            setClickView()
        }

        private fun setClickView() {
            cancelView?.setOnClickListener {
                onCommonAction.onConfirm(false)
                if (shouldDismissOnCancel) dismiss()
            }
            confirmView?.setOnClickListener {
                onCommonAction.onConfirm(true)
                if (shouldDismissOnConfirm) dismiss()
            }
        }

        fun setListener(onCommonAction: OnCommonAction): B {
            this.onCommonAction = onCommonAction
            return this as B
        }

        fun setCustomView(@LayoutRes id: Int): B {
            return setCustomView(
                LayoutInflater.from(getContext()).inflate(id, containerLayout, false)
            )
        }

        fun setCustomView(view: View?): B {
            containerLayout?.addView(view, 1)
            return this as B
        }

        fun setTitle(@StringRes id: Int): B {
            return setTitle(getString(id))
        }

        fun setTitle(text: CharSequence?): B {
            titleView?.text = text
            return this as B
        }

        fun setCancel(@StringRes id: Int): B {
            return setCancel(getString(id))
        }

        fun setCancel(text: CharSequence?): B {
            cancelView?.text = text
            lineView?.visibility =
                if (text == null || "" == text.toString()) View.GONE else View.VISIBLE
            return this as B
        }

        fun setConfirm(@StringRes id: Int): B {
            return setConfirm(getString(id))
        }

        fun setConfirm(text: CharSequence?): B {
            confirmView?.text = text
            return this as B
        }

        fun setAutoDismiss(dismiss: Boolean): B {
            autoDismiss = dismiss
            return this as B
        }

        fun autoDismiss() {
            if (autoDismiss) {
                dismiss()
            }
        }

        fun setDismissOnCancel(dismiss: Boolean): B {
            shouldDismissOnCancel = dismiss
            return this as B
        }

        fun setDismissOnConfirm(dismiss: Boolean): B {
            shouldDismissOnConfirm = dismiss
            return this as B
        }

        override fun onClick(view: View) {
            super.onClick(view)
            getActivity()?.let {
                ExtensionUtils.showInterAd(it){
                    //
                }
            }
        }

        interface OnCommonAction {
            fun onConfirm(isConfirm: Boolean)
        }
    }
}
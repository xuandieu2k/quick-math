package com.dhug.example.base.ui.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.cooldev.base.BaseDialog
import com.dhug.example.R
import com.dhug.example.base.aop.SingleClick

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
class MessageDialog {

    class Builder constructor(context: Context) : CommonDialog.Builder<Builder>(context) {

        private val messageView: TextView? by lazy { findViewById(R.id.tv_message_message) }

        private var listener: OnListener? = null

        init {
            setCustomView(R.layout.dialog_message)
        }

        fun setMessage(@StringRes id: Int): Builder = apply {
            setMessage(getString(id))
        }

        fun setMessage(text: CharSequence?): Builder = apply {
            messageView?.text = text
        }

        fun setListener(listener: OnListener?): Builder = apply {
            this.listener = listener
        }

        override fun create(): BaseDialog {
           //Throw an exception if the content is empty
            if (("" == messageView?.text.toString())) {
                throw IllegalArgumentException("Dialog message not null")
            }
            return super.create()
        }

        @SingleClick
        override fun onClick(view: View) {
            when (view.id) {
                R.id.tv_ui_confirm -> {
                    autoDismiss()
                    listener?.onConfirm(getDialog())
                }
                R.id.tv_ui_cancel -> {
                    autoDismiss()
                    listener?.onCancel(getDialog())
                }
            }
        }
    }

    interface OnListener {

       /**
         * Callback when OK is clicked
         */
        fun onConfirm(dialog: BaseDialog?)

        /**
         * Called when cancel is clicked
         */
        fun onCancel(dialog: BaseDialog?) {}
    }
}
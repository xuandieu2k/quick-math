package com.dhug.example.presentation.dialog

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import com.dhug.example.base.ui.dialog.CommonDialog
import com.dhug.example.databinding.DialogLoadingAdBinding

class LoadingAdDialog {
    class Builder(context: Context) : CommonDialog.Builder<Builder>(context) {
        private val binding: DialogLoadingAdBinding by lazy {
            DialogLoadingAdBinding.inflate(
                LayoutInflater.from(context)
            )
        }

        init {
            setWidth(Resources.getSystem().displayMetrics.widthPixels)
            setHeight(Resources.getSystem().displayMetrics.heightPixels)
            setContentView(binding.root)
            setCancelable(false)
        }

    }
}
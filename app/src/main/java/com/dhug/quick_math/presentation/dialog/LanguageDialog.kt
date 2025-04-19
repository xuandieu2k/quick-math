package com.dhug.quick_math.presentation.dialog

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dhug.base.BaseDialog
import com.dhug.base.action.AnimAction
import com.dhug.quick_math.data.local.entities.Language
import com.dhug.quick_math.databinding.DialogLanguageBinding
import com.dhug.quick_math.presentation.adapter.LanguageAdapter
import com.dhug.quick_math.presentation.viewmodel.LanguageDialogViewModel
import com.dhug.quick_math.utils.AppUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    LanguageDialog.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 19/4/25 at 09:31
 * Description: File LanguageDialog.kt created by admin - 19/4/25 at 09:31
 */
class LanguageDialog {
    class Builder(
        private val context: Context,
        private val activity: AppCompatActivity
    ) :
        BaseDialog.Builder<Builder>(context) {
        private val binding: DialogLanguageBinding by lazy {
            DialogLanguageBinding.inflate(
                LayoutInflater.from(context)
            )
        }

        private val languageDialogViewModel: LanguageDialogViewModel by activity.viewModels()

        lateinit var adapter: LanguageAdapter

        private var listener: OnActionLanguage? = null

        fun setListenerAction(listener: OnActionLanguage): Builder {
            this.listener = listener
            return this
        }

        init {
            setContentView(binding.root)
            setAnimStyle(AnimAction.ANIM_TOAST)
            setWidth(Resources.getSystem().displayMetrics.widthPixels * 5 / 6)
            setCancelable(false)
            initRecycleViewLanguage()
            observerData()
            setOnClickListener(
                binding.btnConfirm,
                binding.btnCancel,
            )
        }

        private fun initRecycleViewLanguage() {
            adapter = LanguageAdapter(context)
            adapter.setActionClick(object : LanguageAdapter.OnActionClick {
                override fun onClick(position: Int) {
                    adapter.updateCurrentSelected(position)
                }

            })
            AppUtils.initRecyclerView(binding.rvLanguage, adapter, AppUtils.Orientation.VERTICAL)
        }

        private fun observerData() {
            activity.lifecycleScope.launch {
                languageDialogViewModel.languages.collectLatest {
                    adapter.setData(it.toMutableList())
                    adapter.updatePosition(languageDialogViewModel.getCurrentPosition())
                }
            }
        }

        override fun onClickNormal(view: View) {
            super.onClickNormal(view)
            when (view) {
                binding.btnConfirm -> {
                    listener?.onChooseLanguage(true, adapter.getCurrentItem())
                    dismiss()
                }

                binding.btnCancel -> {
                    listener?.onChooseLanguage(false, adapter.getCurrentItem())
                    dismiss()

                }
            }
        }

        interface OnActionLanguage {
            fun onChooseLanguage(isConfirm: Boolean, language: Language)
        }
    }
}
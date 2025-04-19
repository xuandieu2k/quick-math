package com.dhug.quick_math.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhug.quick_math.base.AppAdapter
import com.dhug.quick_math.data.local.entities.Language
import com.dhug.quick_math.databinding.ItemLanguageBinding
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    LanguageAdapter.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 19/4/25 at 09:37
 * Description: File LanguageAdapter.kt created by admin - 19/4/25 at 09:37
 */
class LanguageAdapter @Inject constructor(@ApplicationContext private val context: Context) :
    AppAdapter<Language>(context) {

    private var onActionClick: OnActionClick? = null


    private var currentPosition = 0


    fun updatePosition(position: Int) {
        currentPosition = position
    }

    fun setActionClick(listener: OnActionClick) {
        this.onActionClick = listener
    }

    fun getCurrentItem(): Language = getData().firstOrNull { it.isChecked } ?: getData().first()

    @SuppressLint("NotifyDataSetChanged")
    fun updateCurrentSelected(position: Int) {
        currentPosition = position
        getData().first { it.isChecked }.isChecked = false
        getItem(position).isChecked = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppAdapter<Language>.AppViewHolder {
        return LanguageViewHolder(
            ItemLanguageBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    inner class LanguageViewHolder(private val binding: ItemLanguageBinding) :
        AppViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onActionClick?.onClick(bindingAdapterPosition)
                }
            }
        }

        override fun onBindView(position: Int) {
            binding.context = context
            binding.language = getItem(position)
        }

    }

    interface OnActionClick {
        fun onClick(position: Int)
    }
}
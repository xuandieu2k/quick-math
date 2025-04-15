package com.dhug.quick_math.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dhug.quick_math.base.AppPagingAdapter
import com.dhug.quick_math.data.local.entities.Score
import com.dhug.quick_math.databinding.ItemHistoryBinding
import com.dhug.quick_math.databinding.ItemTrainingBinding
import com.dhug.quick_math.utils.EnumConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Copyright (c) 2025 by DHUG.
 * All rights reserved.
 *
 * File Name:    HistoryAdapter.kt
 * Author:       NGUYỄN XUÂN DIỆU
 * Created Date: 12/4/25 at 10:11
 * Description: File HistoryAdapter.kt created by admin - 12/4/25 at 10:11
 */

class HistoryAdapter @Inject constructor(@ApplicationContext private val context: Context) :
    AppPagingAdapter<Score>(context, DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Score>() {
            override fun areItemsTheSame(oldItem: Score, newItem: Score): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Score,
                newItem: Score
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return if(viewType == EnumConstants.PlayType.TRAINING.ordinal){
            TrainingViewHolder(
                ItemTrainingBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )
        }else{
            HistoryViewHolder(
                ItemHistoryBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position)?.type == EnumConstants.PlayType.TRAINING){
            EnumConstants.PlayType.TRAINING.ordinal
        }else{
            EnumConstants.PlayType.COMPETITION.ordinal
        }
    }


    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        AppViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition)?.let { it1 ->
                        //
                    }
                }
            }
        }

        override fun onBindView(position: Int, item: Score) {
            binding.context = context
            binding.score = item
        }

    }

    inner class TrainingViewHolder(private val binding: ItemTrainingBinding) :
        AppViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition)?.let { it1 ->
                        //
                    }
                }
            }
        }

        override fun onBindView(position: Int, item: Score) {
            binding.context = context
            binding.score = item
        }

    }
}
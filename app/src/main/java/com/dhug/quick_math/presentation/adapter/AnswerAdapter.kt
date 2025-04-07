package com.dhug.quick_math.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhug.quick_math.base.AppAdapter
import com.dhug.quick_math.databinding.ItemAnswerBinding
import dagger.hilt.android.qualifiers.ApplicationContext

class AnswerAdapter(@ApplicationContext private val context: Context) :
    AppAdapter<String>(context) {

    private var listener: OnClickAnswer? = null

    fun setOnListener(listener: OnClickAnswer) {
        this.listener = listener
    }


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): AppAdapter<String>.AppViewHolder {
        return AnswerViewHolder(
            ItemAnswerBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    inner class AnswerViewHolder(private val binding: ItemAnswerBinding) :
        AppViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onAnswer(bindingAdapterPosition, getItem(bindingAdapterPosition))
                }
            }
        }

        override fun onBindView(position: Int) {
            binding.answer = getItem(position)
        }
    }

    interface OnClickAnswer {
        fun onAnswer(position: Int, item: String)
    }

}
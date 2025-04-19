package com.dhug.wiget.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import com.dhug.base.BaseAdapter
import com.dhug.wiget.databinding.ItemTabCenterBinding
import com.dhug.wiget.databinding.ItemTabNormalBinding
import com.dhug.wiget.interfaces.OnTabChangeListener
import com.dhug.wiget.model.TabNavigate
import java.util.ArrayList

class TabNavigationAdapter(private val context: Context) :
    BaseAdapter<TabNavigationAdapter.RootViewHolder>(context) {
    private var currentPosition: Int = 0

    /** List data */
    private var dataSet: MutableList<TabNavigate> = ArrayList()

    private lateinit var listener: OnTabChangeListener

    fun setUpListener(listener: OnTabChangeListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RootViewHolder {

        return when (viewType) {
            TabStyle.NORMAL.ordinal -> NormalItemViewHolder(
                ItemTabNormalBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )

            TabStyle.CENTER.ordinal -> CenterItemViewHolder(
                ItemTabCenterBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )

            TabStyle.LEFT_BORDER.ordinal -> LeftBorderItemViewHolder(
                ItemTabNormalBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )

            TabStyle.RIGHT_BORDER.ordinal -> RightBorderItemViewHolder(
                ItemTabNormalBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )

            else -> NormalItemViewHolder(
                ItemTabNormalBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
        }
    }

    /**
     * Get the total number of data
     */
    fun getCount(): Int {
        return dataSet.size
    }

    /**
     * Set new data
     */
    fun setData(data: MutableList<TabNavigate>?) {
        if (data == null) {
            dataSet.clear()
        } else {
            dataSet = data
        }
        notifyDataSetChanged()
    }

    /**
     * Get current data
     */
    fun getData(): MutableList<TabNavigate> {
        return dataSet
    }

    /**
     *Append some data
     */
    fun addData(data: MutableList<TabNavigate>?) {
        if (data == null || data.isEmpty()) {
            return
        }
        dataSet.addAll(data)
        notifyItemRangeInserted(dataSet.size - data.size, data.size)
    }

    /**
     * Clear current data
     */
    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        dataSet.clear()
        notifyDataSetChanged()
    }

    /**
     * Whether the entry data at a certain location is included
     */
    fun containsItem(@IntRange(from = 0) position: Int): Boolean {
        return containsItem(getItem(position))
    }

    /**
     * Whether it contains certain item data
     */
    fun containsItem(item: TabNavigate?): Boolean {
        return if (item == null) {
            false
        } else dataSet.contains(item)
    }

    /**
     * Get data at a certain location
     */
    fun getItem(@IntRange(from = 0) position: Int): TabNavigate {
        return dataSet[position]
    }

    /**
     * Update data at a certain location
     */
    fun setItem(@IntRange(from = 0) position: Int, item: TabNavigate) {
        dataSet[position] = item
        notifyItemChanged(position)
    }

    /**
     * Add a single piece of data
     */
    fun addItem(item: TabNavigate) {
        addItem(dataSet.size, item)
    }

    fun addItem(@IntRange(from = 0) position: Int, item: TabNavigate) {
        var finalPosition = position
        if (finalPosition < dataSet.size) {
            dataSet.add(finalPosition, item)
        } else {
            dataSet.add(item)
            finalPosition = dataSet.size - 1
        }
        notifyItemInserted(finalPosition)
    }

    /**
     * Delete a single piece of data
     */
    fun removeItem(item: TabNavigate) {
        val index = dataSet.indexOf(item)
        if (index != -1) {
            removeItem(index)
        }
    }

    fun removeItem(@IntRange(from = 0) position: Int) {
        dataSet.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateTabSelected(tabCurrent: Int) {
        currentPosition = tabCurrent
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    companion object {
        enum class TabStyle {
            NORMAL, LEFT_BORDER, RIGHT_BORDER, CENTER
        }
    }

    override fun getItemViewType(position: Int): Int {
        val posCenter = (dataSet.size / 2) - 1
        return if (dataSet.size % 2 == 0) {
            TabStyle.NORMAL.ordinal
        } else {
            when (position) {
                posCenter -> TabStyle.CENTER.ordinal
                posCenter + 1 -> TabStyle.LEFT_BORDER.ordinal
                posCenter - 1 -> TabStyle.RIGHT_BORDER.ordinal
                else -> TabStyle.NORMAL.ordinal
            }
        }
    }


    abstract inner class RootViewHolder : BaseViewHolder {

        constructor(@LayoutRes id: Int) : super(id)

        constructor(itemView: View) : super(itemView)

        init {
            this.itemView.setOnClickListener {
                if (adapterPosition != currentPosition) {
                    currentPosition = adapterPosition
                    listener.onTabSelected(adapterPosition, getItem(adapterPosition))
                }
            }
        }
    }

    inner class CenterItemViewHolder(private val binding: ItemTabCenterBinding) :
        RootViewHolder(binding.root) {
        override fun onBindView(position: Int) {
            //
        }

    }

    inner class NormalItemViewHolder(private val binding: ItemTabNormalBinding) :
        RootViewHolder(binding.root) {
        override fun onBindView(position: Int) {
            val item = getItem(position)
            binding.imvIcon.setImageResource(item.icon)
            binding.tvTitle.text = item.title
        }

    }

    inner class LeftBorderItemViewHolder(private val binding: ItemTabNormalBinding) :
        RootViewHolder(binding.root) {
        override fun onBindView(position: Int) {
            val item = getItem(position)
            binding.imvIcon.setImageResource(item.icon)
            binding.tvTitle.text = item.title
        }

    }

    inner class RightBorderItemViewHolder(private val binding: ItemTabNormalBinding) :
        RootViewHolder(binding.root) {
        override fun onBindView(position: Int) {
            val item = getItem(position)
            binding.imvIcon.setImageResource(item.icon)
            binding.tvTitle.text = item.title
        }

    }
}
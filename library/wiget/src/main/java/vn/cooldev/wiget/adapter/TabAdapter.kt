package vn.cooldev.wiget.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import com.dhug.base.BaseAdapter
import vn.cooldev.wiget.databinding.ItemTabBinding
import vn.cooldev.wiget.interfaces.OnTabUpdateListener
import vn.cooldev.wiget.model.Tab
import java.util.ArrayList

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 20 / 03 / 2025
 */
class TabAdapter(private val context: Context) :
    BaseAdapter<TabAdapter.RootViewHolder>(context) {
    private var currentPosition: Int = 0

    /** List data */
    private var dataSet: MutableList<Tab> = ArrayList()

    private lateinit var listener: OnTabUpdateListener

    fun setUpListener(listener: OnTabUpdateListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RootViewHolder {
        return TabViewHolder(
            ItemTabBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
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
    fun setData(data: MutableList<Tab>?) {
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
    fun getData(): MutableList<Tab> {
        return dataSet
    }

    /**
     *Append some data
     */
    fun addData(data: MutableList<Tab>?) {
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
    fun containsItem(item: Tab?): Boolean {
        return if (item == null) {
            false
        } else dataSet.contains(item)
    }

    /**
     * Get data at a certain location
     */
    fun getItem(@IntRange(from = 0) position: Int): Tab {
        return dataSet[position]
    }

    /**
     * Update data at a certain location
     */
    fun setItem(@IntRange(from = 0) position: Int, item: Tab) {
        dataSet[position] = item
        notifyItemChanged(position)
    }

    /**
     * Add a single piece of data
     */
    fun addItem(item: Tab) {
        addItem(dataSet.size, item)
    }

    fun addItem(@IntRange(from = 0) position: Int, item: Tab) {
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
    fun removeItem(item: Tab) {
        val index = dataSet.indexOf(item)
        if (index != -1) {
            removeItem(index)
        }
    }

    fun removeItem(@IntRange(from = 0) position: Int) {
        dataSet.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTabSelected(tabCurrent: Int) {
        currentPosition = tabCurrent
        if (dataSet.isNotEmpty()) {
            dataSet.forEachIndexed { index, tab ->
                tab.isSelected = index == tabCurrent
                notifyItemChanged(index)
            }
        }
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
                if (adapterPosition != currentPosition || (currentPosition == 2 && getData().size == 5)) {
                    currentPosition = adapterPosition
                    listener.onTabSelected(adapterPosition, getItem(adapterPosition))
                }
            }
        }
    }

    inner class TabViewHolder(private val binding: ItemTabBinding) :
        RootViewHolder(binding.root) {
        override fun onBindView(position: Int) {
            val tab = getItem(position)
            binding.imvIcon.layoutParams = LinearLayout.LayoutParams(
                tab.size.toInt(),
                tab.size.toInt()
            )
            binding.tvTitle.isVisible = tab.title.isNotEmpty()
            binding.tvTitle.text = tab.title
            binding.tvTitle.isSelected = tab.isSelected
            binding.imvIcon.setImageResource(tab.icon)
            binding.imvIcon.isSelected = tab.isSelected

            binding.tvBadge.isVisible = tab.badge.isNotBlank()
            binding.tvBadge.text = tab.badge
        }

    }
}
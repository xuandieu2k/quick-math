package com.dhug.quick_math.base

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import com.dhug.base.BaseAdapter
import java.util.*

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
abstract class AppAdapter<T> constructor(context: Context) :
    BaseAdapter<AppAdapter<T>.AppViewHolder>(context) {

    /** List data */
    private var dataSet: MutableList<T> = ArrayList()

    /** The page number of the current list, the default is the first page, used for paging loading function */
    private var pageNumber = 1

    /** Whether it is the last page, the default is false, used for paging loading function */
    private var lastPage = false

    /** Mark object */
    private var tag: Any? = null

    override fun getItemCount(): Int {
        return getCount()
    }

    /**
     * Get the total number of data
     */
    open fun getCount(): Int {
        return dataSet.size
    }

    /**
     * Set new data
     */
    open fun setData(data: MutableList<T>?) {
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
    open fun getData(): MutableList<T> {
        return dataSet
    }

    /**
     *Append some data
     */
    open fun addData(data: MutableList<T>?) {
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
    open fun clearData() {
        dataSet.clear()
        notifyDataSetChanged()
    }

    /**
     * Whether the entry data at a certain location is included
     */
    open fun containsItem(@IntRange(from = 0) position: Int): Boolean {
        return containsItem(getItem(position))
    }

    /**
     * Whether it contains certain item data
     */
    open fun containsItem(item: T?): Boolean {
        return if (item == null) {
            false
        } else dataSet.contains(item)
    }

    /**
     * Get data at a certain location
     */
    open fun getItem(@IntRange(from = 0) position: Int): T {
        return dataSet[position]
    }

    /**
     * Update data at a certain location
     */
    open fun setItem(@IntRange(from = 0) position: Int, item: T) {
        dataSet[position] = item
        notifyItemChanged(position)
    }

    /**
     * Add a single piece of data
     */
    open fun addItem(item: T) {
        addItem(dataSet.size, item)
    }

    open fun addItem(@IntRange(from = 0) position: Int, item: T) {
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
    open fun removeItem(item: T) {
        val index = dataSet.indexOf(item)
        if (index != -1) {
            removeItem(index)
        }
    }

    open fun removeItem(@IntRange(from = 0) position: Int) {
        dataSet.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * Get the current page number
     */
    open fun getPageNumber(): Int {
        return pageNumber
    }

    /**
     * Set the current page number
     */
    open fun setPageNumber(@IntRange(from = 0) number: Int) {
        pageNumber = number
    }

    /**
     * Whether the current page is the last page
     */
    open fun isLastPage(): Boolean {
        return lastPage
    }

    /**
     * Set whether it is the last page
     */
    open fun setLastPage(last: Boolean) {
        lastPage = last
    }

    /**
     * Get tag
     */
    open fun getTag(): Any? {
        return tag
    }

    /**
     * Set mark
     */
    open fun setTag(tag: Any) {
        this.tag = tag
    }

    abstract inner class AppViewHolder : BaseViewHolder {

        constructor(@LayoutRes id: Int) : super(id)

        constructor(itemView: View) : super(itemView)
    }

    inner class SimpleViewHolder : AppViewHolder {

        constructor(@LayoutRes id: Int) : super(id)

        constructor(itemView: View) : super(itemView)

        override fun onBindView(position: Int) {}
    }
}
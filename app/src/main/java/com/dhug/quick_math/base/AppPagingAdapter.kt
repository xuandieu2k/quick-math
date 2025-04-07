package com.dhug.quick_math.base


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 21 / 12 / 2024
 */

open class AppPagingAdapter<T : Any>(
    private val context: Context,
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, AppPagingAdapter<T>.AppViewHolder>(diffCallback) {

    /** Item click listener */
    private var itemClickListener: OnItemClickListener? = null

    /** Item long click listener */
    private var itemLongClickListener: OnItemLongClickListener? = null

    /** Sub-view click listeners */
    private val childClickListeners = mutableMapOf<Int, OnChildClickListener>()

    /** Sub-view long click listeners */
    private val childLongClickListeners = mutableMapOf<Int, OnChildLongClickListener>()

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.onBindView(position, item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(
            LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        )
    }

    /**
     * Set item click listener
     */
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        itemClickListener = listener
    }

    /**
     * Set item long click listener
     */
    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        itemLongClickListener = listener
    }

    /**
     * Set child view click listener
     */
    fun setOnChildClickListener(viewId: Int, listener: OnChildClickListener) {
        childClickListeners[viewId] = listener
    }

    /**
     * Set child view long click listener
     */
    fun setOnChildLongClickListener(viewId: Int, listener: OnChildLongClickListener) {
        childLongClickListeners[viewId] = listener
    }

    open inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        open fun onBindView(position: Int, item: T) {
            // Bind your data here
        }

        override fun onClick(view: View) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (view == itemView) {
                    itemClickListener?.onItemClick(view, position)
                } else {
                    childClickListeners[view.id]?.onChildClick(view, position)
                }
            }
        }

        override fun onLongClick(view: View): Boolean {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (view == itemView) {
                    return itemLongClickListener?.onItemLongClick(view, position) ?: false
                } else {
                    return childLongClickListeners[view.id]?.onChildLongClick(view, position) ?: false
                }
            }
            return false
        }
    }

    /**
     * Item click listener interface
     */
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    /**
     * Item long click listener interface
     */
    interface OnItemLongClickListener {
        fun onItemLongClick(view: View, position: Int): Boolean
    }

    /**
     * Child view click listener interface
     */
    interface OnChildClickListener {
        fun onChildClick(view: View, position: Int)
    }

    /**
     * Child view long click listener interface
     */
    interface OnChildLongClickListener {
        fun onChildLongClick(view: View, position: Int): Boolean
    }
}

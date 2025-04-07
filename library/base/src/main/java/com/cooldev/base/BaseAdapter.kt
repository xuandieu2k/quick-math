package com.cooldev.base

import android.content.*
import android.util.SparseArray
import android.view.*
import android.view.View.OnLongClickListener
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooldev.base.action.ResourcesAction

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
@Suppress("LeakingThis")
abstract class BaseAdapter<VH : BaseAdapter<VH>.BaseViewHolder> (private val context: Context) :
    RecyclerView.Adapter<VH>(), ResourcesAction {

    /** RecyclerView object */
    private var recyclerView: RecyclerView? = null

    /** Item click listener */
    private var itemClickListener: OnItemClickListener? = null

    /** Entry long press listener */
    private var itemLongClickListener: OnItemLongClickListener? = null

    /** Entry sub-view click listener */
    private val childClickListeners: SparseArray<OnChildClickListener?> by lazy { SparseArray() }

    /** Entry sub-view long press listener */
    private val childLongClickListeners: SparseArray<OnChildLongClickListener?> by lazy { SparseArray() }

    /** ViewHolder position offset value */
    private var positionOffset: Int = 0

    override fun onBindViewHolder(holder: VH, position: Int) {
        // Compare based on the position bound by ViewHolder and the incoming position
        // Generally, these two position values are equal, but there is a special case
        // When adding a head View to the outer layer, the two position values are not equal.
        positionOffset = position - holder.adapterPosition
        holder.onBindView(position)
    }

    /**
     * Get the RecyclerView object
     */
    open fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    override fun getContext(): Context {
        return context
    }

    /**
     * Entry ViewHolder, requires subclass ViewHolder inheritance
     */
    abstract inner class BaseViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener, OnLongClickListener {

        constructor(@LayoutRes id: Int) : this(
            LayoutInflater.from(getContext()).inflate(id, recyclerView, false)
        )

        init {
            //Set the click and long press events of the item
            if (itemClickListener != null) {
                itemView.setOnClickListener(this)
            }

            if (itemLongClickListener != null) {
                itemView.setOnLongClickListener(this)
            }

            //Set the item sub-View click event
            for (i in 0 until childClickListeners.size()) {
                findViewById<View>(childClickListeners.keyAt(i))?.setOnClickListener(this)
            }

            //Set the item sub-View long press event
            for (i in 0 until childLongClickListeners.size()) {
                findViewById<View>(childLongClickListeners.keyAt(i))?.setOnLongClickListener(this)
            }
        }

        /**
         * Data binding callback
         */
        abstract fun onBindView(position: Int)

        /**
         * Get ViewHolder position
         */
        protected open fun getViewHolderPosition(): Int {
            // Here is an explanation why getLayoutPosition is used instead of getAdapterPosition
            // If you use getAdapterPosition, it will cause a problem, that is, when you quickly click to delete an entry, -1 will appear because the ViewHolder has been unbound.
            // When using getLayoutPosition, the position will not be -1, because it will not disappear immediately in the layout after unbinding, so there is no need to worry about exceptions in obtaining the position during animation execution.
            return layoutPosition + positionOffset
        }

        /**
         * [View.OnClickListener]
         */
        override fun onClick(view: View) {
            val position: Int = getViewHolderPosition()
            if (position < 0 || position >= itemCount) {
                return
            }
            if (view === getItemView()) {
                itemClickListener?.onItemClick(recyclerView, view, position)
                return
            }
            childClickListeners.get(view.id)?.onChildClick(recyclerView, view, position)
        }

        /**
         * [View.OnLongClickListener]
         */
        override fun onLongClick(view: View): Boolean {
            val position: Int = getViewHolderPosition()
            if (position < 0 || position >= itemCount) {
                return false
            }
            if (view === getItemView()) {
                if (itemLongClickListener != null) {
                    return itemLongClickListener!!.onItemLongClick(recyclerView, view, position)
                }
                return false
            }
            val listener: OnChildLongClickListener? = childLongClickListeners.get(view.id)
            if (listener != null) {
                return listener.onChildLongClick(recyclerView, view, position)
            }
            return false
        }

        open fun getItemView(): View {
            return itemView
        }

        open fun <V : View?> findViewById(@IdRes id: Int): V? {
            return getItemView().findViewById(id)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        // Determine whether the current layout manager is empty, and if it is empty, set the default layout manager
        if (this.recyclerView?.layoutManager == null) {
            this.recyclerView?.layoutManager = generateDefaultLayoutManager(context)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
    }

    /**
     * Generate default layout placer
     */
    protected open fun generateDefaultLayoutManager(context: Context): RecyclerView.LayoutManager? {
        return LinearLayoutManager(context)
    }

    /**
     * Set up RecyclerView entry click listening
     */
    open fun setOnItemClickListener(listener: OnItemClickListener?) {
        checkRecyclerViewState()
        itemClickListener = listener
    }

    /**
     * Set up RecyclerView entry sub-View click monitoring
     */
    open fun setOnChildClickListener(@IdRes id: Int, listener: OnChildClickListener?) {
        checkRecyclerViewState()
        childClickListeners.put(id, listener)
    }

    /**
     * Set up RecyclerView entry long press monitoring
     */
    open fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        checkRecyclerViewState()
        itemLongClickListener = listener
    }

    /**
     * Set up RecyclerView entry sub-View long press monitoring
     */
    open fun setOnChildLongClickListener(@IdRes id: Int, listener: OnChildLongClickListener?) {
        checkRecyclerViewState()
        childLongClickListeners.put(id, listener)
    }

    /**
     * Check RecyclerView status
     */
    private fun checkRecyclerViewState() {
        if (recyclerView != null) {
            // The listener must be set before RecyclerView.setAdapter()
            throw IllegalStateException("are you ok?")
        }
    }

    /**
     * RecyclerView item click listening class
     */
    interface OnItemClickListener {

        /**
         * Called back when an item in RecyclerView is clicked
         *
         * @param recyclerView RecyclerView object
         * @param itemView the clicked item object
         * @param position The position of the clicked item
         */
        fun onItemClick(recyclerView: RecyclerView?, itemView: View?, position: Int)
    }

    /**
     * RecyclerView entry long press listening class
     */
    interface OnItemLongClickListener {

        /**
         * Called back when an item in RecyclerView is long pressed
         *
         * @param recyclerView RecyclerView object
         * @param itemView the clicked item object
         * @param position The position of the clicked item
         * @return whether to intercept the event
         */
        fun onItemLongClick(recyclerView: RecyclerView?, itemView: View?, position: Int): Boolean
    }

    /**
     * RecyclerView item sub-View click listening class
     */
    interface OnChildClickListener {

        /**
         * Called back when a RecyclerView item sub-view is clicked
         *
         * @param recyclerView RecyclerView object
         * @param childView the clicked item subview
         * @param position The position of the clicked item
         */
        fun onChildClick(recyclerView: RecyclerView?, childView: View?, position: Int)
    }

    /**
     * RecyclerView item sub-View long press listening class
     */
    interface OnChildLongClickListener {

        /**
         * Called back when an item sub-view of RecyclerView is long-pressed
         *
         * @param recyclerView RecyclerView object
         * @param childView the clicked item subview
         * @param position The position of the clicked item
         */
        fun onChildLongClick(recyclerView: RecyclerView?, childView: View?, position: Int): Boolean
    }
}
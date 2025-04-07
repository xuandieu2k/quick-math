package vn.cooldev.wiget.wiget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import vn.cooldev.wiget.adapter.TabAdapter
import vn.cooldev.wiget.interfaces.OnTabUpdateListener
import vn.cooldev.wiget.model.Tab

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 20 / 03 / 2025
 */
class TabBottomNavigationView(context: Context, attrs: AttributeSet?) :
    RecyclerView(context, attrs), OnTabUpdateListener {
    private lateinit var tabAdapter: TabAdapter
    private lateinit var listener: OnTabUpdateListener

    init {
        initAdapter()
        initRecyclerView(this, tabAdapter, 5)
        tabAdapter.updateTabSelected(0)
    }

    private fun initAdapter() {
        tabAdapter = TabAdapter(context)
        tabAdapter.setUpListener(this)
    }

    fun addListenerTabChange(listener: OnTabUpdateListener) {
        this.listener = listener
    }

    fun updateBadge(badge: String, tabIndex: Int) {
        val tab = tabAdapter.getItem(tabIndex)
        tab.badge = badge
        tabAdapter.setItem(tabIndex, tab)
    }

    fun updateCurrentTab(currentTab: Int) {
        tabAdapter.updateTabSelected(currentTab)
    }

    fun getCurrentView(): RecyclerView {
        return this
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: MutableList<Tab>) {
        tabAdapter.setData(list)
        tabAdapter.notifyDataSetChanged()
        invalidate()
    }

    fun initRecyclerView(
        view: RecyclerView, adapter: RecyclerView.Adapter<*>?, count: Int
    ) {
        configRecyclerView(view, GridLayoutManager(view.context, count))
        view.adapter = adapter
    }

    private fun configRecyclerView(
        recyclerView: RecyclerView,
        layoutManager: RecyclerView.LayoutManager?,
        isNestedScrollingEnabled: Boolean = false
    ) {
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
        (recyclerView.itemAnimator)!!.changeDuration = 0
        ((recyclerView.itemAnimator) as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.isNestedScrollingEnabled = isNestedScrollingEnabled
    }


    override fun onTabSelected(position: Int, tab: Tab) {
        if (position == 2 && tabAdapter.getData().size == 5) {
            listener.onTabSelected(position, tab)
            return
        }
        if (tab.isSelected) {
            listener.onTabSelected(position, tab)
            return
        }
        tabAdapter.updateTabSelected(position)
        listener.onTabSelected(position, tab)
    }
}
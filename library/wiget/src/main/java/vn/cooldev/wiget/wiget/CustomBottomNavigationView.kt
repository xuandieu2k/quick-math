package vn.cooldev.wiget.wiget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import vn.cooldev.wiget.R
import vn.cooldev.wiget.adapter.TabNavigationAdapter
import vn.cooldev.wiget.interfaces.OnTabChangeListener
import vn.cooldev.wiget.model.TabNavigate

@SuppressLint("ResourceAsColor")
class CustomBottomNavigationView(context: Context, attrs: AttributeSet?) :
    RecyclerView(context, attrs), OnTabChangeListener {
    private lateinit var tabAdapter: TabNavigationAdapter
    private lateinit var listener: OnTabChangeListener

    init {
        this.backgroundTintList = ColorStateList.valueOf(android.R.color.black)
        initAdapter()
        initRecyclerView(this, tabAdapter, 5)
    }

    private fun initAdapter() {
        tabAdapter = TabNavigationAdapter(context)
        tabAdapter.setUpListener(this)
    }

    fun addListenerTabChange(listener: OnTabChangeListener){
        this.listener = listener
    }

    fun setData(list: MutableList<TabNavigate>) {
        tabAdapter.setData(list)
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

    override fun onTabSelected(position: Int, tab: TabNavigate) {
        //
    }
}
package vn.cooldev.wiget.interfaces

import vn.cooldev.wiget.model.TabNavigate

interface OnTabChangeListener {
    fun onTabSelected(position: Int, tab: TabNavigate)
}
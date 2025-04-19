package com.dhug.wiget.interfaces

import com.dhug.wiget.model.TabNavigate

interface OnTabChangeListener {
    fun onTabSelected(position: Int, tab: TabNavigate)
}
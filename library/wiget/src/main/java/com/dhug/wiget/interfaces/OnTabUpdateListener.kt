package com.dhug.wiget.interfaces

import com.dhug.wiget.model.Tab

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 20 / 03 / 2025
 */
interface OnTabUpdateListener {
    fun onTabSelected(position: Int, tab: Tab)
}
package vn.cooldev.wiget.model


/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 20 / 03 / 2025
 */
data class Tab(
    var tabId: Int = 0,
    var title: String = "",
    var icon: Int = 0,
    var isSelected: Boolean = false,
    var size: Float = 0f,
    var colorText: Int = com.cooldev.base.R.color.black,
    var badge: String = ""
) {
}
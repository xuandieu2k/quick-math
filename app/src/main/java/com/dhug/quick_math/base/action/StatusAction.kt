//package com.dhug.quick_math.base.action
//
//import android.graphics.drawable.Drawable
//import android.net.ConnectivityManager
//import android.net.NetworkInfo
//import androidx.annotation.DrawableRes
//import androidx.annotation.RawRes
//import androidx.annotation.StringRes
//import androidx.core.content.ContextCompat
//import com.dhug.quick_math.R
//
///**
// * author: Android Wheel Brother
// * github : https://github.com/getActivity/AndroidProject-Kotlin
// * time: 2019/12/08
// * desc: Status layout intention
// */
//interface StatusAction {
//
//    /**
//     * Get status layout
//     */
//    fun getStatusLayout(): StatusLayout?
//
//    /**
//     * Display loading
//     */
//    fun showLoading(@RawRes id: Int = R.raw.loading) {
//        getStatusLayout()?.let {
//            it.show()
//            it.setAnimResource(id)
//            it.setHint("")
//            it.setOnRetryListener(null)
//        }
//    }
//
//    /**
//     * Display loading is complete
//     */
//    fun showComplete() {
//        getStatusLayout()?.let {
//            if (!it.isShow()) {
//                Return
//            }
//            it.hide()
//        }
//    }
//
//    /**
//     * Show empty prompt
//     */
//    fun showEmpty() {
//        showLayout(R.drawable.status_empty_ic, R.string.status_layout_no_data, null)
//    }
//
//    /**
//     * Show error message
//     */
//    fun showError(listener: OnRetryListener?) {
//        getStatusLayout()?.let {
//            val manager: ConnectivityManager? = ContextCompat.getSystemService(it.context, ConnectivityManager::class.java)
//            if (manager != null) {
//                val info: NetworkInfo? = manager.activeNetworkInfo
//                // Determine whether the network is connected
//                if (info == null || !info.isConnected) {
//                    showLayout(R.drawable.status_network_ic, R.string.status_layout_error_network, listener)
//                    Return
//                }
//            }
//            showLayout(R.drawable.status_error_ic, R.string.status_layout_error_request, listener)
//        }
//    }
//
//    /**
//     * Show custom prompts
//     */
//    fun showLayout(@DrawableRes drawableId: Int, @StringRes stringId: Int, listener: OnRetryListener?) {
//        getStatusLayout()?.let {
//            showLayout(ContextCompat.getDrawable(it.context, drawableId), it.context.getString(stringId), listener)
//        }
//    }
//
//    fun showLayout(drawable: Drawable?, hint: CharSequence?, listener: OnRetryListener?) {
//        getStatusLayout()?.let {
//            it.show()
//            it.setIcon(drawable)
//            it.setHint(hint)
//            it.setOnRetryListener(listener)
//        }
//    }
//}
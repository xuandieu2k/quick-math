package com.dhug.example.base

import com.cooldev.base.BaseAdsFragment
import com.cooldev.base.BaseFragment
import com.dhug.example.base.action.ToastAction
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
@AndroidEntryPoint
abstract class AppFragment : BaseFragment(),
    ToastAction {

    /**
     * Whether the current loading dialog box is being displayed
     */
    open fun isShowDialog(): Boolean {
        val activity = getAttachActivity() as? AppActivity ?: return false
        return activity.isShowDialog()
    }

    /**
     * Show loading dialog
     */
    open fun showDialog() {
        (getAttachActivity() as? AppActivity)?.showDialog()
    }

    /**
     * Hide loading dialog
     */
    open fun hideDialog() {
        (getAttachActivity() as? AppActivity)?.hideDialog()
    }
}
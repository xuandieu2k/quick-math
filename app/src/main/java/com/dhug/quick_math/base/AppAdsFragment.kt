package com.dhug.quick_math.base

import com.dhug.base.BaseAdsFragment
import com.dhug.quick_math.base.action.ToastAction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class AppAdsFragment : BaseAdsFragment(),
    ToastAction {

    /**
     * Whether the current loading dialog box is being displayed
     */
    open fun isShowDialog(): Boolean {
        val activity = getAttachActivity() as? AppAdsActivity ?: return false
        return activity.isShowDialog()
    }

    /**
     * Show loading dialog
     */
    open fun showDialog() {
        (getAttachActivity() as? AppAdsActivity)?.showDialog()
    }

    /**
     * Hide loading dialog
     */
    open fun hideDialog() {
        (getAttachActivity() as? AppAdsActivity)?.hideDialog()
    }
}
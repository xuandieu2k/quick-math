package com.dhug.base

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.*
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.*
import android.util.SparseArray
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.*
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.cooldev.base.R
import com.dhug.base.action.ActivityAction
import com.dhug.base.action.AnimAction
import com.dhug.base.action.ClickAction
import com.dhug.base.action.HandlerAction
import com.dhug.base.action.KeyboardAction
import com.dhug.base.action.ResourcesAction
import java.lang.ref.SoftReference
import java.util.*

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
@Suppress("LeakingThis")
open class BaseDialog constructor(context: Context, @StyleRes themeResId: Int = R.style.BaseDialogTheme) :
    AppCompatDialog(context, themeResId), LifecycleOwner, ActivityAction, ResourcesAction,
    HandlerAction, ClickAction, AnimAction, KeyboardAction, DialogInterface.OnShowListener,
    DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

    private val listeners: ListenersWrapper<BaseDialog> = ListenersWrapper(this)
    private val lifecycleCurrent: LifecycleRegistry = LifecycleRegistry(this)
    private var showListeners: MutableList<OnShowListener?>? = null
    private var cancelListeners: MutableList<OnCancelListener?>? = null
    private var dismissListeners: MutableList<OnDismissListener?>? = null

    /**
     * Get the root layout of Dialog
     */
    open fun getContentView(): View? {
        val contentView: View? = findViewById(Window.ID_ANDROID_CONTENT)
        if (contentView is ViewGroup && contentView.childCount == 1) {
            return contentView.getChildAt(0)
        }
        return contentView
    }

    /**
     * Set Dialog width
     */
    open fun setWidth(width: Int) {
        val window: Window = window ?: return
        val params: WindowManager.LayoutParams? = window.attributes
        params?.width = width
        window.attributes = params
    }

    /**
     * Set Dialog height
     */
    open fun setHeight(height: Int) {
        val window: Window = window ?: return
        val params: WindowManager.LayoutParams? = window.attributes
        params?.height = height
        window.attributes = params
    }

    /**
     * Set horizontal offset
     */
    open fun setXOffset(offset: Int) {
        val window: Window = window ?: return
        val params: WindowManager.LayoutParams? = window.attributes
        params?.x = offset
        window.attributes = params
    }

    /**
     * Set vertical offset
     */
    open fun setYOffset(offset: Int) {
        val window: Window = window ?: return
        val params: WindowManager.LayoutParams? = window.attributes
        params?.y = offset
        window.attributes = params
    }

    /**
     * Get Dialog center of gravity
     */
    open fun getGravity(): Int {
        val window: Window = window ?: return Gravity.NO_GRAVITY
        val params: WindowManager.LayoutParams = window.attributes ?: return Gravity.NO_GRAVITY
        return params.gravity
    }

    /**
     * Set Dialog center of gravity
     */
    open fun setGravity(gravity: Int) {
        window?.setGravity(gravity)
    }

    /**
     * Set up Dialog animation
     */
    open fun setWindowAnimations(@StyleRes id: Int) {
        window?.setWindowAnimations(id)
    }

    /**
     * Get animation of Dialog
     */
    open fun getWindowAnimations(): Int {
        val window: Window = window ?: return AnimAction.Companion.ANIM_DEFAULT
        return window.attributes.windowAnimations
    }

    /**
     * 设置背景遮盖层开关
     */
    open fun setBackgroundDimEnabled(enabled: Boolean) {
        if (enabled) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        } else {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }

    /**
     * 设置背景遮盖层的透明度（前提条件是背景遮盖层开关必须是为开启状态）
     */
    open fun setBackgroundDimAmount(@FloatRange(from = 0.0, to = 1.0) dimAmount: Float) {
        window?.setDimAmount(dimAmount)
    }

    override fun dismiss() {
        removeCallbacks()
        val focusView: View? = currentFocus
        if (focusView != null) {
            getSystemService(InputMethodManager::class.java).hideSoftInputFromWindow(focusView.windowToken, 0)
        }
        super.dismiss()
    }

    override val lifecycle: Lifecycle
        get() = lifecycleCurrent

    /**
     * 设置一个显示监听器
     *
     * @param listener       显示监听器对象
     */
    @Deprecated("请使用 {@link #addOnShowListener(BaseDialog.OnShowListener)}}")
    override fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
        if (listener == null) {
            return
        }
        addOnShowListener(ShowListenerWrapper(listener))
    }

    /**
     * 设置一个取消监听器
     *
     * @param listener       取消监听器对象
     */
    @Deprecated("请使用 {@link #addOnCancelListener(BaseDialog.OnCancelListener)}")
    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        if (listener == null) {
            return
        }
        addOnCancelListener(CancelListenerWrapper(listener))
    }

    /**
     * 设置一个销毁监听器
     *
     * @param listener       销毁监听器对象
     */
    @Deprecated("请使用 {@link #addOnDismissListener(BaseDialog.OnDismissListener)}")
    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        if (listener == null) {
            return
        }
        addOnDismissListener(DismissListenerWrapper(listener))
    }

    /**
     * 设置一个按键监听器
     *
     * @param listener       按键监听器对象
     */
    @Deprecated("请使用 {@link #setOnKeyListener(BaseDialog.OnKeyListener)}", ReplaceWith(
        "super.setOnKeyListener(listener)",
        "androidx.appcompat.app.AppCompatDialog"))
    override fun setOnKeyListener(listener: DialogInterface.OnKeyListener?) {
        super.setOnKeyListener(listener)
    }

    open fun setOnKeyListener(listener: OnKeyListener?) {
        super.setOnKeyListener(KeyListenerWrapper(listener))
    }

    /**
     * 添加一个显示监听器
     *
     * @param listener      监听器对象
     */
    open fun addOnShowListener(listener: OnShowListener?) {
        if (showListeners == null) {
            showListeners = ArrayList()
            super.setOnShowListener(listeners)
        }
        showListeners?.add(listener)
    }

    /**
     * 添加一个取消监听器
     *
     * @param listener      监听器对象
     */
    open fun addOnCancelListener(listener: OnCancelListener?) {
        if (cancelListeners == null) {
            cancelListeners = ArrayList()
            super.setOnCancelListener(listeners)
        }
        cancelListeners?.add(listener)
    }

    /**
     * 添加一个销毁监听器
     *
     * @param listener      监听器对象
     */
    open fun addOnDismissListener(listener: OnDismissListener?) {
        if (dismissListeners == null) {
            dismissListeners = ArrayList()
            super.setOnDismissListener(listeners)
        }
        dismissListeners?.add(listener)
    }

    /**
     * 移除一个显示监听器
     *
     * @param listener      监听器对象
     */
    open fun removeOnShowListener(listener: OnShowListener?) {
        showListeners?.remove(listener)
    }

    /**
     * 移除一个取消监听器
     *
     * @param listener      监听器对象
     */
    open fun removeOnCancelListener(listener: OnCancelListener?) {
        cancelListeners?.remove(listener)
    }

    /**
     * 移除一个销毁监听器
     *
     * @param listener      监听器对象
     */
    open fun removeOnDismissListener(listener: OnDismissListener?) {
        dismissListeners?.remove(listener)
    }

    /**
     * 设置显示监听器集合
     */
    private fun setOnShowListeners(listeners: MutableList<OnShowListener?>?) {
        super.setOnShowListener(this.listeners)
        showListeners = listeners
    }

    /**
     * Set cancellation listener collection
     */
    private fun setOnCancelListeners(listeners: MutableList<OnCancelListener?>?) {
        super.setOnCancelListener(this.listeners)
        cancelListeners = listeners
    }

    /**
     * Set up a collection of destruction listeners
     */
    private fun setOnDismissListeners(listeners: MutableList<OnDismissListener?>?) {
        super.setOnDismissListener(this.listeners)
        dismissListeners = listeners
    }

    /**
     * [DialogInterface.OnShowListener]
     */
    override fun onShow(dialog: DialogInterface?) {
        lifecycleCurrent.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        showListeners?.let {
            for (i in it.indices) {
                it[i]?.onShow(this)
            }
        }
    }

    /**
     * [DialogInterface.OnCancelListener]
     */
    override fun onCancel(dialog: DialogInterface?) {
        cancelListeners?.let {
            for (i in it.indices) {
                it[i]?.onCancel(this)
            }
        }
    }

    /**
     * [DialogInterface.OnDismissListener]
     */
    override fun onDismiss(dialog: DialogInterface?) {
        lifecycleCurrent.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        dismissListeners?.let {
            for (i in it.indices) {
                it[i]?.onDismiss(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleCurrent.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onStart() {
        super.onStart()
        lifecycleCurrent.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onStop() {
        super.onStop()
        lifecycleCurrent.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    @Suppress("UNCHECKED_CAST")
    open class Builder<B : Builder<B>> constructor(private val context: Context) :
        ActivityAction, ResourcesAction, ClickAction, KeyboardAction {

        /** Dialog object */
        private var dialog: BaseDialog? = null

        /** Dialog layout */
        private var contentView: View? = null

        /** theme style */
        private var themeId: Int = R.style.BaseDialogTheme

        /** animation style */
        private var animStyle: Int = AnimAction.Companion.ANIM_DEFAULT

        /** width and height */
        private var width: Int = WindowManager.LayoutParams.WRAP_CONTENT
        private var height: Int = WindowManager.LayoutParams.WRAP_CONTENT

        /** Center of gravity position */
        private var gravity: Int = Gravity.NO_GRAVITY

        /** Horizontal offset */
        private var xOffset: Int = 0

        /** Vertical offset */
        private var yOffset: Int = 0

        /** Whether it can be canceled */
        private var cancelable: Boolean = true

        /** Whether clicking on the blank space can cancel it? The premise is that this dialog box can be canceled */
        private var canceledOnTouchOutside: Boolean = true

        /** Background cover switch */
        private var backgroundDimEnabled: Boolean = true

        /** Background mask transparency */
        private var backgroundDimAmount: Float = 0.5f

        /** Dialog creates a listener */
        private var createListener: OnCreateListener? = null

        /** Dialog display monitoring */
        private val showListeners: MutableList<OnShowListener?> by lazy { ArrayList() }

        /** Dialog cancels monitoring */
        private val cancelListeners: MutableList<OnCancelListener?> by lazy { ArrayList() }

        /** Dialog destruction monitoring */
        private val dismissListeners: MutableList<OnDismissListener?> by lazy { ArrayList() }

        /** Dialog key monitoring */
        private var keyListener: OnKeyListener? = null

        /** Click event collection */
        private var clickArray: SparseArray<OnClickListener<View>?>? = null

        /**
         * Set layout
         */
        open fun setContentView(@LayoutRes id: Int): B {
            // Here is an explanation why new FrameLayout needs to be passed, because if it is not passed, the LayoutParams object obtained by the XML root layout will be empty, which will result in the width and height parameters not being parsed.
            return setContentView(
                LayoutInflater.from(context).inflate(id, FrameLayout(context), false)
            )
        }

        open fun setContentView(view: View?): B {
            // Please do not pass in an empty layout
            if (view == null) {
                throw IllegalArgumentException("are you ok?")
            }
            contentView = view
            if (isCreated()) {
                dialog?.setContentView(view)
                return this as B
            }
            val layoutParams: ViewGroup.LayoutParams? = contentView?.layoutParams
            if ((layoutParams != null) && (width == ViewGroup.LayoutParams.WRAP_CONTENT) && (height == ViewGroup.LayoutParams.WRAP_CONTENT)) {
                // If the width and height of the current Dialog are set to be adaptive, the width and height set in the layout will be the main one.
                setWidth(layoutParams.width)
                setHeight(layoutParams.height)
            }

            // If the center of gravity is not currently set, automatically obtain the layout center of gravity.
            if (gravity == Gravity.NO_GRAVITY) {
                if (layoutParams is FrameLayout.LayoutParams) {
                    val gravity: Int = layoutParams.gravity
                    if (gravity != FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY) {
                        setGravity(gravity)
                    }
                } else if (layoutParams is LinearLayout.LayoutParams) {
                    val gravity: Int = layoutParams.gravity
                    if (gravity != Gravity.NO_GRAVITY) {
                        setGravity(gravity)
                    }
                }
                if (gravity == Gravity.NO_GRAVITY) {
                    //The default center of gravity is centered
                    setGravity(Gravity.CENTER)
                }
            }
            return this as B
        }

        /**
         * Set theme id
         */
        open fun setThemeStyle(@StyleRes id: Int): B {
            themeId = id
            if (isCreated()) {
                // The theme id cannot be set after the Dialog is created.
                throw IllegalStateException("are you ok?")
            }
            return this as B
        }

        /**
         * Set animation, several styles have been encapsulated, see the [AnimAction] class for details
         */
        open fun setAnimStyle(@StyleRes id: Int): B {
            animStyle = id
            if (isCreated()) {
                dialog?.setWindowAnimations(id)
            }
            return this as B
        }

        /**
         * Set width
         */
        open fun setWidth(width: Int): B {
            this.width = width
            if (isCreated()) {
                dialog?.setWidth(width)
                return this as B
            }

            //Here is an explanation of why LayoutParams needs to be reset.
            // Because if you don't set it like this, the first time it is displayed, it will be displayed according to the Dialog width and height.
            // But after the Layout content is changed, it will not be displayed according to the previously set width and height.
            // So here we need to also set the LayoutParams of View

            val params: ViewGroup.LayoutParams? = contentView?.layoutParams
            if (params != null) {
                params.width = width
                contentView?.layoutParams = params
            }
            return this as B
        }

        /**
         * Set height
         */
        open fun setHeight(height: Int): B {
            this.height = height
            if (isCreated()) {
                dialog?.setHeight(height)
                return this as B
            }

            //Here is an explanation of why LayoutParams needs to be reset.
            // Because if this is not set, the first time it is displayed, it will be displayed according to the Dialog width and height.
            // But after the Layout content is changed, it will not be displayed according to the previously set width and height.
            // So here we need to also set the LayoutParams of View
            val params: ViewGroup.LayoutParams? = contentView?.layoutParams
            if (params != null) {
                params.height = height
                contentView?.layoutParams = params
            }
            return this as B
        }

        /**
         * Set the center of gravity position
         */
        open fun setGravity(gravity: Int): B {
            // Adapt layout in reverse direction
            this.gravity = Gravity.getAbsoluteGravity(gravity, getResources().configuration.layoutDirection)
            if (isCreated()) {
                dialog?.setGravity(gravity)
            }
            return this as B
        }

        /**
         * Set horizontal offset
         */
        open fun setXOffset(offset: Int): B {
            xOffset = offset
            if (isCreated()) {
                dialog?.setXOffset(offset)
            }
            return this as B
        }

        /**
         * Set vertical offset
         */
        open fun setYOffset(offset: Int): B {
            yOffset = offset
            if (isCreated()) {
                this.dialog?.setYOffset(offset)
            }
            return this as B
        }

        /**
         * Is it possible to cancel?
         */
        open fun setCancelable(cancelable: Boolean): B {
            this.cancelable = cancelable
            if (isCreated()) {
                dialog?.setCancelable(cancelable)
            }
            return this as B
        }

        /**
         * Is it possible to cancel by clicking on a blank area?
         */
        open fun setCanceledOnTouchOutside(cancel: Boolean): B {
            canceledOnTouchOutside = cancel
            if (isCreated() && cancelable) {
                dialog?.setCanceledOnTouchOutside(cancel)
            }
            return this as B
        }

        /**
         *Set the background mask switch
         */
        open fun setBackgroundDimEnabled(enabled: Boolean): B {
            backgroundDimEnabled = enabled
            if (isCreated()) {
                dialog?.setBackgroundDimEnabled(enabled)
            }
            return this as B
        }

        /**
         * Set the transparency of the background covering layer (the prerequisite is that the background covering layer switch must be turned on)
         */
        open fun setBackgroundDimAmount(@FloatRange(from = 0.0, to = 1.0) dimAmount: Float): B {
            backgroundDimAmount = dimAmount
            if (isCreated()) {
                dialog?.setBackgroundDimAmount(dimAmount)
            }
            return this as B
        }

        /**
         * Set up creation monitoring
         */
        open fun setOnCreateListener(listener: OnCreateListener): B {
            createListener = listener
            return this as B
        }

        /**
         * Add display monitoring
         */
        open fun addOnShowListener(listener: OnShowListener): B {
            showListeners.add(listener)
            return this as B
        }

        /**
         * Add cancel monitoring
         */
        open fun addOnCancelListener(listener: OnCancelListener): B {
            cancelListeners.add(listener)
            return this as B
        }

        /**
         * Add destruction monitoring
         */
        open fun addOnDismissListener(listener: OnDismissListener): B {
            dismissListeners.add(listener)
            return this as B
        }

        /**
         * Set up key monitoring
         */
        open fun setOnKeyListener(listener: OnKeyListener): B {
            keyListener = listener
            if (isCreated()) {
                dialog?.setOnKeyListener(listener)
            }
            return this as B
        }

        /**
         * Set text
         */
        open fun setText(@IdRes viewId: Int, @StringRes stringId: Int): B {
            return setText(viewId, getString(stringId))
        }

        open fun setText(@IdRes id: Int, text: CharSequence?): B {
            (findViewById<View?>(id) as TextView?)?.text = text
            return this as B
        }

        /**
         * Set text color
         */
        open fun setTextColor(@IdRes id: Int, @ColorInt color: Int): B {
            (findViewById<View?>(id) as TextView?)?.setTextColor(color)
            return this as B
        }

        /**
         * Setup tips
         */
        open fun setHint(@IdRes viewId: Int, @StringRes stringId: Int): B {
            return setHint(viewId, getString(stringId))
        }

        open fun setHint(@IdRes id: Int, text: CharSequence?): B {
            (findViewById<View?>(id) as TextView?)?.hint = text
            return this as B
        }

        /**
         * Set visible status
         */
        open fun setVisibility(@IdRes id: Int, visibility: Int): B {
            findViewById<View?>(id)?.visibility = visibility
            return this as B
        }

        /**
         * Set background
         */
        open fun setBackground(@IdRes viewId: Int, @DrawableRes drawableId: Int): B {
            return setBackground(viewId, ContextCompat.getDrawable(context, drawableId))
        }

        open fun setBackground(@IdRes id: Int, drawable: Drawable?): B {
            findViewById<View?>(id)?.background = drawable
            return this as B
        }

        /**
         * Set picture
         */
        open fun setImageDrawable(@IdRes viewId: Int, @DrawableRes drawableId: Int): B {
            return setBackground(viewId, ContextCompat.getDrawable(context, drawableId))
        }

        open fun setImageDrawable(@IdRes id: Int, drawable: Drawable?): B {
            (findViewById<View?>(id) as ImageView?)?.setImageDrawable(drawable)
            return this as B
        }

        /**
         * Set click event
         */
        open fun setOnClickListener(@IdRes id: Int, listener: OnClickListener<out View>): B {
            if (clickArray == null) {
                clickArray = SparseArray()
            }
            clickArray!!.put(id, listener as OnClickListener<View>)
            if (isCreated()) {
                dialog?.findViewById<View?>(id)?.setOnClickListener(ViewClickWrapper(dialog, listener))
            }
            return this as B
        }

        /**
         * Create
         */
        @Suppress("RtlHardcoded")
        open fun create(): BaseDialog {
            // Determine whether the layout is empty
            if (contentView == null) {
                throw IllegalArgumentException("are you ok?")
            }

            // If currently displaying
            if (isShowing()) {
                dismiss()
            }

            // If the center of gravity is not currently set, set a default center of gravity.
            if (gravity == Gravity.NO_GRAVITY) {
                gravity = Gravity.CENTER
            }

            // If no animation effect is currently set, set a default animation effect.
            if (animStyle == AnimAction.Companion.ANIM_DEFAULT) {
                animStyle = when (gravity) {
                    Gravity.TOP -> AnimAction.Companion.ANIM_TOP
                    Gravity.BOTTOM -> AnimAction.Companion.ANIM_BOTTOM
                    Gravity.LEFT -> AnimAction.Companion.ANIM_LEFT
                    Gravity.RIGHT -> AnimAction.Companion.ANIM_RIGHT
                    else -> AnimAction.Companion.ANIM_DEFAULT
                }
            }

            //Create a new Dialog object
            dialog = createDialog(context, themeId)
            dialog!!.let { dialog ->
                dialog.setContentView(contentView!!)
                dialog.setCancelable(cancelable)
                if (cancelable) {
                    dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
                }
                dialog.setOnShowListeners(showListeners)
                dialog.setOnCancelListeners(cancelListeners)
                dialog.setOnDismissListeners(dismissListeners)
                dialog.setOnKeyListener(keyListener)
                val window: Window? = dialog.window
                if (window != null) {
                    val params: WindowManager.LayoutParams = window.attributes
                    params.width = width
                    params.height = height
                    params.gravity = gravity
                    params.x = xOffset
                    params.y = yOffset
                    params.windowAnimations = animStyle

                    if (backgroundDimEnabled) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        window.setDimAmount(backgroundDimAmount)
                    } else {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    }
                    window.attributes = params
                }

                clickArray?.let { array ->
                    var i = 0
                    while (i < array.size()) {
                        contentView!!.findViewById<View?>(array.keyAt(i))?.
                        setOnClickListener(ViewClickWrapper(dialog, array.valueAt(i)))
                        i++
                    }
                }

                getActivity()?.let { activity ->
                    // Bind Dialog's life cycle and Activity together
                    DialogLifecycle.with(activity, dialog)
                }
                createListener?.onCreate(dialog)
            }
            return dialog!!
        }

        /**
         * show
         */
        open fun show() {
            val activity = getActivity()
            if (activity == null || activity.isFinishing || activity.isDestroyed) {
                return
            }
            if (!isCreated()) {
                create()
            }
            if (isShowing()) {
                return
            }
            dialog?.show()
        }

        /**
         * Destroy the current Dialog
         */
        open fun dismiss() {
            val activity = getActivity()
            if (activity == null || activity.isFinishing || activity.isDestroyed) {
                return
            }
            dialog?.dismiss()
        }

        override fun getContext(): Context {
            return context
        }

        /**
         * Whether the current Dialog is created
         */
        open fun isCreated(): Boolean {
            return dialog != null
        }

        /**
         * Whether the current Dialog is displayed
         */
        open fun isShowing(): Boolean {
            return isCreated() && dialog!!.isShowing
        }

        /**
         * Create a Dialog object (subclasses can override this method to change the Dialog type)
         */
        protected open fun createDialog(context: Context, @StyleRes themeId: Int): BaseDialog {
            return BaseDialog(context, themeId)
        }

        /**
         * Delayed execution
         */
        open fun post(runnable: Runnable) {
            if (isShowing()) {
                dialog?.post(runnable)
            } else {
                addOnShowListener(ShowPostWrapper(runnable))
            }
        }

        /**
         * Delay execution for a period of time
         */
        open fun postDelayed(runnable: Runnable, delayMillis: Long) {
            if (isShowing()) {
                dialog?.postDelayed(runnable, delayMillis)
            } else {
                addOnShowListener(ShowPostDelayedWrapper(runnable, delayMillis))
            }
        }

        /**
         * Execute at the specified time
         */
        open fun postAtTime(runnable: Runnable, uptimeMillis: Long) {
            if (isShowing()) {
                dialog?.postAtTime(runnable, uptimeMillis)
            } else {
                addOnShowListener(ShowPostAtTimeWrapper(runnable, uptimeMillis))
            }
        }

        /**
         * Get the root layout of Dialog
         */
        open fun getContentView(): View? {
            return contentView
        }

        /**
         * Find View based on id
         */
        override fun <V : View?> findViewById(@IdRes id: Int): V? {
            if (contentView == null) {
                // Want to findViewById without setContentView?
                throw IllegalStateException("are you ok?")
            }
            return contentView!!.findViewById(id)
        }

        /**
         * Get the current Dialog object
         */
        open fun getDialog(): BaseDialog? {
            return dialog
        }
    }

    /**
     * Dialog life cycle binding
     */
    private class DialogLifecycle(private var activity: Activity?, private var dialog: BaseDialog?):
        ActivityLifecycleCallbacks, OnShowListener, OnDismissListener {

        companion object {

            fun with(activity: Activity, dialog: BaseDialog?) {
                DialogLifecycle(activity, dialog)
            }
        }

        init {
            this.dialog?.addOnShowListener(this)
            this.dialog?.addOnDismissListener(this)
        }

        /** Dialog animation style (to avoid triggering animation effects again after Dialog returns from the background to the foreground) */
        private var dialogAnim: Int = 0

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {
            if (activity !== activity) {
                return
            }

            dialog?.let {
                if (!it.isShowing) {
                    return
                }

                //Restore the Dialog animation style (delay settings must be used here, otherwise there is still a certain chance that it will occur)
                it.postDelayed({
                    if (!it.isShowing) {
                        return@postDelayed
                    }
                    it.setWindowAnimations(dialogAnim)
                }, 100)
            }
        }

        override fun onActivityPaused(activity: Activity) {
            if (this.activity !== activity) {
                return
            }

            this.dialog?.let {
                if (!it.isShowing) {
                    return
                }

                // Get Dialog animation style
                dialogAnim = it.getWindowAnimations()
                //Set Dialog without animation effect
                it.setWindowAnimations(AnimAction.Companion.ANIM_EMPTY)
            }
        }

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
            if (this.activity !== activity) {
                return
            }
            unregisterActivityLifecycleCallbacks()
            this.activity = null
            this.dialog?.let {
                it.removeOnShowListener(this)
                it.removeOnDismissListener(this)
                if (it.isShowing) {
                    it.dismiss()
                }
            }
            this.dialog = null
        }

        override fun onShow(dialog: BaseDialog?) {
            this.dialog = dialog
            registerActivityLifecycleCallbacks()
        }

        override fun onDismiss(dialog: BaseDialog?) {
            this.dialog = null
            unregisterActivityLifecycleCallbacks()
        }

        /**
         * Register Activity life cycle monitor
         */
        private fun registerActivityLifecycleCallbacks() {
            activity?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    it.registerActivityLifecycleCallbacks(this)
                } else {
                    it.application.registerActivityLifecycleCallbacks(this)
                }
            }
        }

        /**
         * Unregister Activity life cycle monitoring
         */
        private fun unregisterActivityLifecycleCallbacks() {
            activity?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    it.unregisterActivityLifecycleCallbacks(this)
                } else {
                    it.application.unregisterActivityLifecycleCallbacks(this)
                }
            }
        }
    }

    /**
     * Dialog listener wrapper class (fixes memory leaks caused by native Dialog listener objects)
     */
    private class ListenersWrapper<T>(referent: T?) :
        SoftReference<T?>(referent), DialogInterface.OnShowListener,
        DialogInterface.OnCancelListener,
        DialogInterface.OnDismissListener
            where T : DialogInterface.OnShowListener,
                  T : DialogInterface.OnCancelListener,
                  T : DialogInterface.OnDismissListener {

        override fun onShow(dialog: DialogInterface?) {
            get()?.onShow(dialog)
        }

        override fun onCancel(dialog: DialogInterface?) {
            get()?.onCancel(dialog)
        }

        override fun onDismiss(dialog: DialogInterface?) {
            get()?.onDismiss(dialog)
        }
    }

    /**
     * Click event packaging class
     */
    private class ViewClickWrapper constructor(
        private val dialog: BaseDialog?,
        private val listener: OnClickListener<View>?) : View.OnClickListener {

        override fun onClick(view: View) {
            listener?.onClick(dialog, view)
        }
    }

    /**
     * Display listening wrapper class
     */
    private class ShowListenerWrapper constructor(referent: DialogInterface.OnShowListener?) :
        SoftReference<DialogInterface.OnShowListener?>(referent), OnShowListener {

        override fun onShow(dialog: BaseDialog?) {
            // The listening object will be empty after switching between horizontal and vertical screens.
            get()?.onShow(dialog)
        }
    }

    /**
     * Cancel monitoring packaging class
     */
    private class CancelListenerWrapper constructor(referent: DialogInterface.OnCancelListener?) :
        SoftReference<DialogInterface.OnCancelListener?>(referent), OnCancelListener {

        override fun onCancel(dialog: BaseDialog?) {
            // The listening object will be empty after switching between horizontal and vertical screens.
            get()?.onCancel(dialog)
        }
    }

    /**
     * Destroy the listening packaging class
     */
    private class DismissListenerWrapper constructor(referent: DialogInterface.OnDismissListener?) :
        SoftReference<DialogInterface.OnDismissListener?>(referent), OnDismissListener {

        override fun onDismiss(dialog: BaseDialog?) {
            // The listening object will be empty after switching between horizontal and vertical screens.
            get()?.onDismiss(dialog)
        }
    }

    /**
     * Button monitoring packaging class
     */
    private class KeyListenerWrapper constructor(private val listener: OnKeyListener?) : DialogInterface.OnKeyListener {

        override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
            // The listening object will be empty after switching between horizontal and vertical screens.
            if (listener == null || dialog !is BaseDialog) {
                return false
            }
            return listener.onKey(dialog, event)
        }
    }

    /**
     * post task packaging class
     */
    private class ShowPostWrapper constructor(private val runnable: Runnable?) : OnShowListener {

        override fun onShow(dialog: BaseDialog?) {
            if (runnable == null) {
                return
            }
            dialog?.removeOnShowListener(this)
            dialog?.post(runnable)
        }
    }

    /**
     * postDelayed task wrapper class
     */
    private class ShowPostDelayedWrapper constructor(
        private val runnable: Runnable?,
        private val delayMillis: Long) : OnShowListener {

        override fun onShow(dialog: BaseDialog?) {
            if (runnable == null) {
                return
            }
            dialog?.removeOnShowListener(this)
            dialog?.postDelayed(runnable, delayMillis)
        }
    }

    /**
     * postAtTime task wrapper class
     */
    private class ShowPostAtTimeWrapper constructor(
        private val runnable: Runnable,
        private val uptimeMillis: Long) : OnShowListener {

        override fun onShow(dialog: BaseDialog?) {
            dialog?.removeOnShowListener(this)
            dialog?.postAtTime(runnable, uptimeMillis)
        }
    }

    /**
     * Click listener
     */
    interface OnClickListener<V : View> {

        /**
         * The click event is triggered
         */
        fun onClick(dialog: BaseDialog?, view: V)
    }

    /**
     * Create listener
     */
    interface OnCreateListener {

        /**
         * Dialog created
         */
        fun onCreate(dialog: BaseDialog?)
    }

    /**
     * Show listener
     */
    interface OnShowListener {

        /**
         *Dialog shows
         */
        fun onShow(dialog: BaseDialog?)
    }

    /**
     * Cancel listener
     */
    interface OnCancelListener {

        /**
         *Dialog canceled
         */
        fun onCancel(dialog: BaseDialog?)
    }

    /**
     * Destroy the listener
     */
    interface OnDismissListener {

        /**
         * Dialog destroyed
         */
        fun onDismiss(dialog: BaseDialog?)
    }

    /**
     * Button listener
     */
    interface OnKeyListener {

        /**
         * Button triggered
         */
        fun onKey(dialog: BaseDialog?, event: KeyEvent?): Boolean
    }


    /**
     * View touch out
     */
    private val mTouchOutsideViews: MutableList<View> = mutableListOf()

    /**
     * Listener touch out
     */
    private var mOnTouchOutsideViewListener: OnTouchOutsideViewListener? = null

    /**
     * Sets a listener that is notified when the user taps outside the given views.
     * To remove the listener, call [removeOnTouchOutsideViewListener].
     *
     * @param views The list of views to monitor.
     * @param onTouchOutsideViewListener The listener to notify when a touch outside occurs.
     */
    fun setOnTouchOutsideViewListener(
        views: List<View>, onTouchOutsideViewListener: OnTouchOutsideViewListener
    ) {
        mTouchOutsideViews.clear()
        mTouchOutsideViews.addAll(views)
        mOnTouchOutsideViewListener = onTouchOutsideViewListener
    }

    /**
     * Get the current touch outside listener.
     */
    fun getOnTouchOutsideViewListener(): OnTouchOutsideViewListener? {
        return mOnTouchOutsideViewListener
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (mOnTouchOutsideViewListener != null) {
                val isOutside = mTouchOutsideViews.all { view ->
                    if (view.visibility == View.VISIBLE) {
                        val viewRect = Rect()
                        view.getGlobalVisibleRect(viewRect)
                        !viewRect.contains(ev.rawX.toInt(), ev.rawY.toInt())
                    } else {
                        true
                    }
                }

                if (isOutside) {
                    mOnTouchOutsideViewListener?.onTouchOutside(mTouchOutsideViews, ev)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * Interface definition for a callback to be invoked when a touch event has occurred outside the specified views.
     */
    interface OnTouchOutsideViewListener {
        /**
         * Called when a touch event has occurred outside the given views.
         *
         * @param views The list of views that were not touched.
         * @param event The MotionEvent object containing full information about the event.
         */
        fun onTouchOutside(views: List<View>, event: MotionEvent)
    }
}
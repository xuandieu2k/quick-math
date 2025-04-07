package com.cooldev.base

import android.animation.ValueAnimator
import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.*
import android.widget.*
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.widget.PopupWindowCompat
import com.cooldev.base.action.ActivityAction
import com.cooldev.base.action.AnimAction
import com.cooldev.base.action.ClickAction
import com.cooldev.base.action.HandlerAction
import com.cooldev.base.action.KeyboardAction
import com.cooldev.base.action.ResourcesAction
import java.lang.ref.SoftReference
import java.util.*

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
open class BasePopupWindow constructor(private val context: Context) : PopupWindow(context),
    ActivityAction,
    HandlerAction, ClickAction, AnimAction, KeyboardAction, PopupWindow.OnDismissListener {

    private var popupBackground: PopupBackground? = null
    private var showListeners: MutableList<OnShowListener?>? = null
    private var dismissListeners: MutableList<OnDismissListener?>? = null

    override fun getContext(): Context {
        return context
    }

    /**
     * Set up a destruction listener
     *
     * @param listener destroy the listener object
     */
    @Deprecated("请使用 {@link #addOnDismissListener(BasePopupWindow.OnDismissListener)}")
    override fun setOnDismissListener(listener: PopupWindow.OnDismissListener?) {
        if (listener == null) {
            return
        }
        addOnDismissListener(DismissListenerWrapper(listener))
    }

    /**
     * Add a display listener
     *
     * @param listener listener object
     */
    open fun addOnShowListener(listener: OnShowListener?) {
        if (showListeners == null) {
            showListeners = ArrayList()
        }
        showListeners!!.add(listener)
    }

    /**
     * Add a destruction listener
     *
     * @param listener listener object
     */
    open fun addOnDismissListener(listener: OnDismissListener?) {
        if (dismissListeners == null) {
            dismissListeners = ArrayList()
            super.setOnDismissListener(this)
        }
        dismissListeners!!.add(listener)
    }

    /**
     * Remove a display listener
     *
     * @param listener listener object
     */
    open fun removeOnShowListener(listener: OnShowListener?) {
        showListeners?.remove(listener)
    }

    /**
     * Remove a destruction listener
     *
     * @param listener listener object
     */
    open fun removeOnDismissListener(listener: OnDismissListener?) {
        dismissListeners?.remove(listener)
    }

    /**
     * Set display listener collection
     */
    private fun setOnShowListeners(listeners: MutableList<OnShowListener?>?) {
        showListeners = listeners
    }

    /**
     * Set up a collection of destruction listeners
     */
    private fun setOnDismissListeners(listeners: MutableList<OnDismissListener?>?) {
        super.setOnDismissListener(this)
        dismissListeners = listeners
    }

    /**
     * [PopupWindow.OnDismissListener]
     */
    override fun onDismiss() {
        if (dismissListeners == null) {
            return
        }
        for (listener: OnDismissListener? in dismissListeners!!) {
            listener?.onDismiss(this)
        }
    }

    override fun showAsDropDown(anchor: View?, xOff: Int, yOff: Int, gravity: Int) {
        if (isShowing || contentView == null) {
            return
        }
        if (showListeners != null) {
            for (listener: OnShowListener? in showListeners!!) {
                listener?.onShow(this)
            }
        }
        super.showAsDropDown(anchor, xOff, yOff, gravity)
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        if (isShowing || contentView == null) {
            return
        }
        if (showListeners != null) {
            for (listener: OnShowListener? in showListeners!!) {
                listener?.onShow(this)
            }
        }
        super.showAtLocation(parent, gravity, x, y)
    }

    override fun dismiss() {
        super.dismiss()
        removeCallbacks()
    }

    override fun <V : View?> findViewById(@IdRes id: Int): V? {
        return contentView.findViewById(id)
    }

    override fun setWindowLayoutType(type: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.setWindowLayoutType(type)
        } else {
            PopupWindowCompat.setWindowLayoutType(this, type)
        }
    }

    override fun getWindowLayoutType(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.getWindowLayoutType()
        } else {
            PopupWindowCompat.getWindowLayoutType(this)
        }
    }

    override fun setOverlapAnchor(overlapAnchor: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.setOverlapAnchor(overlapAnchor)
        } else {
            PopupWindowCompat.setOverlapAnchor(this, overlapAnchor)
        }
    }

    /**
     * Set the transparency of the background mask layer
     */
    open fun setBackgroundDimAmount(@FloatRange(from = 0.0, to = 1.0) dimAmount: Float) {
        val alpha: Float = 1 - dimAmount
        if (isShowing) {
            setActivityAlpha(alpha)
        }
        if (popupBackground == null && alpha != 1f) {
            popupBackground = PopupBackground()
            addOnShowListener(popupBackground)
            addOnDismissListener(popupBackground)
        }
        if (popupBackground != null) {
            popupBackground?.setAlpha(alpha)
        }
    }

    /**
     * Set Activity window transparency
     */
    private fun setActivityAlpha(alpha: Float) {
        val activity: Activity = getActivity() ?: return
        val params: WindowManager.LayoutParams = activity.window.attributes
        val animator: ValueAnimator = ValueAnimator.ofFloat(params.alpha, alpha)
        animator.duration = 300
        animator.addUpdateListener { animation: ValueAnimator ->
            val value: Float = animation.animatedValue as Float
            if (value != params.alpha) {
                params.alpha = value
                activity.window.attributes = params
            }
        }
        animator.start()
    }

    @Suppress("UNCHECKED_CAST")
    open class Builder<B : Builder<B>> constructor(
        private val context: Context
    ) : ActivityAction, ResourcesAction, ClickAction, KeyboardAction {

        companion object {
            private const val DEFAULT_ANCHORED_GRAVITY: Int = Gravity.TOP or Gravity.START
        }

        /** PopupWindow object */
        private var popupWindow: BasePopupWindow? = null

        /** PopupWindow layout */
        private var contentView: View? = null

        /** animation style */
        private var animStyle: Int = AnimAction.ANIM_DEFAULT

        /** width and height */
        private var width: Int = WindowManager.LayoutParams.WRAP_CONTENT
        private var height: Int = WindowManager.LayoutParams.WRAP_CONTENT

        /** Center of gravity position */
        private var gravity: Int = DEFAULT_ANCHORED_GRAVITY

        /** Horizontal offset */
        private var xOffset: Int = 0

        /** Vertical offset */
        private var yOffset: Int = 0

        /** Whether touchable */
        private var touchable: Boolean = true

        /** Whether there is focus */
        private var focusable: Boolean = true

        /** Whether the outer layer is touchable */
        private var outsideTouchable: Boolean = false

        /** Background mask transparency */
        private var backgroundDimAmount: Float = 0f

        /** PopupWindow creates a listener */
        private var createListener: OnCreateListener? = null

        /** PopupWindow display monitoring */
        private val showListeners: MutableList<OnShowListener?> by lazy { ArrayList() }

        /** PopupWindow destruction monitoring */
        private val dismissListeners: MutableList<OnDismissListener?> by lazy { ArrayList() }

        /** Click event collection */
        private var clickArray: SparseArray<OnClickListener<View>>? = null

        /**
         * Set layout
         */
        open fun setContentView(@LayoutRes id: Int): B {
            // Here is an explanation why new FrameLayout needs to be passed, because if it is not passed, the LayoutParams object obtained by the XML root layout will be empty, which will result in the width and height not being parsed.
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
                popupWindow!!.contentView = view
                return this as B
            }
            val layoutParams: ViewGroup.LayoutParams? = contentView!!.layoutParams
            if ((layoutParams != null) && (width == ViewGroup.LayoutParams.WRAP_CONTENT) && (height == ViewGroup.LayoutParams.WRAP_CONTENT)) {
                // If the width and height of the current PopupWindow are set to be adaptive, the width and height set in the layout will be the main one.
                setWidth(layoutParams.width)
                setHeight(layoutParams.height)
            }

            // If the center of gravity is not currently set, automatically obtain the layout center of gravity.
            if (gravity == DEFAULT_ANCHORED_GRAVITY) {
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
         * Set animation, several styles have been encapsulated, see the [AnimAction] class for details
         */
        open fun setAnimStyle(@StyleRes id: Int): B {
            animStyle = id
            if (isCreated()) {
                popupWindow?.animationStyle = id
            }
            return this as B
        }

        /**
         * Set width
         */
        open fun setWidth(width: Int): B {
            this.width = width
            if (isCreated()) {
                popupWindow!!.width = width
                return this as B
            }
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
                popupWindow!!.height = height
                return this as B
            }

            //Here is an explanation of why LayoutParams needs to be reset
            // Because if this is not set, the width and height of the PopupWindow will be displayed when it is displayed for the first time.
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
            return this as B
        }

        /**
         * Set horizontal offset
         */
        open fun setXOffset(offset: Int): B {
            xOffset = offset
            return this as B
        }

        /**
         * Set vertical offset
         */
        open fun setYOffset(offset: Int): B {
            yOffset = offset
            return this as B
        }

        /**
         * Whether it can be touched
         */
        open fun setTouchable(touchable: Boolean): B {
            this.touchable = touchable
            if (isCreated()) {
                popupWindow!!.isTouchable = touchable
            }
            return this as B
        }

        /**
         * Whether there is focus
         */
        open fun setFocusable(focusable: Boolean): B {
            this.focusable = focusable
            if (isCreated()) {
                popupWindow!!.isFocusable = focusable
            }
            return this as B
        }

        /**
         * Whether the outer layer is touchable
         */
        open fun setOutsideTouchable(outsideTouchable: Boolean): B {
            this.outsideTouchable = outsideTouchable
            if (isCreated()) {
                popupWindow!!.isOutsideTouchable = outsideTouchable
            }
            return this as B
        }

        /**
         * Set the transparency of the background mask layer
         */
        open fun setBackgroundDimAmount(@FloatRange(from = 0.0, to = 1.0) dimAmount: Float): B {
            backgroundDimAmount = dimAmount
            if (isCreated()) {
                popupWindow!!.setBackgroundDimAmount(dimAmount)
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
         * Add destruction monitoring
         */
        open fun addOnDismissListener(listener: OnDismissListener): B {
            dismissListeners.add(listener)
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
                popupWindow?.findViewById<View?>(id)?.setOnClickListener(ViewClickWrapper(popupWindow, listener))
            }
            return this as B
        }

        /**
         * Create
         */
        @Suppress("RtlHardcoded")
        open fun create(): BasePopupWindow {
            // Determine whether the layout is empty
            if (contentView == null) {
                throw IllegalArgumentException("are you ok?")
            }

            // If currently displaying
            if (isShowing()) {
                dismiss()
            }

            // If the center of gravity is not currently set, set a default center of gravity.
            if (gravity == DEFAULT_ANCHORED_GRAVITY) {
                gravity = Gravity.CENTER
            }

            // If no animation effect is currently set, set a default animation effect.
            if (animStyle == AnimAction.ANIM_DEFAULT) {
                animStyle = when (gravity) {
                    Gravity.TOP -> AnimAction.ANIM_TOP
                    Gravity.BOTTOM -> AnimAction.ANIM_BOTTOM
                    Gravity.LEFT -> AnimAction.ANIM_LEFT
                    Gravity.RIGHT -> AnimAction.ANIM_RIGHT
                    else -> AnimAction.ANIM_DEFAULT
                }
            }
            popupWindow = createPopupWindow(context)
            popupWindow!!.let { popupWindow ->
                popupWindow.contentView = contentView
                popupWindow.width = width
                popupWindow.height = height
                popupWindow.animationStyle = animStyle
                popupWindow.isFocusable = focusable
                popupWindow.isTouchable = touchable
                popupWindow.isOutsideTouchable = outsideTouchable
                popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                popupWindow.setOnShowListeners(showListeners)
                popupWindow.setOnDismissListeners(dismissListeners)
                popupWindow.setBackgroundDimAmount(backgroundDimAmount)

                clickArray?.let { array ->
                    var i = 0
                    while (i < array.size()) {
                        contentView!!.findViewById<View?>(array.keyAt(i))?.
                        setOnClickListener(ViewClickWrapper(popupWindow, array.valueAt(i)))
                        i++
                    }
                }

                // Bind the life cycle of PopupWindow to Activity
                getActivity()?.let { activity ->
                    PopupWindowLifecycle.with(activity, popupWindow)
                }
                createListener?.onCreate(popupWindow)
            }
            return popupWindow!!
        }

        /**
         * Displayed as drop-down
         */
        open fun showAsDropDown(anchor: View?) {
            getActivity()?.let {
                if (it.isFinishing || it.isDestroyed) {
                    return
                }
                if (!isCreated()) {
                    create()
                }
                popupWindow?.showAsDropDown(anchor, xOffset, yOffset, gravity)
            }
        }

        /**
         * Display at the specified location
         */
        open fun showAtLocation(parent: View?) {
            getActivity()?.let {
                if (it.isFinishing || it.isDestroyed) {
                    return
                }
                if (!isCreated()) {
                    create()
                }
                popupWindow?.showAtLocation(parent, gravity, xOffset, yOffset)
            }
        }

        override fun getContext(): Context {
            return context
        }

        /**
         * Whether the current PopupWindow is created
         */
        open fun isCreated(): Boolean {
            return popupWindow != null
        }

        /**
         * Whether the current PopupWindow is displayed
         */
        open fun isShowing(): Boolean {
            return isCreated() && popupWindow!!.isShowing
        }

        /**
         * Destroy the current PopupWindow
         */
        open fun dismiss() {
            getActivity()?.let {
                if (it.isFinishing || it.isDestroyed) {
                    return
                }
                popupWindow?.dismiss()
            }
        }

        /**
         * Create a PopupWindow object (subclasses can override this method to change the PopupWindow type)
         */
        protected open fun createPopupWindow(context: Context): BasePopupWindow {
            return BasePopupWindow(context)
        }

        /**
         * Get the root layout of PopupWindow
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
         * Get the current PopupWindow object
         */
        open fun getPopupWindow(): BasePopupWindow? {
            return popupWindow
        }

        /**
         * Delayed execution
         */
        open fun post(runnable: Runnable) {
            if (isShowing()) {
                popupWindow!!.post(runnable)
            } else {
                addOnShowListener(ShowPostWrapper(runnable))
            }
        }

        /**
         * Delay execution for a period of time
         */
        open fun postDelayed(runnable: Runnable, delayMillis: Long) {
            if (isShowing()) {
                popupWindow!!.postDelayed(runnable, delayMillis)
            } else {
                addOnShowListener(ShowPostDelayedWrapper(runnable, delayMillis))
            }
        }

        /**
         * Execute at the specified time
         */
        open fun postAtTime(runnable: Runnable, uptimeMillis: Long) {
            if (isShowing()) {
                popupWindow!!.postAtTime(runnable, uptimeMillis)
            } else {
                addOnShowListener(ShowPostAtTimeWrapper(runnable, uptimeMillis))
            }
        }
    }

    /**
     * PopupWindow life cycle binding
     */
    private class PopupWindowLifecycle constructor(private var activity: Activity?,
                                                   private var popupWindow: BasePopupWindow?) :
        ActivityLifecycleCallbacks, OnShowListener, OnDismissListener {

        companion object {
            fun with(activity: Activity, popupWindow: BasePopupWindow?) {
                PopupWindowLifecycle(activity, popupWindow)
            }
        }

        init {
            popupWindow?.addOnShowListener(this)
            popupWindow?.addOnDismissListener(this)
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
            if (this.activity !== activity) {
                return
            }
            unregisterActivityLifecycleCallbacks()
            this.activity = null
            if (this.popupWindow == null) {
                return
            }
            this.popupWindow!!.removeOnShowListener(this)
            this.popupWindow!!.removeOnDismissListener(this)
            if (this.popupWindow!!.isShowing) {
                this.popupWindow!!.dismiss()
            }
            this.popupWindow = null
        }

        override fun onShow(popupWindow: BasePopupWindow?) {
            this.popupWindow = popupWindow
            registerActivityLifecycleCallbacks()
        }

        override fun onDismiss(popupWindow: BasePopupWindow?) {
            this.popupWindow = null
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
     * PopupWindow background mask layer implementation class
     */
    private class PopupBackground : OnShowListener, OnDismissListener {

        private var alpha: Float = 0f

        fun setAlpha(alpha: Float) {
            this.alpha = alpha
        }

        override fun onShow(popupWindow: BasePopupWindow?) {
            popupWindow?.setActivityAlpha(alpha)
        }

        override fun onDismiss(popupWindow: BasePopupWindow?) {
            popupWindow?.setActivityAlpha(1f)
        }
    }

    /**
     * Destroy the listening packaging class
     */
    private class DismissListenerWrapper constructor(referent: PopupWindow.OnDismissListener?) :
        SoftReference<PopupWindow.OnDismissListener?>(referent), OnDismissListener {

        override fun onDismiss(popupWindow: BasePopupWindow?) {
            // The listening object will be empty after switching between horizontal and vertical screens.
            get()?.onDismiss()
        }
    }

    /**
     * Click event packaging class
     */
    private class ViewClickWrapper constructor(

        private val popupWindow: BasePopupWindow?,
        private val listener: OnClickListener<View>?) : View.OnClickListener {

        override fun onClick(view: View) {
            listener?.onClick(popupWindow, view)
        }
    }

    /**
     * post task packaging class
     */
    private class ShowPostWrapper constructor(val runnable: Runnable) : OnShowListener {

        override fun onShow(popupWindow: BasePopupWindow?) {
            popupWindow?.removeOnShowListener(this)
            popupWindow?.post(runnable)
        }
    }

    /**
     * postDelayed task wrapper class
     */
    private class ShowPostDelayedWrapper constructor(
        private val runnable: Runnable,
        private val delayMillis: Long) : OnShowListener {

        override fun onShow(popupWindow: BasePopupWindow?) {
            popupWindow?.removeOnShowListener(this)
            popupWindow?.postDelayed(runnable, delayMillis)
        }
    }

    /**
     * postAtTime task wrapper class
     */
    private class ShowPostAtTimeWrapper constructor(
        private val runnable: Runnable,
        private val uptimeMillis: Long) : OnShowListener {

        override fun onShow(popupWindow: BasePopupWindow?) {
            popupWindow?.removeOnShowListener(this)
            popupWindow?.postAtTime(runnable, uptimeMillis)
        }
    }

    /**
     * Click listener
     */
    interface OnClickListener<V : View> {

        /**
         * The click event is triggered
         */
        fun onClick(popupWindow: BasePopupWindow?, view: V)
    }

    /**
     * Create listener
     */
    interface OnCreateListener {

        /**
         * PopupWindow created
         */
        fun onCreate(popupWindow: BasePopupWindow?)
    }

    /**
     * Show listener
     */
    interface OnShowListener {

        /**
         * PopupWindow shows
         */
        fun onShow(popupWindow: BasePopupWindow?)
    }

    /**
     * Destroy the listener
     */
    interface OnDismissListener {

        /**
         * PopupWindow is destroyed
         */
        fun onDismiss(popupWindow: BasePopupWindow?)
    }
}
@file:Suppress("DEPRECATION")

package com.dhug.base

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
@Suppress("deprecation")
open class FragmentPagerAdapter<F : Fragment> constructor(manager: FragmentManager) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    /** Fragment collection */
    private val fragmentSet: MutableList<F> = ArrayList()

    /** Fragment title */
    private val fragmentTitle: MutableList<CharSequence?> = ArrayList()

    /** Currently displayed Fragment */
    private var showFragment: F? = null

    /** Current ViewPager */
    private var viewPager: ViewPager? = null

    /** Set to lazy loading mode */
    private var lazyMode: Boolean = true

    constructor(activity: FragmentActivity) : this(activity.supportFragmentManager)

    constructor(fragment: Fragment) : this(fragment.childFragmentManager)

    override fun getItem(position: Int): F {
        return fragmentSet[position]
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun getCount(): Int {
        return fragmentSet.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position]
    }

    @Suppress("UNCHECKED_CAST")
    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        if (getShowFragment() !== `object`) {
            // Record the current Fragment object
            showFragment = `object` as F
        }
    }

    /**
     *Add fragment
     */
    @JvmOverloads
    open fun addFragment(fragment: F, title: CharSequence? = null) {
        fragmentSet.add(fragment)
        fragmentTitle.add(title ?: "")
        if (viewPager == null) {
            return
        }
        notifyDataSetChanged()
        viewPager?.offscreenPageLimit = if (lazyMode) count else 1
    }

    /**
     * Get the current Fragment
     */
    open fun getShowFragment(): F? {
        return showFragment
    }

    /**
     * Get the index of a certain Fragment (returns -1 if not)
     */
    open fun getFragmentIndex(clazz: Class<out Fragment?>?): Int {
        if (clazz == null) {
            return -1
        }
        for (i in fragmentSet.indices) {
            if ((clazz.name == fragmentSet[i].javaClass.name)) {
                return i
            }
        }
        return -1
    }

    override fun startUpdate(container: ViewGroup) {
        super.startUpdate(container)
        if (container is ViewPager) {
            //Record binding ViewPager
            viewPager = container
            refreshLazyMode()
        }
    }

    /**
     * Set lazy loading mode
     */
    open fun setLazyMode(lazy: Boolean) {
        lazyMode = lazy
        refreshLazyMode()
    }

    /**
     * Refresh loading mode
     */
    private fun refreshLazyMode() {
        if (viewPager == null) {
            return
        }
        // Set to lazy loading mode (that is, there is no limit on the number of Fragment displays)
        viewPager?.offscreenPageLimit = if (lazyMode) count else 1
    }
}
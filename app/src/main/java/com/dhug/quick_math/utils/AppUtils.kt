package com.dhug.quick_math.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationManager
import android.net.Uri
import android.os.StatFs
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.camera.video.Quality
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dhug.quick_math.R
import com.dhug.quick_math.base.wiget.AppTextView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.hjq.toast.ToastUtils
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds
import androidx.core.net.toUri


@Singleton
object AppUtils {

    val TAG = AppUtils::class.java.simpleName

    enum class PressEffectType {
        ZOOM_IN, ZOOM_OUT, FADE, ROTATE, SHAKE, BLUR, NONE
    }

    enum class Orientation(val value: Int) {
        HORIZONTAL(LinearLayout.HORIZONTAL), VERTICAL(LinearLayout.VERTICAL)
    }

    private const val MINUTE: Int = 60
    private const val HOUR: Int = MINUTE * 60


    @SuppressLint("DefaultLocale")
    fun convertLongToTimeString(timeInSeconds: Long, isDefault: Boolean = true): String {
        val hours = timeInSeconds / 3600
        val minutes = (timeInSeconds % 3600) / 60
        val seconds = timeInSeconds % 60

        if (!isDefault) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
        if (hours == 0L) {
            return String.format("%02d:%02d", minutes, seconds)
        }
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }


    @SuppressLint("ClickableViewAccessibility")
    fun View.setPressEffect(
        effectType: PressEffectType = PressEffectType.ZOOM_OUT,
        onClick: (() -> Unit)? = null,
        onLongClick: (() -> Unit)? = null,
        applyToChildren: Boolean = false,
        clickDelay: Long = 1000
    ) {
        var isLongPressTriggered = false
        var lastClickTime = 0L

        val applyEffect: (View) -> Unit = { v ->
            when (effectType) {
                PressEffectType.ZOOM_IN -> {
                    v.scaleX = 1.05f
                    v.scaleY = 1.05f
                }

                PressEffectType.ZOOM_OUT -> {
                    v.scaleX = 0.95f
                    v.scaleY = 0.95f
                }

                PressEffectType.FADE -> {
                    v.alpha = 0.5f
                }

                PressEffectType.ROTATE -> {
                    v.rotation = 15f
                }

                PressEffectType.SHAKE -> {
                    v.animate().apply {
                        translationXBy(10f)
                        duration = 50
                    }.withEndAction {
                        v.animate().translationXBy(-10f).duration = 50
                    }.start()
                }

                PressEffectType.BLUR -> {
                    if (v is ImageView) {
                        v.alpha = 0.7f
                    }
                }

                PressEffectType.NONE -> {
                    //
                }
            }
        }

        val resetEffect: (View) -> Unit = { v ->
            v.scaleX = 1f
            v.scaleY = 1f
            v.alpha = 1f
            v.rotation = 0f
            v.translationX = 0f
        }

        this.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isLongPressTriggered = false
                    if (applyToChildren && v is ViewGroup) {
                        for (i in 0 until v.childCount) {
                            applyEffect(v.getChildAt(i))
                        }
                    } else {
                        applyEffect(v)
                    }
                }

                MotionEvent.ACTION_UP -> {
                    val currentTime = System.currentTimeMillis()
                    if (!isLongPressTriggered && (currentTime - lastClickTime >= clickDelay)) {
                        lastClickTime = currentTime
                        onClick?.invoke()
                    }
                    if (applyToChildren && v is ViewGroup) {
                        for (i in 0 until v.childCount) {
                            resetEffect(v.getChildAt(i))
                        }
                    } else {
                        resetEffect(v)
                    }
                }

                MotionEvent.ACTION_CANCEL -> {
                    if (applyToChildren && v is ViewGroup) {
                        for (i in 0 until v.childCount) {
                            resetEffect(v.getChildAt(i))
                        }
                    } else {
                        resetEffect(v)
                    }
                }
            }
            false
        }

        this.setOnLongClickListener {
            isLongPressTriggered = true
            onLongClick?.invoke()
            true
        }
    }


    @SuppressLint("DefaultLocale")
    fun convertSizeToReadableFormat(sizeInBytes: Long): String {
        val kb = sizeInBytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0

        fun formatValue(value: Double, unit: String): String {
            return if (value % 1.0 == 0.0) {
                "${value.toInt()} $unit"
            } else {
                String.format("%.1f $unit", value)
            }
        }

        return when {
            gb >= 1 -> formatValue(gb, "GB")
            mb >= 1 -> formatValue(mb, "MB")
            kb >= 1 -> formatValue(kb, "KB")
            else -> "$sizeInBytes Bytes"
        }
    }

    fun formatDurationTime(durationSeconds: Long) =
        durationSeconds.seconds.toComponents { hours, minutes, seconds, _ ->
            String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds,
            )
        }


    fun getAvailableStorageSpace(context: Context): Long {
        val stat = android.os.StatFs(context.filesDir.absolutePath)
        return stat.availableBytes
    }

    fun getAvailableExternalMemorySize(): Long? {
        val externalStorage = android.os.Environment.getExternalStorageDirectory()
        if (externalStorage != null) {
            val stat = StatFs(externalStorage.path)
            val blockSize = stat.blockSizeLong
            val availableBlocks = stat.availableBlocksLong
            return availableBlocks * blockSize
        }
        return null
    }

    fun getAvailableInternalMemorySize(): Long {
        val path = android.os.Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return availableBlocks * blockSize
    }

    fun initRecyclerView(
        view: RecyclerView, adapter: RecyclerView.Adapter<*>?, orientation: Orientation
    ) {
        configRecyclerView(
            view, LinearLayoutManager(view.context, orientation.value, false)
        )
        view.adapter = adapter
    }

    private fun configRecyclerView(
        recyclerView: RecyclerView,
        layoutManager: RecyclerView.LayoutManager?,
        isNestedScrollingEnabled: Boolean = false
    ) {
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        (recyclerView.itemAnimator)?.changeDuration = 0
        ((recyclerView.itemAnimator) as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.isNestedScrollingEnabled = isNestedScrollingEnabled
    }

//    fun initRecyclerViewVertical(view: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
//        configRecyclerView(
//            view, PreCachingLayoutManager(
//                view.context, RecyclerView.VERTICAL, false
//            )
//        )
//        view.adapter = adapter
//    }

    fun initRecyclerViewVertical(
        view: RecyclerView, adapter: RecyclerView.Adapter<*>?, count: Int,
        isNestedScrollingEnabled: Boolean = true
    ) {
        configRecyclerView(view, GridLayoutManager(view.context, count), isNestedScrollingEnabled)
        view.adapter = adapter
    }

    fun initRecyclerViewVerticalWithStaggeredGridLayoutManager(
        view: RecyclerView, adapter: RecyclerView.Adapter<*>?, count: Int
    ) {
        configRecyclerView(
            view, StaggeredGridLayoutManager(
                count, StaggeredGridLayoutManager.VERTICAL
            )
        )
        view.adapter = adapter
    }


//    fun initRecyclerViewHorizontal(view: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
//        configRecyclerView(
//            view, PreCachingLayoutManager(
//                view.context, RecyclerView.HORIZONTAL, false
//            )
//        )
//        view.adapter = adapter
//    }

    fun initRecyclerViewHorizontal(
        view: RecyclerView,
        adapter: RecyclerView.Adapter<*>?,
        count: Int,
        isNestedScrollingEnabled: Boolean = false
    ) {
        configRecyclerView(view, GridLayoutManager(view.context, count), isNestedScrollingEnabled)
        view.adapter = adapter
    }

//    fun initRecyclerViewHorizontalGallery(
//        view: RecyclerView,
//        adapter: RecyclerView.Adapter<*>?,
//        count: Int,
//        isNestedScrollingEnabled: Boolean = false
//    ) {
//        val gridLayoutManager = GridLayoutManager(view.context, count)
//        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                return when (adapter?.getItemViewType(position)) {
//                    MediaAdapter.TYPE_HEADER,
//                    MediaAdapter.TYPE_UNLOCK -> 4
//
//                    else -> 1
//                }
//            }
//        }
//        configRecyclerView(view, gridLayoutManager, isNestedScrollingEnabled)
//        view.adapter = adapter
//    }

//    fun initRecyclerViewReverse(view: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
//        val preCachingLayoutManager = CenterLayoutManager(
//            view.context, RecyclerView.VERTICAL, true
//        )
//        configRecyclerView(view, preCachingLayoutManager)
//        view.adapter = adapter
//    }

    fun initRecyclerViewVerticalWithFlexBoxLayout(
        view: RecyclerView,
        adapter: RecyclerView.Adapter<*>?,
        isNestedScrollingEnabled: Boolean = false
    ) {
        configRecyclerViewWithFlexBoxLayout(
            view, FlexboxLayoutManager(view.context), isNestedScrollingEnabled
        )
        view.adapter = adapter
    }

    private fun configRecyclerViewWithFlexBoxLayout(
        recyclerView: RecyclerView,
        layoutManager: FlexboxLayoutManager?,
        isNestedScrollingEnabled: Boolean = false
    ) {
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
        (recyclerView.itemAnimator)?.changeDuration = 0
        ((recyclerView.itemAnimator) as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.isNestedScrollingEnabled = isNestedScrollingEnabled
        layoutManager?.flexWrap = FlexWrap.WRAP
    }

    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.hide() {
        visibility = View.GONE
    }

    fun View.invisible() {
        visibility = View.INVISIBLE
    }


    fun getMimeType(url: String): String {
        try {
            return url.substring(url.lastIndexOf(".") + 1)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private val PACKAGE_NAME = AppConfig.getPackageName()
    private const val SKU = "mySku"

    fun openPlayStoreAccount(context: Context) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/account/subscriptions?sku=$SKU&package=$PACKAGE_NAME")
                )
            )
        } catch (e: ActivityNotFoundException) {
            ToastUtils.show(context.getString(R.string.cannot_open))
            e.printStackTrace()
        }
    }

    fun shareMyApp(context: Context) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
            var shareMessage = "\n${context.getString(R.string.let_me_application)}\n\n"
            shareMessage =
                (shareMessage + "https://play.google.com/store/apps/details?id=" + PACKAGE_NAME) + "\n\n"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    context.getString(R.string.choose_app)
                )
            )
        } catch (e: Exception) {
            //e.toString();
        }
    }

    /*
    * Start with rating the app
    * Determine if the Play Store is installed on the device
    *
    * */
    fun rateMyApp(context: Context) {
        try {
            val rateIntent = rateIntentForUrl("market://details")
            context.startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            val rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details")
            context.startActivity(rateIntent)
        }
    }

    private fun rateIntentForUrl(url: String): Intent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            java.lang.String.format("%s?id=%s", url, PACKAGE_NAME).toUri()
        )
        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        intent.addFlags(flags)
        return intent
    }

    fun openBrowser(url: String, context: Context) {
        try {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            //
        }
    }

    fun openSettingApp(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        with(intent) {
            data = Uri.fromParts("package", context.packageName, null)
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }

        context.startActivity(intent)
    }

    fun roundAndFormat(number: Double, scale: Int = 1): String {
        val rounded = BigDecimal(number).setScale(scale, RoundingMode.HALF_UP).toDouble()
        val formatted = if (rounded % 1 == 0.0) {
            rounded.toInt().toString()
        } else {
            rounded.toString()
        }

        return formatted
    }


    fun handleSubscriptionCancellation(activity: Activity?) {
        val subscriptionUrl =
            "https://play.google.com/store/account/subscriptions?package=${activity?.packageName}"

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(subscriptionUrl))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity?.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Timber.tag(TAG).e("Can not open: ${e.message}")
        }
    }

    fun composeEmail(addresses: Array<String?>?, subject: String?, context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setData(Uri.parse("mailto:")) // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    fun isGPSEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun requestEnableGPS(activity: Activity) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivity(intent)
    }

    fun openRatingWithReviewApi(
        activity: Activity,
        onDone: (isDone: Boolean, ex: Exception?) -> Unit
    ) {
        val manager = ReviewManagerFactory.create(activity)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                    onDone(true, null)
                }.addOnFailureListener {
                    onDone(false, it)
                }
            } else {
                // There was some problem, log or handle the error code.
                @ReviewErrorCode val reviewErrorCode =
                    (task.exception as ReviewException).errorCode
                onDone(false, task.exception as ReviewException)
            }
        }
    }


}
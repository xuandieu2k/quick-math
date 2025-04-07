package com.dhug.example.base.ui.activity

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.gyf.immersionbar.ImmersionBar
import com.dhug.example.base.aop.SingleClick
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.dhug.example.R
import com.dhug.example.base.AppActivity
import com.dhug.example.databinding.ActivityCrashBinding
import java.io.PrintWriter
import java.io.StringWriter
import java.net.InetAddress
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.min

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2022
 */
@AndroidEntryPoint
class CrashActivity : AppActivity() {

    companion object {

        private const val INTENT_KEY_IN_THROWABLE: String = "throwable"

        /** System package prefix list */
        private val SYSTEM_PACKAGE_PREFIX_LIST: Array<String> = arrayOf("android", "com.android",
            "androidx", "com.google.android", "java", "javax", "dalvik", "kotlin")

        /** Regular expression for the number of lines of error code */
        private val CODE_REGEX: Pattern = Pattern.compile("\\(\\w+\\.\\w+:\\d+\\)")

        fun start(application: Application, throwable: Throwable?) {
            if (throwable == null) {
                return
            }
            val intent = Intent(application, CrashActivity::class.java)
            intent.putExtra(INTENT_KEY_IN_THROWABLE, throwable)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application.startActivity(intent)
        }
    }

    private val titleView: TextView? by lazy { findViewById(R.id.tv_crash_title) }
    private val drawerLayout: DrawerLayout? by lazy { findViewById(R.id.dl_crash_drawer) }
    private val infoView: TextView? by lazy { findViewById(R.id.tv_crash_info) }
    private val messageView: TextView? by lazy { findViewById(R.id.tv_crash_message) }
    private var stackTrace: String? = null

    override fun isHasInterstitialAd(): Boolean {
        return false
    }

    override fun getLayoutView(): View {
        return ActivityCrashBinding.inflate(layoutInflater).root
    }

    override fun initView() {
        setOnClickListener(R.id.iv_crash_info, R.id.iv_crash_share, R.id.iv_crash_restart)

        // Set status bar immersion
        ImmersionBar.setTitleBar(this, findViewById(R.id.ll_crash_bar))
        ImmersionBar.setTitleBar(this, findViewById(R.id.ll_crash_info))
    }

    override fun initData() {
        val throwable: Throwable = getSerializable(INTENT_KEY_IN_THROWABLE) ?: return
        titleView?.text = throwable.javaClass.simpleName
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        throwable.printStackTrace(printWriter)
        throwable.cause?.printStackTrace(printWriter)
        stackTrace = stringWriter.toString()
        val matcher: Matcher = CODE_REGEX.matcher(stackTrace!!)
        val spannable = SpannableStringBuilder(stackTrace)
        if (spannable.isNotEmpty()) {
            while (matcher.find()) {
                // does not include the left parenthesis (
                val start: Int = matcher.start() + "(".length
                // does not include the closing bracket )
                val end: Int = matcher.end() - ")".length

                // Code information color
                var codeColor: Int = Color.parseColor("#999999")
                val lineIndex: Int = stackTrace!!.lastIndexOf("at ", start)
                if (lineIndex != -1) {
                    val lineData: String = spannable.subSequence(lineIndex, start).toString()
                    if (TextUtils.isEmpty(lineData)) {
                        continue
                    }
                    // Whether to highlight the number of lines of code
                    var highlight = true
                    for (packagePrefix: String? in SYSTEM_PACKAGE_PREFIX_LIST) {
                        if (lineData.startsWith("at $packagePrefix")) {
                            highlight = false
                            break
                        }
                    }
                    if (highlight) {
                        codeColor = Color.parseColor("#287BDE")
                    }
                }

                //Set the foreground
                spannable.setSpan(ForegroundColorSpan(codeColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                //Set underline
                spannable.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            messageView?.text = spannable
        }
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val screenWidth: Int = displayMetrics.widthPixels
        val screenHeight: Int = displayMetrics.heightPixels
        val smallestWidth: Float = min(screenWidth, screenHeight) / displayMetrics.density
        val targetResource: String?
        when {
            displayMetrics.densityDpi > 480 -> {
                targetResource = "xxxhdpi"
            }
            displayMetrics.densityDpi > 320 -> {
                targetResource = "xxhdpi"
            }
            displayMetrics.densityDpi > 240 -> {
                targetResource = "xhdpi"
            }
            displayMetrics.densityDpi > 160 -> {
                targetResource = "hdpi"
            }
            displayMetrics.densityDpi > 120 -> {
                targetResource = "mdpi"
            }
            else -> {
                targetResource = "ldpi"
            }
        }
        val builder: StringBuilder = StringBuilder()
        builder.append("Equipment brand:\t").append(Build.BRAND)
            .append("\nDevice model:\t").append(Build.MODEL)
            .append("\nDevice type:\t").append(if (isTablet()) "Tablet" else "Mobile phone")

        builder.append("\nScreen width and height:\t").append(screenWidth).append(" x ").append(screenHeight)
            .append("\nScreen density:\t").append(displayMetrics.densityDpi)
            .append("\nDensity pixels:\t").append(displayMetrics.density)
            .append("\nTarget resource:\t").append(targetResource)
            .append("\nMinimum width:\t").append(smallestWidth.toInt())

        builder.append("\nAndroid version:\t").append(Build.VERSION.RELEASE)
            .append("\nAPI version:\t").append(Build.VERSION.SDK_INT)
            .append("\nCPU architecture:\t").append(Build.SUPPORTED_ABIS[0])

//        builder.append("\nApplication version:\t").append(AppConfig.getVersionName())
//            .append("\nVersion code:\t").append(AppConfig.getVersionCode())

        try {
            val dateFormat = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
            val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            builder.append("\nFirst time installation：\t")
                .append(dateFormat.format(Date(packageInfo.firstInstallTime)))
                .append("\nRecently installed：\t").append(dateFormat.format(Date(packageInfo.lastUpdateTime)))
                .append("\nCRASH TIME：\t").append(dateFormat.format(Date()))
            val permissions: MutableList<String> = mutableListOf(
                *(packageInfo.requestedPermissions?.filterNotNull()?.toTypedArray() ?: emptyArray())
            )
            if (permissions.contains(Permission.READ_EXTERNAL_STORAGE) ||
                permissions.contains(Permission.WRITE_EXTERNAL_STORAGE)) {
                builder.append("\nStorage permissions：\t").append(
                    if (XXPermissions.isGranted(this, *Permission.Group.STORAGE)) "obtained" else "not obtained"
                )
            }
            if (permissions.contains(Permission.ACCESS_FINE_LOCATION) ||
                permissions.contains(Permission.ACCESS_COARSE_LOCATION)) {
                builder.append("\nLocation permissions：\t")
                if (XXPermissions.isGranted(this, Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)) {
                    builder.append("precise, rough")
                } else {
                    when {
                        XXPermissions.isGranted(this, Permission.ACCESS_FINE_LOCATION) -> {
                            builder.append("accurate")
                        }
                        XXPermissions.isGranted(this, Permission.ACCESS_COARSE_LOCATION) -> {
                            builder.append("rough")
                        }
                        else -> {
                            builder.append("Not obtained")
                        }
                    }
                }
            }
            if (permissions.contains(Permission.CAMERA)) {
                builder.append("\nCamera permissions：\t")
                    .append(if (XXPermissions.isGranted(this, Permission.CAMERA)) "obtained" else "not obtained")
            }
            if (permissions.contains(Permission.RECORD_AUDIO)) {
                builder.append("\nRecording permission：\t").append(
                    if (XXPermissions.isGranted(this, Permission.RECORD_AUDIO)) "obtained" else "not obtained"
                )
            }
            if (permissions.contains(Permission.SYSTEM_ALERT_WINDOW)) {
                builder.append("\nFloating window permissions：\t").append(
                    if (XXPermissions.isGranted(this, Permission.SYSTEM_ALERT_WINDOW)) "obtained" else "not obtained"
                )
            }
            if (permissions.contains(Permission.REQUEST_INSTALL_PACKAGES)) {
                builder.append("\nInstallation package permissions：\t").append(
                    if (XXPermissions.isGranted(this, Permission.REQUEST_INSTALL_PACKAGES)) "obtained" else "not obtained"
                )
            }
            if (permissions.contains(Manifest.permission.INTERNET)) {
                builder.append("\nCurrent network access：\t")

                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        InetAddress.getByName("www.baidu.com")
                        builder.append("normal")
                    } catch (ignored: UnknownHostException) {
                        builder.append("abnormal")
                    }
                    lifecycleScope.launch(Dispatchers.Main) {
                        infoView?.text = builder
                    }
                }
            } else {
                infoView?.text = builder
            }
        } catch (e: PackageManager.NameNotFoundException) {
//            CrashReport.postCatchedException(e)
        }
    }

    override fun observerData() {
        //
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_crash_info -> {
                drawerLayout?.openDrawer(GravityCompat.START)
            }
            R.id.iv_crash_share -> {
                // Share text
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, stackTrace)
                startActivity(Intent.createChooser(intent, ""))
            }
            R.id.iv_crash_restart -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        @Suppress("DEPRECATION")
        super.onBackPressed()
        // Restart the application
        RestartActivity.restart(this)
        finish()
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig() // Specify the background color of the navigation bar
            .navigationBarColor(R.color.white)
    }

    /**
     * Determine whether the current device is a tablet
     */
    fun isTablet(): Boolean {
        return ((resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }
}
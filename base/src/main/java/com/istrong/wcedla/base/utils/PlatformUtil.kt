package com.istrong.wcedla.base.utils

import android.app.Activity
import android.app.Application
import android.content.pm.ApplicationInfo
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.ColorInt
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object PlatformUtil {


    lateinit var application: Application
        private set
    val displayMetrics: DisplayMetrics by lazy { application.resources.displayMetrics }
    val screenWidth by lazy { displayMetrics.widthPixels }
    val screenHeight by lazy { displayMetrics.heightPixels }
    val density by lazy { displayMetrics.density }
    val scaledDensity by lazy { displayMetrics.scaledDensity }
    val GSONInstance = Gson()
    val prettyPrintingGSonInstance =
        GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create()

    /**
     * 获取Manifest中的debuggable标志位状态
     */
    val isManifestHaveDebugFlag by lazy { ((application.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0) }

    internal fun init(application: Application) {
        this.application = application
    }

    fun setNavAndBarTranslate(activity: Activity, darkBarText: Boolean, translateNav: Boolean) {
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = activity.window.decorView
            var viewOptions =
                (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            if (darkBarText) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    viewOptions = (viewOptions or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }
            }
            decorView.systemUiVisibility = viewOptions
            if (translateNav) {
                activity.window.navigationBarColor = Color.TRANSPARENT
            }
            activity.window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun setNavAndBarTranslate(
        activity: Activity,
        darkBarText: Boolean, @ColorInt navColor: Int
    ) {
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = activity.window.decorView
            var viewOptions =
                (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            if (darkBarText) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    viewOptions = viewOptions or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
            decorView.systemUiVisibility = viewOptions
            activity.window.navigationBarColor = navColor
            activity.window.statusBarColor = Color.TRANSPARENT
        }
    }

}
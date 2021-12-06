package com.reactnativenavigation.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.ColorInt
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlin.math.abs
import kotlin.math.ceil


object SystemUiUtils {
    private const val STATUS_BAR_HEIGHT_M = 24
    private const val STATUS_BAR_HEIGHT_L = 25
    internal const val STATUS_BAR_HEIGHT_TRANSLUCENCY = 0.65f
    private var statusBarHeight = -1
    var navigationBarDefaultColor = -1
        private set


    @JvmStatic
    fun getStatusBarHeight(activity: Activity?): Int {
        val res = if (statusBarHeight > 0) {
            statusBarHeight
        } else {
            statusBarHeight = activity?.let {
                val rectangle = Rect()
                val window: Window = activity.window
                window.decorView.getWindowVisibleDisplayFrame(rectangle)
                val statusBarHeight: Int = rectangle.top
                val contentView = window.findViewById<View>(Window.ID_ANDROID_CONTENT)
                contentView?.let {
                    val contentViewTop = contentView.top
                    abs(contentViewTop - statusBarHeight)
                }
            } ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) STATUS_BAR_HEIGHT_M else STATUS_BAR_HEIGHT_L
            statusBarHeight
        }
        return res
    }

    @JvmStatic
    fun saveStatusBarHeight(height: Int) {
        statusBarHeight = height
    }


    @JvmStatic
    fun getStatusBarHeightDp(activity: Activity?): Int {
        return UiUtils.pxToDp(activity, getStatusBarHeight(activity).toFloat())
            .toInt()
    }

    @JvmStatic
    fun hideNavigationBar(window: Window?, view: View) {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, view).let { controller ->
                controller.hide(WindowInsetsCompat.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    @JvmStatic
    fun showNavigationBar(window: Window?, view: View) {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(window, view).show(WindowInsetsCompat.Type.navigationBars())
        }
    }

    @JvmStatic
    fun setStatusBarColorScheme(window: Window?, view: View, isDark: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        window?.let {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = isDark
           // Workaround: on devices with api 30 status bar icons flickers or get hidden when removing view
            //turns out it is a bug on such devices, fixed by using system flags until it is fixed.
            var flags = view.systemUiVisibility
            flags = if (isDark) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }

            view.systemUiVisibility = flags
        }
    }

    @JvmStatic
    fun setStatusBarTranslucent(window: Window?) {
        window?.let {
            setStatusBarColor(window, window.statusBarColor, true)
        }
    }

    @JvmStatic
    fun isTranslucent(window: Window?): Boolean {
        return window?.let {
            Color.alpha(it.statusBarColor) < 255
        } ?: false
    }

    @JvmStatic
    fun clearStatusBarTranslucency(window: Window?) {
        window?.let {
            setStatusBarColor(it, it.statusBarColor, false)
        }
    }

    @JvmStatic
    fun setStatusBarColor(
        window: Window?,
        @ColorInt color: Int,
        translucent: Boolean
    ) {
        val opaqueColor = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Color.BLACK
        }else{
            val colorAlpha = Color.alpha(color)
            val alpha = if (translucent && colorAlpha == 255) STATUS_BAR_HEIGHT_TRANSLUCENCY else colorAlpha/255.0f
            val red: Int = Color.red(color)
            val green: Int = Color.green(color)
            val blue: Int = Color.blue(color)
            Color.argb(ceil(alpha * 255).toInt(), red, green, blue)
        }
        window?.statusBarColor = opaqueColor
    }

    @JvmStatic
    fun hideStatusBar(window: Window?, view: View) {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, view).let { controller ->
                controller.hide(WindowInsetsCompat.Type.statusBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    @JvmStatic
    fun showStatusBar(window: Window?, view: View) {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(window, view).show(WindowInsetsCompat.Type.statusBars())
        }
    }

    @JvmStatic
    fun setNavigationBarBackgroundColor(window: Window?, color: Int, lightColor: Boolean) {
        window?.let {
            if (navigationBarDefaultColor == -1) {
                navigationBarDefaultColor = window.navigationBarColor
            }
            WindowInsetsControllerCompat(window, window.decorView).let { controller ->
                controller.isAppearanceLightNavigationBars = lightColor
            }
            window.navigationBarColor = color
        }
    }

}
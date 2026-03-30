package com.reactnativenavigation.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlin.math.abs
import kotlin.math.ceil

object SystemUiUtils {
    private const val STATUS_BAR_HEIGHT_M = 24
    internal const val STATUS_BAR_HEIGHT_TRANSLUCENCY = 0.65f
    private var statusBarHeight = -1
    var navigationBarDefaultColor = Color.BLACK
        private set

    private var navBarBackgroundView: View? = null

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
            } ?: STATUS_BAR_HEIGHT_M
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
            WindowInsetsControllerCompat(window, view).let { controller ->
                controller.hide(WindowInsetsCompat.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    @JvmStatic
    fun showNavigationBar(window: Window?, view: View) {
        window?.let {
            WindowInsetsControllerCompat(window, view).show(WindowInsetsCompat.Type.navigationBars())
        }
    }

    @JvmStatic
    fun setStatusBarColorScheme(window: Window?, view: View, isDark: Boolean) {
        window?.let {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = isDark
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
        val colorAlpha = Color.alpha(color)
        val alpha = if (translucent && colorAlpha == 255) STATUS_BAR_HEIGHT_TRANSLUCENCY else colorAlpha/255.0f
        val red: Int = Color.red(color)
        val green: Int = Color.green(color)
        val blue: Int = Color.blue(color)
        val opaqueColor = Color.argb(ceil(alpha * 255).toInt(), red, green, blue)
        setStatusBarColor(window, opaqueColor)
    }

    fun setStatusBarColor(window: Window?, color: Int) {
        window?.statusBarColor = color
    }

    @JvmStatic
    fun getStatusBarColor(window: Window?): Int? {
        return window?.statusBarColor
    }

    @JvmStatic
    fun hideStatusBar(window: Window?, view: View) {
        window?.let {
            WindowInsetsControllerCompat(window, view).let { controller ->
                controller.hide(WindowInsetsCompat.Type.statusBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    @JvmStatic
    fun showStatusBar(window: Window?, view: View) {
        window?.let {
            WindowInsetsControllerCompat(window, view).show(WindowInsetsCompat.Type.statusBars())
        }
    }

    @JvmStatic
    fun setupNavigationBarBackground(contentLayout: ViewGroup) {
        if (navBarBackgroundView != null) return

        val view = View(contentLayout.context).apply {
            setBackgroundColor(navigationBarDefaultColor)
        }
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, 0, Gravity.BOTTOM
        )
        contentLayout.addView(view, params)
        navBarBackgroundView = view

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            val lp = v.layoutParams
            if (lp.height != navBarHeight) {
                lp.height = navBarHeight
                v.layoutParams = lp
            }
            insets
        }
        view.requestApplyInsets()
    }

    @JvmStatic
    fun setNavigationBarBackgroundColor(window: Window?, color: Int, lightColor: Boolean) {
        window?.let {
            WindowInsetsControllerCompat(window, window.decorView).let { controller ->
                controller.isAppearanceLightNavigationBars = lightColor
            }
        }
        navBarBackgroundView?.setBackgroundColor(color)
    }

}

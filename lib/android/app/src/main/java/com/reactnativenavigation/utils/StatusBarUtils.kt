package com.reactnativenavigation.utils

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlin.math.abs

object StatusBarUtils {
    private const val STATUS_BAR_HEIGHT_M = 24
    private const val STATUS_BAR_HEIGHT_L = 25
    private var statusBarHeight = -1

    @JvmStatic
    fun getStatusBarHeight(activity: Activity?): Int {
        val res= if (statusBarHeight > 0) {
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
        logd("StatusBarHeight $res","STATUSXXXX")
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
    fun isTranslucent(window: Window?): Boolean {
        return window?.let {
            val lp = window.attributes
            return lp != null && lp.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } ?: false
    }

    fun hideSystemUi(window: Window?, view: View) {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, view).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    fun showSystemUi(window: Window?, view: View) {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(window, view).show(WindowInsetsCompat.Type.navigationBars())
        }
    }

    fun hideNavigationBar(window: Window?, view: View) {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, view).let { controller ->
                controller.hide(WindowInsetsCompat.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    fun showNavigationBar(window: Window?, view: View) {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(window, view).show(WindowInsetsCompat.Type.navigationBars())
        }
    }
    @JvmStatic
    fun hideStatusBar(window: Window?, view: View) {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(window, true)
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
}
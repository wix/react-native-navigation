package com.reactnativenavigation.utils

import android.content.Context
import android.os.Build
import android.view.Window
import android.view.WindowManager

object StatusBarUtils {
    private const val STATUS_BAR_HEIGHT_M = 24
    private const val STATUS_BAR_HEIGHT_L = 25
    private var statusBarHeight = -1
    @JvmStatic
    fun saveStatusBarHeight(height: Int) {
        statusBarHeight = height
    }
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        if (statusBarHeight > 0) {
            return statusBarHeight
        }
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        statusBarHeight = if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else UiUtils.dpToPx(
            context,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) STATUS_BAR_HEIGHT_M else STATUS_BAR_HEIGHT_L
        )
        return statusBarHeight
    }
    @JvmStatic
    fun getStatusBarHeightDp(context: Context): Int {
        return UiUtils.pxToDp(context, getStatusBarHeight(context).toFloat())
            .toInt()
    }
    @JvmStatic
    fun isTranslucent(window: Window): Boolean {
        val lp = window.attributes
        return lp != null && lp.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
    }
}
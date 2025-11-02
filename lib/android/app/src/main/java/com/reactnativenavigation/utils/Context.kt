package com.reactnativenavigation.utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.react.ReactApplication
import com.reactnativenavigation.NavigationApplication

fun Context.isDebug(): Boolean {
    return (applicationContext as ReactApplication).reactNativeHost.useDeveloperSupport
}
fun isDarkMode() = NavigationApplication.instance.isDarkMode()
fun Context.isDarkMode(): Boolean = when (AppCompatDelegate.getDefaultNightMode()) {
    AppCompatDelegate.MODE_NIGHT_YES -> true
    AppCompatDelegate.MODE_NIGHT_NO -> false
    else -> resources.configuration.isDarkMode()
}
fun Configuration.isDarkMode() =
    (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
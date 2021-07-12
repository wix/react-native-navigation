package com.reactnativenavigation.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import com.facebook.react.ReactApplication
import com.reactnativenavigation.NavigationApplication

fun Context.isDebug(): Boolean {
    return (applicationContext as ReactApplication).reactNativeHost.useDeveloperSupport
}
fun isDarkMode() = NavigationApplication.instance.isDarkMode()
fun Context.isDarkMode(): Boolean =
    (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == UI_MODE_NIGHT_YES
fun Configuration.isDarkMode() = (uiMode and Configuration.UI_MODE_NIGHT_MASK) == UI_MODE_NIGHT_YES
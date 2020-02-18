package com.reactnativenavigation.utils

import android.content.Context
import com.facebook.react.ReactApplication

fun Context.isDebug(): Boolean {
    return (applicationContext as ReactApplication).reactNativeHost.useDeveloperSupport
}
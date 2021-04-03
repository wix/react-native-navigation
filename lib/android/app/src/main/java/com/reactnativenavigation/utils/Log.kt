package com.reactnativenavigation.utils

import android.util.Log
import com.reactnativenavigation.BuildConfig

const val MAIN_LIB_TAG = "RNN";
fun logd(msg: String?, tag: String = MAIN_LIB_TAG) {
    if (BuildConfig.DEBUG) {
        if (msg != null)
            Log.d(tag, msg)
        else
            Log.d(tag, "Cannot log null msg: $msg")
    }
}

fun wran(msg: String?, tag: String = MAIN_LIB_TAG) {
    if (!BuildConfig.DEBUG) {
        if (msg != null)
            Log.w(tag, msg)
        else
            Log.w(tag, "Cannot log null msg: $msg")
    }
}
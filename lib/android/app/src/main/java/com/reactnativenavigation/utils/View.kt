package com.reactnativenavigation.utils

import android.view.View
import androidx.core.view.doOnLayout

inline fun View.doIfMeasuredOrOnLayout(crossinline action: () -> Unit) {
    if (width > 0 && height > 0) action() else doOnLayout { action() }
}
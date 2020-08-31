package com.reactnativenavigation.utils

import android.view.View
import androidx.core.view.doOnLayout
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun View.awaitPost() = suspendCoroutine<Unit> { cont ->
    post { cont.resume(Unit) }
}

inline fun View.doOnLayoutCompat(crossinline action: (view: View) -> Unit) {
    if (isLaidOut || (width > 0 && height > 0)) action(this) else doOnLayout { action(this) }
}
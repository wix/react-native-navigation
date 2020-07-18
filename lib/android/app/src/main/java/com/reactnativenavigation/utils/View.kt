package com.reactnativenavigation.utils

import android.view.View
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun View.awaitPost() = suspendCoroutine<Unit> { cont ->
    post { cont.resume(Unit) }
}
package com.reactnativenavigation.utils

import android.view.View
import android.view.ViewParent
import androidx.core.view.doOnLayout
import androidx.core.view.doOnNextLayout
import androidx.core.view.doOnPreDraw
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun View.awaitPost() = suspendCoroutine<Unit> { cont ->
    post { cont.resume(Unit) }
}

suspend fun View.awaitLayout() = suspendCoroutine<Unit> { cont ->
    doOnLayout { cont.resume(Unit) }
}

suspend fun View.awaitNextLayout() = suspendCoroutine<Unit> { cont ->
    doOnNextLayout { cont.resume(Unit) }
}

suspend fun View.awaitDraw() = suspendCoroutine<Unit> { cont ->
    doOnPreDraw { cont.resume(Unit) }
}

inline fun View.doOnLayoutCompat(crossinline action: (view: View) -> Unit) {
    if (isLaidOut || (width > 0 && height > 0)) action(this) else doOnLayout { action(this) }
}

val View.grandparent: ViewParent
    get() = parent.parent
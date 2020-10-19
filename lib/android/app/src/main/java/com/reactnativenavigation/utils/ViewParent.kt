package com.reactnativenavigation.utils

import android.view.View
import android.view.ViewParent
import androidx.core.view.doOnLayout
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val ViewParent.scaleX: Float
    get() = (this as View).scaleX

val ViewParent.scaleY: Float
    get() = (this as View).scaleY

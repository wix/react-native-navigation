package com.reactnativenavigation.utils

import android.view.View
import android.view.ViewParent

val ViewParent.scaleX: Float
    get() = (this as View).scaleX

val ViewParent.scaleY: Float
    get() = (this as View).scaleY

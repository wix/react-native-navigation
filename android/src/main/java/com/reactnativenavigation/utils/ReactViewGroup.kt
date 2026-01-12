package com.reactnativenavigation.utils

import com.facebook.react.views.view.ReactViewGroup

val ReactViewGroup.borderRadius: Float
    get() = 0f // CSSBackgroundDrawable is no longer available, return 0f as fallback
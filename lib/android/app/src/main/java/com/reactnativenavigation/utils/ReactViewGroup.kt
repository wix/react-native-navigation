package com.reactnativenavigation.utils

import com.facebook.react.views.view.ReactViewBackgroundDrawable
import com.facebook.react.views.view.ReactViewGroup

val ReactViewGroup.borderRadius: Float
    get() = (background as? ReactViewBackgroundDrawable)?.fullBorderRadius ?: 0f
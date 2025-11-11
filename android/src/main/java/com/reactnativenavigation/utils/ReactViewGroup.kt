package com.reactnativenavigation.utils

import com.facebook.react.common.annotations.UnstableReactNativeAPI
import com.facebook.react.uimanager.drawable.CSSBackgroundDrawable
import com.facebook.react.views.view.ReactViewGroup

@OptIn(UnstableReactNativeAPI::class)
val ReactViewGroup.borderRadius: Float
    get() = (background as? CSSBackgroundDrawable)?.fullBorderWidth ?: 0f
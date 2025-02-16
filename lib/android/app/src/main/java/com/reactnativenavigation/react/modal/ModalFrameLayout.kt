package com.reactnativenavigation.react.modal

import android.widget.FrameLayout
import com.facebook.react.bridge.ReactContext
import com.reactnativenavigation.utils.SystemUiUtils

class ModalFrameLayout(context: ReactContext) : FrameLayout(context) {
    val modalContentLayout = ModalContentLayout(context)

    init {
        addView(modalContentLayout, MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.MATCH_PARENT)
            .apply {
                val translucent = context.currentActivity?.window?.let {
                    SystemUiUtils.isTranslucent(it)
                } ?: false
                topMargin = if (translucent) 0 else SystemUiUtils.getStatusBarHeight(context.currentActivity)
            })
    }
}
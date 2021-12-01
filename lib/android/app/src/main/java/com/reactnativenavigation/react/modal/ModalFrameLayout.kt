package com.reactnativenavigation.react.modal

import android.widget.FrameLayout
import com.facebook.react.bridge.ReactContext
import com.reactnativenavigation.utils.SystemUiUtils

class ModalFrameLayout(context: ReactContext) : FrameLayout(context) {
    val modalContentLayout = ModalContentLayout(context)

    init {
        addView(modalContentLayout, MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT)
            .apply {
                val translucent = context.currentActivity?.window?.let {
                    SystemUiUtils.isTranslucent(it)
                } ?: false
                topMargin = if (translucent) 0 else SystemUiUtils.getStatusBarHeight(context.currentActivity)
            })
    }
}
package com.reactnativenavigation.viewcontrollers.viewcontroller

import android.view.View
import com.facebook.react.ReactInstanceManager
import com.facebook.react.modules.i18nmanager.I18nUtil
import com.reactnativenavigation.options.Options

class LayoutDirectionApplier {
    fun apply(root: ViewController<*>, options: Options, instanceManager: ReactInstanceManager) {
        if (instanceManager.currentReactContext != null) {
            if (I18nUtil.getInstance().isRTL(instanceManager.currentReactContext)) {
                root.activity.window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
            } else {
                root.activity.window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
            }
        }
    }
}
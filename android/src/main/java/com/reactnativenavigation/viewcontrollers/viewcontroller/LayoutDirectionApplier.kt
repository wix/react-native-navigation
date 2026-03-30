package com.reactnativenavigation.viewcontrollers.viewcontroller

import com.facebook.react.ReactInstanceManager
import com.facebook.react.modules.i18nmanager.I18nUtil
import com.reactnativenavigation.options.Options

class LayoutDirectionApplier {
    fun apply(root: ViewController<*>, options: Options, instanceManager: ReactInstanceManager) {
        if (options.layout.direction.hasValue() && instanceManager.currentReactContext != null) {
            root.activity.window.decorView.layoutDirection = options.layout.direction.get()
            I18nUtil.getInstance().allowRTL(instanceManager.currentReactContext, options.layout.direction.isRtl)
            I18nUtil.getInstance().forceRTL(instanceManager.currentReactContext, options.layout.direction.isRtl)
        }
    }
}
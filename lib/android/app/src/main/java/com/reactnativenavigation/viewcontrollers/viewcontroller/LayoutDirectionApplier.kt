package com.reactnativenavigation.viewcontrollers.viewcontroller

import android.annotation.SuppressLint
import com.facebook.react.ReactInstanceManager
import com.facebook.react.modules.i18nmanager.I18nUtil
import com.reactnativenavigation.options.Options

class LayoutDirectionApplier {
    @SuppressLint("WrongConstant")
    fun apply(root: ViewController<*>, options: Options, instanceManager: ReactInstanceManager) {
        if (options.layout.direction.hasValue() && instanceManager.currentReactContext != null) {
            root.activity.window.decorView.layoutDirection = options.layout.direction.get()
            I18nUtil.instance.allowRTL(instanceManager.currentReactContext?.applicationContext!!, options.layout.direction.isRtl)
            I18nUtil.instance.forceRTL(instanceManager.currentReactContext?.applicationContext!!, options.layout.direction.isRtl)
        }
    }
}
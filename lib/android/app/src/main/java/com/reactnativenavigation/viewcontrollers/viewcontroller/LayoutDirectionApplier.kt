package com.reactnativenavigation.viewcontrollers.viewcontroller

import android.annotation.SuppressLint
import android.content.Context
import com.facebook.react.ReactInstanceManager
import com.facebook.react.modules.i18nmanager.I18nUtil
import com.reactnativenavigation.options.Options

class LayoutDirectionApplier {
    @SuppressLint("WrongConstant")
    fun apply(root: ViewController<*>, options: Options, context: Context) {
        if (options.layout.direction.hasValue()) {
            root.activity.window.decorView.layoutDirection = options.layout.direction.get()
            I18nUtil.instance.allowRTL(context.applicationContext, options.layout.direction.isRtl)
            I18nUtil.instance.forceRTL(context.applicationContext, options.layout.direction.isRtl)
        }
    }
}
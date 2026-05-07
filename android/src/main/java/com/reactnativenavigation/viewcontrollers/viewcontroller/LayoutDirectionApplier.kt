package com.reactnativenavigation.viewcontrollers.viewcontroller

import android.annotation.SuppressLint
import com.facebook.react.modules.i18nmanager.I18nUtil
import com.reactnativenavigation.options.Options

class LayoutDirectionApplier {
    @SuppressLint("WrongConstant")
    fun apply(root: ViewController<*>, options: Options) {
        val currentContext = root.view?.context ?: return

        if (options.layout.direction.hasValue()) {
            val direction = options.layout.direction.get()
            root.activity?.window?.decorView?.let { decor ->
                decor.layoutDirection = direction
            }
            I18nUtil.instance.allowRTL(currentContext, options.layout.direction.isRtl)
            I18nUtil.instance.forceRTL(currentContext, options.layout.direction.isRtl)
        }
    }
}

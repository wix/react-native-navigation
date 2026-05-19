package com.reactnativenavigation.customrow

import android.app.Application
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule

/**
 * RN bridge module that lets JS push the latest `bottomTabs.customRow`
 * configuration to native. The JS-side `AndroidCustomRowForwarder`
 * scans `Navigation.setRoot` / `setDefaultOptions` / `mergeOptions`
 * payloads and calls [configure] whenever it finds a `customRow` block.
 */
@ReactModule(name = BottomTabsCustomRowModule.NAME)
class BottomTabsCustomRowModule(
    reactContext: ReactApplicationContext,
) : ReactContextBaseJavaModule(reactContext) {

    init {
        Log.d(TAG, "init")
        val app = reactContext.applicationContext as? Application
        if (app != null) BottomTabsCustomRowAttacher.registerOnce(app)
    }

    override fun getName(): String = NAME

    @ReactMethod
    fun configure(config: ReadableMap?) {
        Log.d(TAG, "configure $config")
        BottomTabsCustomRowConfigStore.update(BottomTabsCustomRowOptions.fromMap(config))
        BottomTabsCustomRowAttacher.rescan()
    }

    companion object {
        const val NAME = "RNNBottomTabsCustomRowModule"
        private const val TAG = "RNNCustomRow"
    }
}

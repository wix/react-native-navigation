package com.reactnativenavigation.react

import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.reactnativenavigation.NavigationActivity
import com.reactnativenavigation.options.LayoutFactory

class NavigationPackage(private val reactNativeHost: ReactNativeHost) : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(
            NavigationModule(
                reactContext,
                reactNativeHost.reactInstanceManager,
                LayoutFactory(reactNativeHost.reactInstanceManager)
            )
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return listOf(RNNModalViewManager((reactContext.currentActivity as NavigationActivity).navigator))
    }
}
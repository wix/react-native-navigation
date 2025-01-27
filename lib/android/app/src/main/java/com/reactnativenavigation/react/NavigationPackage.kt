package com.reactnativenavigation.react

import com.facebook.react.ReactApplication
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.reactnativenavigation.options.LayoutFactory
import com.reactnativenavigation.react.modal.ModalViewManager

class NavigationPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {

        val reactInstanceUtils = ReactInstanceUtils(reactContext.applicationContext as ReactApplication)

        return listOf(
            NavigationModule(
                reactContext,
                LayoutFactory(reactInstanceUtils)
            )
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {

        return listOf(ModalViewManager(reactContext))
    }
}

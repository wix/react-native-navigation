package com.reactnativenavigation.react

import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.reactnativenavigation.options.LayoutFactory
import com.reactnativenavigation.react.modal.ModalViewManager

class NavigationPackage() : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        val reactApp = reactContext.applicationContext as ReactApplication
        return listOf(
            NavigationModule(
                reactContext,
                LayoutFactory(reactApp.reactHost)
            )
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {

        return listOf(ModalViewManager(reactContext))
    }
}

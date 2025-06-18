package com.reactnativenavigation.react

import com.facebook.react.BaseReactPackage
import com.facebook.react.ReactApplication
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.uimanager.ViewManager
import com.reactnativenavigation.options.LayoutFactory
import com.reactnativenavigation.react.modal.ModalViewManager

class NavigationPackage() : BaseReactPackage() {

    override fun getModule(name: String, context: ReactApplicationContext): NativeModule? {
        val reactApp = context.applicationContext as ReactApplication
        return when (name) {
            NavigationTurboModule.NAME -> {
                NavigationTurboModule(context, LayoutFactory(reactApp.reactHost))
            }
            else -> {
                null
            }
        }
    }

    override fun getReactModuleInfoProvider() = ReactModuleInfoProvider {
        mapOf(NavigationTurboModule.NAME to ReactModuleInfo(
            name = NavigationTurboModule.NAME,
            className = NavigationTurboModule.NAME,
            canOverrideExistingModule = false,
            needsEagerInit = false,
            isCxxModule = false,
            isTurboModule = true
        ))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return mutableListOf(ModalViewManager(reactContext))
    }
}

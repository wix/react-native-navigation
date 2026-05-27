package com.reactnativenavigation

import com.facebook.react.BaseReactPackage
import com.facebook.react.ReactApplication
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.uimanager.ViewManager
import android.app.Application
import com.reactnativenavigation.customrow.BottomTabsCustomRowAttacher
import com.reactnativenavigation.customrow.BottomTabsCustomRowModule
import com.reactnativenavigation.options.LayoutFactory
import com.reactnativenavigation.react.NavigationTurboModule
import com.reactnativenavigation.react.modal.ModalViewManager

class NavigationPackage() : BaseReactPackage() {

    override fun getModule(name: String, context: ReactApplicationContext): NativeModule? {
        val reactApp = context.applicationContext as ReactApplication
        (context.applicationContext as? Application)?.let {
            BottomTabsCustomRowAttacher.registerOnce(it, context.currentActivity)
        }
        return when (name) {
            NavigationTurboModule.NAME -> {
                NavigationTurboModule(context, LayoutFactory(reactApp.reactHost))
            }
            BottomTabsCustomRowModule.NAME -> {
                BottomTabsCustomRowModule(context)
            }
            else -> {
                null
            }
        }
    }

    override fun getReactModuleInfoProvider() = ReactModuleInfoProvider {
        mapOf(
            NavigationTurboModule.NAME to ReactModuleInfo(
                name = NavigationTurboModule.NAME,
                className = NavigationTurboModule.NAME,
                canOverrideExistingModule = false,
                needsEagerInit = false,
                isCxxModule = false,
                isTurboModule = true
            ),
            BottomTabsCustomRowModule.NAME to ReactModuleInfo(
                name = BottomTabsCustomRowModule.NAME,
                className = BottomTabsCustomRowModule.NAME,
                canOverrideExistingModule = false,
                needsEagerInit = true,
                isCxxModule = false,
                isTurboModule = false
            )
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return mutableListOf(ModalViewManager(reactContext))
    }
}


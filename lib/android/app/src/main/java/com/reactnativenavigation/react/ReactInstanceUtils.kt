package com.reactnativenavigation.react

import android.app.Activity
import com.facebook.react.ReactApplication
import com.facebook.react.ReactInstanceEventListener
import com.facebook.react.bridge.ReactContext
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import org.joor.Reflect

class ReactInstanceUtils(reactApplication: ReactApplication) {

    private val reactHost = reactApplication.reactHost
    private val reactInstanceManager = reactApplication.reactNativeHost.reactInstanceManager

    fun getContext(): ReactContext? {
        return if (DefaultNewArchitectureEntryPoint.bridgelessEnabled) {
            reactHost?.currentReactContext
        } else {
            reactInstanceManager.currentReactContext
        }
    }

    fun addReactInstanceEventListener(listener: ReactInstanceEventListener) {
        if (DefaultNewArchitectureEntryPoint.bridgelessEnabled) {
            reactHost?.addReactInstanceEventListener(listener)
        } else {
            reactInstanceManager.addReactInstanceEventListener(
                listener
            )
        }
    }

    fun removeReactInstanceEventListener(listener: ReactInstanceEventListener) {
        if (DefaultNewArchitectureEntryPoint.bridgelessEnabled) {
            reactHost?.removeReactInstanceEventListener(listener)
        } else {
            reactInstanceManager.removeReactInstanceEventListener(
                listener
            )
        }
    }

    fun hasStartedCreatingInitialContext(): Boolean {
        return if (DefaultNewArchitectureEntryPoint.bridgelessEnabled) {
            Reflect.on(reactHost).field("mStartTask").get<Any>() != null
        } else {
            reactInstanceManager.hasStartedCreatingInitialContext()
        }
    }

    fun onHostResume(activity: Activity, defaultBackButtonImpl: DefaultHardwareBackBtnHandler) {
        if (DefaultNewArchitectureEntryPoint.bridgelessEnabled) {
            reactHost?.onHostResume(activity, defaultBackButtonImpl)
        } else {
            reactInstanceManager.onHostResume(activity, defaultBackButtonImpl)
        }
    }

    fun onHostPause(activity: Activity) {
        if (DefaultNewArchitectureEntryPoint.bridgelessEnabled) {
            reactHost?.onHostPause(activity)
        } else {
            reactInstanceManager.onHostPause(activity)
        }
    }

    fun onHostDestroy(activity: Activity) {
        if (DefaultNewArchitectureEntryPoint.bridgelessEnabled) {
            reactHost?.onHostDestroy(activity)
        } else {
            reactInstanceManager.onHostDestroy(activity)
        }
    }

    fun createReactContextInBackground() {
        if (DefaultNewArchitectureEntryPoint.bridgelessEnabled) {
            reactHost?.start()
        } else {
            reactInstanceManager.createReactContextInBackground()
        }
    }
}
package com.reactnativenavigation.react

import com.facebook.react.ReactInstanceEventListener
import com.facebook.react.ReactInstanceManager
import com.facebook.react.bridge.ReactContext
import com.reactnativenavigation.NavigationActivity
import com.reactnativenavigation.react.events.EventEmitter

class NavigationReactInitializer internal constructor(
    private val reactInstanceUtils: ReactInstanceUtils,
    isDebug: Boolean
) :
    ReactInstanceEventListener {
    private val devPermissionRequest = DevPermissionRequest(isDebug)
    private var waitingForAppLaunchEvent = true
    private var isActivityReadyForUi = false

    fun onActivityCreated() {
        reactInstanceUtils.addReactInstanceEventListener(this)
        waitingForAppLaunchEvent = true
    }

    fun onActivityResumed(activity: NavigationActivity) {
        if (devPermissionRequest.shouldAskPermission(activity)) {
            devPermissionRequest.askPermission(activity)
        } else {
            reactInstanceUtils.onHostResume(activity, activity)
            isActivityReadyForUi = true
            prepareReactApp()
        }
    }

    fun onActivityPaused(activity: NavigationActivity) {
        isActivityReadyForUi = false
        if (reactInstanceUtils.hasStartedCreatingInitialContext()) {
            reactInstanceUtils.onHostPause(activity)
        }
    }

    fun onActivityDestroyed(activity: NavigationActivity) {
        reactInstanceUtils.removeReactInstanceEventListener(this)
        if (reactInstanceUtils.hasStartedCreatingInitialContext()) {
            reactInstanceUtils.onHostDestroy(activity)
        }
    }

    private fun prepareReactApp() {
        if (shouldCreateContext()) {
            reactInstanceUtils.createReactContextInBackground()
        } else if (waitingForAppLaunchEvent) {
            reactInstanceUtils.getContext()?.let {
                emitAppLaunched(it)
            }
        }
    }

    private fun emitAppLaunched(context: ReactContext) {
        if (!isActivityReadyForUi) return
        waitingForAppLaunchEvent = false
        EventEmitter(context).appLaunched()
    }

    private fun shouldCreateContext(): Boolean {
        return !reactInstanceUtils.hasStartedCreatingInitialContext()
    }

    override fun onReactContextInitialized(context: ReactContext) {
        emitAppLaunched(context)
    }
}
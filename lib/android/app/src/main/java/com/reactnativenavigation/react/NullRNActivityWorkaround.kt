package com.reactnativenavigation.react

import com.facebook.react.bridge.ReactApplicationContext
import com.reactnativenavigation.utils.sleep

private const val ACTIVITY_WAIT_INTERVAL = 100L
private const val ACTIVITY_WAIT_TRIES = 150

object NullRNActivityWorkaround {
    @JvmStatic
    fun waitForActivity(reactAppContext: ReactApplicationContext) {
        var tries = 0
        while (tries < ACTIVITY_WAIT_TRIES && !isActivityReady(reactAppContext)) {
            sleep(ACTIVITY_WAIT_INTERVAL)
            tries++
        }
    }

    private fun isActivityReady(reactAppContext: ReactApplicationContext): Boolean {
        return reactAppContext.hasCurrentActivity()
    }
}

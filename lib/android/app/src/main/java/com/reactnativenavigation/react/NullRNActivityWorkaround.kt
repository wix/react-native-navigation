package com.reactnativenavigation.react

import android.app.Activity
import android.util.Log
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.ReactApplicationContext
import com.reactnativenavigation.utils.sleepSafe
import java.lang.ref.WeakReference

private const val ACTIVITY_WAIT_INTERVAL = 100L
private const val ACTIVITY_WAIT_TRIES = 150

private const val LOG_TAG = "RNN.NullActivity"

/**
 * This is a helper class to work-around this RN problem: https://github.com/facebook/react-native/issues/37518
 *
 * It is, hopefully, temporary.
 */
class NullRNActivityWorkaround(reactAppContext: ReactApplicationContext) {
    private val reactAppContext: WeakReference<ReactApplicationContext>
    @Volatile
    private var hasHost = true

    init {
        this.reactAppContext = WeakReference(reactAppContext)
        reactAppContext.addLifecycleEventListener(object: LifecycleEventListener {
            override fun onHostDestroy() {
                Log.d(LOG_TAG, "HOST_DESTROY")
                hasHost = false
            }
            override fun onHostResume() {
                Log.d(LOG_TAG, "HOST_RESUME")
                hasHost = true
            }
            override fun onHostPause() {}
        })
    }

    fun getActivity(): Activity? {
        waitForActivity()
        return reactAppContext.get()?.currentActivity
    }

    private fun waitForActivity() {
        val activityReady = { (reactAppContext.get()?.hasCurrentActivity() ?: false) }

        var tries = 0
        while (tries < ACTIVITY_WAIT_TRIES && hasHost && !activityReady()) {
            Log.d(LOG_TAG, "Busy-waiting for activity! Try: #$tries...")
            sleepSafe(ACTIVITY_WAIT_INTERVAL)
            tries++
        }
    }
}

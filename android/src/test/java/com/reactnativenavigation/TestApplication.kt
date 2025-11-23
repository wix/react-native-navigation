package com.reactnativenavigation

import android.app.Application
import androidx.appcompat.R
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost

class TestApplication : Application(), ReactApplication {
    override val reactNativeHost: ReactNativeHost = object : ReactNativeHost(this) {
        override fun getUseDeveloperSupport(): Boolean {
            return true
        }

        override fun getPackages(): MutableList<ReactPackage> {
            return mutableListOf()
        }
    }

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.Theme_AppCompat)
    }

    override val reactHost: ReactHost
        get() = getDefaultReactHost(this, reactNativeHost)
}

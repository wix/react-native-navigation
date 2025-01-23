package com.reactnativenavigation.playground

import android.app.Application
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.soloader.OpenSourceMergedSoMapping
import com.facebook.soloader.SoLoader
import com.reactnativenavigation.react.NavigationPackage
import com.reactnativenavigation.react.NavigationReactNativeHost

class MainApplication : Application(), ReactApplication {
    override val reactNativeHost: ReactNativeHost = object : NavigationReactNativeHost(this) {
        override fun getJSMainModuleName(): String {
            return "index"
        }

        override fun getUseDeveloperSupport(): Boolean {
            return BuildConfig.DEBUG
        }

        public override fun getPackages(): List<ReactPackage> {
            val packages = PackageList(this).packages
            packages.add(NavigationPackage(this))
            return packages
        }

        override val isHermesEnabled: Boolean
            get() = BuildConfig.IS_HERMES_ENABLED

        override val isNewArchEnabled: Boolean
            get() = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
    }

    override val reactHost: ReactHost
        get() = getDefaultReactHost(applicationContext, reactNativeHost)

    override fun onCreate() {
        super.onCreate()
        //registerExternalComponent("RNNCustomComponent", FragmentCreator())

        SoLoader.init(this, OpenSourceMergedSoMapping)
        if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
            // If you opted-in for the New Architecture, we load the native entry point for this app.
            load()
        }
    }
}

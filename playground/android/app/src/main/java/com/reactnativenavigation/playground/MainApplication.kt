package com.reactnativenavigation.playground

import com.facebook.react.PackageList
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.reactnativenavigation.NavigationApplication
import com.reactnativenavigation.RNNToggles
import com.reactnativenavigation.NavigationPackage
import com.reactnativenavigation.react.NavigationReactNativeHost

class MainApplication : NavigationApplication(mapOf(
    RNNToggles.TOP_BAR_COLOR_ANIMATION__PUSH to true,
    RNNToggles.TOP_BAR_COLOR_ANIMATION__TABS to true,
    RNNToggles.TAB_BAR_TRANSLUCENCE to true,
)) {
    override val reactNativeHost: ReactNativeHost = object : NavigationReactNativeHost(this) {
        override fun getJSMainModuleName(): String {
            return "index"
        }

        override fun getUseDeveloperSupport(): Boolean {
            return BuildConfig.DEBUG
        }

        public override fun getPackages(): List<ReactPackage> {
            val packages = PackageList(this).packages
            packages.add(NavigationPackage())
            return packages
        }

        override val isHermesEnabled: Boolean
            get() = BuildConfig.IS_HERMES_ENABLED

        override val isNewArchEnabled: Boolean
            get() = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
    }

    override fun onCreate() {
        super.onCreate()
        registerExternalComponent("RNNCustomComponent", FragmentCreator())
    }
    override val reactHost: ReactHost
        get() = getDefaultReactHost(this, reactNativeHost)

}

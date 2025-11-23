package com.reactnativenavigation

import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.shell.MainReactPackage
import com.reactnativenavigation.react.NavigationReactNativeHost

class TestApplication : NavigationApplication() {


    override val reactNativeHost: ReactNativeHost
        get() = object : NavigationReactNativeHost(this) {
            override fun getJSMainModuleName(): String {
                return "index"
            }

            override fun getUseDeveloperSupport(): Boolean {
                return false
            }

            public override fun getPackages(): List<ReactPackage> {
                val packages = listOf(MainReactPackage(null), NavigationPackage())
                return packages
            }

            override val isHermesEnabled: Boolean
                get() = true

            override val isNewArchEnabled: Boolean
                get() = true
        }

    override val reactHost: ReactHost
        get() = getDefaultReactHost(this, reactNativeHost)

}
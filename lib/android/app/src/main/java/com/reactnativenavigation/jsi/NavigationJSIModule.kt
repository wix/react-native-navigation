package com.reactnativenavigation.jsi

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.reactnativenavigation.react.NavigationModule

class NavigationJSIModule : JSIModulePackage {
    override fun getJSIModules(
        reactApplicationContext: ReactApplicationContext?,
        jsContext: JavaScriptContextHolder?
    ): MutableList<JSIModuleSpec<JSIModule>> {
        reactApplicationContext!!.getNativeModule(NavigationModule::class.java)?.installLib(jsContext)

        return mutableListOf()
    }
}
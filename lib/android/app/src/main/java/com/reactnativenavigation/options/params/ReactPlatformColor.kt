package com.reactnativenavigation.options.params

import com.facebook.react.bridge.ColorPropConverter
import com.facebook.react.bridge.ReadableMap
import com.reactnativenavigation.NavigationApplication

private fun parsePlatformColor(paths: ReadableMap) =
    ColorPropConverter.getColor(paths, NavigationApplication.instance)

class ReactPlatformColor(private val paths: ReadableMap) :
    Colour(parsePlatformColor(paths)) {
    override fun get(): Int {
        return parsePlatformColor(paths)
    }

    override fun get(defaultValue: Int?): Int? {
        return try {
            parsePlatformColor(paths)
        }catch (e:Exception){
            defaultValue
        }
    }
}
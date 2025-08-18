package com.reactnativenavigation.options.params

import com.facebook.react.bridge.ColorPropConverter
import com.facebook.react.bridge.ReadableMap
import com.reactnativenavigation.NavigationApplication

private fun parsePlatformColor(paths: ReadableMap): Int {
    return ColorPropConverter.getColor(paths, NavigationApplication.instance) ?: 0 // fallback to black
}

class ReactPlatformColor(private val paths: ReadableMap) :
    Colour(parsePlatformColor(paths)) {

    override fun get(): Int {
        return parsePlatformColor(paths)
    }

    override fun get(defaultValue: Int?): Int {
        return try {
            ColorPropConverter.getColor(paths, NavigationApplication.instance) ?: defaultValue ?: 0
        } catch (e: Exception) {
            defaultValue ?: 0
        }
    }
}
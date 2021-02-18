package com.reactnativenavigation.options

import android.content.Context
import com.reactnativenavigation.options.animations.ViewAnimationOptions
import com.reactnativenavigation.options.params.*
import com.reactnativenavigation.options.parsers.BoolParser
import com.reactnativenavigation.options.parsers.ColorParser
import com.reactnativenavigation.options.parsers.FractionParser
import org.json.JSONObject


open class HardwareBackButtonOptions(json: JSONObject? = null) {

    @JvmField var dismissModalOnPress: Bool = NullBool()
    @JvmField var popStackOnPress: Bool = NullBool()

    init {
        parse(json)
    }

    fun mergeWith(other: HardwareBackButtonOptions) {
        if (other.dismissModalOnPress.hasValue()) dismissModalOnPress = other.dismissModalOnPress
        if (other.popStackOnPress.hasValue()) popStackOnPress = other.popStackOnPress
    }

    fun mergeWithDefault(defaultOptions: HardwareBackButtonOptions) {
        if (!dismissModalOnPress.hasValue()) dismissModalOnPress = defaultOptions.dismissModalOnPress
        if (!popStackOnPress.hasValue()) popStackOnPress = defaultOptions.popStackOnPress
    }

    private fun parse(json: JSONObject?) {
        json ?: return
        dismissModalOnPress = BoolParser.parse(json, "dismissModalOnPress")
        popStackOnPress = BoolParser.parse(json, "popStackOnPress")
    }
}
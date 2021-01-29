package com.reactnativenavigation.options

import android.content.Context
import com.reactnativenavigation.options.params.*
import com.reactnativenavigation.options.params.Number
import com.reactnativenavigation.options.parsers.ColorParser
import com.reactnativenavigation.options.parsers.FractionParser
import com.reactnativenavigation.options.parsers.NumberParser
import org.json.JSONObject

fun parseShadowOptions(context: Context,shadowJson: JSONObject?): ShadowOptions = shadowJson?.let {
    json->
    ShadowOptions(ColorParser.parse(context,json,"color"),FractionParser.parse(json,"opacity"),NumberParser.parse(json,"radius"))
} ?: NullShadowOptions

object NullShadowOptions : ShadowOptions()

open class ShadowOptions(
        var color: Colour = NullColor,
        var opacity: Fraction = NullFraction,
        var radius: Number = NullNumber
) {

    fun mergeWith(other: ShadowOptions) {
        if (other.color.hasValue()) this.color = other.color
        if (other.opacity.hasValue()) this.opacity = other.opacity
        if (other.radius.hasValue()) this.radius = other.radius

    }

    fun mergeWithDefaults(defaultOptions: ShadowOptions = NullShadowOptions) {
        if (!this.color.hasValue()) this.color = defaultOptions.color
        if (!this.opacity.hasValue()) this.opacity = defaultOptions.opacity
        if (!this.radius.hasValue()) this.radius = defaultOptions.radius
    }

    fun hasValue() = color.hasValue() && radius.hasValue()

}
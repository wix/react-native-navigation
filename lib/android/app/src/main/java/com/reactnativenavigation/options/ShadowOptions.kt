package com.reactnativenavigation.options

import android.content.Context
import com.reactnativenavigation.options.params.*
import com.reactnativenavigation.options.parsers.FractionParser
import org.json.JSONObject


fun parseShadowOptions(context: Context, shadowJson: JSONObject?): ShadowOptions = shadowJson?.let { json ->
    ShadowOptions(
        parseThemeColour(context, json.optJSONObject("color")), FractionParser.parse(json, "radius"), FractionParser
            .parse(
                json,
                "opacity"
            )
    )
} ?: NullShadowOptions

object NullShadowOptions : ShadowOptions() {
    override fun hasValue(): Boolean = false
}

open class ShadowOptions(
    var color: ThemeColour = RNNNullColor(), var radius: Fraction = NullFraction(), var opacity: Fraction =
        NullFraction()
) {

    fun copy(): ShadowOptions = ShadowOptions(this.color, this.radius, this.opacity)

    fun mergeWith(other: ShadowOptions): ShadowOptions {
        if(other.color.hasValue()) this.color = other.color;
        if (other.opacity.hasValue()) this.opacity = other.opacity
        if (other.radius.hasValue()) this.radius = other.radius
        return this
    }

    fun mergeWithDefaults(defaultOptions: ShadowOptions = NullShadowOptions): ShadowOptions {
        if(!this.color.hasValue()) this.color = defaultOptions.color;
        if (!this.opacity.hasValue()) this.opacity = defaultOptions.opacity
        if (!this.radius.hasValue()) this.radius = defaultOptions.radius
        return this
    }

    open fun hasValue() = color.hasValue() || radius.hasValue() || opacity.hasValue()
}
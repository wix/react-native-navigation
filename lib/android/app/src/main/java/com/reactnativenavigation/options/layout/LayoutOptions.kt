package com.reactnativenavigation.options.layout

import android.content.Context
import com.reactnativenavigation.options.LayoutDirection
import com.reactnativenavigation.options.OrientationOptions
import com.reactnativenavigation.options.params.NullNumber
import com.reactnativenavigation.options.params.NullThemeColour
import com.reactnativenavigation.options.params.Number
import com.reactnativenavigation.options.params.ThemeColour
import com.reactnativenavigation.options.parsers.NumberParser
import org.json.JSONObject

class LayoutOptions {
    @JvmField
    var backgroundColor: ThemeColour = NullThemeColour()

    @JvmField
    var componentBackgroundColor: ThemeColour = NullThemeColour()

    @JvmField
    var topMargin: Number = NullNumber()

    @JvmField
    var orientation = OrientationOptions()

    @JvmField
    var direction = LayoutDirection.DEFAULT

    var insets: LayoutInsets = LayoutInsets()


    fun mergeWith(other: LayoutOptions) {
        if (other.backgroundColor.hasValue()) backgroundColor = other.backgroundColor
        if (other.componentBackgroundColor.hasValue()) componentBackgroundColor = other.componentBackgroundColor
        if (other.topMargin.hasValue()) topMargin = other.topMargin
        if (other.orientation.hasValue()) orientation = other.orientation
        if (other.direction.hasValue()) direction = other.direction
        insets.merge(other.insets, null)
    }

    fun mergeWithDefault(defaultOptions: LayoutOptions) {
        if (!backgroundColor.hasValue()) backgroundColor = defaultOptions.backgroundColor
        if (!componentBackgroundColor.hasValue()) componentBackgroundColor = defaultOptions.componentBackgroundColor
        if (!topMargin.hasValue()) topMargin = defaultOptions.topMargin
        if (!orientation.hasValue()) orientation = defaultOptions.orientation
        if (!direction.hasValue()) direction = defaultOptions.direction
        insets.merge(null, defaultOptions.insets)

    }

    companion object {
        @JvmStatic
        fun parse(context: Context?, json: JSONObject?): LayoutOptions {
            val result = LayoutOptions()
            if (json == null) return result
            result.backgroundColor = ThemeColour.parse(context!!, json.optJSONObject("backgroundColor"))
            result.componentBackgroundColor = ThemeColour.parse(context, json.optJSONObject("componentBackgroundColor"))
            result.topMargin = NumberParser.parse(json, "topMargin")
            result.insets = LayoutInsets.parse(json.optJSONObject("insets"))
            result.orientation = OrientationOptions.parse(json)
            result.direction = LayoutDirection.fromString(json.optString("direction", ""))
            return result
        }
    }

}
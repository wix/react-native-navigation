package com.reactnativenavigation.options.parsers

import android.content.Context
import com.facebook.react.bridge.ColorPropConverter
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.params.DontApplyColour
import com.reactnativenavigation.options.params.NullColor
import com.reactnativenavigation.options.params.ReactPlatformColor
import org.json.JSONObject

object ColorParser {
    private const val KEY_RESOURCE_PATHS = "resource_paths"
    private const val VAL_NO_COLOR = "NoColor"

    @JvmStatic
    fun parse(context: Context?, json: JSONObject, colorName: String?): Colour {
        if (json.has(KEY_RESOURCE_PATHS)) {
            return ReactPlatformColor(JSONParser.convert(json))
        }
        return when (val color = json.opt(colorName)) {
            null, VAL_NO_COLOR -> {
                DontApplyColour()
            }
            is Int -> {
                Colour(json.optInt(colorName))
            }
            is JSONObject -> {
                ColorPropConverter.getColor(color, context)?.let {
                    Colour(it)
                } ?: NullColor()
            }
            else -> {
                NullColor()
            }
        }

    }
}
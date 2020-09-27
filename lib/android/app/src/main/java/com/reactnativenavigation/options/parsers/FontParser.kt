package com.reactnativenavigation.options.parsers

import com.reactnativenavigation.options.FontOptions
import org.json.JSONObject

class FontParser {
    companion object {
        @JvmStatic
        fun parse(typefaceLoader: TypefaceLoader, title: JSONObject): FontOptions {
            return FontOptions(typefaceLoader).apply {
                fontFamily = TextParser.parse(title, "fontFamily")
                fontStyle = TextParser.parse(title, "fontStyle")
                fontWeight = TextParser.parse(title, "fontWeight")
            }
        }
    }
}
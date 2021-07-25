package com.reactnativenavigation.options

import android.content.Context
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.params.NullColor
import com.reactnativenavigation.options.params.NullText
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.options.parsers.ColorParser
import com.reactnativenavigation.options.parsers.TextParser
import org.json.JSONObject

fun parseBadgeOptions(context: Context, json: JSONObject?): BadgeOptions {
    return json?.let {
        BadgeOptions(TextParser.parse(json, "text"), ColorParser.parse(context, json, "textColor"),
                ColorParser.parse(context, json, "backgroundColor")
        )
    } ?: BadgeOptions()
}

class BadgeOptions(var text: Text = NullText(), var textColor: Colour = NullColor(), var backgroundColor: Colour = NullColor()) {
    fun hasValue() = text.hasValue() || textColor.hasValue() || backgroundColor.hasValue()
    fun mergeWith(other: BadgeOptions) {
        if (other.text.hasValue())
            this.text = other.text
        if (other.textColor.hasValue())
            this.textColor = other.textColor
        if (other.backgroundColor.hasValue())
            this.backgroundColor = other.backgroundColor
    }

    fun mergeWithDefaults(other: BadgeOptions) {
        if (!this.text.hasValue())
            this.text = other.text
        if (!this.textColor.hasValue())
            this.textColor = other.textColor
        if (!this.backgroundColor.hasValue())
            this.backgroundColor = other.backgroundColor
    }
}
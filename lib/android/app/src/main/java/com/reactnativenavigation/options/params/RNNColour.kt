package com.reactnativenavigation.options.params

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import com.reactnativenavigation.options.parsers.ColorParser
import org.json.JSONObject

fun parseRNNColour(context: Context, json: JSONObject?): RNNColour {
    return json?.let {
        RNNColour(ColorParser.parse(context, json, "light"), ColorParser.parse(context, json, "dark"))
    } ?: transparent()
}

fun transparent() = RNNColour(Colour(Color.TRANSPARENT), Colour(Color.TRANSPARENT))
class RNNNullColor() : RNNColour(NullColor(), NullColor()) {
    override fun hasValue(): Boolean {
        return false;
    }
}

open class RNNColour(private var lightColor: Colour, private var darkColor: Colour) {


    private var isDark = false

    fun selectMode(isDarkMode: Boolean): RNNColour {
        isDark = isDarkMode
        return this
    }

    fun value() = if (isDark) darkColor else lightColor

    fun get(@ColorInt defaultColor:Int) = value().get(defaultColor)
    fun get() = value().get()
    open fun hasValue() = if (isDark) darkColor.hasValue() else lightColor.hasValue()

    fun mergeWith(rnnColour: RNNColour) {
        if (rnnColour.darkColor.hasValue()) darkColor = rnnColour.darkColor
        if (rnnColour.lightColor.hasValue()) lightColor = rnnColour.lightColor
    }

    fun mergeWithDefault(rnnColour: RNNColour) {
        if (!darkColor.hasValue()) darkColor = rnnColour.darkColor
        if (!lightColor.hasValue()) lightColor = rnnColour.lightColor
    }

    fun hasTransparency() = if (isDark) darkColor.hasTransparency() else lightColor.hasTransparency()
}
package com.reactnativenavigation.options.params

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import com.reactnativenavigation.options.parsers.ColorParser
import com.reactnativenavigation.utils.isDarkMode
import org.json.JSONObject

fun parse(context: Context, json: JSONObject?): RNNColour {
    return json?.let {
        RNNColour(ColorParser.parse(context, json, "light"), ColorParser.parse(context, json, "dark"))
    } ?: RNNNullColor()
}

private fun DontApplyRNNColour() = RNNColour(DontApplyColour(), DontApplyColour())
fun transparent() = RNNColour(Colour(Color.TRANSPARENT), Colour(Color.TRANSPARENT))
class RNNNullColor() : RNNColour(NullColor(), NullColor()) {
    override fun hasValue(): Boolean {
        return false;
    }
}

open class RNNColour(private var lightColor: Colour, private var darkColor: Colour) {

    private fun selectedColor() = if (isDarkMode()) darkColor else lightColor

    fun get(@ColorInt defaultColor: Int?): Int? = selectedColor().get(defaultColor)
    fun get(): Int = selectedColor().get()
    open fun hasValue() = selectedColor().hasValue()

    fun mergeWith(rnnColour: RNNColour) {
        if (rnnColour.darkColor.hasValue()) darkColor = rnnColour.darkColor
        if (rnnColour.lightColor.hasValue()) lightColor = rnnColour.lightColor
    }

    fun mergeWithDefault(rnnColour: RNNColour) {
        if (!darkColor.hasValue()) darkColor = rnnColour.darkColor
        if (!lightColor.hasValue()) lightColor = rnnColour.lightColor
    }

    fun hasTransparency() = selectedColor().hasTransparency()
    fun canApplyValue() = selectedColor().canApplyValue()
}
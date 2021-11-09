package com.reactnativenavigation.options

import android.graphics.Typeface
import com.reactnativenavigation.options.params.NullText
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.options.parsers.TypefaceLoader

class FontOptions {
    private var isDirty = false
    var fontFamily: Text = NullText()
        set(value) {
            field = value
            isDirty = true
        }
    var fontStyle: Text = NullText()
        set(value) {
            field = value
            isDirty = true
        }
    var fontWeight: Text = NullText()
        set(value) {
            field = value
            isDirty = true
        }
    private var _typeface: Typeface? = null

    @JvmOverloads fun getTypeface(typefaceLoader: TypefaceLoader, defaultTypeface: Typeface? = null): Typeface? {
        if (isDirty) {
            _typeface = typefaceLoader.getTypeFace(fontFamily.get(null), fontStyle.get(""), fontWeight.get(""), defaultTypeface)
            isDirty = false
        }
        return _typeface
                ?: defaultTypeface?.let { typefaceLoader.getTypeFace(fontFamily.get(null), fontStyle.get(""), fontWeight.get(""), it) }
    }

    fun mergeWith(other: FontOptions) {
        if (other.fontFamily.hasValue()) fontFamily = other.fontFamily
        if (other.fontStyle.hasValue()) fontStyle = other.fontStyle
        if (other.fontWeight.hasValue()) fontWeight = other.fontWeight
    }

    fun mergeWithDefault(defaultOptions: FontOptions) {
        if (!fontFamily.hasValue()) fontFamily = defaultOptions.fontFamily
        if (!fontStyle.hasValue()) fontStyle = defaultOptions.fontStyle
        if (!fontWeight.hasValue()) fontWeight = defaultOptions.fontWeight
    }

    fun hasValue() = fontFamily.hasValue() || fontStyle.hasValue() || fontWeight.hasValue()

    @JvmOverloads fun get(defaultValue: FontOptions? = null): FontOptions? {
        return if (hasValue()) this else defaultValue
    }

    override fun equals(other: Any?) = (other as? FontOptions)?.let {
        fontFamily.equals(other.fontFamily) &&
        fontStyle.equals(other.fontStyle) &&
        fontWeight.equals(other.fontWeight)
    } ?: false
}
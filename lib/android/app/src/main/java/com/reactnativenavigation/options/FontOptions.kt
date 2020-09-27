package com.reactnativenavigation.options

import android.graphics.Typeface
import com.reactnativenavigation.options.params.NullText
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.options.parsers.TypefaceLoader

class FontOptions constructor(private var typefaceLoader: TypefaceLoader? = null) {
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
    var typeface: Typeface? = null
        private set
        get() {
            if (isDirty) {
                field = typefaceLoader?.getTypeFace(fontFamily.get(""), fontStyle.get(""), fontWeight.get(""))
                isDirty = false
            }
            return field
        }

    fun mergeWith(other: FontOptions) {
        if (other.typefaceLoader != null) typefaceLoader = other.typefaceLoader
        if (other.fontFamily.hasValue()) fontFamily = other.fontFamily
        if (other.fontStyle.hasValue()) fontStyle = other.fontStyle
        if (other.fontWeight.hasValue()) fontWeight = other.fontWeight
    }

    fun mergeWithDefault(defaultOptions: FontOptions) {
        if (typefaceLoader == null) typefaceLoader = defaultOptions.typefaceLoader
        if (!fontFamily.hasValue()) fontFamily = defaultOptions.fontFamily
        if (!fontStyle.hasValue()) fontStyle = defaultOptions.fontStyle
        if (!fontWeight.hasValue()) fontWeight = defaultOptions.fontWeight
    }

    fun hasValue() = fontFamily.hasValue() || fontStyle.hasValue() || fontWeight.hasValue()
}
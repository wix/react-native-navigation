package com.reactnativenavigation.options.parsers

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import com.facebook.react.views.text.ReactTypefaceUtils

open class TypefaceLoader(private val context: Context) {
    @JvmOverloads open fun getTypeFace(
            fontFamilyName: String?,
            fontStyle: String?,
            fontWeight: String?,
            defaultTypeFace: Typeface? = null
    ): Typeface? {
        return ReactTypefaceUtils.applyStyles(
                defaultTypeFace,
                ReactTypefaceUtils.parseFontStyle(fontStyle),
                ReactTypefaceUtils.parseFontWeight(fontWeight),
                fontFamilyName,
                context.assets
        )
    }
}
package com.reactnativenavigation.options.parsers

import android.content.Context
import android.graphics.Typeface
import com.aurelhubert.ahbottomnavigation.AHTextView
import com.reactnativenavigation.utils.ReactTypefaceUtils

open class TypefaceLoader(private val context: Context) {
  open  val defaultTypeFace: Typeface by lazy {
        AHTextView(context).typeface ?: Typeface.DEFAULT
    }

    @JvmOverloads
    open fun getTypeFace(
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
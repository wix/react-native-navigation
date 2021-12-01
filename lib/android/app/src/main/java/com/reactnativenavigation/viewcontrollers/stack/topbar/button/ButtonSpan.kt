package com.reactnativenavigation.viewcontrollers.stack.topbar.button

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import com.reactnativenavigation.options.ButtonOptions
import com.reactnativenavigation.options.parsers.TypefaceLoader
import com.reactnativenavigation.utils.UiUtils

class ButtonSpan(
        private val context: Context,
        private val button: ButtonOptions,
        private val typefaceLoader: TypefaceLoader = TypefaceLoader(context)
) : MetricAffectingSpan() {
    val fontSize: Float?
        get() {
            return if (button.fontSize.hasValue()) UiUtils.dpToPx(context, button.fontSize.get().toFloat()) else null
        }

    override fun updateDrawState(drawState: TextPaint) = apply(drawState)

    override fun updateMeasureState(paint: TextPaint) = apply(paint)

    fun apply(paint: Paint) {
        with(button.font) {
            val typeface = getTypeface(typefaceLoader, paint.typeface)
            val fakeStyle = (paint.typeface?.style ?: 0) and (typeface?.style?.inv() ?: 1)
            if (fakeStyle and Typeface.BOLD != 0) paint.isFakeBoldText = true
            if (fakeStyle and Typeface.ITALIC != 0) paint.textSkewX = -0.25f
            fontSize?.let { paint.textSize = it }
            paint.typeface = typeface
        }
    }
}

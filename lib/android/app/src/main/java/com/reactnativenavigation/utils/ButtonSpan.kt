package com.reactnativenavigation.utils

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import com.reactnativenavigation.parse.params.Button
import com.reactnativenavigation.parse.params.Colour
import com.reactnativenavigation.parse.params.Fraction

class ButtonSpan(private val button: Button) : MetricAffectingSpan() {
    override fun updateDrawState(drawState: TextPaint) = apply(drawState)

    override fun updateMeasureState(paint: TextPaint) = apply(paint)

    private fun apply(paint: Paint) {
        with(button) {
            val fakeStyle = (paint.typeface?.style ?: 0) and (fontFamily?.style?.inv() ?: 1)
            if (fakeStyle and Typeface.BOLD != 0) paint.isFakeBoldText = true
            if (fakeStyle and Typeface.ITALIC != 0) paint.textSkewX = -0.25f
            if (fontSize.hasValue()) paint.textSize = fontSize.get().toFloat()
            if (color.hasValue()) paint.color = if (enabled.isTrueOrUndefined) color.get() else disabledColor.get()
            paint.typeface = fontFamily
        }

    }
}

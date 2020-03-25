package com.reactnativenavigation.utils

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import com.reactnativenavigation.parse.params.Colour
import com.reactnativenavigation.parse.params.Fraction

class ButtonSpan(private val typeface: Typeface?, private val size: Fraction, private val color: Colour) : MetricAffectingSpan() {
    override fun updateDrawState(drawState: TextPaint) = apply(drawState)

    override fun updateMeasureState(paint: TextPaint) = apply(paint)

    private fun apply(paint: Paint) {
        val oldTypeface: Typeface = paint.typeface
        val fakeStyle = if (typeface == null) oldTypeface.style else oldTypeface.style and typeface.style.inv()
        if (fakeStyle and Typeface.BOLD != 0) paint.isFakeBoldText = true
        if (fakeStyle and Typeface.ITALIC != 0) paint.textSkewX = -0.25f
        if (size.hasValue()) paint.textSize = size.get().toFloat()
        if (color.hasValue()) paint.color = color.get()
        paint.typeface = typeface
    }
}

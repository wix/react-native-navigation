package com.reactnativenavigation.utils

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import com.reactnativenavigation.parse.params.Button

class ButtonSpan(private val button: Button) : MetricAffectingSpan() {
    companion object {
        const val DISABLED_COLOR = Color.LTGRAY
    }

    override fun updateDrawState(drawState: TextPaint) = apply(drawState)

    override fun updateMeasureState(paint: TextPaint) = apply(paint)

    fun apply(paint: Paint) {
        with(button) {
            val fakeStyle = (paint.typeface?.style ?: 0) and (fontFamily?.style?.inv() ?: 1)
            if (fakeStyle and Typeface.BOLD != 0) paint.isFakeBoldText = true
            if (fakeStyle and Typeface.ITALIC != 0) paint.textSkewX = -0.25f
            if (fontSize.hasValue()) paint.textSize = fontSize.get().toFloat()
            if (enabled.hasValue()) paint.color = disabledColor.get(DISABLED_COLOR)
            paint.typeface = fontFamily
        }
    }
}

package com.reactnativenavigation.viewcontrollers.stack.topbar.button

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import com.reactnativenavigation.options.ButtonOptions
import com.reactnativenavigation.utils.UiUtils

class ButtonSpan(private val context: Context, private val button: ButtonOptions) : MetricAffectingSpan() {
    override fun updateDrawState(drawState: TextPaint) = apply(drawState)

    override fun updateMeasureState(paint: TextPaint) = apply(paint)

    fun apply(paint: Paint) {
        with(button) {
            val fakeStyle = (paint.typeface?.style ?: 0) and (fontFamily?.style?.inv() ?: 1)
            if (fakeStyle and Typeface.BOLD != 0) paint.isFakeBoldText = true
            if (fakeStyle and Typeface.ITALIC != 0) paint.textSkewX = -0.25f
            if (fontSize.hasValue()) paint.textSize = UiUtils.dpToPx(context, fontSize.get().toFloat())
            paint.typeface = fontFamily
        }
    }
}

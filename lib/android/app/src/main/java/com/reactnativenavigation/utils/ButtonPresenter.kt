package com.reactnativenavigation.utils

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import com.reactnativenavigation.parse.params.Button

class ButtonPresenter(private val button: Button) {
    fun tint(drawable: Drawable, tint: Int) {
        drawable.colorFilter = PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN)
    }

    val styledText: SpannableString
        get() {
            return SpannableString(button.text.get("")).apply {
                setSpan(ButtonSpan(button), 0, button.text.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            }
        }
}
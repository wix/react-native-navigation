package com.reactnativenavigation.options

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import com.reactnativenavigation.options.params.NullNumber
import com.reactnativenavigation.options.params.Number
import com.reactnativenavigation.options.parsers.NumberParser
import com.reactnativenavigation.views.animations.BkgColorProperty
import org.json.JSONObject

class ColorAnimationOptions {
    var duration: Number = NullNumber()
    private var parsed = false

    fun hasValue() = parsed

    fun getAnimation(from: Int, to: Int): ValueAnimator? = getAnimation(null, from, to)

    fun getAnimation(view: View?, from: Int, to: Int): ValueAnimator? =
        if (hasValue()) {
            createObjectAnimator(view, from, to)
        } else {
            null
        }

    private fun createObjectAnimator(view: View?, from: Int, to: Int) =
        if (view == null) {
            ObjectAnimator.ofArgb(from, to)
        } else {
            ObjectAnimator.ofArgb(
                view,
                view.BkgColorProperty,
                from,
                to,
            )
        }.also {
            if (duration.hasValue()) {
                it.duration = duration.get().toLong()
            }
        }

    companion object {
        fun parse(json: JSONObject?): ColorAnimationOptions {
            val options = ColorAnimationOptions()
            json?.optJSONObject("bkgColor")?.let { colorJson ->
               options.duration = NumberParser.parse(colorJson, "duration")
            }
            options.parsed = true
            return options
        }
    }
}

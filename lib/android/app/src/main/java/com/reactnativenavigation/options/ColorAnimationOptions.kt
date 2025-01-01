package com.reactnativenavigation.options

import android.animation.Animator
import android.animation.ObjectAnimator
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

    fun getAnimation(view: View, from: Int, to: Int): Animator? =
        if (hasValue()) {
            ObjectAnimator.ofArgb(
                view,
                view.BkgColorProperty,
                from,
                to,
            ).also {
                if (duration.hasValue()) {
                    it.duration = duration.get().toLong()
                }
            }
        } else null

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

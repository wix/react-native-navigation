package com.reactnativenavigation.options

import android.animation.TimeInterpolator
import com.reactnativenavigation.options.params.*
import com.reactnativenavigation.options.params.Number
import com.reactnativenavigation.options.parsers.InterpolationParser
import com.reactnativenavigation.options.parsers.NumberParser
import com.reactnativenavigation.options.parsers.TextParser
import org.json.JSONObject

class SharedElementTransitionOptions {
    var fromId: Text = NullText()
    var toId: Text = NullText()
    var duration: Number = NullNumber()
    var startDelay: Number = NullNumber()
    var interpolation = Interpolation.NO_VALUE

    fun getDuration() = duration[0].toLong()
    fun getStartDelay() = startDelay[0].toLong()
    fun getInterpolator(): TimeInterpolator = interpolation.interpolator

    companion object {
        @JvmStatic
        fun parse(json: JSONObject?): SharedElementTransitionOptions {
            val transition = SharedElementTransitionOptions()
            if (json == null) return transition
            transition.fromId = TextParser.parse(json, "fromId")
            transition.toId = TextParser.parse(json, "toId")
            transition.duration = NumberParser.parse(json, "duration")
            transition.startDelay = NumberParser.parse(json, "startDelay")
            transition.interpolation = InterpolationParser.parse(json, "interpolation")
            return transition
        }
    }
}
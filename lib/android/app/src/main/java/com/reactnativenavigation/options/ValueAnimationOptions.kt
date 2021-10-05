package com.reactnativenavigation.options

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.util.Property
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import com.reactnativenavigation.options.params.*
import com.reactnativenavigation.options.params.Number
import com.reactnativenavigation.options.parsers.FloatParser
import com.reactnativenavigation.options.parsers.InterpolationParser
import com.reactnativenavigation.options.parsers.NumberParser
import com.reactnativenavigation.utils.UiUtils.dpToPx
import org.json.JSONObject

class ValueAnimationOptions {
    private var animProp: Property<View, Float>? = null
    private var animPropType: Int? = null
    private var animationValueAccessor: ((View) -> Float)? = null
    var from: FloatParam = NullFloatParam()
        private set
    private var fromDelta = FloatParam(0f)
    var to: FloatParam = NullFloatParam()
        private set
    private var toDelta = FloatParam(0f)
    var duration: Number = NullNumber()
    private var startDelay: Number = NullNumber()
    private var interpolator: TimeInterpolator = LinearInterpolator()

    fun setFromDelta(fromDelta: Float) {
        this.fromDelta = FloatParam(fromDelta)
    }

    fun setToDelta(toDelta: Float) {
        this.toDelta = FloatParam(toDelta)
    }

    fun getAnimation(view: View): Animator {
        require(!(!from.hasValue() && !to.hasValue())) { "Params 'from' and 'to' are mandatory" }

        var from = fromDelta.get()
        var to = toDelta.get()
        if (animPropType == TypedValue.COMPLEX_UNIT_DIP) {
            from += dpToPx(view.context, this.from[animationValueAccessor!!(view)])
            to += dpToPx(view.context, this.to[animationValueAccessor!!(view)])
        } else {
            from += this.from[animationValueAccessor!!(view)]
            to += this.to[animationValueAccessor!!(view)]
        }
        val animator = ObjectAnimator.ofFloat(view,
                animProp,
                from,
                to
        )
        animator.interpolator = interpolator
        if (duration.hasValue()) animator.duration = duration.get().toLong()
        if (startDelay.hasValue()) animator.startDelay = startDelay.get().toLong()
        return animator
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other == null || javaClass != other.javaClass) false else animProp == (other as ValueAnimationOptions).animProp
    }

    fun equals(animationProperty: Property<View?, Float?>): Boolean {
        return animProp!!.name == animationProperty.name
    }

    override fun hashCode(): Int {
        return animProp.hashCode()
    }

    fun isAlpha(): Boolean = animProp == View.ALPHA

    companion object {
        fun parse(json: JSONObject?, property: Triple<Property<View, Float>?, Int?, (View) -> Float>): ValueAnimationOptions {
            val options = ValueAnimationOptions()
            options.animProp = property.first
            options.animPropType = property.second
            options.animationValueAccessor = property.third
            options.from = FloatParser.parse(json, "from")
            options.to = FloatParser.parse(json, "to")
            options.duration = NumberParser.parse(json, "duration")
            options.startDelay = NumberParser.parse(json, "startDelay")
            options.interpolator = InterpolationParser.parse(json)
            return options
        }
    }
}
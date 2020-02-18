package com.reactnativenavigation.parse

import android.animation.Animator
import android.animation.ObjectAnimator
import android.util.Property
import android.util.TypedValue
import android.view.View
import com.reactnativenavigation.parse.params.*
import com.reactnativenavigation.parse.params.Number
import com.reactnativenavigation.parse.parsers.FloatParser
import com.reactnativenavigation.parse.parsers.InterpolationParser
import com.reactnativenavigation.parse.parsers.NumberParser
import com.reactnativenavigation.utils.UiUtils.dpToPx
import org.json.JSONObject

class ValueAnimationOptions {
    private var animProp: Property<View, Float>? = null
    private var animPropType: Int? = null
    private var animationValueAccessor: ((View) -> Float)? = null
    private var from: FloatParam = NullFloatParam()
    private var fromDelta = FloatParam(0f)
    private var to: FloatParam = NullFloatParam()
    private var toDelta = FloatParam(0f)
    var duration: Number = NullNumber()
    private var startDelay: Number = NullNumber()
    private var interpolation = Interpolation.NO_VALUE

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
        animator.interpolator = interpolation.interpolator
        if (duration.hasValue()) animator.duration = duration.get().toLong()
        if (startDelay.hasValue()) animator.startDelay = startDelay.get().toLong()
        return animator
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        return if (o == null || javaClass != o.javaClass) false else animProp == (o as ValueAnimationOptions).animProp
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
            options.interpolation = InterpolationParser.parse(json, "interpolation")
            return options
        }
    }
}
package com.reactnativenavigation.options

import android.animation.Animator
import android.animation.ObjectAnimator
import android.util.Property
import android.util.TypedValue
import android.view.View
import com.reactnativenavigation.options.params.*
import com.reactnativenavigation.options.params.Number
import com.reactnativenavigation.options.parsers.FloatParser
import com.reactnativenavigation.options.parsers.InterpolationParser
import com.reactnativenavigation.options.parsers.NumberParser
import com.reactnativenavigation.utils.UiUtils.dpToPx
import org.json.JSONObject

class ViewPropertyAnimation {
    private lateinit var viewProperty: ViewProperty
    var from: FloatParam = NullFloatParam()
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

    fun setCurrentViewProperty(view: View, value: Float) {
        viewProperty.setValue(view, value)
    }

    fun getAnimation(view: View): Animator {
        require(!(!from.hasValue() && !to.hasValue())) { "Params 'from' and 'to' are mandatory" }

        var from = fromDelta.get()
        var to = toDelta.get()
        if (viewProperty.type == TypedValue.COMPLEX_UNIT_DIP) {
            from += dpToPx(view.context, this.from[viewProperty.getValue(view)])
            to += dpToPx(view.context, this.to[viewProperty.getValue(view)])
        } else {
            from += this.from[viewProperty.getValue(view)]
            to += this.to[viewProperty.getValue(view)]
        }
        return ObjectAnimator.ofFloat(view, viewProperty.property, from, to).also {
            it.interpolator = interpolation.interpolator
            if (duration.hasValue()) it.duration = duration.get().toLong()
            if (startDelay.hasValue()) it.startDelay = startDelay.get().toLong()
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        return if (o == null || javaClass != o.javaClass) false else viewProperty == (o as ViewPropertyAnimation).viewProperty
    }

    fun equals(animationProperty: Property<View?, Float?>): Boolean {
        return viewProperty.property.name == animationProperty.name
    }

    override fun hashCode() = viewProperty.hashCode()

    fun isAlpha(): Boolean = viewProperty.property == View.ALPHA

    companion object {
        fun parse(json: JSONObject?, viewProperty: ViewProperty): ViewPropertyAnimation {
            val options = ViewPropertyAnimation()
            options.viewProperty = viewProperty
            options.from = FloatParser.parse(json, "from")
            options.to = FloatParser.parse(json, "to")
            options.duration = NumberParser.parse(json, "duration")
            options.startDelay = NumberParser.parse(json, "startDelay")
            options.interpolation = InterpolationParser.parse(json, "interpolation")
            return options
        }
    }
}
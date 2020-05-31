package com.reactnativenavigation.parse

import android.animation.Animator
import android.animation.AnimatorSet
import android.util.Property
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.COMPLEX_UNIT_FRACTION
import android.view.View
import android.view.View.*
import com.reactnativenavigation.parse.params.Bool
import com.reactnativenavigation.parse.params.NullBool
import com.reactnativenavigation.parse.params.NullText
import com.reactnativenavigation.parse.params.Text
import com.reactnativenavigation.parse.parsers.BoolParser
import com.reactnativenavigation.parse.parsers.TextParser
import com.reactnativenavigation.utils.CollectionUtils
import com.reactnativenavigation.utils.CollectionUtils.forEach
import org.json.JSONObject
import java.util.*
import kotlin.math.max

open class AnimationOptions(json: JSONObject?) {
    constructor() : this(null)

    private fun parse(json: JSONObject?) {
        json?.let {
            val iter = json.keys()
            while (iter.hasNext()) {
                when (val key = iter.next()) {
                    "id" -> id = TextParser.parse(json, key)
                    "enable", "enabled" -> enabled = BoolParser.parse(json, key)
                    "waitForRender" -> waitForRender = BoolParser.parse(json, key)
                    else -> valueOptions.add(ValueAnimationOptions.parse(json.optJSONObject(key), getAnimProp(key)))
                }
            }
        }
    }

    @JvmField
    var id: Text = NullText()
    @JvmField
    var enabled: Bool = NullBool()
    @JvmField
    var waitForRender: Bool = NullBool()
    private var valueOptions = HashSet<ValueAnimationOptions>()
    fun mergeWith(other: AnimationOptions) {
        if (other.id.hasValue()) id = other.id
        if (other.enabled.hasValue()) enabled = other.enabled
        if (other.waitForRender.hasValue()) waitForRender = other.waitForRender
        if (other.valueOptions.isNotEmpty()) valueOptions = other.valueOptions
    }

    fun mergeWithDefault(defaultOptions: AnimationOptions) {
        if (!id.hasValue()) id = defaultOptions.id
        if (!enabled.hasValue()) enabled = defaultOptions.enabled
        if (!waitForRender.hasValue()) waitForRender = defaultOptions.waitForRender
        if (valueOptions.isEmpty()) valueOptions = defaultOptions.valueOptions
    }

    fun hasValue(): Boolean {
        return id.hasValue() || enabled.hasValue() || waitForRender.hasValue()
    }

    fun getAnimation(view: View): AnimatorSet {
        return getAnimation(view, AnimatorSet())
    }

    fun getAnimation(view: View, defaultAnimation: AnimatorSet): AnimatorSet {
        if (!hasAnimation()) return defaultAnimation
        val animationSet = AnimatorSet()
        val animators: MutableList<Animator> = ArrayList()
        forEach(valueOptions) { options: ValueAnimationOptions -> animators.add(options.getAnimation(view)) }
        animationSet.playTogether(animators)
        return animationSet
    }

    val duration: Int
        get() = CollectionUtils.reduce(valueOptions, 0, { item: ValueAnimationOptions, currentValue: Int -> max(item.duration[currentValue], currentValue) })

    open fun hasAnimation(): Boolean = valueOptions.isNotEmpty()

    fun isFadeAnimation(): Boolean = valueOptions.size == 1 && valueOptions.find(ValueAnimationOptions::isAlpha) != null

    fun setValueDy(animation: Property<View?, Float?>?, fromDelta: Float, toDelta: Float) {
        CollectionUtils.first(valueOptions, { o: ValueAnimationOptions -> o.equals(animation) }) { param: ValueAnimationOptions ->
            param.setFromDelta(fromDelta)
            param.setToDelta(toDelta)
        }
    }

    companion object {
        private fun getAnimProp(key: String): Triple<Property<View, Float>, Int, (View) -> Float> {
            when (key) {
                "x" -> return Triple(X, COMPLEX_UNIT_DIP, View::getX)
                "y" -> return Triple(Y, COMPLEX_UNIT_DIP, View::getY)
                "translationX" -> return Triple(TRANSLATION_X, COMPLEX_UNIT_DIP, View::getTranslationX)
                "translationY" -> return Triple(TRANSLATION_Y, COMPLEX_UNIT_DIP, View::getTranslationY)
                "alpha" -> return Triple(ALPHA, COMPLEX_UNIT_FRACTION, View::getAlpha)
                "scaleX" -> return Triple(SCALE_X, COMPLEX_UNIT_FRACTION, View::getScaleX)
                "scaleY" -> return Triple(SCALE_Y, COMPLEX_UNIT_FRACTION, View::getScaleY)
                "rotationX" -> return Triple(ROTATION_X, COMPLEX_UNIT_FRACTION, View::getRotationX)
                "rotationY" -> return Triple(ROTATION_Y, COMPLEX_UNIT_FRACTION, View::getRotationY)
                "rotation" -> return Triple(ROTATION, COMPLEX_UNIT_FRACTION, View::getRotation)
            }
            throw IllegalArgumentException("This animation is not supported: $key")
        }
    }

    init {
        json?.let { parse(it) }
    }
}
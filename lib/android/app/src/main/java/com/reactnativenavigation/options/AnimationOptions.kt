package com.reactnativenavigation.options

import android.animation.Animator
import android.animation.AnimatorSet
import android.util.Property
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.COMPLEX_UNIT_FRACTION
import android.view.View
import android.view.View.*
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.NullBool
import com.reactnativenavigation.options.params.NullText
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.options.parsers.BoolParser
import com.reactnativenavigation.options.parsers.TextParser
import com.reactnativenavigation.utils.CollectionUtils
import com.reactnativenavigation.utils.CollectionUtils.first
import org.json.JSONObject
import java.util.*
import kotlin.math.max

open class AnimationOptions @JvmOverloads constructor(json: JSONObject? = null) : LayoutAnimation {

    @JvmField var id: Text = NullText()
    @JvmField var enabled: Bool = NullBool()
    @JvmField var waitForRender: Bool = NullBool()
    override var sharedElements = SharedElements()
    override var elementTransitions = ElementTransitions()
    private var valueOptions = HashSet<ValueAnimationOptions>()

    init {
        parse(json)
    }

    private fun parse(json: JSONObject?) {
        json ?: return
        val iter = json.keys()
        while (iter.hasNext()) {
            when (val key = iter.next()) {
                "id" -> id = TextParser.parse(json, key)
                "enable", "enabled" -> enabled = BoolParser.parse(json, key)
                "waitForRender" -> waitForRender = BoolParser.parse(json, key)
                "sharedElementTransitions" -> sharedElements = SharedElements.parse(json)
                "elementTransitions" -> elementTransitions = ElementTransitions.parse(json)
                else -> valueOptions.add(ValueAnimationOptions.parse(json.optJSONObject(key), getAnimProp(key)))
            }
        }
    }

    fun mergeWith(other: AnimationOptions) {
        if (other.id.hasValue()) id = other.id
        if (other.enabled.hasValue()) enabled = other.enabled
        if (other.waitForRender.hasValue()) waitForRender = other.waitForRender
        if (other.valueOptions.isNotEmpty()) valueOptions = other.valueOptions
        if (other.sharedElements.hasValue()) sharedElements = other.sharedElements
        if (other.elementTransitions.hasValue()) elementTransitions = other.elementTransitions
    }

    fun mergeWithDefault(defaultOptions: AnimationOptions) {
        if (!id.hasValue()) id = defaultOptions.id
        if (!enabled.hasValue()) enabled = defaultOptions.enabled
        if (!waitForRender.hasValue()) waitForRender = defaultOptions.waitForRender
        if (valueOptions.isEmpty()) valueOptions = defaultOptions.valueOptions
        if (!sharedElements.hasValue()) sharedElements = defaultOptions.sharedElements
        if (!elementTransitions.hasValue()) elementTransitions = defaultOptions.elementTransitions
    }

    fun hasValue() = id.hasValue()
            || enabled.hasValue()
            || waitForRender.hasValue()
            || sharedElements.hasValue()
            || elementTransitions.hasValue()
            || valueOptions.isNotEmpty()

    fun getAnimation(view: View) = getAnimation(view, AnimatorSet())

    fun getAnimation(view: View, defaultAnimation: Animator): Animator {
        if (!hasAnimation()) return defaultAnimation
        return AnimatorSet().apply { playTogether(valueOptions.map { it.getAnimation(view) }) }
    }

    fun shouldWaitForRender() = Bool(waitForRender.isTrue or hasElementTransitions())

    fun hasElementTransitions() = sharedElements.hasValue() or elementTransitions.hasValue()

    val duration: Int
        get() = CollectionUtils.reduce(valueOptions, 0, { item: ValueAnimationOptions, currentValue: Int -> max(item.duration[currentValue], currentValue) })

    open fun hasAnimation(): Boolean = valueOptions.isNotEmpty()

    fun isFadeAnimation(): Boolean = valueOptions.size == 1 && valueOptions.find(ValueAnimationOptions::isAlpha) != null

    fun setValueDy(animation: Property<View?, Float?>?, fromDelta: Float, toDelta: Float) {
        first(valueOptions, { o: ValueAnimationOptions -> o.equals(animation) }) { param: ValueAnimationOptions ->
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
}
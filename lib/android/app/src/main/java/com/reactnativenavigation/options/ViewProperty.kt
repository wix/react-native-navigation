package com.reactnativenavigation.options

import android.util.Property
import android.util.TypedValue
import android.view.View

class ViewProperty(
        val property: Property<View, Float>,
        val type: Int,
        val getValue: (View) -> Float,
        val setValue: (View, Float) -> Unit
) {
    companion object {
        fun create(key: String): ViewProperty {
            when (key) {
                "x" -> return ViewProperty(View.X, TypedValue.COMPLEX_UNIT_DIP, View::getX, View::setX)
                "y" -> return ViewProperty(View.Y, TypedValue.COMPLEX_UNIT_DIP, View::getY, View::setY)
                "translationX" -> return ViewProperty(View.TRANSLATION_X, TypedValue.COMPLEX_UNIT_DIP, View::getTranslationX, View::setTranslationX)
                "translationY" -> return ViewProperty(View.TRANSLATION_Y, TypedValue.COMPLEX_UNIT_DIP, View::getTranslationY, View::setTranslationY)
                "alpha" -> return ViewProperty(View.ALPHA, TypedValue.COMPLEX_UNIT_FRACTION, View::getAlpha, View::setAlpha)
                "scaleX" -> return ViewProperty(View.SCALE_X, TypedValue.COMPLEX_UNIT_FRACTION, View::getScaleX, View::setScaleX)
                "scaleY" -> return ViewProperty(View.SCALE_Y, TypedValue.COMPLEX_UNIT_FRACTION, View::getScaleY, View::setScaleY)
                "rotationX" -> return ViewProperty(View.ROTATION_X, TypedValue.COMPLEX_UNIT_FRACTION, View::getRotationX, View::setRotationX)
                "rotationY" -> return ViewProperty(View.ROTATION_Y, TypedValue.COMPLEX_UNIT_FRACTION, View::getRotationY, View::setRotationY)
                "rotation" -> return ViewProperty(View.ROTATION, TypedValue.COMPLEX_UNIT_FRACTION, View::getRotation, View::setRotation)
            }
            throw IllegalArgumentException("This animation is not supported: $key")
        }
    }
}

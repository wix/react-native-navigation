package com.reactnativenavigation.options

import android.animation.Animator
import android.animation.AnimatorSet
import android.util.Property
import android.view.View
import org.json.JSONObject

class ElementTransitionOptions(json: JSONObject) {
    private val animation: AnimationOptions = AnimationOptions(json)
    val id: String
        get() = animation.id.get()

    fun getAnimation(view: View): Animator = animation.getAnimation(view)

    fun setValueDy(property: Property<View?, Float?>?, fromDelta: Float, toDelta: Float) {
        animation.setValueDy(property, fromDelta, toDelta)
    }
}
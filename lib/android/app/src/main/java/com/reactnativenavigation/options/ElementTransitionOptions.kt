package com.reactnativenavigation.options

import android.animation.AnimatorSet
import android.view.View
import org.json.JSONObject

class ElementTransitionOptions(json: JSONObject) {
    private val animation: AnimationOptions = AnimationOptions(json)
    val id: String
        get() = animation.id.get()
    val viewPropertyAnimations: List<ViewPropertyAnimation>
        get() = animation.viewPropertyAnimations

    fun getAnimation(view: View): AnimatorSet = animation.getAnimation(view)
}
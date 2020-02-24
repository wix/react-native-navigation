package com.reactnativenavigation.parse

import android.animation.AnimatorSet
import android.view.View
import org.json.JSONObject

class ElementTransitionOptions(json: JSONObject) {
    private val animation: AnimationOptions = AnimationOptions(json)
    val id: String
        get() = animation.id.get()

    fun getAnimation(view: View): AnimatorSet = animation.getAnimation(view)
}
package com.reactnativenavigation.options.animations

import com.reactnativenavigation.options.AnimationOptions
import org.json.JSONObject

class ViewAnimationOptions(json: JSONObject? = null) {

    @JvmField var enter = AnimationOptions()
    @JvmField var exit = AnimationOptions()

    init {
        json?.let { parse(it) }
    }

    fun mergeWith(other: ViewAnimationOptions) {
        enter.mergeWith(other.enter)
        exit.mergeWith(other.exit)
    }

    fun mergeWithDefault(defaultOptions: ViewAnimationOptions) {
        enter.mergeWithDefault(defaultOptions.enter)
        exit.mergeWithDefault(defaultOptions.exit)
    }

    private fun parse(json: JSONObject?) {
        json?.let {
            json.optJSONObject("enter")?.let { enter = AnimationOptions(it) }
            json.optJSONObject("exit")?.let { exit = AnimationOptions(it) }
        }
    }
}
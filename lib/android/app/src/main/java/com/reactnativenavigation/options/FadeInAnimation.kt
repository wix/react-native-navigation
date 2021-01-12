package com.reactnativenavigation.options

import org.json.JSONException
import org.json.JSONObject

class FadeInAnimation : StackAnimationOptions() {
    init {
        try {
            val alpha = JSONObject()
            alpha.put("from", 0)
            alpha.put("to", 1)
            alpha.put("duration", 300)
            val content = JSONObject()
            content.put("alpha", alpha)
            val animation = JSONObject()
            animation.put("content", content)
            mergeWith(StackAnimationOptions(animation))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
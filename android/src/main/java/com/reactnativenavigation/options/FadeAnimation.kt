package com.reactnativenavigation.options

import org.json.JSONObject

const val FADE_DURATION = 300

object FadeAnimation : StackAnimationOptions() {
    init {
        val enterAlpha = JSONObject()
        enterAlpha.put("from", 0)
        enterAlpha.put("to", 1)
        enterAlpha.put("duration", FADE_DURATION)
        val enter = JSONObject()
        enter.put("alpha", enterAlpha)

        val exitAlpha = JSONObject()
        exitAlpha.put("from", 1)
        exitAlpha.put("to", 0)
        exitAlpha.put("duration", FADE_DURATION)
        val exit = JSONObject()
        exit.put("alpha", exitAlpha)

        val animation = JSONObject()
        animation.put("enter", enter)
        animation.put("exit", exit)

        val content = JSONObject()
        content.put("content", animation)
        mergeWith(StackAnimationOptions(content))
    }
}

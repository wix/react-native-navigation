package com.reactnativenavigation.options

import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SharedElements {
    private var transitions: MutableList<SharedElementTransitionOptions> = ArrayList()
    fun get(): List<SharedElementTransitionOptions> {
        return transitions
    }

    fun hasValue() = transitions.isNotEmpty()

    fun mergeWith(other: SharedElements) {
        if (other.hasValue()) transitions = other.transitions
    }

    fun mergeWithDefault(defaultOptions: SharedElements) {
        if (!hasValue()) transitions = defaultOptions.transitions
    }

    private fun add(transition: SharedElementTransitionOptions) {
        transitions.add(transition)
    }

    companion object {
        @JvmStatic
        fun parse(json: JSONObject): SharedElements {
            val result = SharedElements()
            return try {
                val sharedElements = json.getJSONArray("sharedElementTransitions")
                if (sharedElements == null || sharedElements.length() == 0) return result
                for (i in 0 until sharedElements.length()) {
                    result.add(SharedElementTransitionOptions.parse(sharedElements.getJSONObject(i)))
                }
                result
            } catch (e: JSONException) {
                result
            }
        }
    }
}
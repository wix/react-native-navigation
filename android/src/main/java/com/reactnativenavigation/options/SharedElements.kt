package com.reactnativenavigation.options

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
            val sharedElementsJSONArray = json.optJSONArray("sharedElementTransitions")
            if (sharedElementsJSONArray == null || sharedElementsJSONArray.length() == 0) return result
            for (i in 0 until sharedElementsJSONArray.length()) {
                result.add(SharedElementTransitionOptions.parse(sharedElementsJSONArray.getJSONObject(i)))
            }
            return result
        }
    }
}
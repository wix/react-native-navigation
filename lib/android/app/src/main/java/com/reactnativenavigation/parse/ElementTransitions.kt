package com.reactnativenavigation.parse

import org.json.JSONException
import org.json.JSONObject

class ElementTransitions {
    var transitions = arrayListOf<ElementTransitionOptions>()
    val hasValue: Boolean
        get() = transitions.isNotEmpty()

    companion object {
        fun parse(json: JSONObject): ElementTransitions {
            val result = ElementTransitions()
            try {
                val elementTransitions = json.getJSONArray("elementTransitions")
                if (elementTransitions.length() == 0) return result
                for (i in 0..elementTransitions.length()) {
                    result.transitions.add(ElementTransitionOptions(elementTransitions.getJSONObject(i)))
                }
            } catch (e: JSONException) {

            }
            return result
        }
    }

    fun mergeWith(other: ElementTransitions) {
        if (other.hasValue) transitions = other.transitions
    }

    fun mergeWithDefault(defaultOptions: ElementTransitions) {
        if (!hasValue) transitions = defaultOptions.transitions
    }
}
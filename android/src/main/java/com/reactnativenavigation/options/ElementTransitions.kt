package com.reactnativenavigation.options

import org.json.JSONObject

class ElementTransitions {
    var transitions = arrayListOf<ElementTransitionOptions>()

    companion object {
        fun parse(json: JSONObject): ElementTransitions {
            val result = ElementTransitions()
            val elementTransitions = json.optJSONArray("elementTransitions")
            if (elementTransitions == null || elementTransitions.length() == 0) return result
            for (i in 0 until elementTransitions.length()) {
                result.transitions.add(ElementTransitionOptions(elementTransitions.getJSONObject(i)))
            }
            return result
        }
    }

    fun mergeWith(other: ElementTransitions) {
        if (other.hasValue()) transitions = other.transitions
    }

    fun mergeWithDefault(defaultOptions: ElementTransitions) {
        if (!hasValue()) transitions = defaultOptions.transitions
    }

    fun hasValue() = transitions.isNotEmpty()
}
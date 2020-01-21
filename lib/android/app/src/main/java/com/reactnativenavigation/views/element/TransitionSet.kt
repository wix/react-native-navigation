package com.reactnativenavigation.views.element

import java.util.*

class TransitionSet {
    var validSharedElementTransitions: MutableList<SharedElementTransition> = ArrayList()
    var validElementTransitions: MutableList<ElementTransition> = ArrayList()
    val isEmpty: Boolean
        get() = validSharedElementTransitions.isEmpty() && validElementTransitions.isEmpty()
    val transitions: List<Transition>
        get() = validElementTransitions + validSharedElementTransitions

    fun add(transition: SharedElementTransition) {
        validSharedElementTransitions.add(transition)
    }

    fun add(transition: ElementTransition) {
        validElementTransitions.add(transition)
    }

    fun size(): Int {
        return validElementTransitions.size + validSharedElementTransitions.size
    }
}
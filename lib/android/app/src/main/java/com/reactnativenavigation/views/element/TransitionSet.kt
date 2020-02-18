package com.reactnativenavigation.views.element

import java.util.*

class TransitionSet {
    var validSharedElementTransitions: MutableList<SharedElementTransition> = ArrayList()
    var validElementTransitions: MutableList<ElementTransition> = ArrayList()
    val isEmpty: Boolean
        get() = size() == 0
    val transitions: List<Transition>
        get() = validElementTransitions + validSharedElementTransitions

    fun add(transition: SharedElementTransition) {
        validSharedElementTransitions.add(transition)
    }

    fun add(transition: ElementTransition) {
        validElementTransitions.add(transition)
    }

    fun forEach(action: ((Transition) -> Unit)) {
        validSharedElementTransitions.forEach(action)
        validElementTransitions.forEach(action)
    }

    fun size(): Int = validElementTransitions.size + validSharedElementTransitions.size
}
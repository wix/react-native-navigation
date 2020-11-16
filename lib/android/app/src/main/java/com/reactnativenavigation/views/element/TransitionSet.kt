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

    fun addAll(transitions: List<Transition>) {
        transitions.forEach {
            if (it is SharedElementTransition) {
                validSharedElementTransitions.add(it)
            } else if (it is ElementTransition) {
                validElementTransitions.add(it)
            }
        }
    }

    fun forEach(action: ((Transition) -> Unit)) {
        validSharedElementTransitions.forEach(action)
        validElementTransitions.forEach(action)
    }

    fun size(): Int = validElementTransitions.size + validSharedElementTransitions.size
}
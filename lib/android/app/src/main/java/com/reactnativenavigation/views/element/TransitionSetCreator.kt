package com.reactnativenavigation.views.element

import com.reactnativenavigation.options.ElementTransitions
import com.reactnativenavigation.options.NestedAnimationsOptions
import com.reactnativenavigation.options.SharedElements
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.element.finder.ExistingViewFinder
import com.reactnativenavigation.views.element.finder.OptimisticViewFinder

class TransitionSetCreator {
    suspend fun create(
            animation: NestedAnimationsOptions,
            fromScreen: ViewController<*>,
            toScreen: ViewController<*>
    ): TransitionSet {
        val sharedElements = animation.sharedElements
        val elementTransitions = animation.elementTransitions
        if (!sharedElements.hasValue() && !elementTransitions.hasValue()) return TransitionSet()

        val result = TransitionSet()
        result.addAll(createSharedElementTransitions(fromScreen, toScreen, sharedElements))
        result.addAll(createElementTransitions(fromScreen, toScreen, elementTransitions))
        return result
    }

    private suspend fun createSharedElementTransitions(
            fromScreen: ViewController<*>,
            toScreen: ViewController<*>,
            sharedElements: SharedElements
    ): List<Transition> {
        val transitions = mutableListOf<SharedElementTransition>()

        for (transitionOptions in sharedElements.get()) {
            val transition = SharedElementTransition(toScreen, transitionOptions)
            ExistingViewFinder().find(fromScreen, transition.fromId)?.let { transition.from = it }
            transition.to = OptimisticViewFinder().find(toScreen, transition.toId)
            if (transition.isValid()) transitions.add(transition)
        }

        return transitions
    }

    private suspend fun createElementTransitions(
            fromScreen: ViewController<*>,
            toScreen: ViewController<*>,
            elementTransitions: ElementTransitions
    ): List<ElementTransition> {
        val transitions = mutableListOf<ElementTransition>()

        for (transitionOptions in elementTransitions.transitions) {
            val transition = ElementTransition(transitionOptions)
            ExistingViewFinder().find(fromScreen, transition.id)?.let {
                transition.view = it
                transition.viewController = fromScreen
                transitions.add(transition)
            }

            if (transition.isValid()) continue

            transition.view = OptimisticViewFinder().find(toScreen, transition.id)
            transition.viewController = toScreen
            transitions.add(transition)
        }

        return transitions
    }
}
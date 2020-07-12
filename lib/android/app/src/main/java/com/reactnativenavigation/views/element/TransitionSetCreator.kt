package com.reactnativenavigation.views.element

import android.view.View
import com.reactnativenavigation.options.ElementTransitions
import com.reactnativenavigation.options.NestedAnimationsOptions
import com.reactnativenavigation.options.SharedElements
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.element.finder.ExistingViewFinder
import com.reactnativenavigation.views.element.finder.OptimisticViewFinder

class TransitionSetCreator {
    fun create(animation: NestedAnimationsOptions, fromScreen: ViewController<*>, toScreen: ViewController<*>, onAnimatorsCreated: (TransitionSet) -> Unit) {
        val sharedElements = animation.sharedElements
        val elementTransitions = animation.elementTransitions
        if (!sharedElements.hasValue() && !elementTransitions.hasValue()) {
            onAnimatorsCreated(TransitionSet())
            return
        }
        val transitionSet = TransitionSet()
        createSharedElementTransitions(fromScreen, toScreen, transitionSet, sharedElements, elementTransitions, onAnimatorsCreated)
        createElementTransitions(fromScreen, toScreen, transitionSet, sharedElements, elementTransitions, onAnimatorsCreated)
    }

    private fun createSharedElementTransitions(fromScreen: ViewController<*>, toScreen: ViewController<*>, transitionSet: TransitionSet, sharedElements: SharedElements, elementTransitions: ElementTransitions, onTransitionCreated: (TransitionSet) -> Unit) {
        for (transitionOptions in sharedElements.get()) {
            val transition = SharedElementTransition(toScreen, transitionOptions)
            OptimisticViewFinder().find(fromScreen, transition.fromId) {
                transition.from = it
                reportTransitionCreated(transitionSet, sharedElements, elementTransitions, transition, onTransitionCreated)
            }
            OptimisticViewFinder().find(toScreen, transition.toId) {
                transition.to = it
                reportTransitionCreated(transitionSet, sharedElements, elementTransitions, transition, onTransitionCreated)
            }
        }
    }

    private fun reportTransitionCreated(transitionSet: TransitionSet, sharedElements: SharedElements, elementTransitions: ElementTransitions, transition: SharedElementTransition, onTransitionCreated: (TransitionSet) -> Unit) {
        if (transition.isValid()) transitionSet.add(transition)
        if (transitionSet.size() == sharedElements.get().size + elementTransitions.transitions.size) {
            onTransitionCreated(transitionSet)
        }
    }

    private fun createElementTransitions(fromScreen: ViewController<*>, toScreen: ViewController<*>, transitionSet: TransitionSet, sharedElements: SharedElements, elementTransitions: ElementTransitions, onAnimatorsCreated: (TransitionSet) -> Unit) {
        for (transitionOptions in elementTransitions.transitions) {
            val transition = ElementTransition(transitionOptions)
            ExistingViewFinder().find(fromScreen, transition.id) {
                transition.viewController = fromScreen
                reportTransitionCreated(transitionSet, sharedElements, elementTransitions, transition, it, onAnimatorsCreated)
            }
            if (transition.isValid()) continue
            OptimisticViewFinder().find(toScreen, transition.id) {
                transition.viewController = toScreen
                reportTransitionCreated(transitionSet, sharedElements, elementTransitions, transition, it, onAnimatorsCreated)
            }
        }
    }

    private fun reportTransitionCreated(transitionSet: TransitionSet, sharedElements: SharedElements, elementTransitions: ElementTransitions, transition: ElementTransition, it: View, onAnimatorsCreated: (TransitionSet) -> Unit) {
        transition.view = it
        transitionSet.add(transition)
        if (transitionSet.size() == sharedElements.get().size + elementTransitions.transitions.size) {
            onAnimatorsCreated(transitionSet)
        }
    }
}
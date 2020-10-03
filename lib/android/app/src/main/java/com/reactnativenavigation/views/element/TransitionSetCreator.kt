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

    fun createPIPIn(pipContainer: View, animation: NestedAnimationsOptions, screen: ViewController<*>, onAnimatorsCreated: (TransitionSet) -> Unit) {
        val elementTransitions = animation.elementTransitions
        if (!elementTransitions.hasValue()) {
            onAnimatorsCreated(TransitionSet())
            return
        }
        val transitionSet = TransitionSet()
        createPIPInElementTransitions(pipContainer, screen, transitionSet, elementTransitions, onAnimatorsCreated)
    }

    fun createPIPOut(pipContainer: View, animation: NestedAnimationsOptions, screen: ViewController<*>, onAnimatorsCreated: (TransitionSet) -> Unit) {
        val elementTransitions = animation.elementTransitions
        if (!elementTransitions.hasValue()) {
            onAnimatorsCreated(TransitionSet())
            return
        }
        val transitionSet = TransitionSet()
        createPIPOutElementTransitions(pipContainer, screen, transitionSet, elementTransitions, onAnimatorsCreated)
    }

    private fun createPIPInElementTransitions(pipContainer: View, screen: ViewController<*>, transitionSet: TransitionSet, elementTransitions: ElementTransitions, onTransitionCreated: (TransitionSet) -> Unit) {
        for (transitionOptions in elementTransitions.transitions) {
            val transition = ElementTransition(transitionOptions)
            ExistingViewFinder().find(screen, transition.id) {
                transition.view = it
                transition.viewController = screen
                reportPIPTransitionCreated(transitionSet, elementTransitions, transition, onTransitionCreated)
            }
            if (transition.isValid()) continue
        }
    }

    private fun createPIPOutElementTransitions(pipContainer: View, screen: ViewController<*>, transitionSet: TransitionSet, elementTransitions: ElementTransitions, onTransitionCreated: (TransitionSet) -> Unit) {
        for (transitionOptions in elementTransitions.transitions) {
            val transition = ElementTransition(transitionOptions)
            ExistingViewFinder().find(screen, transition.id) {
                transition.viewController = screen
                transition.view = it
                transition.setValueDy(View.X, pipContainer.x, 0.toFloat())
                transition.setValueDy(View.Y, pipContainer.y, 0.toFloat())
                reportPIPTransitionCreated(transitionSet, elementTransitions, transition, onTransitionCreated)
            }
            if (transition.isValid()) continue
        }
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

    private fun reportTransitionCreated(transitionSet: TransitionSet, sharedElements: SharedElements, elementTransitions: ElementTransitions, transition: SharedElementTransition, onTransitionCreated: (TransitionSet) -> Unit) {
        if (transition.isValid()) transitionSet.add(transition)
        if (transitionSet.size() == sharedElements.get().size + elementTransitions.transitions.size) {
            onTransitionCreated(transitionSet)
        }
    }

    private fun reportPIPTransitionCreated(transitionSet: TransitionSet, elementTransitions: ElementTransitions, transition: ElementTransition, onTransitionCreated: (TransitionSet) -> Unit) {
        if (transition.isValid()) transitionSet.add(transition)
        if (transitionSet.size() == elementTransitions.transitions.size) {
            onTransitionCreated(transitionSet)
        }
    }
}
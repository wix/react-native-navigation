package com.reactnativenavigation.views.element

import com.reactnativenavigation.options.ElementTransitions
import com.reactnativenavigation.options.NestedAnimationsOptions
import com.reactnativenavigation.options.SharedElements
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.element.finder.ExistingViewFinder

class TransitionSetCreator {
    fun create(
            animation: NestedAnimationsOptions,
            fromScreen: ViewController<*>,
            toScreen: ViewController<*>
    ) = TransitionSet().apply {
        addAll(createSharedElementTransitions(fromScreen, toScreen, animation.sharedElements))
        addAll(createElementTransitions(fromScreen, toScreen, animation.elementTransitions))
    }

    private fun createSharedElementTransitions(
            fromScreen: ViewController<*>,
            toScreen: ViewController<*>,
            sharedElements: SharedElements
    ) = sharedElements.get()
            .map { options ->
                SharedElementTransition(toScreen, options).apply {
                    ExistingViewFinder().find(fromScreen, fromId)?.let { from = it }
                    ExistingViewFinder().find(toScreen, toId)?.let { to = it }
                }
            }
            .filter { it.isValid() }

    private fun createElementTransitions(
            fromScreen: ViewController<*>,
            toScreen: ViewController<*>,
            elementTransitions: ElementTransitions
    ): List<ElementTransition> {
        return elementTransitions.transitions
                .map { options ->
                    val transition = ElementTransition(options)
                    ExistingViewFinder().find(fromScreen, transition.id)?.let {
                        transition.view = it
                        transition.viewController = fromScreen
                    } ?: run {
                        ExistingViewFinder().find(toScreen, transition.id)?.let {
                            transition.view = it
                            transition.viewController = toScreen
                        }
                    }
                    transition
                }
                .filter { it.isValid() }
    }
}
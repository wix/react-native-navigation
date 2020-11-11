package com.reactnativenavigation.views.element

import com.reactnativenavigation.options.ElementTransitions
import com.reactnativenavigation.options.LayoutAnimation
import com.reactnativenavigation.options.SharedElements
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.element.finder.ExistingViewFinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class TransitionSetCreator {
    suspend fun create(
            animation: LayoutAnimation,
            fromScreen: ViewController<*>,
            toScreen: ViewController<*>
    ) = TransitionSet().apply {
        addAll(createSharedElementTransitions(fromScreen, toScreen, animation.sharedElements))
        addAll(createElementTransitions(fromScreen, toScreen, animation.elementTransitions))
    }

    private suspend fun createSharedElementTransitions(
            fromScreen: ViewController<*>,
            toScreen: ViewController<*>,
            sharedElements: SharedElements
    ): List<Transition> = withContext(Dispatchers.Main.immediate) {
        sharedElements.get()
                .map {
                    async {
                        SharedElementTransition(toScreen, it).apply {
                            ExistingViewFinder().find(fromScreen, fromId)?.let { from = it }
                            ExistingViewFinder().find(toScreen, toId)?.let { to = it }
                        }
                    }
                }
                .awaitAll()
                .filter { it.isValid() }
    }

    private suspend fun createElementTransitions(
            fromScreen: ViewController<*>,
            toScreen: ViewController<*>,
            elementTransitions: ElementTransitions
    ): List<ElementTransition> = withContext(Dispatchers.Main.immediate) {
        elementTransitions.transitions
                .map {
                    async {
                        val transition = ElementTransition(it)
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
                }
                .awaitAll()
                .filter { it.isValid() }
    }
}
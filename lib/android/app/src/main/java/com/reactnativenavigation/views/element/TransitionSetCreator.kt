package com.reactnativenavigation.views.element

import android.view.View
import com.reactnativenavigation.options.ElementTransitions
import com.reactnativenavigation.options.LayoutAnimation
import com.reactnativenavigation.options.NestedAnimationsOptions
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

    suspend fun createPIPIn(pipContainer: View, animation: NestedAnimationsOptions, screen: ViewController<*>, onAnimatorsCreated: (TransitionSet) -> Unit) {
        val elementTransitions = animation.elementTransitions
        if (!elementTransitions.hasValue()) {
            onAnimatorsCreated(TransitionSet())
            return
        }
        val transitionSet = TransitionSet()
        createPIPInElementTransitions(pipContainer, screen, transitionSet, elementTransitions, onAnimatorsCreated)
    }

    suspend fun createPIPOut(pipContainer: View, animation: NestedAnimationsOptions, screen: ViewController<*>, onAnimatorsCreated: (TransitionSet) -> Unit) {
        val elementTransitions = animation.elementTransitions
        if (!elementTransitions.hasValue()) {
            onAnimatorsCreated(TransitionSet())
            return
        }
        val transitionSet = TransitionSet()
        createPIPOutElementTransitions(pipContainer, screen, transitionSet, elementTransitions, onAnimatorsCreated)
    }

    private suspend fun createPIPInElementTransitions(pipContainer: View, screen: ViewController<*>, transitionSet: TransitionSet, elementTransitions: ElementTransitions, onTransitionCreated: (TransitionSet) -> Unit) {
        for (transitionOptions in elementTransitions.transitions) {
            val transition = ElementTransition(transitionOptions)
            ExistingViewFinder().find(screen, transition.id)?.let {
                transition.view = it
                transition.viewController = screen
                reportPIPTransitionCreated(transitionSet, elementTransitions, transition, onTransitionCreated)
            }
            if (transition.isValid()) continue
        }
    }

    private suspend fun createPIPOutElementTransitions(pipContainer: View, screen: ViewController<*>, transitionSet: TransitionSet, elementTransitions: ElementTransitions, onTransitionCreated: (TransitionSet) -> Unit) {
        for (transitionOptions in elementTransitions.transitions) {
            val transition = ElementTransition(transitionOptions)
            ExistingViewFinder().find(screen, transition.id)?.let {
                transition.viewController = screen
                transition.view = it
                transition.setValueDy(View.X, pipContainer.x, 0.toFloat())
                transition.setValueDy(View.Y, pipContainer.y, 0.toFloat())
                reportPIPTransitionCreated(transitionSet, elementTransitions, transition, onTransitionCreated)
            }
            if (transition.isValid()) continue
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

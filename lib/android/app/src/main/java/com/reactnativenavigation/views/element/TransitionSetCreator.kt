package com.reactnativenavigation.views.element

import android.view.View
import androidx.core.view.doOnLayout
import com.facebook.react.uimanager.util.ReactFindViewUtil
import com.reactnativenavigation.options.ElementTransitions
import com.reactnativenavigation.options.NestedAnimationsOptions
import com.reactnativenavigation.options.SharedElements
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController

class TransitionSetCreator {
    fun create(animation: NestedAnimationsOptions, fromScreen: ViewController<*>, toScreen: ViewController<*>, onAnimatorsCreated: (TransitionSet) -> Unit) {
        val sharedElements = animation.sharedElements
        val elementTransitions = animation.elementTransitions
        if (!sharedElements.hasValue() && !elementTransitions.hasValue) {
            onAnimatorsCreated(TransitionSet())
            return
        }
        val transitionSet = TransitionSet()
        createSharedElementTransitions(sharedElements, toScreen, fromScreen, transitionSet, elementTransitions, onAnimatorsCreated)
        createElementTransitions(elementTransitions, fromScreen, transitionSet, toScreen, sharedElements, onAnimatorsCreated)
    }

    private fun createSharedElementTransitions(sharedElements: SharedElements, toScreen: ViewController<*>, fromScreen: ViewController<*>, transitionSet: TransitionSet, elementTransitions: ElementTransitions, onAnimatorsCreated: (TransitionSet) -> Unit) {
        for (transitionOptions in sharedElements.get()) {
            val transition = SharedElementTransition(toScreen, transitionOptions!!)
            ReactFindViewUtil.findView(fromScreen.view, transition.fromId)?.let { transition.from = it }
            ReactFindViewUtil.findView(toScreen.view, object : ReactFindViewUtil.OnViewFoundListener {
                override fun getNativeId(): String {
                    return transition.toId
                }

                override fun onViewFound(view: View) {
                    if (view.width > 0) {
                        transition.to = view
                        if (transition.isValid()) transitionSet.add(transition)
                        if (transitionSet.size() == sharedElements.get().size + elementTransitions.transitions.size) {
                            onAnimatorsCreated(transitionSet)
                        }
                    } else {
                        view.doOnLayout {
                            transition.to = view
                            if (transition.isValid()) transitionSet.add(transition)
                            if (transitionSet.size() == sharedElements.get().size + elementTransitions.transitions.size) {
                                onAnimatorsCreated(transitionSet)
                            }
                        }
                    }
                }
            })
        }
    }

    private fun createElementTransitions(elementTransitions: ElementTransitions, fromScreen: ViewController<*>, transitionSet: TransitionSet, toScreen: ViewController<*>, sharedElements: SharedElements, onAnimatorsCreated: (TransitionSet) -> Unit) {
        for (transitionOptions in elementTransitions.transitions) {
            val transition = ElementTransition(transitionOptions)
            ReactFindViewUtil.findView(fromScreen.view, transition.id)?.let {
                transition.view = it
                transition.viewController = fromScreen
                transitionSet.add(transition)
            }
            if (transition.isValid()) continue
            ReactFindViewUtil.findView(toScreen.view, object : ReactFindViewUtil.OnViewFoundListener {
                override fun getNativeId(): String {
                    return transition.id
                }

                override fun onViewFound(view: View) {
                    if (view.width > 0) {
                        transition.view = view
                        transition.viewController = toScreen
                        transitionSet.add(transition)
                        if (transitionSet.size() == sharedElements.get().size + elementTransitions.transitions.size) {
                            onAnimatorsCreated(transitionSet)
                        }
                    } else {
                        view.doOnLayout {
                            transition.view = view
                            transition.viewController = toScreen
                            transitionSet.add(transition)
                            if (transitionSet.size() == sharedElements.get().size + elementTransitions.transitions.size) {
                                onAnimatorsCreated(transitionSet)
                            }
                        }
                    }
                }
            })
        }
    }
}

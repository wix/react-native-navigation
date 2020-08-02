package com.reactnativenavigation.views.element

import android.animation.AnimatorSet
import android.view.View
import android.view.View.X
import android.view.View.Y
import androidx.core.view.doOnLayout
import com.facebook.react.uimanager.util.ReactFindViewUtil.OnViewFoundListener
import com.facebook.react.uimanager.util.ReactFindViewUtil.findView
import com.reactnativenavigation.parse.AnimationOptions
import com.reactnativenavigation.parse.NestedAnimationsOptions
import com.reactnativenavigation.utils.Functions.Func1
import com.reactnativenavigation.viewcontrollers.ViewController

open class ElementTransitionManager {
    private val animatorCreator: TransitionAnimatorCreator = TransitionAnimatorCreator()

    fun createTransitions(animation: NestedAnimationsOptions, fromScreen: ViewController<*>, toScreen: ViewController<*>, onAnimatorsCreated: Func1<TransitionSet?>) {
        val sharedElements = animation.sharedElements
        val elementTransitions = animation.elementTransitions
        if (!sharedElements.hasValue() && !elementTransitions.hasValue) {
            onAnimatorsCreated.run(TransitionSet())
            return
        }
        val transitionSet = TransitionSet()
        for (transitionOptions in sharedElements.get()) {
            val transition = SharedElementTransition(toScreen, transitionOptions!!)
            findView(fromScreen.view, transition.fromId)?.let { transition.from = it }
            findView(toScreen.view, object : OnViewFoundListener {
                override fun getNativeId(): String {
                    return transition.toId
                }

                override fun onViewFound(view: View) {
                    view.doOnLayout {
                        transition.to = view
                        if (transition.isValid()) transitionSet.add(transition)
                        if (transitionSet.size() == sharedElements.get().size + elementTransitions.transitions.size) {
                            onAnimatorsCreated.run(transitionSet)
                        }
                    }
                }
            })
        }
        for (transitionOptions in elementTransitions.transitions) {
            val transition = ElementTransition(transitionOptions)
            findView(fromScreen.view, transition.id)?.let {
                transition.view = it
                transition.viewController = fromScreen
                transitionSet.add(transition)
            }
            if (transition.isValid()) continue
            findView(toScreen.view, object : OnViewFoundListener {
                override fun getNativeId(): String {
                    return transition.id
                }

                override fun onViewFound(view: View) {
                    view.doOnLayout {
                        transition.view = view
                        transition.viewController = toScreen
                        transitionSet.add(transition)
                        if (transitionSet.size() == sharedElements.get().size + elementTransitions.transitions.size) {
                            onAnimatorsCreated.run(transitionSet)
                        }
                    }
                }
            })
        }
    }

    fun createPIPTransitions(animation: NestedAnimationsOptions, pipContainer: View, pipScreen: ViewController<*>, onAnimatorsCreated: Func1<TransitionSet?>) {
        val elementTransitions = animation.elementTransitions
        if (!elementTransitions.hasValue) {
            onAnimatorsCreated.run(TransitionSet())
            return
        }
        val transitionSet = TransitionSet()
        for (transitionOptions in elementTransitions.transitions) {
            val transition = ElementTransition(transitionOptions)
            findView(pipContainer, transition.id)?.let {
                transition.view = it
                transition.viewController = pipScreen
                transitionSet.add(transition)
            }
            if (transition.isValid()) continue
        }
        onAnimatorsCreated.run(transitionSet)
    }

    fun createPIPOutTransitions(animation: NestedAnimationsOptions, pipContainer: View, pipScreen: ViewController<*>, onAnimatorsCreated: Func1<TransitionSet?>) {
        val elementTransitions = animation.elementTransitions
        if (!elementTransitions.hasValue) {
            onAnimatorsCreated.run(TransitionSet())
            return
        }
        val transitionSet = TransitionSet()
        for (transitionOptions in elementTransitions.transitions) {
            val transition = ElementTransition(transitionOptions)
            findView(pipContainer, transition.id)?.let {
                transition.view = it
                transition.viewController = pipScreen
                transitionSet.add(transition)
                transition.setValueDy(X, pipContainer.x, 0.toFloat())
                transition.setValueDy(Y, pipContainer.y, 0.toFloat())
            }
            if (transition.isValid()) continue
        }
        onAnimatorsCreated.run(transitionSet)
    }


    fun createAnimators(fadeAnimation: AnimationOptions?, transitionSet: TransitionSet?): AnimatorSet {
        return animatorCreator.create(fadeAnimation!!, transitionSet!!)
    }

}
package com.reactnativenavigation.views.element

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import com.facebook.react.uimanager.ViewGroupManager
import com.reactnativenavigation.NavigationActivity
import com.reactnativenavigation.R
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.options.FadeAnimation
import com.reactnativenavigation.options.NestedAnimationsOptions
import com.reactnativenavigation.utils.ViewTags
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.viewcontrollers.splash.SplashViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import java.util.*

open class TransitionAnimatorCreator @JvmOverloads constructor(private val transitionSetCreator: TransitionSetCreator = TransitionSetCreator()) {
    suspend fun createSetRootAnimator(animation: AnimationOptions, splash: SplashViewController, root: ViewController<*>): AnimatorSet {
        val transitions = transitionSetCreator.create(animation, splash, root)
        return createAnimator(transitions)
    }

    suspend fun create(animation: NestedAnimationsOptions, defaultDuration: Long, fromScreen: ViewController<*>, toScreen: ViewController<*>): AnimatorSet {
        val transitions = transitionSetCreator.create(animation, fromScreen, toScreen)
        return createAnimator(transitions, defaultDuration)
    }

    private fun createAnimator(transitions: TransitionSet, defaultDuration: Long = FadeAnimation.DURATION): AnimatorSet {
        recordIndices(transitions)
        reparentViews(transitions)
        val animators = ArrayList<Animator>()
        animators.addAll(createSharedElementTransitionAnimators(transitions.validSharedElementTransitions))
        animators.addAll(createElementTransitionAnimators(transitions.validElementTransitions))

        setAnimatorsDuration(animators, defaultDuration)
        return AnimatorSet().apply {
            doOnEnd { restoreViewsToOriginalState(transitions) }
            doOnCancel { restoreViewsToOriginalState(transitions) }
            playTogether(animators)
        }
    }

    private fun recordIndices(transitions: TransitionSet) {
        transitions.forEach {
            it.view.setTag(R.id.original_index_in_parent, ViewUtils.getIndexInParent(it.view))
        }
    }

    private fun setAnimatorsDuration(animators: Collection<Animator>, defaultDuration: Long) {
        for (animator in animators) {
            if (animator is AnimatorSet) {
                setAnimatorsDuration(animator.childAnimations, defaultDuration)
            } else if (animator.duration.toInt() <= 0) {
                animator.duration = defaultDuration
            }
        }
    }

    private fun reparentViews(transitions: TransitionSet) {
        transitions.transitions
                .sortedBy { ViewGroupManager.getViewZIndex(it.view) }
                .forEach { reparent(it) }
    }

    private fun createSharedElementTransitionAnimators(transitions: List<SharedElementTransition>) = transitions.map {
        createSharedElementAnimator(it)
    }

    private fun createSharedElementAnimator(transition: SharedElementTransition): AnimatorSet {
        return transition
                .createAnimators()
                .apply {
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator) {
                            transition.from.alpha = 0f
                        }
                    })
                }
    }

    private fun createElementTransitionAnimators(transitions: List<ElementTransition>) = transitions.map {
        it.createAnimators()
    }

    private fun restoreViewsToOriginalState(transitions: TransitionSet) {
        mutableListOf<Transition>().apply {
            addAll(transitions.validSharedElementTransitions)
            addAll(transitions.validElementTransitions)
            sortBy { ViewGroupManager.getViewZIndex(it.view) }
            sortBy { it.view.getTag(R.id.original_index_in_parent) as Int }
            forEach {
                removeOverlay(it)
                returnToOriginalParent(it.view)
            }
        }
        transitions.validSharedElementTransitions.forEach {
            it.from.alpha = 1f
        }
    }

    private fun reparent(transition: Transition) {
        with(transition) {
            val loc = ViewUtils.getLocationOnScreen(view)
            val biologicalParent = view.parent as ViewGroup
            view.setTag(R.id.original_parent, biologicalParent)
            view.setTag(R.id.original_layout_params, view.layoutParams)
            view.setTag(R.id.original_top, view.top)
            view.setTag(R.id.original_bottom, view.bottom)
            view.setTag(R.id.original_right, view.right)
            view.setTag(R.id.original_left, view.left)
            view.setTag(R.id.original_pivot_x, view.pivotX)
            view.setTag(R.id.original_pivot_y, view.pivotY)

            biologicalParent.removeView(view)

            val lp = FrameLayout.LayoutParams(view.layoutParams)
            lp.topMargin = loc.y
            lp.leftMargin = loc.x
            lp.width = view.width
            lp.height = view.height
            addOverlay(viewController, view, lp)
        }
    }

    private fun returnToOriginalParent(element: View) {
        ViewUtils.removeFromParent(element)
        element.top = ViewTags.get(element, R.id.original_top)
        element.bottom = ViewTags.get(element, R.id.original_bottom)
        element.right = ViewTags.get(element, R.id.original_right)
        element.left = ViewTags.get(element, R.id.original_left)
        element.pivotX = ViewTags.get(element, R.id.original_pivot_x)
        element.pivotY = ViewTags.get(element, R.id.original_pivot_y)
        val parent = ViewTags.get<ViewGroup>(element, R.id.original_parent)
        val lp = ViewTags.get<ViewGroup.LayoutParams>(element, R.id.original_layout_params)
        val index = ViewTags.get<Int>(element, R.id.original_index_in_parent)
        parent.addView(element, index, lp)
    }

    open fun addOverlay(viewController: ViewController<*>, view: View, layoutParams: FrameLayout.LayoutParams) {
        if (viewController.parentController != null) {
            viewController.requireParentController().addOverlay(view, layoutParams)
        } else {
            (viewController.activity as NavigationActivity).navigator.addOverlay(view, layoutParams)
        }
    }

    open fun removeOverlay(transition: Transition) = with(transition) {
        if (viewController.parentController != null) {
            viewController.requireParentController().removeOverlay(view)
        } else {
            (viewController.activity as NavigationActivity).navigator.removeOverlay(view)
        }
    }
}
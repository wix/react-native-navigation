package com.reactnativenavigation.views.element

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.view.marginLeft
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.views.image.ReactImageView
import com.reactnativenavigation.R
import com.reactnativenavigation.parse.AnimationOptions
import com.reactnativenavigation.utils.ViewTags
import com.reactnativenavigation.utils.ViewUtils
import java.util.*

open class TransitionAnimatorCreator {
    fun create(fadeAnimation: AnimationOptions, transitions: TransitionSet): AnimatorSet {
        if (transitions.isEmpty) return AnimatorSet()
        recordIndices(transitions)
        reparentViews(transitions)
        val animators = ArrayList<Animator>()
        animators.addAll(createSharedElementTransitionAnimators(transitions.validSharedElementTransitions))
        animators.addAll(createElementTransitionAnimators(transitions.validElementTransitions))

        setAnimatorsDuration(animators, fadeAnimation)
        val set = AnimatorSet()
        set.doOnEnd { restoreViewsToOriginalState(transitions) }
        set.doOnCancel { restoreViewsToOriginalState(transitions) }
        set.playTogether(animators)
        return set
    }

    private fun recordIndices(transitions: TransitionSet) {
        transitions.forEach {
            it.view.setTag(R.id.original_index_in_parent, ViewUtils.getIndexInParent(it.view))
        }
    }

    private fun setAnimatorsDuration(animators: Collection<Animator>, fadeAnimation: AnimationOptions) {
        for (animator in animators) {
            if (animator is AnimatorSet) {
                setAnimatorsDuration(animator.childAnimations, fadeAnimation)
            } else if (animator.duration.toInt() <= 0) {
                animator.duration = fadeAnimation.duration.toLong()
            }
        }
    }

    private fun reparentViews(transitions: TransitionSet) {
        transitions.transitions
                .sortedBy { ViewGroupManager.getViewZIndex(it.view) }
                .forEach { reparent(it) }
    }

    private fun createSharedElementTransitionAnimators(transitions: List<SharedElementTransition>): List<AnimatorSet> {
        val animators: MutableList<AnimatorSet> = ArrayList()
        for (transition in transitions) {
            animators.add(createSharedElementAnimator(transition))
        }
        return animators
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

    private fun createElementTransitionAnimators(transitions: List<ElementTransition>): List<AnimatorSet> {
        val animators: MutableList<AnimatorSet> = ArrayList()
        for (transition in transitions) {
            animators.add(transition.createAnimators())
        }
        return animators
    }

    private fun restoreViewsToOriginalState(transitions: TransitionSet) {
        mutableListOf<Transition>().apply {
            addAll(transitions.validSharedElementTransitions)
            addAll(transitions.validElementTransitions)
            sortBy { ViewGroupManager.getViewZIndex(it.view) }
            sortBy { it.view.getTag(R.id.original_index_in_parent) as Int}
            forEach {
                it.viewController.requireParentController().removeOverlay(it.view)
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
            view.setTag(R.id.original_top, loc.y - transition.topInset)
            view.setTag(R.id.original_bottom, view.bottom)
            view.setTag(R.id.original_right, view.right)
            view.setTag(R.id.original_left, loc.x)

            biologicalParent.removeView(view)

            val lp = FrameLayout.LayoutParams(view.layoutParams)
            lp.topMargin = loc.y
            lp.leftMargin = loc.x
            lp.width = view.width
            lp.height = view.height
            view.layoutParams = lp
            transition.viewController.requireParentController().addOverlay(view)
        }
    }

    private fun returnToOriginalParent(element: View) {
        ViewUtils.removeFromParent(element)
        element.top = ViewTags.get(element, R.id.original_top)
        element.bottom = ViewTags.get(element, R.id.original_bottom)
        element.right = ViewTags.get(element, R.id.original_right)
        element.left = ViewTags.get(element, R.id.original_left)
        val parent = ViewTags.get<ViewGroup>(element, R.id.original_parent)
        val lp = ViewTags.get<ViewGroup.LayoutParams>(element, R.id.original_layout_params)
        val index = ViewTags.get<Int>(element, R.id.original_index_in_parent)
        parent.addView(element, index, lp)
    }
}
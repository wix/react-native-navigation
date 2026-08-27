package com.reactnativenavigation.views.element

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.options.LayoutAnimation
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest = Config.NONE)
class TransitionAnimatorCreatorTest {
    private val context = RuntimeEnvironment.getApplication()
    private val parent = FrameLayout(context)
    private val overlay = FrameLayout(context)
    private val back = View(context)
    private val stationary = View(context)
    private val front = View(context)

    @Test
    fun reparentsInNativeDrawingOrderAndRestoresSiblingIndicesOnEnd() {
        val animator = createAnimator()
        assertThat(overlay.getChildAt(0)).isSameAs(back)
        assertThat(overlay.getChildAt(1)).isSameAs(front)

        animator.start()
        animator.end()
        assertRestored()
    }

    @Test
    fun restoresSiblingIndicesOnCancel() {
        val animator = createAnimator()
        animator.start()
        animator.cancel()
        assertRestored()
    }

    private fun createAnimator(): AnimatorSet = runBlocking {
        parent.addView(back)
        parent.addView(stationary)
        parent.addView(front)
        val controller = mock<ViewController<*>>()
        doAnswer {
            overlay.addView(it.getArgument<View>(0), it.getArgument<ViewGroup.LayoutParams>(1))
        }.whenever(controller).addOverlay(any(), any())
        doAnswer {
            overlay.removeView(it.getArgument<View>(0))
        }.whenever(controller).removeOverlay(any())

        fun transition(element: View) = mock<ElementTransition>().apply {
            whenever(view).thenReturn(element)
            whenever(viewController).thenReturn(controller)
            whenever(createAnimators()).thenReturn(ValueAnimator.ofFloat(0f, 1f))
        }

        val transitions = TransitionSet().apply {
            add(transition(front))
            add(transition(back))
        }
        val layoutAnimation = mock<LayoutAnimation>()
        val creator = mock<TransitionSetCreator>()
        whenever(creator.create(layoutAnimation, controller, controller)).thenReturn(transitions)
        TransitionAnimatorCreator(creator).create(
            layoutAnimation, AnimationOptions(), controller, controller
        )
    }

    private fun assertRestored() {
        assertThat(overlay.childCount).isZero()
        assertThat(parent.childCount).isEqualTo(3)
        assertThat(parent.getChildAt(0)).isSameAs(back)
        assertThat(parent.getChildAt(1)).isSameAs(stationary)
        assertThat(parent.getChildAt(2)).isSameAs(front)
    }
}

package com.reactnativenavigation.viewcontrollers.viewcontroller

import android.animation.AnimatorSet
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.mocks.SimpleViewController
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.utils.awaitRender
import kotlinx.coroutines.launch
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock

class ViewControllerScopeTest : BaseTest() {
    @Test
    fun destroyCancelsRenderWaitsEvenBeforeTheViewWasCreated() {
        val controller = SimpleViewController(newActivity(), mock(), "screen", Options())
        var resumed = false
        val job = controller.coroutineScope.launch {
            controller.awaitRender()
            resumed = true
        }
        controller.destroy()
        controller.onViewWillAppear()
        idleMainLooper()
        assertThat(job.isCancelled).isTrue()
        assertThat(resumed).isFalse()
        assertThat(controller.isDestroyed).isTrue()
    }

    @Test
    fun cancellingAPendingAnimationRemovesItsRenderWait() {
        val controller = SimpleViewController(newActivity(), mock(), "screen", Options())
        val animator = AnimatorSet()
        var prepared = false
        var cancelled = 0
        controller.coroutineScope.launchAnimation(animator, { cancelled++ }) {
            controller.awaitRender()
            prepared = true
        }
        animator.cancelPendingOrRunning()
        controller.onViewWillAppear()
        idleMainLooper()
        assertThat(prepared).isFalse()
        assertThat(cancelled).isEqualTo(1)
        controller.destroy()
    }
}

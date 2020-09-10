package com.reactnativenavigation.viewcontrollers.modal

import com.nhaarman.mockitokotlin2.*
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.mocks.SimpleViewController
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.utils.ScreenAnimationListener
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class ModalAnimatorTest : BaseTest() {
    private lateinit var uut: ModalAnimator;
    private lateinit var modal1: ViewController<*>

    override fun beforeEach() {
        val activity = newActivity()
        val childRegistry = mock<ChildControllersRegistry>()
        uut = ModalAnimator(activity)
        modal1 = SimpleViewController(activity, childRegistry, "child1", Options())
    }

    @Test
    fun show_isRunning() {
        uut.show(modal1.view, AnimationOptions(), object : ScreenAnimationListener() {})
        assertThat(uut.isRunning).isTrue()
    }

    @Test
    fun dismiss_dismissModalDuringShowAnimation() {
        val showListener = spy<ScreenAnimationListener>()
        uut.show(modal1.view, AnimationOptions(), showListener)

        val dismissListener = spy<ScreenAnimationListener>()
        uut.dismiss(modal1.view, AnimationOptions(), dismissListener)

        verify(showListener, times(1)).onCancel()
        verify(showListener, never()).onEnd()
        verify(dismissListener, times(1)).onEnd()
        assertThat(uut.isRunning).isFalse()
    }
}
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
import org.bouncycastle.crypto.tls.MACAlgorithm.sha
import org.junit.Test
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowApplication
import org.robolectric.shadows.ShadowLooper

class ModalAnimatorTest : BaseTest() {
    private lateinit var uut: ModalAnimator;
    private lateinit var modal1: ViewController<*>
    private lateinit var root: ViewController<*>

    override fun beforeEach() {
        val activity = newActivity()
        val childRegistry = mock<ChildControllersRegistry>()
        uut = ModalAnimator(activity)
        modal1 = SimpleViewController(activity, childRegistry, "child1", Options())
        root = SimpleViewController(activity, childRegistry, "root", Options())
    }

    @Test
    fun show_isRunning() {
        uut.show(modal1, root, AnimationOptions(), object : ScreenAnimationListener() {})
        assertThat(uut.isRunning).isTrue()
    }

    @Test
    fun dismiss_dismissModalDuringShowAnimation() {
        val showListener = spy<ScreenAnimationListener>()
        uut.show(modal1, root, AnimationOptions(), showListener)

        verify(showListener).onStart()
        val dismissListener = spy<ScreenAnimationListener>()
        uut.dismiss(root, modal1, AnimationOptions(), dismissListener)

        verify(showListener).onCancel()
        verify(showListener, never()).onEnd()
        verify(dismissListener).onEnd()
        assertThat(uut.isRunning).isFalse()
    }
}
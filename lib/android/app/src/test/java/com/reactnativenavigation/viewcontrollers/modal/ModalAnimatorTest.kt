package com.reactnativenavigation.viewcontrollers.modal

import com.nhaarman.mockitokotlin2.*
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.mocks.SimpleViewController
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.options.TransitionAnimationOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.newAnimationOptionsJson
import com.reactnativenavigation.utils.ScreenAnimationListener
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class ModalAnimatorTest : BaseTest() {
    private lateinit var uut: ModalAnimator;
    private lateinit var modal1: ViewController<*>
    private lateinit var root: ViewController<*>
    private lateinit var modal1View: SimpleViewController.SimpleView
    private lateinit var rootView: SimpleViewController.SimpleView

    override fun beforeEach() {
        val activity = newActivity()
        val childRegistry = mock<ChildControllersRegistry>()
        modal1View = SimpleViewController.SimpleView(activity)
        rootView = SimpleViewController.SimpleView(activity)
        uut = spy(ModalAnimator(activity))
        modal1 = object : SimpleViewController(activity, childRegistry, "child1", Options()) {
            override fun createView(): SimpleView {
                return modal1View
            }
        }

        root = object : SimpleViewController(activity, childRegistry, "root", Options()) {
            override fun createView(): SimpleView {
                return rootView
            }
        }
    }

    @Test
    fun show_isRunning() {
        uut.show(modal1, root, TransitionAnimationOptions(), object : ScreenAnimationListener() {})
        assertThat(uut.isRunning).isTrue()
    }

    @Test
    fun `show - play enter animation on appearing if hasValue`() {
        val enter = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val exit = spy(AnimationOptions())
        val animationOptions = TransitionAnimationOptions(enter = enter, exit = exit)
        val screenAnimationListener: ScreenAnimationListener = mock { }
        uut.show(modal1, root, animationOptions, screenAnimationListener)

        verify(enter).getAnimation(modal1.view)
        verify(exit, never()).getAnimation(root.view)
    }

    @Test
    fun `show - play default animation on appearing modal if enter does not hasValue`() {
        val enter = spy(AnimationOptions())
        val exit = spy(AnimationOptions())
        val animationOptions = TransitionAnimationOptions(enter = enter, exit = exit)
        val screenAnimationListener: ScreenAnimationListener = mock { }
        uut.show(modal1, root, animationOptions, screenAnimationListener)

        verify(uut).getDefaultPushAnimation(modal1.view)
        verify(enter, never()).getAnimation(modal1.view)
        verify(exit, never()).getAnimation(root.view)
    }

    @Test
    fun `show - play enter animation on appearing modal, exit on disappearing one`() {
        val enter = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val exit = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val animationOptions = TransitionAnimationOptions(enter = enter, exit = exit)
        val screenAnimationListener: ScreenAnimationListener = mock { }
        uut.show(modal1, root, animationOptions, screenAnimationListener)

        verify(enter).getAnimation(modal1.view)
        verify(exit).getAnimation(root.view)
    }

    @Test
    fun `show - should not play exit on null disappearing one`() {
        val enter = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val exit = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val animationOptions = TransitionAnimationOptions(enter = enter, exit = exit)
        val screenAnimationListener: ScreenAnimationListener = mock { }
        uut.show(modal1, null, animationOptions, screenAnimationListener)

        verify(enter).getAnimation(modal1.view)
        verify(exit, never()).getAnimation(root.view)
    }

    @Test
    fun `dismiss - play default animation on disappearing modal if exit does not hasValue`() {
        val enter = spy(AnimationOptions())
        val exit = spy(AnimationOptions())
        val animationOptions = TransitionAnimationOptions(enter = enter, exit = exit)
        val screenAnimationListener: ScreenAnimationListener = mock { }
        uut.dismiss(root, modal1, animationOptions, screenAnimationListener)

        verify(uut).getDefaultPopAnimation(modal1.view)
        verify(enter, never()).getAnimation(any())
        verify(exit, never()).getAnimation(any())
    }

    @Test
    fun `dismiss - play exit animation on disappearing modal, enter on appearing one`() {
        val enter = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val exit = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val animationOptions = TransitionAnimationOptions(enter = enter, exit = exit)
        val screenAnimationListener: ScreenAnimationListener = mock { }
        uut.dismiss(root, modal1, animationOptions, screenAnimationListener)

        verify(exit).getAnimation(modal1.view)
        verify(enter).getAnimation(root.view)
    }

    @Test
    fun `dismiss - should not play enter on null appearing one`() {
        val enter = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val exit = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val animationOptions = TransitionAnimationOptions(enter = enter, exit = exit)
        val screenAnimationListener: ScreenAnimationListener = mock { }
        uut.dismiss(null, root, animationOptions, screenAnimationListener)

        verify(enter, never()).getAnimation(any())
        verify(exit).getAnimation(root.view)
    }


    @Test
    fun dismiss_dismissModalDuringShowAnimation() {
        val showListener = spy<ScreenAnimationListener>()
        uut.show(modal1, root, TransitionAnimationOptions(), showListener)

        verify(showListener).onStart()
        val dismissListener = spy<ScreenAnimationListener>()
        uut.dismiss(root, modal1, TransitionAnimationOptions(), dismissListener)

        verify(showListener).onCancel()
        verify(showListener, never()).onEnd()
        verify(dismissListener).onEnd()
        assertThat(uut.isRunning).isFalse()
    }
}
package com.reactnativenavigation.viewcontrollers.modal

import android.app.Activity
import org.mockito.kotlin.*
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.mocks.SimpleViewController
import com.reactnativenavigation.options.*
import com.reactnativenavigation.options.animations.ViewAnimationOptions
import com.reactnativenavigation.utils.ScreenAnimationListener
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.element.TransitionAnimatorCreator
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class ModalAnimatorTest : BaseTest() {
    private lateinit var uut: ModalAnimator
    private lateinit var activity: Activity
    private lateinit var modal1: ViewController<*>
    private lateinit var root: ViewController<*>
    private lateinit var modal1View: SimpleViewController.SimpleView
    private lateinit var rootView: SimpleViewController.SimpleView
    private lateinit var mockDefaultAnimation: StackAnimationOptions
    private lateinit var screenAnimationListener: ScreenAnimationListener
    override fun beforeEach() {
        val mockTransitionAnimatorCreator = spy(TransitionAnimatorCreator())
        val childRegistry = mock<ChildControllersRegistry>()
        val enter = spy(AnimationOptions())
        val exit = spy(AnimationOptions())

        screenAnimationListener = mock { }
        activity = newActivity()
        modal1View = SimpleViewController.SimpleView(activity)
        rootView = SimpleViewController.SimpleView(activity)

        mockDefaultAnimation = StackAnimationOptions().apply {
            val viewAnimationOptions = ViewAnimationOptions()
            viewAnimationOptions.enter = enter
            viewAnimationOptions.exit = exit
            content = viewAnimationOptions
        }
        uut = spy(ModalAnimator(activity, defaultAnimation = mockDefaultAnimation, transitionAnimatorCreator = mockTransitionAnimatorCreator))
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
    fun `show shared elements - should make alpha 0 before animation`() {
        val sharedElements = SharedElements.parse(newAnimationOptionsJson(true).apply {
            put("sharedElementTransitions", newSharedElementAnimationOptionsJson())
        })
        val spyView = spy(modal1View)
        val mockModal = spy(modal1)
        whenever(mockModal.createView()).thenReturn(spyView)
        mockModal.onViewWillAppear() // to avoid wait for render
        uut.show(mockModal, root, TransitionAnimationOptions(sharedElements = sharedElements), screenAnimationListener)
        verify(spyView).alpha=0f
    }

    @Test
    fun `show shared elements - should play default fade-in`() {
        val sharedElements = SharedElements.parse(newAnimationOptionsJson(true).apply {
            put("sharedElementTransitions", newSharedElementAnimationOptionsJson())
        })
        val mockModal = spy(modal1)
        mockModal.onViewWillAppear() // to avoid wait for render
        uut.show(mockModal, root, TransitionAnimationOptions(sharedElements = sharedElements), screenAnimationListener)
        verify(mockDefaultAnimation.content.enter).getAnimation(mockModal.view)
    }

    @Test
    fun `dismiss shared elements - should play default fade-out`() {
        val sharedElements = SharedElements.parse(newAnimationOptionsJson(true).apply {
            put("sharedElementTransitions", newSharedElementAnimationOptionsJson())
        })
        val mockModal = spy(modal1)
        mockModal.onViewWillAppear() // to avoid wait for render
        uut.show(mockModal, root, TransitionAnimationOptions(sharedElements = sharedElements), screenAnimationListener)
        verify(mockDefaultAnimation.content.enter).getAnimation(mockModal.view)

        uut.dismiss(root, mockModal, TransitionAnimationOptions(sharedElements = sharedElements), screenAnimationListener)
        verify(mockDefaultAnimation.content.exit).getAnimation(mockModal.view)
    }

    @Test
    fun `show - should play shared transition if it has value`() {
        val sharedElements = SharedElements.parse(newAnimationOptionsJson(true).apply {
            put("sharedElementTransitions", newSharedElementAnimationOptionsJson())
        })
        val mockModal = spy(modal1)
        uut.show(mockModal, root, TransitionAnimationOptions(sharedElements = sharedElements), screenAnimationListener)

        verify(mockModal).setWaitForRender(any())
    }

    @Test
    fun `show - should not play shared transition if it does not has value`() {
        val enter = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val mockModal = spy(modal1)
        uut.show(mockModal, root, TransitionAnimationOptions(enter = enter), screenAnimationListener)
        verify(mockModal, never()).setWaitForRender(any())
    }

    @Test
    fun `show - play enter animation on appearing if hasValue`() {
        val enter = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val exit = spy(AnimationOptions())
        val animationOptions = TransitionAnimationOptions(enter = enter, exit = exit)
        uut.show(modal1, root, animationOptions, screenAnimationListener)

        verify(enter).getAnimation(modal1.view)
        verify(exit, never()).getAnimation(root.view)
    }

    @Test
    fun `show - play default animation on appearing modal if enter does not hasValue`() {
        val enter = spy(AnimationOptions())
        val exit = spy(AnimationOptions())
        val animationOptions = TransitionAnimationOptions(enter = enter, exit = exit)
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
        uut.show(modal1, root, animationOptions, screenAnimationListener)

        verify(enter).getAnimation(modal1.view)
        verify(exit).getAnimation(root.view)
    }

    @Test
    fun `show - should not play exit on null disappearing one`() {
        val enter = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val exit = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val animationOptions = TransitionAnimationOptions(enter = enter, exit = exit)
        uut.show(modal1, null, animationOptions, screenAnimationListener)

        verify(enter).getAnimation(modal1.view)
        verify(exit, never()).getAnimation(root.view)
    }

    @Test
    fun `dismiss - play default animation on disappearing modal if exit does not hasValue`() {
        val enter = spy(AnimationOptions())
        val exit = spy(AnimationOptions())
        val animationOptions = TransitionAnimationOptions(enter = enter, exit = exit)
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
        uut.dismiss(root, modal1, animationOptions, screenAnimationListener)

        verify(exit).getAnimation(modal1.view)
        verify(enter).getAnimation(root.view)
    }

    @Test
    fun `dismiss - should not play enter on null appearing one`() {
        val enter = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val exit = spy(AnimationOptions(newAnimationOptionsJson(true)))
        val animationOptions = TransitionAnimationOptions(enter = enter, exit = exit)
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
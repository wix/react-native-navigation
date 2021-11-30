package com.reactnativenavigation.viewcontrollers.navigator

import android.app.Activity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.facebook.react.ReactInstanceManager
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.TestActivity
import com.reactnativenavigation.hierarchy.root.RootAnimator
import com.reactnativenavigation.mocks.SimpleViewController
import com.reactnativenavigation.options.*
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.react.CommandListenerAdapter
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry
import com.reactnativenavigation.viewcontrollers.viewcontroller.LayoutDirectionApplier
import com.reactnativenavigation.viewcontrollers.viewcontroller.RootPresenter
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.BehaviourDelegate
import org.assertj.core.api.Java6Assertions
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.robolectric.android.controller.ActivityController

class RootPresenterTest : BaseTest() {
    private lateinit var uut: RootPresenter
    private lateinit var rootContainer: CoordinatorLayout
    private lateinit var root: ViewController<*>
    private lateinit var root2: ViewController<*>
    private lateinit var animator: RootAnimator
    private lateinit var layoutDirectionApplier: LayoutDirectionApplier
    private lateinit var defaultOptions: Options
    private lateinit var reactInstanceManager: ReactInstanceManager
    private lateinit var activity: Activity
    private lateinit var activityController: ActivityController<TestActivity>
    private lateinit var root1View: SimpleViewController.SimpleView
    private lateinit var root2View: SimpleViewController.SimpleView
    override fun beforeEach() {
        activityController = newActivityController(TestActivity::class.java)
        activity = activityController.create().get()
        reactInstanceManager = Mockito.mock(ReactInstanceManager::class.java)
        rootContainer = CoordinatorLayout(activity)
        root1View = SimpleViewController.SimpleView(activity)
        root2View = SimpleViewController.SimpleView(activity)

        root = object : SimpleViewController(activity, ChildControllersRegistry(), "child1", Options()) {
            override fun createView(): SimpleView {
                return root1View
            }
        }
        root2 = object : SimpleViewController(activity, ChildControllersRegistry(), "child1", Options()) {
            override fun createView(): SimpleView {
                return root2View
            }
        }
        setupWithAnimator(Mockito.spy(createAnimator()))
    }

    @Test
    fun setRoot_viewIsAddedToContainer() {
        uut.setRoot(root, null, defaultOptions, CommandListenerAdapter(), reactInstanceManager)
        Java6Assertions.assertThat(root.view.parent).isEqualTo(rootContainer)
        Java6Assertions.assertThat((root.view.layoutParams as CoordinatorLayout.LayoutParams).behavior).isInstanceOf(BehaviourDelegate::class.java)
    }

    @Test
    fun setRoot_reportsOnSuccess() {
        val listener = Mockito.spy(CommandListenerAdapter())
        uut.setRoot(root, null, defaultOptions, listener, reactInstanceManager)
        Mockito.verify(listener).onSuccess(root.id)
    }

    @Test
    fun setRoot_doesNotAnimateByDefault() {
        val listener = Mockito.spy(CommandListenerAdapter())
        uut.setRoot(root, null, defaultOptions, listener, reactInstanceManager)
        Mockito.verifyNoInteractions(animator)
        Mockito.verify(listener).onSuccess(root.id)
    }

    @Test
    fun setRoot_playNoAnimationWhenOptionsHaveThemDisabled() {
        setupWithAnimator(RootAnimator())
        val animatedSetRoot = Options()
        val enter = Mockito.spy(AnimationOptions())
        val exit = Mockito.spy(AnimationOptions())
        enter.enabled = Bool(false)
        exit.enabled = Bool(false)
        Mockito.`when`(enter.hasValue()).thenReturn(true)
        Mockito.`when`(enter.hasAnimation()).thenReturn(true)

        Mockito.`when`(exit.hasValue()).thenReturn(true)
        Mockito.`when`(exit.hasValue()).thenReturn(true)


        animatedSetRoot.animations.setRoot = createEnterExitTransitionAnim(enter, exit)
        val spy = Mockito.spy(root)
        val spy2 = Mockito.spy(root2)
        Mockito.`when`(spy.resolveCurrentOptions(defaultOptions)).thenReturn(animatedSetRoot)
        val listener = Mockito.spy(CommandListenerAdapter())
        uut.setRoot(spy, spy2, defaultOptions, listener, reactInstanceManager)

        Mockito.verify(enter, never()).getAnimation(spy.view)
        Mockito.verify(exit, never()).getAnimation(spy2.view)
    }

    @Test
    fun setRoot_playEnterAnimOnlyWhenNoDisappearingView() {
        setupWithAnimator(RootAnimator())
        val animatedSetRoot = Options()
        val enter = Mockito.spy(AnimationOptions())
        val exit = Mockito.spy(AnimationOptions())

        Mockito.`when`(enter.hasValue()).thenReturn(true)
        Mockito.`when`(enter.hasAnimation()).thenReturn(true)
        Mockito.`when`(exit.hasValue()).thenReturn(false)

        animatedSetRoot.animations.setRoot = createEnterExitTransitionAnim(enter, exit)
        val spy = Mockito.spy(root)
        Mockito.`when`(spy.resolveCurrentOptions(defaultOptions)).thenReturn(animatedSetRoot)
        val listener = Mockito.spy(CommandListenerAdapter())
        uut.setRoot(spy, null, defaultOptions, listener, reactInstanceManager)
        Mockito.verify(enter).getAnimation(spy.view)
        Mockito.verify(exit, never()).getAnimation(spy.view)
    }

    @Test
    fun setRoot_playExitAnimOnlyWhenNoEnterAnimation() {
        setupWithAnimator(RootAnimator())
        val animatedSetRoot = Options()
        val enter = Mockito.spy(AnimationOptions())
        val exit = Mockito.spy(AnimationOptions())

        Mockito.`when`(enter.hasValue()).thenReturn(false)
        Mockito.`when`(enter.hasAnimation()).thenReturn(false)

        Mockito.`when`(exit.hasValue()).thenReturn(true)
        Mockito.`when`(exit.hasAnimation()).thenReturn(true)

        animatedSetRoot.animations.setRoot = createEnterExitTransitionAnim(enter, exit)
        val spy = Mockito.spy(root)
        val spy2 = Mockito.spy(root2)
        Mockito.`when`(spy.resolveCurrentOptions(defaultOptions)).thenReturn(animatedSetRoot)
        val listener = Mockito.spy(CommandListenerAdapter())
        uut.setRoot(spy, spy2, defaultOptions, listener, reactInstanceManager)
        Mockito.verify(enter, never()).getAnimation(spy.view)
        Mockito.verify(exit).getAnimation(spy2.view)
    }


    @Test
    fun setRoot_playEnterExitAnimOnBothViews() {
        setupWithAnimator(RootAnimator())
        val animatedSetRoot = Options()
        val enter = Mockito.spy(AnimationOptions())
        val exit = Mockito.spy(AnimationOptions())

        Mockito.`when`(enter.hasValue()).thenReturn(true)
        Mockito.`when`(enter.hasAnimation()).thenReturn(true)

        Mockito.`when`(exit.hasValue()).thenReturn(true)
        Mockito.`when`(exit.hasAnimation()).thenReturn(true)

        animatedSetRoot.animations.setRoot = createEnterExitTransitionAnim(enter, exit)
        val spy = Mockito.spy(root)
        val spy2 = Mockito.spy(root2)
        Mockito.`when`(spy.resolveCurrentOptions(defaultOptions)).thenReturn(animatedSetRoot)
        val listener = Mockito.spy(CommandListenerAdapter())
        uut.setRoot(spy, spy2, defaultOptions, listener, reactInstanceManager)

        Mockito.verify(enter).getAnimation(spy.view)
        Mockito.verify(exit).getAnimation(spy2.view)
    }



    @Test
    fun setRoot_animates() {
        val animatedSetRoot = Options()
        val enter = Mockito.spy(AnimationOptions())
        Mockito.`when`(enter.hasValue()).thenReturn(true)
        Mockito.`when`(enter.hasAnimation()).thenReturn(true)

        animatedSetRoot.animations.setRoot = createEnterTransitionAnim(enter)
        val spy = Mockito.spy(root)
        Mockito.`when`(spy.resolveCurrentOptions(defaultOptions)).thenReturn(animatedSetRoot)
        val listener = Mockito.spy(CommandListenerAdapter())
        uut.setRoot(spy, null, defaultOptions, listener, reactInstanceManager)
        Mockito.verify(listener).onSuccess(spy.id)
        Mockito.verify(animator).setRoot(eq(spy), eq(null), eq(animatedSetRoot.animations.setRoot), any())
    }

    @Test
    fun setRoot_waitForRenderIsSet() {
        root.options.animations.setRoot.enter.waitForRender = Bool(true)
        val spy = Mockito.spy(root)
        uut.setRoot(spy, null, defaultOptions, CommandListenerAdapter(), reactInstanceManager)
        val captor = ArgumentCaptor.forClass(Bool::class.java)
        Mockito.verify(spy).setWaitForRender(captor.capture())
        Java6Assertions.assertThat(captor.value.get()).isTrue()
    }

    @Test
    fun setRoot_waitForRender() {
        root.options.animations.setRoot.enter.waitForRender = Bool(true)
        val spy = Mockito.spy(root)
        val listener = Mockito.spy(CommandListenerAdapter())
        uut.setRoot(spy, null, defaultOptions, listener, reactInstanceManager)
        Mockito.verify(spy).addOnAppearedListener(any())
        Java6Assertions.assertThat(spy.view.alpha).isZero()
        Mockito.verifyNoInteractions(listener)
        spy.onViewWillAppear()
        idleMainLooper()
        Java6Assertions.assertThat(spy.view.alpha).isOne()
        Mockito.verify(listener).onSuccess(spy.id)
    }

    @Test
    fun setRoot_appliesLayoutDirection() {
        val listener = Mockito.spy(CommandListenerAdapter())
        uut.setRoot(root, null, defaultOptions, listener, reactInstanceManager)
        Mockito.verify(layoutDirectionApplier).apply(root, defaultOptions, reactInstanceManager)
    }

    private fun createAnimator(): RootAnimator {
        return object : RootAnimator() {
            override fun setRoot(appearing: ViewController<*>, disappearing: ViewController<*>?, setRoot: TransitionAnimationOptions, onAnimationEnd: () -> Unit) {
                super.setRoot(appearing, disappearing, setRoot, onAnimationEnd)
            }
        }
    }

    private fun createEnterExitTransitionAnim(enter: AnimationOptions, exit: AnimationOptions): TransitionAnimationOptions {
        return TransitionAnimationOptions(enter, exit, SharedElements(), ElementTransitions())
    }

    private fun createEnterTransitionAnim(enter: AnimationOptions): TransitionAnimationOptions {
        return createEnterExitTransitionAnim(enter, AnimationOptions())
    }

    private fun createExitTransitionAnim(exit: AnimationOptions): TransitionAnimationOptions {
        return createEnterExitTransitionAnim(AnimationOptions(), exit)
    }

    private fun setupWithAnimator(rootAnimator: RootAnimator) {
        animator = rootAnimator
        layoutDirectionApplier = Mockito.mock(LayoutDirectionApplier::class.java)
        uut = RootPresenter(animator, layoutDirectionApplier)
        uut.setRootContainer(rootContainer)
        defaultOptions = Options()
    }
}
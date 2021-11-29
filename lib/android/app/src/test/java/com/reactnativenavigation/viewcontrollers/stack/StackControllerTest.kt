package com.reactnativenavigation.viewcontrollers.stack

import android.animation.AnimatorSet
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import org.mockito.kotlin.*
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.TestUtils
import com.reactnativenavigation.mocks.*
import com.reactnativenavigation.mocks.SimpleViewController.SimpleView
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.StackAnimationOptions
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.react.CommandListenerAdapter
import com.reactnativenavigation.react.events.EventEmitter
import com.reactnativenavigation.utils.*
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry
import com.reactnativenavigation.viewcontrollers.parent.ParentController
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarAnimator
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarController
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.BackButtonHelper
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.IconResolver
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.stack.StackBehaviour
import com.reactnativenavigation.views.stack.StackLayout
import com.reactnativenavigation.views.stack.topbar.ScrollDIsabledBehavior
import com.reactnativenavigation.views.stack.topbar.TopBar
import org.assertj.core.api.Java6Assertions.assertThat
import org.assertj.core.api.iterable.Extractor
import org.json.JSONObject
import org.junit.Ignore
import org.junit.Test
import org.robolectric.Robolectric
import org.robolectric.shadows.ShadowLooper
import java.util.*
import kotlin.test.fail

class StackControllerTest : BaseTest() {
    private lateinit var activity: Activity
    private lateinit var childRegistry: ChildControllersRegistry
    private lateinit var uut: StackController
    private lateinit var child1: ViewController<*>
    private lateinit var child1a: ViewController<*>
    private lateinit var child2: ViewController<*>
    private lateinit var child3: ViewController<*>
    private var child3View: SimpleView? = null
    private lateinit var child4: ViewController<*>
    private lateinit var animator: StackAnimator
    private lateinit var topBarAnimator: TopBarAnimator
    private lateinit var topBarController: TopBarController
    private lateinit var presenter: StackPresenter
    private lateinit var backButtonHelper: BackButtonHelper
    private lateinit var eventEmitter: EventEmitter

    override fun beforeEach() {
        super.beforeEach()
        eventEmitter = mock()
        backButtonHelper = spy(BackButtonHelper())
        activity = newActivity()
        SystemUiUtils.saveStatusBarHeight(63)
        animator = spy(StackAnimator(activity))
        childRegistry = ChildControllersRegistry()
        presenter = spy(StackPresenter(
                activity,
                TitleBarReactViewCreatorMock(),
                TopBarBackgroundViewCreatorMock(),
                TitleBarButtonCreatorMock(),
                IconResolver(activity, ImageLoaderMock.mock()),
                TypefaceLoaderMock(),
                RenderChecker(),
                Options()
        )
        )
        createChildren()
        uut = createStack()
        activity.setContentView(uut.view)
    }

    private fun createChildren() {
        child1 = spy(SimpleViewController(activity, childRegistry, "child1", Options()))
        child1a = spy(SimpleViewController(activity, childRegistry, "child1", Options()))
        child2 = spy(SimpleViewController(activity, childRegistry, "child2", Options()))
        child3 = spy(object : SimpleViewController(activity, childRegistry, "child3", Options()) {
            override fun createView(): SimpleView {
                return child3View ?: super.createView()
            }
        })
        child4 = spy(SimpleViewController(activity, childRegistry, "child4", Options()))
    }

    @Test
    fun isAViewController() {
        assertThat(uut).isInstanceOf(ViewController::class.java)
    }

    @Test
    fun childrenMustBeUniqueById() {
        try {
            val uut: StackController = createStack(listOf(child1, child2, child1))
            fail("Stack should not have duplicate ids!")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).contains(child1.id)
        }
    }

    @Test
    fun childrenAreAssignedParent() {
        val uut: StackController = createStack(listOf(child1, child2))
        for (child in uut.childControllers) {
            assertThat(child.parentController == uut).isTrue()
        }
    }

    @Test
    fun constructor_backButtonIsAddedToChild() {
        createStack(listOf(child1, child2, child3))
        assertThat(child2.options.topBar.buttons.back.visible[false]).isTrue()
        assertThat(child3.options.topBar.buttons.back.visible[false]).isTrue()
    }

    @Test
    fun createView_currentChildIsAdded() {
        val uut: StackController = createStack(listOf(child1, child2, child3, child4))
        assertThat(uut.childControllers.size).isEqualTo(4)
        assertThat(uut.view.childCount).isEqualTo(2)
        assertThat(uut.view.getChildAt(0)).isEqualTo(child4.view)
    }

    @Test
    fun createView_topBarScrollIsDisabled() {
        val behavior = (uut.topBar.layoutParams as CoordinatorLayout.LayoutParams).behavior
        assertThat(behavior is ScrollDIsabledBehavior).isTrue()
    }

    @Test
    fun holdsAStackOfViewControllers() {
        assertThat(uut.isEmpty).isTrue()
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        uut.push(child3, CommandListenerAdapter())
        assertThat(uut.peek()).isEqualTo(child3)
        assertContainsOnlyId(child1.id, child2.id, child3.id)
    }

    @Test
    fun isRendered_falseIfStackIsEmpty() {
        assertThat(uut.size()).isZero()
        assertThat(uut.isRendered).isFalse()
    }

    @Test
    fun isRendered() {
        disablePushAnimation(child1)
        uut.push(child1, CommandListenerAdapter())
        assertThat(uut.isRendered).isTrue()
        child1.setWaitForRender(Bool(true))
        assertThat(uut.isRendered).isFalse()
        child1.view.addView(View(activity))
        assertThat(uut.isRendered).isTrue()
        whenever(presenter.isRendered(child1.view)).then { false }
        assertThat(uut.isRendered).isFalse()
    }

    @Test
    fun push() {
        assertThat(uut.isEmpty).isTrue()
        val listener = spy(CommandListenerAdapter())
        uut.push(child1, listener)
        assertContainsOnlyId(child1.id)
        assertThat((child1.view.layoutParams as CoordinatorLayout.LayoutParams).behavior).isInstanceOf(StackBehaviour::class.java)
        verify(listener).onSuccess(child1.id)
    }

    @Test
    fun push_backButtonIsNotAddedIfScreenContainsLeftButton() {
        disablePushAnimation(child1, child2)
        uut.push(child1, CommandListenerAdapter())
        child2.options.topBar.buttons.left = ArrayList(setOf(TitleBarHelper.iconButton("someButton", "icon.png")))
        uut.push(child2, CommandListenerAdapter())
        ShadowLooper.idleMainLooper()
        assertThat(topBarController.leftButtonCount).isOne()
        verify(topBarController.view, never()).setBackButton(any())
    }

    @Test
    fun push_backButtonIsNotAddedIfScreenClearsLeftButton() {
        child1.options.topBar.buttons.left = ArrayList()
        uut.push(child1, CommandListenerAdapter())
        verify(child1, never()).mergeOptions(any())
    }

    @Test
    fun setRoot_pushDuringSetRootAnimationShouldNotCrash() {
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        idleMainLooper()
        uut.setRoot(listOf(child1), CommandListenerAdapter())
        uut.push(child3, CommandListenerAdapter())
        assertThat(uut.currentChild).isEqualTo(child3)
    }

    @Test
    fun push_backButtonAddedBeforeChildViewIsCreated() {
        disablePopAnimation(child1, child2)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        val inOrder = inOrder(backButtonHelper, child2)
        inOrder.verify(backButtonHelper)!!.addToPushedChild(child2)
        inOrder.verify(child2)!!.parentController = uut
        inOrder.verify(child2, atLeastOnce())!!.view // creates view
    }

    @Test
    fun push_waitForRender() {
        disablePushAnimation(child1)
        uut.push(child1, CommandListenerAdapter())
        assertThat(child1.view.parent).isEqualTo(uut.view)
        child2.options.animations.push.waitForRender = Bool(true)
        uut.push(child2, CommandListenerAdapter())

        // Both children are attached
        assertThat(child1.view.parent).isEqualTo(uut.view)
        assertThat(child2.view.parent).isEqualTo(uut.view)
        assertThat(child2.isViewShown).isFalse()
        verify(child2, never()).onViewWillAppear()
        child2.view.addView(View(activity))
        ShadowLooper.idleMainLooper()
        verify(child2).onViewWillAppear()
        assertThat(child2.isViewShown).isTrue()
        animator.endPushAnimation(child2)
        assertThat(child1.view.parent).isNull()
    }

    @Test
    fun push_backPressedDuringPushAnimationDestroysPushedScreenImmediately() {
        backPressedDuringPushAnimation(false)
    }

    @Test
    @Ignore
    fun push_backPressedDuringPushAnimationDestroysPushedScreenImmediatelyWaitForRender() {
        backPressedDuringPushAnimation(true)
    }

    private fun backPressedDuringPushAnimation(waitForRender: Boolean) {
        disablePushAnimation(child1)
        uut.push(child1, CommandListenerAdapter())
        val pushListener = spy(CommandListenerAdapter())
        child2.options.animations.push.waitForRender = Bool(waitForRender)
        uut.push(child2, pushListener)
        // both children are attached
        assertThat(child1.view.parent).isEqualTo(uut.view)
        assertThat(child2.view.parent).isEqualTo(uut.view)
        val backListener = spy(CommandListenerAdapter())
        uut.handleBack(backListener)
        assertThat(uut.size()).isOne()
        assertThat(child1.view.parent).isEqualTo(uut.view)
        assertThat(child2.isDestroyed).isTrue()
        val inOrder = inOrder(pushListener, backListener)
        inOrder.verify(pushListener).onSuccess(any())
        inOrder.verify(backListener).onSuccess(any())
    }

    @Test
    fun push_rejectIfStackContainsChildWithId() {
        disablePushAnimation(child1)
        uut.push(child1, CommandListenerAdapter())
        assertThat(uut.size()).isEqualTo(1)
        val listener = spy(CommandListenerAdapter())
        uut.push(child1a, listener)
        verify(listener).onError(any())
        assertThat(uut.size()).isEqualTo(1)
    }

    @Test
    fun push_onViewDidAppearInvokedOnPushedScreen() {
        disablePushAnimation(child1, child2)
        uut.push(child1, CommandListenerAdapter()) // Initialize stack with a child
        uut.push(child2, CommandListenerAdapter())
        idleMainLooper()
        verify(child2).onViewDidAppear()
    }

    @Test
    fun `push - should make push when stack is not yet created`() {
        val child11 = spy(child1)
        val child21 = spy(child2)
        disablePushAnimation(child11, child21)
        uut.view = null
        uut.push(child11, mock())
        assertThat(uut.size()).isEqualTo(1)
        uut.push(child21, mock())
        assertThat(uut.size()).isEqualTo(2)
        verify(child11, never()).view
        verify(child21, never()).view
    }

    @Test
    fun animateSetRoot() {
        disablePushAnimation(child1, child2, child3)
        assertThat(uut.isEmpty).isTrue()
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        uut.setRoot(listOf(child3), object : CommandListenerAdapter() {
            override fun onSuccess(childId: String) {
                assertContainsOnlyId(child3.id)
            }
        })
    }

    @Test
    fun setRoot_singleChild() {
        activity.setContentView(uut.view)
        disablePushAnimation(child1, child2, child3)
        assertThat(uut.isEmpty).isTrue()
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        ShadowLooper.idleMainLooper()
        assertThat(uut.topBar.navigationIcon).isNotNull()
        uut.setRoot(listOf(child3), object : CommandListenerAdapter() {
            override fun onSuccess(childId: String) {
                assertContainsOnlyId(child3.id)
                ShadowLooper.idleMainLooper()
                assertThat(uut.topBar.navigationIcon).isNull()
            }
        })
    }

    @Test
    fun setRoot_multipleChildren() {
        Robolectric.getForegroundThreadScheduler().pause()
        activity.setContentView(uut.view)
        disablePushAnimation(child1, child2, child3, child4)
        disablePopAnimation(child4)
        assertThat(uut.isEmpty).isTrue()
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        ShadowLooper.idleMainLooper()
        assertThat(uut.topBar.navigationIcon).isNotNull()
        uut.setRoot(listOf(child3, child4), object : CommandListenerAdapter() {
            override fun onSuccess(childId: String) {
                assertContainsOnlyId(child3.id, child4.id)
                assertThat(child4.isViewShown).isTrue()
                assertThat(child3.isViewShown).isFalse()
                assertThat(uut.currentChild).isEqualTo(child4)
                uut.pop(Options.EMPTY, CommandListenerAdapter())
                ShadowLooper.idleMainLooper()
                assertThat(uut.topBar.leftButtonBar.navigationIcon).isNull()
                assertThat(uut.currentChild).isEqualTo(child3)
            }
        })
    }

    @Test
    fun setRoot_backButtonIsAddedToAllChildren() {
        Robolectric.getForegroundThreadScheduler().pause()
        activity.setContentView(uut.view)
        disablePushAnimation(child1, child2)
        uut.setRoot(listOf(child1, child2), CommandListenerAdapter())
        assertThat(child1.options.topBar.buttons.back.visible[false]).isFalse()
        assertThat(child2.options.topBar.buttons.back.visible[false]).isTrue()
    }

    @Test
    fun setRoot_doesNotCrashWhenCalledInQuickSuccession() {
        disablePushAnimation(child1)
        uut.setRoot(listOf(child1), CommandListenerAdapter())
        uut.setRoot(listOf(child2), CommandListenerAdapter())
        uut.setRoot(listOf(child3), CommandListenerAdapter())
        animator.endPushAnimation(child2)
        animator.endPushAnimation(child3)
        assertContainsOnlyId(child3.id)
    }

    @Test
    fun setRoot_doesNotCrashWhenCalledWithSameId() {
        disablePushAnimation(child1, child1a)
        uut.setRoot(listOf(child1), CommandListenerAdapter())
        uut.setRoot(listOf(child1a), CommandListenerAdapter())
        assertContainsOnlyId(child1a.id)
    }

    @Test
    fun setRoot_topScreenIsStartedThenTheRest() {
        disablePushAnimation(child1, child2, child3)
        child3View = spy(SimpleView(activity))
        uut.setRoot(listOf(child1, child2, child3), CommandListenerAdapter())
        ShadowLooper.idleMainLooper()
        val inOrder = inOrder(child3View!!, child2, child1)
        inOrder.verify(child3View)!!.start()
        inOrder.verify(child2)!!.start()
        inOrder.verify(child1)!!.start()
    }

    @Test
    fun setRoot_onViewDidAppearIsInvokedOnAppearingChild() {
        disablePushAnimation(child1)
        uut.setRoot(listOf(child1), CommandListenerAdapter())
        verify(child1).onViewDidAppear()
    }

    @Test
    fun `setRoot - should change stack children, no view creation, when stack is not yet created`() {
        val spyChild = spy(child1)
        val spyChild2 = spy(child2)
        uut.view = null

        uut.setRoot(listOf(spyChild), CommandListenerAdapter())
        verify(spyChild, never()).view
        verify(spyChild).parentController = uut
        assertThat(uut.size()).isEqualTo(1)

        val listOf = listOf(spyChild, spyChild2)
        uut.setRoot(listOf, CommandListenerAdapter())
        verify(spyChild, never()).view
        verify(spyChild2, never()).view
        verify(spyChild2).parentController = uut

        assertThat(uut.size()).isEqualTo(2)
        assertThat(uut.childControllers).isEqualTo(listOf)
    }

    @Test
    fun setRoot_onViewDidAppearIsInvokedBeforePreviousRootIsDestroyed() {
        disablePushAnimation(child1, child2, child3)
        uut.push(child1, CommandListenerAdapter())
        uut.setRoot(listOf(child2, child3), CommandListenerAdapter())
        ShadowLooper.idleMainLooper()
        val inOrder = inOrder(child2, child3, child1)
        inOrder.verify(child3)!!.onViewDidAppear()
        inOrder.verify(child1)!!.onViewDisappear()
        verify(child2, never()).onViewDidAppear()
    }

    @Test
    fun pop() {
        disablePushAnimation(child1, child2)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, object : CommandListenerAdapter() {
            override fun onSuccess(childId: String) {
                assertContainsOnlyId(child2.id, child1.id)
                uut.pop(Options.EMPTY, CommandListenerAdapter())
                assertContainsOnlyId(child1.id)
            }
        })
    }

    @Test
    fun `pop - should make pop when stack is not yet created`() {
        val child11 = spy(child1)
        val child21 = spy(child2)
        disablePushAnimation(child11, child21)
        uut.view = null
        uut.push(child11, mock())
        uut.push(child21, mock())
        uut.pop(Options.EMPTY, mock())
        assertThat(uut.size()).isEqualTo(1)
        verify(child11, never()).view
        verify(child21, never()).view
    }
    @Test
    fun pop_screenCurrentlyBeingPushedIsPopped() {
        disablePushAnimation(child1, child2)
        uut.push(child1, mock())
        uut.push(child2, mock())
        uut.push(child3, mock())
        uut.pop(Options.EMPTY, mock())
        assertThat(uut.size()).isEqualTo(2)
        assertContainsOnlyId(child1.id, child2.id)
    }

    @Test
    fun pop_appliesOptionsAfterPop() {
        disablePushAnimation(child1, child2)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())

        uut.pop(Options.EMPTY, CommandListenerAdapter())
        dispatchOnGlobalLayout(child1.view)
        verify(presenter).applyChildOptions(any(), eq(uut), eq(child1))
    }

    @Test
    fun pop_popEventIsEmitted() {
        disablePushAnimation(child1, child2)
        disablePopAnimation(child2)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        uut.pop(Options.EMPTY, CommandListenerAdapter())
        verify(eventEmitter).emitScreenPoppedEvent(child2.id)
    }

    @Test
    fun popToRoot_popEventIsEmitted() {
        disablePushAnimation(child1, child2, child3)
        disablePopAnimation(child2, child3)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        uut.push(child3, CommandListenerAdapter())
        uut.pop(Options.EMPTY, CommandListenerAdapter())
        verify(eventEmitter).emitScreenPoppedEvent(child3.id)
        verifyNoMoreInteractions(eventEmitter)
    }

    @Test
    fun stackOperations() {
        assertThat(uut.peek()).isNull()
        assertThat(uut.size()).isZero()
        assertThat(uut.isEmpty).isTrue()
        uut.push(child1, CommandListenerAdapter())
        assertThat(uut.peek()).isEqualTo(child1)
        assertThat(uut.size()).isEqualTo(1)
        assertThat(uut.isEmpty).isFalse()
    }

    @Test
    fun onChildDestroyed() {
        uut.onChildDestroyed(child2)
        verify(presenter).onChildDestroyed(child2)
    }

    @Test
    fun handleBack_PopsUnlessSingleChild() {
        assertThat(uut.isEmpty).isTrue()
        assertThat(uut.handleBack(CommandListenerAdapter())).isFalse()
        uut.push(child1, CommandListenerAdapter())
        assertThat(uut.size()).isEqualTo(1)
        assertThat(uut.handleBack(CommandListenerAdapter())).isFalse()
        uut.push(child2, object : CommandListenerAdapter() {
            override fun onSuccess(childId: String) {
                assertThat(uut.size()).isEqualTo(2)
                assertThat(uut.handleBack(CommandListenerAdapter())).isTrue()
                assertThat(uut.size()).isEqualTo(1)
                assertThat(uut.handleBack(CommandListenerAdapter())).isFalse()
            }
        })
    }

    @Test
    fun pop_doesNothingWhenZeroOrOneChild() {
        assertThat(uut.isEmpty).isTrue()
        uut.pop(Options.EMPTY, CommandListenerAdapter())
        assertThat(uut.isEmpty).isTrue()
        uut.push(child1, CommandListenerAdapter())
        uut.pop(Options.EMPTY, CommandListenerAdapter())
        assertContainsOnlyId(child1.id)
    }

    @Test
    fun pop_animationOptionsAreMergedCorrectlyToDisappearingChild() {
        disablePushAnimation(child1, child2)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())

        val mergeOptions = Options()
        mergeOptions.animations.pop.content = createEnterExitAnimation(duration = 123)
        uut.pop(mergeOptions, CommandListenerAdapter())

        val captor = argumentCaptor<Options>()
        verify(animator).pop(any(), any(), captor.capture(), any(), any())
        val animator = captor.firstValue.animations.pop.content.exit
                .getAnimation(mockView(activity))
        assertThat((animator as AnimatorSet).childAnimations.first().duration).isEqualTo(123)
    }

    @Test
    fun pop_animationOptionsAreMergedCorrectlyToDisappearingChildWithDefaultOptions() {
        disablePushAnimation(child1, child2)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())

        val defaultOptions = Options()
        defaultOptions.animations.pop.content = createEnterExitAnimation(duration = 123)
        uut.setDefaultOptions(defaultOptions)
        uut.pop(Options.EMPTY, CommandListenerAdapter())
        val captor = argumentCaptor<Options>()
        verify(animator).pop(any(), any(), captor.capture(), any(), any())
        val animator = captor.firstValue.animations.pop.content.exit
                .getAnimation(mockView(activity))
        assertThat((animator as AnimatorSet).childAnimations.first().duration).isEqualTo(123)
    }

    @Test
    fun canPopWhenSizeIsMoreThanOne() {
        assertThat(uut.isEmpty).isTrue()
        assertThat(uut.canPop()).isFalse()
        uut.push(child1, CommandListenerAdapter())
        assertContainsOnlyId(child1.id)
        assertThat(uut.canPop()).isFalse()
        uut.push(child2, CommandListenerAdapter())
        assertContainsOnlyId(child1.id, child2.id)
        assertThat(uut.canPop()).isTrue()
    }

    @Test
    fun push_addsToViewTree() {
        assertNotChildOf(uut.view, child1.view)
        uut.push(child1, CommandListenerAdapter())
        assertIsChild(uut.view, child1.view)
    }

    @Test
    fun push_removesPreviousFromTree() {
        disablePushAnimation(child1, child2)
        assertNotChildOf(uut.view, child1.view)
        uut.push(child1, CommandListenerAdapter())
        assertIsChild(uut.view, child1.view)
        idleMainLooper()
        uut.push(child2, CommandListenerAdapter())
        idleMainLooper()
        assertIsChild(uut.view, child2)
        assertNotChildOf(uut.view, child1)
    }

    @Test
    fun push_assignsRefToSelfOnPushedController() {
        assertThat(child1.parentController).isNull()
        uut.push(child1, CommandListenerAdapter())
        assertThat(child1.parentController).isEqualTo(uut)
        val anotherNavController = createStack("another")
        anotherNavController.ensureViewIsCreated()
        anotherNavController.push(child2, CommandListenerAdapter())
        assertThat(child2.parentController).isEqualTo(anotherNavController)
    }

    @Test
    fun push_doesNotAnimateTopBarIfScreenIsPushedWithoutAnimation() {
        uut.ensureViewIsCreated()
        child1.ensureViewIsCreated()
        child1.options.topBar.visible = Bool(false)
        child1.options.topBar.animate = Bool(false)
        disablePushAnimation(child1, child2)

        uut.push(child1, CommandListenerAdapter())
        child1.onViewWillAppear()
        assertThat(uut.topBar.visibility).isEqualTo(View.GONE)

        uut.push(child2, CommandListenerAdapter())
        child2.onViewWillAppear()
        verify(topBarController, never()).showAnimate(any(), any())
        assertThat(uut.topBar.visibility).isEqualTo(View.VISIBLE)
        verify(topBarController.view).resetViewProperties()
    }

    @Test
    fun push_animatesAndClearsPreviousAnimationValues() {
        uut.ensureViewIsCreated()
        child1.options.topBar.visible = Bool(false)
        child1.options.topBar.animate = Bool(false)
        child1.options.animations.push.enabled = Bool(false)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        verify(topBarController.view).resetViewProperties()
    }

    @Test
    fun pop_replacesViewWithPrevious() {
        disablePushAnimation(child1, child2)
        disablePopAnimation(child2)
        val child2View: View = child2.view
        val child1View: View = child1.view
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        idleMainLooper()
        assertIsChild(uut.view, child2View)
        assertNotChildOf(uut.view, child1View)
        uut.pop(Options.EMPTY, CommandListenerAdapter())
        assertNotChildOf(uut.view, child2View)
        assertIsChild(uut.view, child1View)
    }

    @Test
    fun popTo_PopsTopUntilControllerIsNewTop() {
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        uut.push(child3, object : CommandListenerAdapter() {
            override fun onSuccess(childId: String) {
                assertThat(uut.size()).isEqualTo(3)
                assertThat(uut.peek()).isEqualTo(child3)
                uut.popTo(child1, Options.EMPTY, CommandListenerAdapter())
                assertThat(uut.size()).isEqualTo(1)
                assertThat(uut.peek()).isEqualTo(child1)
            }
        })
    }

    @Test
    fun popTo_optionsAreMergedOnTopChild() {
        disablePushAnimation(child1, child2)
        uut.push(child1, CommandListenerAdapter())
        val mergeOptions = Options()
        uut.popTo(child2, mergeOptions, CommandListenerAdapter())
        uut.popTo(child1, mergeOptions, CommandListenerAdapter())
        verify(child1, never()).mergeOptions(mergeOptions)
        uut.push(child2, CommandListenerAdapter())
        uut.popTo(child1, mergeOptions, CommandListenerAdapter())
        verify(child2).mergeOptions(mergeOptions)
    }

    @Test
    fun popTo_NotAChildOfThisStack_DoesNothing() {
        uut.push(child1, CommandListenerAdapter())
        uut.push(child3, CommandListenerAdapter())
        assertThat(uut.size()).isEqualTo(2)
        uut.popTo(child2, Options.EMPTY, CommandListenerAdapter())
        assertThat(uut.size()).isEqualTo(2)
    }

    @Test
    fun popTo_animatesTopController() {
        disablePushAnimation(child1, child2, child3, child4)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        uut.push(child3, CommandListenerAdapter())
        uut.push(child4, CommandListenerAdapter())
        idleMainLooper()
        uut.popTo(child2, Options.EMPTY, CommandListenerAdapter())
        verify(animator, never()).pop(any(), eq(child1), any(), any(), any())
        verify(animator, never()).pop(any(), eq(child2), any(), any(), any())
        verify(animator, never()).pop(any(), eq(child3), any(), any(), any())
        verify(animator).pop(any(), eq(child4), any(), any(), any())
    }

    @Test
    fun popTo_pushAnimationIsCancelled() {
        disablePushAnimation(child1, child2)
        uut.push(child1, mock())
        uut.push(child2, mock())
        uut.push(child3, mock())
        idleMainLooper()
        uut.popTo(child1, Options.EMPTY, mock())
        animator.endPushAnimation(child3)
        assertContainsOnlyId(child1.id)
    }

    @Test
    fun popToRoot_PopsEverythingAboveFirstController() {
        child1.options.animations.push.enabled = Bool(false)
        child2.options.animations.push.enabled = Bool(false)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        uut.push(child3, object : CommandListenerAdapter() {
            override fun onSuccess(childId: String) {
                assertThat(uut.size()).isEqualTo(3)
                assertThat(uut.peek()).isEqualTo(child3)
                uut.popToRoot(Options.EMPTY, object : CommandListenerAdapter() {
                    override fun onSuccess(childId: String) {
                        assertThat(uut.size()).isEqualTo(1)
                        assertThat(uut.peek()).isEqualTo(child1)
                    }
                })
            }
        })
    }

    @Test
    fun popToRoot_onlyTopChildIsAnimated() {
        disablePushAnimation(child1, child2, child3)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        uut.push(child3, CommandListenerAdapter())
        idleMainLooper()
        uut.popToRoot(Options.EMPTY, object : CommandListenerAdapter() {
            override fun onSuccess(childId: String) {
                verify(animator).pop(eq(child1), eq(child3), any(), any(), any())
            }
        })
    }

    @Test
    fun popToRoot_topChildrenAreDestroyed() {
        child1.options.animations.push.enabled = Bool(false)
        child2.options.animations.push.enabled = Bool(false)
        child3.options.animations.push.enabled = Bool(false)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        uut.push(child3, CommandListenerAdapter())
        idleMainLooper()
        uut.popToRoot(Options.EMPTY, object : CommandListenerAdapter() {
            override fun onSuccess(childId: String) {
                verify(child1, never()).destroy()
                verify(child2).destroy()
                verify(child3).destroy()
            }
        })
    }

    @Test
    fun popToRoot_EmptyStackDoesNothing() {
        assertThat(uut.isEmpty).isTrue()
        val listener = spy(CommandListenerAdapter())
        uut.popToRoot(Options.EMPTY, listener)
        assertThat(uut.isEmpty).isTrue()
        verify(listener).onSuccess("")
    }

    @Test
    fun popToRoot_optionsAreMergedOnTopChild() {
        disablePushAnimation(child1, child2)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        val mergeOptions = Options()
        uut.popToRoot(mergeOptions, CommandListenerAdapter())
        verify(child2).mergeOptions(mergeOptions)
        verify(child1, never()).mergeOptions(mergeOptions)
    }

    @Test
    fun popToRoot_screenPushedBeforePopAnimationCompletesIsPopped() {
        disablePushAnimation(child1, child2)
        uut.push(child1, mock())
        uut.push(child2, mock())
        uut.push(child3, mock())
        idleMainLooper()
        uut.popToRoot(Options.EMPTY, mock())
        animator.endPushAnimation(child3)
        assertContainsOnlyId(child1.id)
    }

    @Test
    fun findControllerById_ReturnsSelfOrChildrenById() {
        assertThat(uut.findController("123")).isNull()
        assertThat(uut.findController(uut.id)).isEqualTo(uut)
        uut.push(child1, CommandListenerAdapter())
        assertThat(uut.findController(child1.id)).isEqualTo(child1)
    }

    @Test
    fun findControllerById_Deeply() {
        val stack = createStack("another")
        stack.ensureViewIsCreated()
        stack.push(child2, CommandListenerAdapter())
        uut.push(stack, CommandListenerAdapter())
        assertThat(uut.findController(child2.id)).isEqualTo(child2)
    }

    @Test
    fun pop_callsDestroyOnPoppedChild() {
        child2 = spy(child2)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())

        verify(child2, never()).destroy()
        uut.pop(Options.EMPTY, CommandListenerAdapter())
        verify(child2).destroy()
    }

    @Test
    fun pop_callWillDisappear() {
        disablePushAnimation(child1, child2)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        uut.pop(Options.EMPTY, CommandListenerAdapter())
        verify(child2).onViewWillDisappear()
    }

    @Test
    fun pop_callDidAppear() {
        disablePushAnimation(child1, child2)
        disablePopAnimation(child2)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        uut.pop(Options.EMPTY, CommandListenerAdapter())
        verify(child1).onViewDidAppear()
    }

    @Test
    fun pop_animatesTopBar() {
        uut.ensureViewIsCreated()
        disablePushAnimation(child1, child2)
        child1.options.topBar.visible = Bool(false)

        uut.push(child1, CommandListenerAdapter())
        child1.onViewWillAppear()
        assertThat(uut.topBar.visibility).isEqualTo(View.GONE)

        uut.push(child2, CommandListenerAdapter())
        child2.onViewWillAppear()
        assertThat(uut.topBar.visibility).isEqualTo(View.VISIBLE)

        uut.pop(Options.EMPTY, CommandListenerAdapter())
        assertThat(topBarAnimator.isAnimatingHide()).isTrue()
    }

    @Test
    fun pop_doesNotAnimateTopBarIfScreenIsPushedWithoutAnimation() {
        disablePushAnimation(child1)
        child1.options.topBar.visible = Bool(false)
        child1.options.topBar.animate = Bool(false)

        assertThat(uut.topBar.visibility).isEqualTo(View.VISIBLE)
        uut.push(child1, CommandListenerAdapter())
        child1.onViewWillAppear()
        assertThat(topBarAnimator.isAnimatingHide()).isFalse()
        assertThat(uut.topBar.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun popTo_CallsDestroyOnPoppedChild() {
        child1 = spy(child1)
        child2 = spy(child2)
        child3 = spy(child3)
        uut.push(child1, CommandListenerAdapter())
        uut.push(child2, CommandListenerAdapter())
        uut.push(child3, object : CommandListenerAdapter() {
            override fun onSuccess(childId: String) {
                verify(child2, never()).destroy()
                verify(child3, never()).destroy()
                uut.popTo(child1, Options.EMPTY, object : CommandListenerAdapter() {
                    override fun onSuccess(childId: String) {
                        verify(child2).destroy()
                        verify(child3).destroy()
                    }
                })
            }
        })
    }

    @Test
    fun stackCanBePushed() {
        uut.view.removeFromParent()
        val parent = createStack("someStack")
        parent.ensureViewIsCreated()
        parent.push(uut, CommandListenerAdapter())
        uut.onViewWillAppear()
        assertThat(parent.view.getChildAt(0)).isEqualTo(uut.view)
    }

    @Test
    fun applyOptions_applyOnlyOnFirstStack() {
        uut.view.removeFromParent()
        val parent = spy(createStack("someStack"))
        parent.ensureViewIsCreated()
        parent.push(uut, CommandListenerAdapter())
        val childOptions = Options()
        childOptions.topBar.title.text = Text("Something")
        child1.options = childOptions
        uut.push(child1, CommandListenerAdapter())
        child1.ensureViewIsCreated()
        child1.onViewWillAppear()
        val optionsCaptor = argumentCaptor<Options>()
        val viewCaptor = argumentCaptor<ViewController<*>>()
        verify(parent).applyChildOptions(optionsCaptor.capture(), viewCaptor.capture())
        assertThat(optionsCaptor.firstValue.topBar.title.text.hasValue()).isFalse()
    }

    @Test
    fun applyOptions_topTabsAreNotVisibleIfNoTabsAreDefined() {
        uut.ensureViewIsCreated()
        uut.push(child1, CommandListenerAdapter())
        child1.ensureViewIsCreated()
        child1.onViewWillAppear()
        assertThat(ViewHelper.isVisible(uut.topBar.topTabs)).isFalse()
    }

    @Test
    fun buttonPressInvokedOnCurrentStack() {
        uut.ensureViewIsCreated()
        uut.push(child1, CommandListenerAdapter())
        uut.sendOnNavigationButtonPressed("btn1")
        verify(child1).sendOnNavigationButtonPressed("btn1")
    }

    @Test
    fun mergeChildOptions_updatesViewWithNewOptions() {
        val uut = spy(TestUtils.newStackController(activity)
                .setId("stack")
                .build())
        val optionsToMerge = Options()
        val vc = mock<ViewController<*>>()
        uut.mergeChildOptions(optionsToMerge, vc)
        verify(uut).mergeChildOptions(optionsToMerge, vc)
    }

    @Test
    fun mergeOptions_doesNotMergeOptionsIfViewIsNotVisible() {
        uut.mergeOptions(Options.EMPTY)
        verify(presenter, never()).mergeOptions(any(), any(), any())
    }

    @Test
    fun mergeChildOptions_updatesParentControllerWithNewOptions() {
        val uut = TestUtils.newStackController(activity)
                .setId("stack")
                .build()
        val parentController = mock<ParentController<*>>()
        uut.parentController = parentController
        uut.ensureViewIsCreated()
        val optionsToMerge = Options()
        optionsToMerge.topBar.testId = Text("topBarID")
        optionsToMerge.bottomTabsOptions.testId = Text("bottomTabsID")
        val vc = mock<ViewController<*>>()
        uut.mergeChildOptions(optionsToMerge, vc)
        val captor = argumentCaptor<Options>()
        verify(parentController).mergeChildOptions(captor.capture(), eq(vc))
        assertThat(captor.firstValue.topBar.testId.hasValue()).isFalse()
        assertThat(captor.firstValue.bottomTabsOptions.testId.get()).isEqualTo(optionsToMerge.bottomTabsOptions.testId.get())
    }

    @Test
    fun mergeChildOptions_StackRelatedOptionsAreCleared() {
        uut.ensureViewIsCreated()
        val parentController = mock<ParentController<*>>()
        uut.parentController = parentController
        val options = Options()
        options.animations.push = StackAnimationOptions(JSONObject())
        options.topBar.testId = Text("id")
        options.fabOptions.id = Text("fabId")
        val vc = mock<ViewController<*>>()
        assertThat(options.fabOptions.hasValue()).isTrue()
        uut.mergeChildOptions(options, vc)
        val captor = argumentCaptor<Options>()
        verify(parentController).mergeChildOptions(captor.capture(), eq(vc))
        assertThat(captor.firstValue.animations.push.hasEnterValue()).isFalse()
        assertThat(captor.firstValue.topBar.testId.hasValue()).isFalse()
        assertThat(captor.firstValue.fabOptions.hasValue()).isFalse()
    }

    @Test
    fun applyChildOptions_appliesResolvedOptions() {
        disablePushAnimation(child1, child2)
        uut.push(child1, CommandListenerAdapter())
        assertThat(uut.topBar.title).isNullOrEmpty()
        val uutOptions = Options()
        uutOptions.topBar.title.text = Text("UUT")
        uut.mergeOptions(uutOptions)
        assertThat(uut.topBar.title).isEqualTo("UUT")
        uut.push(child2, CommandListenerAdapter())
        assertThat(uut.topBar.title).isEqualTo("UUT")
    }

    @Test
    fun mergeChildOptions_presenterDoesNotApplyOptionsIfViewIsNotShown() {
        val vc = mock<ViewController<*>>()
        whenever(vc.isViewShown).then { true }
        uut.mergeChildOptions(Options(), vc)
        verify(presenter, never()).mergeChildOptions(any(), any(), any(), any())
    }

    @Test
    fun mergeChildOptions_presenterMergesOptionsOnlyForCurrentChild() {
        val vc = mock<ViewController<*>>()
        whenever(vc.isViewShown).then { true }
        uut.mergeChildOptions(Options(), vc)
        verify(presenter, never()).mergeChildOptions(any(), any(), any(), any())
    }

    @Test
    fun resolvedOptionsAreAppliedWhenStackIsAttachedToParentAndNotVisible() {
        val parent = FrameLayout(activity)
        activity.setContentView(parent)

        val child = SimpleViewController(activity, childRegistry, "child1", Options())
        val stack = createStack(Collections.singletonList(child))
        stack.view.visibility = View.INVISIBLE

        parent.addView(stack.view)

        ShadowLooper.idleMainLooper()
        verify(presenter).applyChildOptions(any(), eq(stack), eq(child))
    }

    @Test
    fun onAttachToParent_doesNotCrashWhenCalledAfterDestroy() {
        Robolectric.getForegroundThreadScheduler().pause()
        val spy = spy(createStack())
        val view = spy.view
        spy.push(child1, CommandListenerAdapter())
        activity.setContentView(view)
        child1.destroy()
        ShadowLooper.idleMainLooper()
        verify(spy).onAttachToParent()
    }

    @Test
    fun onDependentViewChanged_delegatesToPresenter() {
        val parent = mock<CoordinatorLayout>()
        uut.push(child1, CommandListenerAdapter())
        assertThat(uut.onDependentViewChanged(parent, child1.view, mock<TopBar>())).isFalse()
        verify(presenter).applyTopInsets(eq(uut), eq(child1))
    }

    @Test
    fun onDependentViewChanged_TopBarIsRenderedBellowStatusBar() {
        disablePushAnimation(child1)
        uut.push(child1, CommandListenerAdapter())
        ShadowLooper.idleMainLooper()
        assertThat(ViewUtils.topMargin(uut.topBar)).isEqualTo(SystemUiUtils.getStatusBarHeight(activity))
    }

    @Test
    fun onDependentViewChanged_TopBarIsRenderedBehindStatusBar() {
        uut.initialOptions.statusBar.visible = Bool(false)
        disablePushAnimation(child1)
        uut.push(child1, CommandListenerAdapter())
        ShadowLooper.idleMainLooper()
        assertThat(uut.topBar.y).isEqualTo(0f)
    }

    @Test
    fun topInset() {
        disablePushAnimation(child1)
        uut.push(child1, CommandListenerAdapter())
        assertThat(uut.getTopInset(child1)).isEqualTo(topBarController.height)
        val options = Options()
        options.topBar.drawBehind = Bool(true)
        child1.mergeOptions(options)
        assertThat(uut.getTopInset(child1)).isEqualTo(0)
    }

    @Test
    fun topInset_defaultOptionsAreTakenIntoAccount() {
        assertThat(uut.getTopInset(child1)).isEqualTo(topBarController.height)
        val defaultOptions = Options()
        defaultOptions.topBar.drawBehind = Bool(true)
        uut.setDefaultOptions(defaultOptions)
        assertThat(uut.getTopInset(child1)).isZero()
    }

    private fun assertContainsOnlyId(vararg ids: String) {
        assertThat(uut.size()).isEqualTo(ids.size)
        assertThat(uut.childControllers).extracting(Extractor { obj: ViewController<*> -> obj.id } as Extractor<ViewController<*>, String>)
                .containsOnly(*ids)
    }

    private fun createStack(): StackController {
        return createStackBuilder("stack", ArrayList()).build()
    }

    private fun createStack(id: String): StackController {
        return createStackBuilder(id, ArrayList()).build()
    }

    private fun createStack(children: List<ViewController<*>>): StackController {
        return createStackBuilder("stack", children).build()
    }

    private fun createStackBuilder(id: String, children: List<ViewController<*>>): StackControllerBuilder {
        createTopBarController()
        return TestUtils.newStackController(activity)
                .setEventEmitter(eventEmitter)
                .setChildren(children)
                .setId(id)
                .setTopBarController(topBarController)
                .setChildRegistry(childRegistry)
                .setAnimator(animator)
                .setStackPresenter(presenter)
                .setBackButtonHelper(backButtonHelper)
    }

    private fun createTopBarController() {
        topBarAnimator = TopBarAnimator()
        topBarController = spy(object : TopBarController(topBarAnimator) {
            override fun createTopBar(context: Context, stackLayout: StackLayout): TopBar {
                val spy = spy(super.createTopBar(context, stackLayout))
                spy.layout(0, 0, 1000, UiUtils.getTopBarHeight(activity))
                return spy
            }
        })
    }
}
package com.reactnativenavigation.viewcontrollers.bottomtabs

import android.animation.AnimatorSet
import com.nhaarman.mockitokotlin2.*
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.mocks.SimpleViewController
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.params.Number
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.bottomtabs.BottomTabs
import com.reactnativenavigation.views.bottomtabs.BottomTabsContainer
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class BottomTabsPresenterTest : BaseTest() {
    private lateinit var tabs: List<ViewController<*>>
    private lateinit var uut: BottomTabsPresenter
    private lateinit var bottomTabs: BottomTabs
    private lateinit var bottomTabsContainer: BottomTabsContainer
    private lateinit var animator: BottomTabsAnimator
    private lateinit var tabSelector: TabSelector

    override fun beforeEach() {
        val activity = newActivity()
        val childRegistry = ChildControllersRegistry()
        val child1 = spy(SimpleViewController(activity, childRegistry, "child1", Options()))
        val child2 = spy(SimpleViewController(activity, childRegistry, "child2", Options()))
        tabs = listOf(child1, child2)
        bottomTabsContainer = mock()
        bottomTabs = mock()
        whenever(bottomTabsContainer.bottomTabs).thenReturn(bottomTabs)
        animator = spy(BottomTabsAnimator(bottomTabs))
        uut = BottomTabsPresenter(tabs, Options(), animator)
        tabSelector = mock()
        uut.bindView(bottomTabsContainer, tabSelector)
    }

    @Test
    fun mergeChildOptions_onlyDeclaredOptionsAreApplied() { // default options are not applied on merge
        val defaultOptions = Options()
        defaultOptions.bottomTabsOptions.visible = Bool(false)
        uut.setDefaultOptions(defaultOptions)
        val options = Options()
        options.bottomTabsOptions.backgroundColor = Colour(10)
        uut.mergeChildOptions(options, tabs[0])
        verify(bottomTabs).setBackgroundColor(options.bottomTabsOptions.backgroundColor.get())
        verifyNoMoreInteractions(bottomTabs)
    }

    @Test
    fun mergeChildOptions_visibilityIsAppliedOnlyIfChildIsShown() {
        assertThat(tabs[0].isViewShown).isFalse()
        assertThat(bottomTabs.isHidden).isFalse()

        val options = Options()
        options.bottomTabsOptions.visible = Bool(false)
        uut.mergeChildOptions(options, tabs[0])
        verify(animator, never()).hide()

        whenever(tabs[0].isViewShown).thenAnswer { true }
        uut.mergeChildOptions(options, tabs[0])
        verify(animator).hide(any(), any(), anyOrNull())
    }

    @Test
    fun applyChildOptions_currentTabIndexIsConsumedAfterApply() {
        val defaultOptions = Options()
        defaultOptions.bottomTabsOptions.currentTabIndex = Number(1)
        uut.setDefaultOptions(defaultOptions)
        uut.applyChildOptions(Options.EMPTY, tabs[0])
        verify(tabSelector).selectTab(1)
        uut.applyChildOptions(Options.EMPTY, tabs[0])
        verifyNoMoreInteractions(tabSelector)
    }

    @Test
    fun applyChildOptions_currentTabIdIsConsumedAfterApply() {
        val defaultOptions = Options()
        defaultOptions.bottomTabsOptions.currentTabId = Text(tabs[1].id)
        uut.setDefaultOptions(defaultOptions)
        uut.applyChildOptions(Options.EMPTY, tabs[0])
        verify(tabSelector).selectTab(1)
        uut.applyChildOptions(Options.EMPTY, tabs[0])
        verifyNoMoreInteractions(tabSelector)
    }

    @Test
    fun getPushAnimation_returnsNullIfAnimateFalse() {
        val appearing = Options()
        appearing.bottomTabsOptions.animate = Bool(false)
        assertThat(uut.getPushAnimation(appearing)).isNull()
    }

    @Test
    fun getPushAnimation_delegatesToAnimator() {
        val someAnimator = AnimatorSet()
        val options = Options.EMPTY
        doReturn(someAnimator).whenever(animator).getPushAnimation(
                options.animations.push.bottomTabs,
                options.bottomTabsOptions.visible,
                0f
        )
        val result = uut.getPushAnimation(options)
        assertThat(result).isEqualTo(someAnimator)
    }

    @Test
    fun getPopAnimation_returnsNullIfAnimateFalse() {
        val appearing = Options()
        val disappearing = Options()
        disappearing.bottomTabsOptions.animate = Bool(false)
        assertThat(uut.getPopAnimation(appearing, disappearing)).isNull()
    }

    @Test
    fun getPopAnimation_delegatesToAnimator() {
        val someAnimator = AnimatorSet()
        val appearing = Options.EMPTY
        val disappearing = Options.EMPTY
        doReturn(someAnimator).whenever(animator).getPopAnimation(
                disappearing.animations.pop.bottomTabs,
                appearing.bottomTabsOptions.visible,
                0f
        )
        val result = uut.getPopAnimation(appearing, disappearing)
        assertThat(result).isEqualTo(someAnimator)
    }

    @Test
    fun getSetStackRootAnimation_returnsNullIfAnimateFalse() {
        val appearing = Options()
        appearing.bottomTabsOptions.animate = Bool(false)
        assertThat(uut.getSetStackRootAnimation(appearing)).isNull()
    }

    @Test
    fun getSetStackRootAnimation_delegatesToAnimator() {
        val someAnimator = AnimatorSet()
        val options = Options.EMPTY
        doReturn(someAnimator).whenever(animator).getSetStackRootAnimation(
                options.animations.setStackRoot.bottomTabs,
                options.bottomTabsOptions.visible,
                0f
        )
        val result = uut.getSetStackRootAnimation(options)
        assertThat(result).isEqualTo(someAnimator)
    }
}
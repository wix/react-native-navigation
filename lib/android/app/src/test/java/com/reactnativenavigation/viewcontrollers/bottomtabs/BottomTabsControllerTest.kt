package com.reactnativenavigation.viewcontrollers.bottomtabs

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation.TitleState
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.TestUtils
import com.reactnativenavigation.mocks.ImageLoaderMock.mock
import com.reactnativenavigation.mocks.Mocks
import com.reactnativenavigation.mocks.SimpleViewController
import com.reactnativenavigation.mocks.TypefaceLoaderMock
import com.reactnativenavigation.options.BottomTabsOptions
import com.reactnativenavigation.options.HwBackBottomTabsBehaviour
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.*
import com.reactnativenavigation.react.CommandListenerAdapter
import com.reactnativenavigation.react.events.EventEmitter
import com.reactnativenavigation.utils.OptionHelper
import com.reactnativenavigation.utils.SystemUiUtils.getStatusBarHeight
import com.reactnativenavigation.utils.SystemUiUtils.saveStatusBarHeight
import com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.BottomTabsAttacher
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry
import com.reactnativenavigation.viewcontrollers.fakes.FakeParentController
import com.reactnativenavigation.viewcontrollers.stack.StackController
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.bottomtabs.BottomTabs
import com.reactnativenavigation.views.bottomtabs.BottomTabsContainer
import com.reactnativenavigation.views.bottomtabs.BottomTabsLayout
import org.assertj.core.api.Java6Assertions
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import java.util.*

class BottomTabsControllerTest : BaseTest() {
    private lateinit var activity: Activity
    private lateinit var bottomTabs: BottomTabs
    private lateinit var bottomTabsContainer: BottomTabsContainer
    private lateinit var uut: BottomTabsController
    private val initialOptions = Options()
    private lateinit var child1: ViewController<*>
    private lateinit var child2: ViewController<*>
    private lateinit var child3: ViewController<*>
    private lateinit var stackChild: ViewController<*>
    private lateinit var child4: StackController
    private lateinit var child5: ViewController<*>
    private val tabOptions = OptionHelper.createBottomTabOptions()
    private val imageLoaderMock = mock()
    private lateinit var eventEmitter: EventEmitter
    private lateinit var childRegistry: ChildControllersRegistry
    private lateinit var tabs: MutableList<ViewController<*>>
    private lateinit var presenter: BottomTabsPresenter
    private lateinit var bottomTabPresenter: BottomTabPresenter
    private lateinit var tabsAttacher: BottomTabsAttacher
    override fun beforeEach() {
        super.beforeEach()
        activity = newActivity()
        childRegistry = ChildControllersRegistry()
        eventEmitter = Mockito.mock(EventEmitter::class.java)
        prepareViewsForTests()
        saveStatusBarHeight(63)
    }

    @Test
    fun createView_checkProperStructure() {
        idleMainLooper()
        Java6Assertions.assertThat(uut.view).isInstanceOf(CoordinatorLayout::class.java)
        Java6Assertions.assertThat(uut.view.getChildAt(uut.view.childCount - 1)).isInstanceOf(
            BottomTabsContainer::class.java
        )
        Java6Assertions.assertThat((uut.bottomTabsContainer.layoutParams as CoordinatorLayout.LayoutParams).gravity)
            .isEqualTo(Gravity.BOTTOM)
    }

    @Test
    fun createView_tabsWithoutIconsAreAccepted() {
        tabOptions.bottomTabOptions.icon = NullText()
        prepareViewsForTests()
        Java6Assertions.assertThat(uut.bottomTabs.itemsCount).isEqualTo(tabs.size)
    }

    @Test
    fun createView_showTitlesWhenAllTabsDontHaveIcons() {
        tabOptions.bottomTabOptions.icon = NullText()
        Java6Assertions.assertThat(tabOptions.bottomTabsOptions.titleDisplayMode.hasValue()).isFalse
        prepareViewsForTests()
        presenter.applyOptions(Options.EMPTY)
        Java6Assertions.assertThat(bottomTabsContainer.bottomTabs.titleState).isEqualTo(TitleState.ALWAYS_SHOW)
    }

    @Test(expected = RuntimeException::class)
    fun setTabs_ThrowWhenMoreThan5() {
        tabs.add(SimpleViewController(activity, childRegistry, "6", tabOptions))
        createBottomTabs()
        idleMainLooper()
    }

    @Test
    fun parentControllerIsSet() {
        uut = createBottomTabs()
        for (tab in tabs) {
            Java6Assertions.assertThat(tab.parentController).isEqualTo(uut)
        }
    }

    @Test
    fun setTabs_allChildViewsAreAttachedToHierarchy() {
        uut.onViewWillAppear()
        Java6Assertions.assertThat(uut.view.childCount).isEqualTo(6)
        for (child in uut.childControllers) {
            Java6Assertions.assertThat(child.view.parent).isNotNull
        }
    }

    @Test
    fun setTabs_firstChildIsVisibleOtherAreGone() {
        uut.onViewWillAppear()
        for (i in uut.childControllers.indices) {
            Java6Assertions.assertThat(uut.view.getChildAt(i)).isEqualTo(tabs[i].view)
            Java6Assertions.assertThat(uut.view.getChildAt(i).visibility)
                .isEqualTo(if (i == 0) View.VISIBLE else View.INVISIBLE)
        }
    }

    @Test
    fun onTabSelected() {
        uut.ensureViewIsCreated()
        Java6Assertions.assertThat(uut.selectedIndex).isZero
        Java6Assertions.assertThat(((uut.childControllers as List<*>)[0] as ViewController<*>).view.visibility)
            .isEqualTo(
                View.VISIBLE
            )
        uut.onTabSelected(3, false)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(3)
        Java6Assertions.assertThat(((uut.childControllers as List<*>)[0] as ViewController<*>).view.visibility)
            .isEqualTo(
                View.INVISIBLE
            )
        Java6Assertions.assertThat(((uut.childControllers as List<*>)[3] as ViewController<*>).view.visibility)
            .isEqualTo(
                View.VISIBLE
            )
        Mockito.verify(eventEmitter).emitBottomTabSelected(0, 3)
    }

    @Test
    fun onTabReSelected() {
        uut.ensureViewIsCreated()
        Java6Assertions.assertThat(uut.selectedIndex).isZero
        uut.onTabSelected(0, true)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(0)
        Java6Assertions.assertThat(((uut.childControllers as List<*>)[0] as ViewController<*>).view.parent).isNotNull
        Mockito.verify(eventEmitter).emitBottomTabSelected(0, 0)
    }

    @Test
    fun handleBack_DelegatesToSelectedChild() {
        uut.ensureViewIsCreated()
        Java6Assertions.assertThat(uut.handleBack(CommandListenerAdapter())).isFalse
        uut.selectTab(4)
        Java6Assertions.assertThat(uut.handleBack(CommandListenerAdapter())).isTrue
        Mockito.verify(child5).handleBack(ArgumentMatchers.any())
    }

    @Test
    fun `handleBack - PrevSelection - reselect tab selection history of navigation when root has bottom tabs`() {
        val options = Options().apply {
            hardwareBack.bottomTabOnPress = HwBackBottomTabsBehaviour.PrevSelection
        }
        prepareViewsForTests(options = options)
        idleMainLooper()
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(0)

        uut.selectTab(1)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(1)

        uut.selectTab(3)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(3)

        uut.selectTab(2)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(2)

        Java6Assertions.assertThat(uut.handleBack(CommandListenerAdapter())).isTrue
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(3)

        Java6Assertions.assertThat(uut.handleBack(CommandListenerAdapter())).isTrue
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(1)

        Java6Assertions.assertThat(uut.handleBack(CommandListenerAdapter())).isTrue
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(0)

        Java6Assertions.assertThat(uut.handleBack(CommandListenerAdapter())).isFalse
    }

    @Test
    fun `handleBack - JumpToFirst - reselect first tab`() {
        val options = Options().apply {
            hardwareBack.bottomTabOnPress = HwBackBottomTabsBehaviour.JumpToFirst
        }
        prepareViewsForTests(options = options)
        idleMainLooper()
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(0)

        uut.selectTab(1)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(1)

        uut.selectTab(3)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(3)

        uut.selectTab(2)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(2)

        Java6Assertions.assertThat(uut.handleBack(CommandListenerAdapter())).isTrue
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(0)

        Java6Assertions.assertThat(uut.handleBack(CommandListenerAdapter())).isFalse
    }

    @Test
    fun `handleBack - Default - should exit app with no reselection`() {

        prepareViewsForTests()
        idleMainLooper()
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(0)

        uut.selectTab(1)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(1)

        uut.selectTab(3)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(3)

        uut.selectTab(2)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(2)

        Java6Assertions.assertThat(uut.handleBack(CommandListenerAdapter())).isFalse
    }

    @Test
    fun `handleBack - Exit - reselect first tab`() {
        val options = Options().apply {
            hardwareBack.bottomTabOnPress = HwBackBottomTabsBehaviour.Exit
        }
        prepareViewsForTests(options = options)
        idleMainLooper()
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(0)

        uut.selectTab(1)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(1)

        uut.selectTab(3)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(3)

        uut.selectTab(2)
        Java6Assertions.assertThat(uut.selectedIndex).isEqualTo(2)

        Java6Assertions.assertThat(uut.handleBack(CommandListenerAdapter())).isFalse
    }

    @Test
    fun applyChildOptions_bottomTabsOptionsAreClearedAfterApply() {
        val parent = Mocks.parentController()
        uut.parentController = parent
        child1.options.bottomTabsOptions.backgroundColor = ThemeColour(Colour(Color.RED))
        child1.onViewWillAppear()
        val optionsCaptor = ArgumentCaptor.forClass(
            Options::class.java
        )
        Mockito.verify(parent).applyChildOptions(optionsCaptor.capture(), ArgumentMatchers.any())
        Java6Assertions.assertThat(optionsCaptor.value.bottomTabsOptions.backgroundColor.hasValue()).isFalse
    }

    @Test
    fun applyOptions_bottomTabsCreateViewOnlyOnce() {
        idleMainLooper()
        Mockito.verify(presenter).applyOptions(any())
        Mockito.verify(bottomTabsContainer.bottomTabs, times(2))
            .superCreateItems() // first time when view is created, second time when options are applied
    }

    @Test
    fun onSizeChanged_recreateItemsIfSizeHasChanged() {
        val numberOfPreviousInvocations = 1
        bottomTabs.onSizeChanged(0, 0, 0, 0)
        Mockito.verify(bottomTabs, Mockito.times(numberOfPreviousInvocations)).superCreateItems()
        bottomTabs.onSizeChanged(100, 0, 0, 0)
        Mockito.verify(bottomTabs, Mockito.times(numberOfPreviousInvocations)).superCreateItems()
        bottomTabs.onSizeChanged(1080, 147, 0, 0)
        Mockito.verify(bottomTabs, Mockito.times(numberOfPreviousInvocations + 1)).superCreateItems()
        bottomTabs.onSizeChanged(1920, 147, 0, 0)
        Mockito.verify(bottomTabs, Mockito.times(numberOfPreviousInvocations + 2)).superCreateItems()
        Mockito.`when`(bottomTabs.itemsCount).thenReturn(0)
        bottomTabs.onSizeChanged(1080, 147, 0, 0)
        Mockito.verify(bottomTabs, Mockito.times(numberOfPreviousInvocations + 2)).superCreateItems()
    }

    @Test
    fun mergeOptions_currentTabIndex() {
        uut.ensureViewIsCreated()
        Java6Assertions.assertThat(uut.selectedIndex).isZero
        val options = Options()
        options.bottomTabsOptions.currentTabIndex = Number(1)
        uut.mergeOptions(options)
        Java6Assertions.assertThat(uut.selectedIndex).isOne
        Mockito.verify(eventEmitter, Mockito.times(0)).emitBottomTabSelected(
            ArgumentMatchers.any(
                Int::class.java
            ), ArgumentMatchers.any(Int::class.java)
        )
    }
    @Test
    fun `mergeOptions - select tab calls onViewWillAppear to apply options on the selected child`(){
        uut.ensureViewIsCreated()
        Java6Assertions.assertThat(uut.selectedIndex).isZero

        val options = Options()
        options.bottomTabsOptions.currentTabIndex = Number(1)
        uut.mergeOptions(options)
        Java6Assertions.assertThat(uut.selectedIndex).isOne
        Mockito.verify(child2).onViewWillAppear()
        Mockito.verify(child2).onViewDidAppear()

        options.bottomTabsOptions.currentTabIndex = Number(0)
        uut.mergeOptions(options)
        Java6Assertions.assertThat(uut.selectedIndex).isZero
        Mockito.verify(child1).onViewWillAppear()
        Mockito.verify(child1).onViewDidAppear()
    }
    @Test
    fun mergeOptions_drawBehind() {
        Java6Assertions.assertThat(uut.getBottomInset(child1)).isEqualTo(uut.bottomTabs.height)
        val o1 = Options()
        o1.bottomTabsOptions.drawBehind = Bool(true)
        child1.mergeOptions(o1)
        Java6Assertions.assertThat(uut.getBottomInset(child1)).isEqualTo(0)
        val o2 = Options()
        o2.topBar.title.text = Text("Some text")
        child1.mergeOptions(o1)
        Java6Assertions.assertThat(uut.getBottomInset(child1)).isEqualTo(0)
    }

    @Test
    fun mergeOptions_drawBehind_stack() {
        uut.ensureViewIsCreated()
        uut.selectTab(3)
        Java6Assertions.assertThat((stackChild.view.layoutParams as MarginLayoutParams).bottomMargin).isEqualTo(
            bottomTabs.height
        )
        val o1 = Options()
        o1.bottomTabsOptions.drawBehind = Bool(true)
        stackChild.mergeOptions(o1)
        Java6Assertions.assertThat((stackChild.view.layoutParams as MarginLayoutParams).bottomMargin).isEqualTo(0)
    }

    @Test
    fun mergeOptions_mergesBottomTabOptions() {
        val options = Options()
        uut.mergeOptions(options)
        Mockito.verify(bottomTabPresenter).mergeOptions(options)
    }

    @Test
    fun applyChildOptions_resolvedOptionsAreUsed() {
        val childOptions = Options()
        val pushedScreen = SimpleViewController(activity, childRegistry, "child4.1", childOptions)
        disablePushAnimation(pushedScreen)
        child4 = spyOnStack(pushedScreen)
        tabs = ArrayList(listOf(child4))
        tabsAttacher = BottomTabsAttacher(tabs, presenter, Options.EMPTY)
        initialOptions.bottomTabsOptions.currentTabIndex = Number(0)
        val resolvedOptions = Options()
        uut = object : BottomTabsController(
            activity,
            tabs,
            childRegistry,
            eventEmitter,
            imageLoaderMock,
            "uut",
            initialOptions,
            Presenter(activity, Options()),
            tabsAttacher,
            presenter,
            BottomTabPresenter(activity, tabs, mock(), TypefaceLoaderMock(), Options())
        ) {
            override fun resolveCurrentOptions(): Options {
                return resolvedOptions
            }

            override fun createBottomTabs(): BottomTabs {
                return object : BottomTabs(activity) {
                    override fun createItems() {}
                }
            }
        }
        activity.setContentView(uut.view)
        idleMainLooper()
        Mockito.verify(presenter, Mockito.times(2))
            .applyChildOptions(eq(resolvedOptions), any())
    }

    @Test
    fun child_mergeOptions_currentTabIndex() {
        uut.ensureViewIsCreated()
        Java6Assertions.assertThat(uut.selectedIndex).isZero
        val options = Options()
        options.bottomTabsOptions.currentTabIndex = Number(1)
        child1.mergeOptions(options)
        Java6Assertions.assertThat(uut.selectedIndex).isOne
    }

    @Test
    fun resolveCurrentOptions_returnsFirstTabIfInvokedBeforeViewIsCreated() {
        uut = createBottomTabs()
        Java6Assertions.assertThat(uut.currentChild).isEqualTo(tabs[0])
    }

    @Test
    fun buttonPressInvokedOnCurrentTab() {
        uut.ensureViewIsCreated()
        uut.selectTab(4)
        uut.sendOnNavigationButtonPressed("btn1")
        Mockito.verify(child5, Mockito.times(1)).sendOnNavigationButtonPressed("btn1")
    }

    @Test
    fun push() {
        uut.selectTab(3)
        val stackChild2 = SimpleViewController(activity, childRegistry, "stackChild2", Options())
        disablePushAnimation(stackChild2)
        TestUtils.hideBackButton(stackChild2)
        Java6Assertions.assertThat(child4.size()).isEqualTo(1)
        child4.push(stackChild2, CommandListenerAdapter())
        Java6Assertions.assertThat(child4.size()).isEqualTo(2)
    }

    @Test
    fun oneTimeOptionsAreAppliedOnce() {
        val options = Options()
        options.bottomTabsOptions.currentTabIndex = Number(1)
        Java6Assertions.assertThat(uut.selectedIndex).isZero
        uut.mergeOptions(options)
        Java6Assertions.assertThat(uut.selectedIndex).isOne
        Java6Assertions.assertThat(uut.options.bottomTabsOptions.currentTabIndex.hasValue()).isFalse
        Java6Assertions.assertThat(uut.initialOptions.bottomTabsOptions.currentTabIndex.hasValue()).isFalse
    }

    @Test
    fun selectTab() {
        uut.selectTab(1)
        Mockito.verify(tabsAttacher).onTabSelected(tabs[1])
    }

    @Test
    fun selectTab_onViewDidAppearIsInvokedAfterSelection() {
        uut.selectTab(1)
        Mockito.verify(child2).onViewDidAppear()
    }

    @Test
    fun creatingTabs_onViewDidAppearInvokedAfterInitialTabIndexSet() {
        val options = Options.EMPTY.copy()
        options.bottomTabsOptions.currentTabIndex = Number(1)
        prepareViewsForTests(options.bottomTabsOptions)
        idleMainLooper()
        Mockito.verify(tabs[0], Mockito.times(0)).onViewDidAppear()
        Mockito.verify(tabs[1], Mockito.times(1)).onViewDidAppear()
        Mockito.verify(tabs[2], Mockito.times(0)).onViewDidAppear()
        Mockito.verify(tabs[3], Mockito.times(0)).onViewDidAppear()
        Mockito.verify(tabs[4], Mockito.times(0)).onViewDidAppear()
    }

    @Test
    fun topInset() {
        Java6Assertions.assertThat(child1.topInset).isEqualTo(statusBarHeight)
        Java6Assertions.assertThat(child2.topInset).isEqualTo(statusBarHeight)
        child1.options.statusBar.drawBehind = Bool(true)
        Java6Assertions.assertThat(child1.topInset).isEqualTo(0)
        Java6Assertions.assertThat(child2.topInset).isEqualTo(statusBarHeight)
        Java6Assertions.assertThat(stackChild.topInset).isEqualTo(statusBarHeight + child4.topBar.height)
    }

    @Test
    fun bottomInset_defaultOptionsAreTakenIntoAccount() {
        val defaultOptions = Options()
        defaultOptions.bottomTabsOptions.visible = Bool(false)
        Java6Assertions.assertThat(uut.getBottomInset(child1)).isEqualTo(bottomTabs.height)
        uut.setDefaultOptions(defaultOptions)
        Java6Assertions.assertThat(uut.getBottomInset(child1)).isZero
    }

    @Test
    fun destroy() {
        uut.destroy()
        Mockito.verify(tabsAttacher).destroy()
    }

    private fun prepareViewsForTests(
        bottomTabsOptions: BottomTabsOptions = initialOptions.bottomTabsOptions,
        options: Options = initialOptions, defaultOptions: Options = initialOptions
    ) {
        if(::uut.isInitialized){
            uut.destroy()
        }
//        ObjectUtils.perform(uut, { obj: BottomTabsController -> obj.destroy() })
        bottomTabs = Mockito.spy(object : BottomTabs(activity) {
            override fun superCreateItems() {}
        })
        bottomTabsContainer = Mockito.spy(BottomTabsContainer(activity, bottomTabs))
        createChildren()
        tabs = mutableListOf(child1, child2, child3, child4, child5)
        defaultOptions.bottomTabsOptions = bottomTabsOptions
        presenter = Mockito.spy(BottomTabsPresenter(tabs, defaultOptions, BottomTabsAnimator()))
        bottomTabPresenter =
            Mockito.spy(BottomTabPresenter(activity, tabs, mock(), TypefaceLoaderMock(), defaultOptions))
        tabsAttacher = Mockito.spy(BottomTabsAttacher(tabs, presenter, defaultOptions))
        uut = createBottomTabs(options = options, defaultOptions = defaultOptions)
        activity.setContentView(FakeParentController(activity, childRegistry, uut).view)
    }

    private fun createChildren() {
        child1 = Mockito.spy(SimpleViewController(activity, childRegistry, "child1", tabOptions))
        child2 = Mockito.spy(SimpleViewController(activity, childRegistry, "child2", tabOptions))
        child3 = Mockito.spy(SimpleViewController(activity, childRegistry, "child3", tabOptions))
        stackChild = Mockito.spy(SimpleViewController(activity, childRegistry, "stackChild", tabOptions))
        child4 = spyOnStack(stackChild)
        child5 = Mockito.spy(SimpleViewController(activity, childRegistry, "child5", tabOptions))
        Mockito.`when`(child5.handleBack(any())).thenReturn(true)
    }

    private fun spyOnStack(initialChild: ViewController<*>?): StackController {
        val build = TestUtils.newStackController(activity)
            .setInitialOptions(tabOptions)
            .build()
        val stack = Mockito.spy(build)
        disablePushAnimation(initialChild)
        stack.ensureViewIsCreated()
        stack.push(initialChild, CommandListenerAdapter())
        return stack
    }

    private fun createBottomTabs(
        options: Options = initialOptions,
        defaultOptions: Options = initialOptions
    ): BottomTabsController {
        val presenter1 = Presenter(activity, defaultOptions)
        return object : BottomTabsController(
            activity,
            tabs,
            childRegistry,
            eventEmitter,
            imageLoaderMock,
            "uut",
            options,
            presenter1,
            tabsAttacher,
            presenter,
            bottomTabPresenter
        ) {
            override fun getTopMostParent(): ViewController<*> {
                return  Mocks.parentController(null, FrameLayout(activity))
            }
            override fun ensureViewIsCreated() {
                super.ensureViewIsCreated()
                uut.view.layout(0, 0, 1000, 1000)
            }

            override fun createView(): BottomTabsLayout {
                val view = super.createView()
                this@BottomTabsControllerTest.bottomTabs.layoutParams.height = 100
                return view
            }

            override fun createBottomTabsContainer(): BottomTabsContainer {
                return this@BottomTabsControllerTest.bottomTabsContainer
            }

            override fun createBottomTabs(): BottomTabs {
                return this@BottomTabsControllerTest.bottomTabs
            }
        }
    }

    private val statusBarHeight: Int
        get() = getStatusBarHeight(activity)
}
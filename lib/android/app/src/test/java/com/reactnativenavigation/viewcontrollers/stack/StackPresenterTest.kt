package com.reactnativenavigation.viewcontrollers.stack

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import org.mockito.kotlin.*
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.TestUtils
import com.reactnativenavigation.fakes.IconResolverFake
import com.reactnativenavigation.mocks.*
import com.reactnativenavigation.options.*
import com.reactnativenavigation.options.params.*
import com.reactnativenavigation.options.params.Number
import com.reactnativenavigation.options.parsers.TypefaceLoader
import com.reactnativenavigation.react.CommandListenerAdapter
import com.reactnativenavigation.utils.*
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarController
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonPresenter
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.IconResolver
import com.reactnativenavigation.viewcontrollers.stack.topbar.title.TitleBarReactViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.stack.StackLayout
import com.reactnativenavigation.views.stack.topbar.TopBar
import com.reactnativenavigation.views.stack.topbar.titlebar.DEFAULT_LEFT_MARGIN_PX
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarReactView
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleSubTitleLayout
import org.assertj.core.api.Assertions
import org.assertj.core.api.Java6Assertions.assertThat
import org.json.JSONObject
import org.junit.Test
import org.mockito.Mockito
import org.robolectric.shadows.ShadowLooper
import java.util.*
import kotlin.collections.ArrayList


class StackPresenterTest : BaseTest() {
    private lateinit var parent: StackController
    private lateinit var uut: StackPresenter
    private lateinit var ogUut: StackPresenter
    private lateinit var child: ViewController<*>
    private lateinit var otherChild: ViewController<*>
    private lateinit var activity: Activity
    private lateinit var topBar: TopBar
    private lateinit var renderChecker: RenderChecker
    private val textBtn1 = TitleBarHelper.textualButton("btn1")
    private val textBtn2 = TitleBarHelper.textualButton("btn2")
    private val componentBtn1 = TitleBarHelper.reactViewButton("btn1_")
    private val componentBtn2 = TitleBarHelper.reactViewButton("btn2_")
    private val titleComponent1 = TitleBarHelper.titleComponent("component1")
    private val titleComponent2 = TitleBarHelper.titleComponent("component2")
    private lateinit var topBarController: TopBarController
    private lateinit var childRegistry: ChildControllersRegistry
    private lateinit var typefaceLoader: TypefaceLoader
    private lateinit var iconResolver: IconResolver
    private lateinit var buttonCreator: TitleBarButtonCreatorMock
    private lateinit var reactTitleView: TitleBarReactView

    override fun beforeEach() {
        super.beforeEach()
        activity = spy(newActivity())
        val titleViewCreator: TitleBarReactViewCreatorMock = object : TitleBarReactViewCreatorMock() {
            override fun create(activity: Activity, componentId: String, componentName: String): TitleBarReactView {
                reactTitleView = spy(super.create(activity, componentId, componentName))
                return reactTitleView
            }
        }
        renderChecker = spy(RenderChecker())
        typefaceLoader = createTypeFaceLoader()
        iconResolver = IconResolverFake(activity)
        buttonCreator = TitleBarButtonCreatorMock()
        ogUut = StackPresenter(
                activity,
                titleViewCreator,
                TopBarBackgroundViewCreatorMock(),
                buttonCreator,
                iconResolver,
                typefaceLoader,
                renderChecker,
                Options()
        )
        uut = spy(ogUut)
        createTopBarController()
        parent = TestUtils.newStackController(activity)
                .setTopBarController(topBarController)
                .setStackPresenter(uut)
                .build()
        childRegistry = ChildControllersRegistry()
        child = spy(SimpleViewController(activity, childRegistry, "child1", Options.EMPTY))
        otherChild = spy(SimpleViewController(activity, childRegistry, "child1", Options.EMPTY))
        activity.setContentView(parent.view)
    }

    @Test
    fun onConfigurationChange_shouldApplyColors() {
        val options = Options.EMPTY.copy()
        options.topBar.borderColor = ThemeColour.of(Color.BLACK, Color.RED)
        options.topBar.background = TopBarBackgroundOptions().apply {
            color = ThemeColour.of(Color.BLACK, Color.RED)
        }
        options.topBar.title = TitleOptions().apply {
            color = ThemeColour.of(Color.BLACK, Color.RED)
        }
        options.topBar.subtitle = SubtitleOptions().apply {
            color = ThemeColour.of(Color.BLACK, Color.RED)
        }
        options.topBar.buttons.back = BackButton().apply {
            color = ThemeColour.of(Color.BLACK, Color.RED)
            visible = Bool(true)
        }
        options.topBar.rightButtonColor = ThemeColour.of(Color.BLACK, Color.RED)
        options.topBar.buttons.left = arrayListOf(ButtonOptions())
        options.topBar.buttons.right = arrayListOf(ButtonOptions())

        options.topTabs.selectedTabColor = ThemeColour.of(Color.BLACK, Color.RED)
        options.topTabs.unselectedTabColor = ThemeColour.of(Color.BLACK, Color.RED)

        mockConfiguration.uiMode = Configuration.UI_MODE_NIGHT_NO
        uut.onConfigurationChanged(options)

        verify(topBar).setTitleTextColor(Color.BLACK)
        verify(topBar).setSubtitleColor(Color.BLACK)
        verify(topBar).setBackgroundColor(Color.BLACK)
        verify(topBar).setBorderColor(Color.BLACK)
        verify(topBar).applyTopTabsColors(options.topTabs.selectedTabColor,options.topTabs.unselectedTabColor)
        verify(topBar).setOverflowButtonColor(Color.BLACK)
        verify(topBar).setBackButton(any())

        mockConfiguration.uiMode = Configuration.UI_MODE_NIGHT_YES
        uut.onConfigurationChanged(options)

        verify(topBar).setTitleTextColor(Color.RED)
        verify(topBar).setSubtitleColor(Color.RED)
        verify(topBar).setBackgroundColor(Color.RED)
        verify(topBar).setBorderColor(Color.RED)
        verify(topBar,times(2)).applyTopTabsColors(options.topTabs.selectedTabColor,options.topTabs.unselectedTabColor)
        verify(topBar).setOverflowButtonColor(Color.RED)
        verify(topBar,times(2)).setBackButton(any())

    }

    @Test
    fun onConfigurationChange_shouldApplyColorsOnTopBarButtons() {
        val options = Options.EMPTY.copy()
        options.topBar.buttons.left = arrayListOf(ButtonOptions())
        options.topBar.buttons.right = arrayListOf(ButtonOptions())
        uut.applyChildOptions(options,parent,child)

        uut.onConfigurationChanged(options)

        verify(topBarController, times(2)).applyRightButtons(any())
        verify(topBarController, times(2)).applyLeftButtons(any())
    }

    @Test
    fun isRendered() {
        val o1 = Options()
        o1.topBar.title.component = component(Alignment.Default)
        o1.topBar.background.component = component(Alignment.Default)
        o1.topBar.buttons.right = ArrayList(listOf(componentBtn1))
        o1.topBar.buttons.left = ArrayList(listOf(componentBtn2))

        uut.applyChildOptions(o1, parent, child)
        uut.isRendered(child.view)

        val controllers = argumentCaptor<Collection<ViewController<*>>>()
        verify(renderChecker).areRendered(controllers.capture())
        val items = controllers.firstValue
        assertThat(items.size).isEqualTo(4)
        assertThat(items.containsAll(listOf(
                uut.getComponentButtons(child.view)[0],
                uut.titleComponents[child.view] as ViewController<*>,
                uut.backgroundComponents[child.view]
        ))).isTrue()
    }

    @Test
    fun applyChildOptions_setTitleComponent() {
        val options = Options()
        options.topBar.title.component = component(Alignment.Default)
        uut.applyChildOptions(options, parent, child)
        verify(topBar).setTitleComponent(uut.titleComponents[child.view]!!.view, Alignment.Default)
    }

    @Test
    fun applyChildOptions_setTitleComponentCreatesOnce() {
        val options = Options()
        options.topBar.title.component = component(Alignment.Default)
        uut.applyChildOptions(options, parent, child)
        uut.applyChildOptions(Options.EMPTY, parent, otherChild)
        val titleController = uut.titleComponents[child.view]
        uut.applyChildOptions(options, parent, child)
        assertThat(uut.titleComponents.size).isOne()
        assertThat(uut.titleComponents[child.view]).isEqualTo(titleController)
    }

    @Test
    fun applyChildOptions_setTitleComponentAlignmentCenter() {
        val options = Options()
        parent.view.layout(0, 0, 1000, 1000)
        options.topBar.title.component = component(Alignment.Center)
        uut.applyChildOptions(options, parent, child)
        val component = topBar.titleAndButtonsContainer.getComponent()
        Assertions.assertThat(component).isEqualTo(reactTitleView)
        child.view.requestLayout()
        idleMainLooper()
        Assertions.assertThat(component?.left).isEqualTo(parent.view.width / 2 - reactTitleView.width / 2)
        Assertions.assertThat(component?.right).isEqualTo(parent.view.width / 2 + reactTitleView.width / 2)
    }

    @Test
    fun applyChildOptions_setTitleComponentAlignmentStart() {
        val options = Options()
        options.topBar.title.component = component(Alignment.Fill)
        uut.applyChildOptions(options, parent, child)
        val component = topBar.titleAndButtonsContainer.getComponent()
        Assertions.assertThat(component).isEqualTo(reactTitleView)
        Mockito.doReturn(100).`when`(reactTitleView).measuredWidth
        child.view.requestLayout()
        idleMainLooper()
        Assertions.assertThat(component?.left).isEqualTo(DEFAULT_LEFT_MARGIN_PX)
        Assertions.assertThat(component?.right).isEqualTo(DEFAULT_LEFT_MARGIN_PX + reactTitleView.measuredWidth + DEFAULT_LEFT_MARGIN_PX)
    }

    @Test
    fun onChildDestroyed_destroyTitleComponent() {
        val options = Options()
        options.topBar.title.component = component(Alignment.Default)
        uut.applyChildOptions(options, parent, child)
        val titleView = uut.titleComponents[child.view]!!.view
        uut.onChildDestroyed(child)
        verify(titleView).destroy()
    }

    @Test
    fun mergeOrientation() {
        val options = Options()
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child)
        verify(uut, never()).applyOrientation(any())
        val orientation = JSONObject().put("orientation", "landscape")
        options.layout.orientation = OrientationOptions.parse(orientation)
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child)
        verify(uut).applyOrientation(options.layout.orientation)
    }

    @Test
    fun mergeButtons() {
        uut.mergeChildOptions(EMPTY_OPTIONS, EMPTY_OPTIONS, parent, child)
        verify(topBarController, never()).applyRightButtons(any())
        verify(topBarController, never()).applyLeftButtons(any())

        val options = Options()
        val button = ButtonOptions()
        button.text = Text("btn")
        options.topBar.buttons.right = ArrayList(setOf(button))
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child)
        verify(topBarController).mergeRightButtons(any(), any())

        options.topBar.buttons.left = ArrayList(setOf(button))
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child)
        verify(topBarController).mergeLeftButtons(any(), any())
    }

    @Test
    fun `mergeButtons - modify BackButton should not have effect on stack with with one child`() {
        val options = Options()
        options.topBar.buttons.back = BackButton.parse(activity, JSONObject().apply {
            put("color", Color.RED)
        })
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child)
        verify(topBar, times(0)).setBackButton(any())
    }

    @Test
    fun mergeButtons_previousRightButtonsAreDestroyed() {
        val options = Options()
        options.topBar.buttons.right = ArrayList(listOf(componentBtn1))
        uut.applyChildOptions(options, parent, child)
        val initialButtons = uut.getComponentButtons(child.view)
        CollectionUtils.forEach(initialButtons) { obj: ButtonController -> obj.ensureViewIsCreated() }
        options.topBar.buttons.right = ArrayList(listOf(componentBtn2))
        uut.mergeChildOptions(options, Options.EMPTY, parent, child)
        for (button in initialButtons) {
            assertThat(button.isDestroyed).isTrue()
        }
    }

    @Test
    fun mergeRightButtons_mergingButtonsOnlyDestroysRightButtons() {
        val a = Options()
        a.topBar.buttons.right = ArrayList(listOf(componentBtn1))
        a.topBar.buttons.left = ArrayList(listOf(componentBtn2))
        uut.applyChildOptions(a, parent, child)
        val initialButtons = uut.getComponentButtons(child.view)
        CollectionUtils.forEach(initialButtons) { obj: ButtonController -> obj.ensureViewIsCreated() }
        val b = Options()
        b.topBar.buttons.right = ArrayList(listOf(componentBtn2))
        uut.mergeChildOptions(b, Options.EMPTY, parent, child)
        assertThat(initialButtons[0].isDestroyed).isTrue()
        assertThat(initialButtons[1].isDestroyed).isFalse()
    }

    @Test
    fun mergeRightButtons_buttonsAreCreatedOnlyIfNeeded() {
        val toApply = Options()
        textBtn1.color = ThemeColour(Colour(Color.GREEN))
        toApply.topBar.buttons.right = arrayListOf(textBtn1, componentBtn1)
        uut.applyChildOptions(toApply, parent, child)

        val captor1 = argumentCaptor<List<ButtonController>>()
        verify(topBarController).applyRightButtons(captor1.capture())
        assertThat(topBar.rightButtonBar.menu.size()).isEqualTo(2)

        val appliedButtons = captor1.firstValue
        val toMerge = Options()
        toMerge.topBar.buttons.right = ArrayList(toApply.topBar.buttons.right!!.map(ButtonOptions::copy))
        toMerge.topBar.buttons.right!![0].color = ThemeColour(Colour(Color.RED))
        toMerge.topBar.buttons.right!!.add(1, componentBtn2)
        uut.mergeChildOptions(toMerge, Options.EMPTY, parent, child)

        assertThat(topBar.rightButtonBar.menu.size()).isEqualTo(3)
        val captor2 = argumentCaptor<List<ButtonController>>()
        verify(topBarController).mergeRightButtons(captor2.capture(), any())
        val mergedButtons = captor2.firstValue
        assertThat(mergedButtons).hasSize(3)
        assertThat(appliedButtons[0]).isNotEqualTo(mergedButtons[0])
        assertThat(appliedButtons[1]).isEqualTo(mergedButtons[2])
    }

    @Test
    fun mergeButtons_mergingLeftButtonsDoesNotDestroyRightButtons() {
        val a = Options()
        a.topBar.buttons.right = ArrayList(listOf(componentBtn1))
        a.topBar.buttons.left = ArrayList(listOf(componentBtn2))
        uut.applyChildOptions(a, parent, child)

        val initialButtons = uut.getComponentButtons(child.view)
        initialButtons.forEach(ButtonController::ensureViewIsCreated)
        val b = Options()
        b.topBar.buttons.left = ArrayList(listOf(componentBtn2))
        uut.mergeChildOptions(b, Options.EMPTY, parent, child)
        assertThat(initialButtons[0].isDestroyed).isFalse()
    }

    @Test
    fun mergeChildOptions_backButtonShouldNotAffectLeftButtons() {
        val options = Options()
        options.topBar.buttons.right = ArrayList(listOf(textBtn1))
        options.topBar.buttons.back = BackButton.parse(activity, JSONObject())
        options.topBar.buttons.back.setVisible()
        options.topBar.buttons.left = ArrayList(listOf(textBtn2))
        uut.applyChildOptions(options, parent, child)
        ShadowLooper.idleMainLooper()
        verify(topBar, times(1)).clearLeftButtons()
        verify(topBar, times(1)).clearBackButton()

        val backButtonHidden = Options()
        backButtonHidden.topBar.buttons.back.setHidden()
        uut.mergeChildOptions(backButtonHidden, options, parent, child)
        ShadowLooper.idleMainLooper()
        verify(topBar, times(1)).clearLeftButtons()
        verify(topBar, times(2)).clearBackButton()
    }

    @Test
    fun mergeButtons_backButtonIsRemovedIfVisibleFalse() {
        val pushedChild = spy<ViewController<*>>(SimpleViewController(activity, childRegistry, "child2", Options()))
        disablePushAnimation(child, pushedChild)

        parent.push(child, CommandListenerAdapter())
        assertThat(topBar.navigationIcon).isNull()

        parent.push(pushedChild, CommandListenerAdapter())
        ShadowLooper.idleMainLooper()
        verify(pushedChild).onViewWillAppear()
        assertThat(topBar.navigationIcon).isInstanceOf(BackDrawable::class.java)

        val backButtonHidden = Options()
        backButtonHidden.topBar.buttons.back.setHidden()
        uut.mergeChildOptions(backButtonHidden, backButtonHidden, parent, child)
        ShadowLooper.idleMainLooper()
        assertThat(topBar.navigationIcon).isNull()
    }

    @Test
    fun mergeButtons_actualLeftButtonIsAppliedEvenIfBackButtonHasValue() {
        val toMerge = Options()
        toMerge.topBar.buttons.back.setHidden()
        toMerge.topBar.buttons.left = ArrayList()
        val leftButton = ButtonOptions()
        leftButton.id = "id"
        leftButton.icon = Text("")
        toMerge.topBar.buttons.left!!.add(leftButton)

        assertThat(toMerge.topBar.buttons.back.hasValue()).isTrue()
        uut.mergeChildOptions(toMerge, Options.EMPTY, parent, child)
        verify(topBarController).mergeLeftButtons(any(), any())
        verify(topBar, never()).clearLeftButtons()
    }

    @Test
    fun mergeChildOptions_mergeAnimateLeftRightButtons() {
        val options = Options().apply {
            topBar.animateLeftButtons = Bool(false)
        }
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child)
        verify(topBar).animateLeftButtons(false)
        verify(topBar, never()).animateRightButtons(any())

        options.apply {
            topBar.animateRightButtons = Bool(true)
        }
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child)
        verify(topBar).animateRightButtons(true)


        options.apply {
            topBar.animateRightButtons = Bool(false)
            topBar.animateLeftButtons = Bool(true)
        }
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child)
        verify(topBar).animateRightButtons(false)
        verify(topBar).animateLeftButtons(true)
    }
    @Test
    fun mergeTopBarOptions() {
        val options = Options()
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child)
        assertTopBarOptions(options, 0)
        val title = TitleOptions()
        title.text = Text("abc")
        title.color = ThemeColour(Colour(0))
        title.fontSize = Fraction(1.0)
        title.font = FontOptions()
        title.font.fontStyle = Text("bold")
        options.topBar.title = title
        val subtitleOptions = SubtitleOptions()
        subtitleOptions.text = Text("Sub")
        subtitleOptions.color = ThemeColour(Colour(1))
        subtitleOptions.font.fontStyle = Text("bold")
        subtitleOptions.fontSize = Fraction(1.0)
        options.topBar.subtitle = subtitleOptions
        options.topBar.background.color = ThemeColour(Colour(0))
        options.topBar.testId = Text("test123")
        options.topBar.animate = Bool(false)
        options.topBar.visible = Bool(false)
        options.topBar.drawBehind = Bool(false)
        options.topBar.hideOnScroll = Bool(false)
        options.topBar.validate()
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child)
        assertTopBarOptions(options, 1)
        options.topBar.drawBehind = Bool(true)
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child)
    }

    @Test
    fun mergeOptions_defaultOptionsAreNotApplied() {
        val defaultOptions = Options()
        defaultOptions.topBar.background.color = ThemeColour(Colour(10))
        uut.defaultOptions = defaultOptions
        val toMerge = Options()
        toMerge.topBar.title.text = Text("someText")
        uut.mergeOptions(toMerge, parent, child)
        verify(topBar, never()).setBackgroundColor(any())
    }

    @Test
    fun mergeOptions_shouldUpdateTitleAlignmentWhenNotDefault() {
        val defaultOptions = Options()
        defaultOptions.topBar.title.text = Text("title")
        uut.defaultOptions = defaultOptions
        val toMerge = Options()
        toMerge.topBar.title.text = Text("newTitle")
        uut.mergeOptions(toMerge, parent, child)

        val alignmentOptions = Options()
        alignmentOptions.topBar.title.alignment = Alignment.Center
        uut.mergeOptions(alignmentOptions, parent, child)
        verify(topBarController).alignTitleComponent(Alignment.Center)
    }

    @Test
    fun mergeOptions_resolvedTitleFontOptionsAreApplied() {
        val childOptions = Options()
        childOptions.topBar.title.font.fontFamily = Text(SOME_FONT_FAMILY)
        child.mergeOptions(childOptions)
        val parentOptions = Options()
        parentOptions.topBar.title.color = ThemeColour.of(Color.RED)
        parent.mergeOptions(parentOptions)
        val defaultOptions = Options()
        defaultOptions.topBar.title.fontSize = Fraction(9.0)
        uut.defaultOptions = defaultOptions
        val toMerge = Options()
        toMerge.topBar.title.text = Text("New Title")
        uut.mergeOptions(toMerge, parent, child)
        val title = (topBar.titleAndButtonsContainer.getTitleComponent() as TitleSubTitleLayout).getTitleTxtView()
        assertThat(title).isNotNull()
        assertThat(title.typeface).isEqualTo(SOME_TYPEFACE)
        verify(topBar).setTitleFontSize(9.0)
        verify(topBar).setTitleTextColor(Color.RED)
    }

    @Test
    fun mergeOptions_resolvedSubtitleFontOptionsAreApplied() {
        val childOptions = Options()
        childOptions.topBar.subtitle.font.fontFamily = Text(SOME_FONT_FAMILY)
        child.mergeOptions(childOptions)
        val parentOptions = Options()
        parentOptions.topBar.subtitle.color = ThemeColour(Colour(Color.RED))
        parent.mergeOptions(parentOptions)
        val defaultOptions = Options()
        defaultOptions.topBar.subtitle.fontSize = Fraction(9.0)
        uut.defaultOptions = defaultOptions
        val toMerge = Options()
        toMerge.topBar.subtitle.text = Text("New Title")
        uut.mergeOptions(toMerge, parent, child)
        val subtitle = (topBar.titleAndButtonsContainer.getTitleComponent() as TitleSubTitleLayout).getSubTitleTxtView()
        assertThat(subtitle).isNotNull()
        assertThat(subtitle.typeface).isEqualTo(SOME_TYPEFACE)
        verify(topBar).setSubtitleFontSize(9.0)
        verify(topBar).setSubtitleColor(Color.RED)
    }

    @Test
    fun mergeChildOptions_resolvedTitleFontOptionsAreApplied() {
        val defaultOptions = Options()
        defaultOptions.topBar.title.fontSize = Fraction(9.0)
        uut.defaultOptions = defaultOptions
        val resolvedOptions = Options()
        resolvedOptions.topBar.title.font.fontFamily = Text(SOME_FONT_FAMILY)
        resolvedOptions.topBar.title.color = ThemeColour(Colour(Color.RED))
        val toMerge = Options()
        toMerge.topBar.title.text = Text("New Title")
        uut.mergeChildOptions(toMerge, resolvedOptions, parent, child)
        val title = (topBar.titleAndButtonsContainer.getTitleComponent() as TitleSubTitleLayout).getTitleTxtView()
        assertThat(title).isNotNull()
        assertThat(title.typeface).isEqualTo(SOME_TYPEFACE)
        verify(topBar).setTitleFontSize(9.0)
        verify(topBar).setTitleTextColor(Color.RED)
    }

    @Test
    fun mergeChildOptions_resolvedSubtitleFontOptionsAreApplied() {
        val defaultOptions = Options()
        defaultOptions.topBar.subtitle.fontSize = Fraction(9.0)
        uut.defaultOptions = defaultOptions
        val resolvedOptions = Options()
        resolvedOptions.topBar.subtitle.font.fontFamily = Text(SOME_FONT_FAMILY)
        resolvedOptions.topBar.subtitle.color = ThemeColour(Colour(Color.RED))
        val toMerge = Options()
        toMerge.topBar.subtitle.text = Text("New Title")
        uut.mergeChildOptions(toMerge, resolvedOptions, parent, child)
        val subtitle = (topBar.titleAndButtonsContainer.getTitleComponent() as TitleSubTitleLayout).getSubTitleTxtView()
        assertThat(subtitle).isNotNull()
        assertThat(subtitle.typeface).isEqualTo(SOME_TYPEFACE)
        verify(topBar).setSubtitleFontSize(9.0)
        verify(topBar).setSubtitleColor(Color.RED)
    }

    @Test
    fun mergeChildOptions_defaultOptionsAreNotApplied() {
        val defaultOptions = Options()
        defaultOptions.topBar.background.color = ThemeColour(Colour(10))
        uut.defaultOptions = defaultOptions
        val childOptions = Options()
        childOptions.topBar.title.text = Text("someText")
        uut.mergeChildOptions(childOptions, EMPTY_OPTIONS, parent, child)
        verify(topBar, never()).setBackgroundColor(any())
    }

    @Test
    fun applyTopBarOptions_setTitleComponent() {
        val applyComponent = Options()
        applyComponent.topBar.title.component.name = Text("Component1")
        applyComponent.topBar.title.component.componentId = Text("Component1id")
        uut.applyChildOptions(applyComponent, parent, child)
        verify(topBarController).setTitleComponent(any())
    }

    @Test
    fun mergeTopBarOptions_settingTitleDestroysComponent() {
        val componentOptions = Options()
        componentOptions.topBar.title.component = titleComponent1
        uut.applyChildOptions(componentOptions, parent, child)
        val applyCaptor = argumentCaptor<TitleBarReactViewController>()
        verify(topBarController).setTitleComponent(applyCaptor.capture())
        val titleOptions = Options()
        titleOptions.topBar.title.text = Text("Some title")
        uut.mergeChildOptions(titleOptions, Options.EMPTY, parent, child)
        assertThat(applyCaptor.firstValue.isDestroyed).isTrue()
    }

    @Test
    fun mergeTopBarOptions_doesNotRecreateTitleComponentIfEquals() {
        val options = Options()
        options.topBar.title.component = titleComponent1
        uut.applyChildOptions(options, parent, child)
        val applyCaptor = argumentCaptor<TitleBarReactViewController>()
        verify(topBarController).setTitleComponent(applyCaptor.capture())
        uut.mergeChildOptions(options, Options.EMPTY, parent, child)
        verify(topBarController, times(2)).setTitleComponent(applyCaptor.firstValue)
    }

    @Test
    fun mergeTopBarOptions_previousTitleComponentIsDestroyed() {
        val options = Options()
        options.topBar.title.component = titleComponent1
        uut.applyChildOptions(options, parent, child)

        val toMerge = Options()
        toMerge.topBar.title.component = titleComponent2
        uut.mergeChildOptions(toMerge, Options.EMPTY, parent, child)
        val captor = argumentCaptor<TitleBarReactViewController>()
        verify(topBarController, times(2)).setTitleComponent(captor.capture())
        assertThat(captor.firstValue).isNotEqualTo(captor.secondValue)
        assertThat(captor.firstValue.isDestroyed).isTrue()
    }

    @Test
    fun mergeTopTabsOptions() {
        val options = Options()
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child)
        verify(topBar, never()).applyTopTabsColors(any(), any())
        verify(topBar, never()).applyTopTabsFontSize(any())
        verify(topBar, never()).setTopTabsVisible(any())
        options.topTabs.selectedTabColor = ThemeColour(Colour(1))
        options.topTabs.unselectedTabColor = ThemeColour(Colour(1))
        options.topTabs.fontSize = Number(1)
        options.topTabs.visible = Bool(true)
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child)
        verify(topBar).applyTopTabsColors(options.topTabs.selectedTabColor, options.topTabs.unselectedTabColor)
        verify(topBar).applyTopTabsFontSize(options.topTabs.fontSize)
        verify(topBar).setTopTabsVisible(any())
    }

    @Test
    fun applyInitialChildLayoutOptions() {
        val options = Options()
        options.topBar.visible = Bool(false)
        options.topBar.animate = Bool(true)
        uut.applyInitialChildLayoutOptions(options)
        verify(topBarController).hide()
    }

    @Test
    fun applyButtons_buttonColorIsMergedToButtons() {
        val options = Options()
        val rightButton1 = ButtonOptions()
        val rightButton2 = ButtonOptions()
        val leftButton = ButtonOptions()
        options.topBar.rightButtonColor = ThemeColour(Colour(10))
        options.topBar.leftButtonColor = ThemeColour(Colour(100))
        options.topBar.buttons.right = ArrayList()
        options.topBar.buttons.right!!.add(rightButton1)
        options.topBar.buttons.right!!.add(rightButton2)
        options.topBar.buttons.left = ArrayList()
        options.topBar.buttons.left!!.add(leftButton)
        uut.applyChildOptions(options, parent, child)
        val rightCaptor = argumentCaptor<List<ButtonController>>()
        verify(topBarController).applyRightButtons(rightCaptor.capture())
        assertThat(rightCaptor.firstValue[0].button.color.get()).isEqualTo(options.topBar.rightButtonColor.get())
        assertThat(rightCaptor.firstValue[1].button.color.get()).isEqualTo(options.topBar.rightButtonColor.get())
        assertThat(rightCaptor.firstValue[0]).isNotEqualTo(rightButton1)
        assertThat(rightCaptor.firstValue[1]).isNotEqualTo(rightButton2)
        val leftCaptor = argumentCaptor<List<ButtonController>>()
        verify(topBarController).applyLeftButtons(leftCaptor.capture())
        assertThat(leftCaptor.firstValue[0].button.color).isEqualTo(options.topBar.leftButtonColor)
        assertThat(leftCaptor.firstValue[0]).isNotEqualTo(leftButton)
    }

    @Test
    fun applyTopBarOptions_backgroundComponentIsCreatedOnceIfNameAndIdAreEqual() {
        val o = Options()
        o.topBar.background.component.name = Text("comp")
        o.topBar.background.component.componentId = Text("compId")
        uut.applyChildOptions(o, parent, Mocks.viewController())
        assertThat(uut.backgroundComponents.size).isOne()
        uut.applyChildOptions(o, parent, Mocks.viewController())
        assertThat(uut.backgroundComponents.size).isOne()
    }

    @Test
    fun mergeChildOptions_applyTopBarButtonsColor() {
        val mergeOptions = Options()
        val initialOptions = Options()
        val rightButton = ButtonOptions()
        val leftButton = ButtonOptions()
        initialOptions.topBar.buttons.right = ArrayList(listOf(rightButton))
        initialOptions.topBar.buttons.left = ArrayList(listOf(leftButton))

        //add buttons
        uut.applyChildOptions(initialOptions, parent, child)

        //Merge color change for right and left buttons
        mergeOptions.topBar.rightButtonColor = ThemeColour(Colour(100))
        mergeOptions.topBar.leftButtonColor = ThemeColour(Colour(10))
        val rightController = spy(ButtonController(activity, ButtonPresenter(activity, rightButton, iconResolver), rightButton, buttonCreator, mock()))
        val leftController = spy(ButtonController(activity, ButtonPresenter(activity, leftButton, iconResolver), leftButton, buttonCreator, mock()))
        uut.setComponentsButtonController(child.view, rightController, leftController)
        uut.mergeChildOptions(mergeOptions, initialOptions, parent, child)

        val rightColorCaptor = argumentCaptor<ThemeColour>()
        verify(rightController).applyColor(any(), rightColorCaptor.capture())
        assertThat(rightColorCaptor.allValues[0]).isEqualTo(mergeOptions.topBar.rightButtonColor)

        val leftColorCaptor = argumentCaptor<ThemeColour>()
        verify(leftController).applyColor(any(), leftColorCaptor.capture())
        assertThat(leftColorCaptor.allValues[0]).isEqualTo(mergeOptions.topBar.leftButtonColor)
    }

    @Test
    fun mergeChildOptions_applyTopBarButtonsDisabledColor() {
        val mergeOptions = Options()
        val initialOptions = Options()
        val rightButton = ButtonOptions()
        val leftButton = ButtonOptions()
        initialOptions.topBar.buttons.right = ArrayList(listOf(rightButton))
        initialOptions.topBar.buttons.left = ArrayList(listOf(leftButton))

        //add buttons
        uut.applyChildOptions(initialOptions, parent, child)

        //Merge color change for right and left buttons
        mergeOptions.topBar.rightButtonDisabledColor = ThemeColour(Colour(100))
        mergeOptions.topBar.leftButtonDisabledColor = ThemeColour(Colour(10))
        val rightController = spy(ButtonController(activity, ButtonPresenter(activity, rightButton, iconResolver), rightButton, buttonCreator, mock { }))
        val leftController = spy(ButtonController(activity, ButtonPresenter(activity, leftButton, iconResolver), leftButton, buttonCreator, mock { }))
        uut.setComponentsButtonController(child.view, rightController, leftController)
        uut.mergeChildOptions(mergeOptions, initialOptions, parent, child)

        val rightColorCaptor = argumentCaptor<ThemeColour>()
        verify(rightController).applyDisabledColor(any(), rightColorCaptor.capture())
        assertThat(rightColorCaptor.allValues[0]).isEqualTo(mergeOptions.topBar.rightButtonDisabledColor)

        val leftColorCaptor = argumentCaptor<ThemeColour>()
        verify(leftController).applyDisabledColor(any(), leftColorCaptor.capture())
        assertThat(leftColorCaptor.allValues[0]).isEqualTo(mergeOptions.topBar.leftButtonDisabledColor)
    }

    @Test
    fun mergeChildOptions_ignoreColorWhenClearingButtons() {
        val mergeOptions = Options()
        val initialOptions = Options()
        val rightButton = ButtonOptions()
        val leftButton = ButtonOptions()


        //add buttons
        initialOptions.topBar.buttons.right = ArrayList()
        initialOptions.topBar.buttons.left = ArrayList()
        uut.applyChildOptions(initialOptions, parent, child)

        //Merge color change for right and left buttons with clear buttons
        mergeOptions.topBar.buttons.right = ArrayList()
        mergeOptions.topBar.buttons.left = ArrayList()
        mergeOptions.topBar.rightButtonColor = ThemeColour(Colour(100))
        mergeOptions.topBar.leftButtonColor = ThemeColour(Colour(100))
        mergeOptions.topBar.rightButtonDisabledColor = ThemeColour(Colour(100))
        mergeOptions.topBar.leftButtonDisabledColor = ThemeColour(Colour(10))
        val rightController = spy(ButtonController(activity, ButtonPresenter(activity, rightButton, iconResolver), rightButton, buttonCreator, mock { }))
        val leftController = spy(ButtonController(activity, ButtonPresenter(activity, leftButton, iconResolver), leftButton, buttonCreator, mock { }))
        uut.setComponentsButtonController(child.view, rightController, leftController)
        uut.mergeChildOptions(mergeOptions, initialOptions, parent, child)

        verify(rightController, never()).applyColor(any(), any())
        verify(leftController, never()).applyColor(any(), any())
        verify(leftController, never()).applyDisabledColor(any(), any())
        verify(leftController, never()).applyDisabledColor(any(), any())

    }


    @Test
    fun mergeChildOptions_buttonColorIsResolvedFromAppliedOptions() {
        val appliedOptions = Options()
        appliedOptions.topBar.rightButtonColor = ThemeColour(Colour(10))
        appliedOptions.topBar.leftButtonColor = ThemeColour(Colour(100))

        val options2 = Options()
        val rightButton1 = ButtonOptions()
        val rightButton2 = ButtonOptions()
        val leftButton = ButtonOptions()
        options2.topBar.buttons.right = ArrayList(listOf(rightButton1, rightButton2))
        options2.topBar.buttons.left = ArrayList(listOf(leftButton))

        uut.mergeChildOptions(options2, appliedOptions, parent, child)
        val rightCaptor = argumentCaptor<List<ButtonController>>()
        verify(topBarController).mergeRightButtons(rightCaptor.capture(), any())
        assertThat(rightCaptor.firstValue[0].button.color.get()).isEqualTo(appliedOptions.topBar.rightButtonColor.get())
        assertThat(rightCaptor.firstValue[1].button.color.get()).isEqualTo(appliedOptions.topBar.rightButtonColor.get())
        assertThat(rightCaptor.firstValue[0]).isNotEqualTo(rightButton1)
        assertThat(rightCaptor.firstValue[1]).isNotEqualTo(rightButton2)
        val leftCaptor = argumentCaptor<List<ButtonController>>()
        verify(topBarController).mergeLeftButtons(leftCaptor.capture(), any())
        assertThat(leftCaptor.firstValue[0].button.color.get()).isEqualTo(appliedOptions.topBar.leftButtonColor.get())
        assertThat(leftCaptor.firstValue[0]).isNotEqualTo(leftButton)
    }

    @Test
    fun mergeChildOptions_buttonColorIsResolvedFromMergedOptions() {
        val resolvedOptions = Options()
        resolvedOptions.topBar.rightButtonColor = ThemeColour(Colour(10))
        resolvedOptions.topBar.leftButtonColor = ThemeColour(Colour(100))

        val rightButton1 = ButtonOptions()
        val rightButton2 = ButtonOptions()
        val leftButton = ButtonOptions()
        val options2 = Options()
        options2.topBar.buttons.right = ArrayList(listOf(rightButton1, rightButton2))
        options2.topBar.buttons.left = ArrayList(listOf(leftButton))

        uut.mergeChildOptions(options2, resolvedOptions, parent, child)
        val rightCaptor = argumentCaptor<List<ButtonController>>()
        verify(topBarController).mergeRightButtons(rightCaptor.capture(), any())
        assertThat(rightCaptor.firstValue[0].button.color.get()).isEqualTo(resolvedOptions.topBar.rightButtonColor.get())
        assertThat(rightCaptor.firstValue[1].button.color.get()).isEqualTo(resolvedOptions.topBar.rightButtonColor.get())
        assertThat(rightCaptor.firstValue[0]).isNotEqualTo(rightButton1)
        assertThat(rightCaptor.firstValue[1]).isNotEqualTo(rightButton2)
        val leftCaptor = argumentCaptor<List<ButtonController>>()
        verify(topBarController).mergeLeftButtons(leftCaptor.capture(), any())
        assertThat(leftCaptor.firstValue[0].button.color.get()).isEqualTo(resolvedOptions.topBar.leftButtonColor.get())
        assertThat(leftCaptor.firstValue[0]).isNotEqualTo(leftButton)
    }

    @Test
    fun buttonControllers_buttonControllersArePassedToTopBar() {
        val options = Options()
        options.topBar.buttons.right = ArrayList(listOf(textBtn1))
        options.topBar.buttons.left = ArrayList(listOf(textBtn1))
        uut.applyChildOptions(options, parent, child)
        val rightCaptor = argumentCaptor<List<ButtonController>>()
        val leftCaptor = argumentCaptor<List<ButtonController>>()
        verify(topBarController).applyRightButtons(rightCaptor.capture())
        verify(topBarController).applyLeftButtons(leftCaptor.capture())
        assertThat(rightCaptor.firstValue.size).isOne()
        assertThat(leftCaptor.firstValue.size).isOne()
    }

    @Test
    fun buttonControllers_storesButtonsByComponent() {
        val options = Options()
        options.topBar.buttons.right = ArrayList(listOf(textBtn1))
        options.topBar.buttons.left = ArrayList(listOf(textBtn2))
        uut.applyChildOptions(options, parent, child)
        val componentButtons = uut.getComponentButtons(child.view)
        assertThat(componentButtons.size).isEqualTo(2)
        assertThat(componentButtons[0].button.text.get()).isEqualTo(textBtn1.text.get())
        assertThat(componentButtons[1].button.text.get()).isEqualTo(textBtn2.text.get())
    }

    @Test
    fun buttonControllers_createdOnce() {
        val options = Options()
        options.topBar.buttons.right = ArrayList(listOf(textBtn1))
        options.topBar.buttons.left = ArrayList(listOf(textBtn2))
        uut.applyChildOptions(options, parent, child)
        val buttons1 = uut.getComponentButtons(child.view)
        uut.applyChildOptions(options, parent, child)
        val buttons2 = uut.getComponentButtons(child.view)
        for (i in 0..1) {
            assertThat(buttons1[i]).isEqualTo(buttons2[i])
        }
    }

    @Test
    fun applyButtons_doesNotDestroyOtherComponentButtons() {
        val options = Options()
        options.topBar.buttons.right = ArrayList(listOf(componentBtn1))
        options.topBar.buttons.left = ArrayList(listOf(componentBtn2))
        uut.applyChildOptions(options, parent, child)
        val buttons = uut.getComponentButtons(child.view)
        buttons.forEach(ButtonController::ensureViewIsCreated)
        uut.applyChildOptions(options, parent, otherChild)
        buttons.forEach { assertThat(it.isDestroyed).isFalse() }
    }

    @Test
    fun applyChildOptions_shouldNotPassAnimateLeftRightButtonBarWhenNoValue() {
        val options = Options().apply {
            topBar.buttons.right = ArrayList(listOf(componentBtn1))
            topBar.buttons.left = ArrayList(listOf(componentBtn2))

        }
        uut.applyChildOptions(options, parent, child)
        verify(topBar, never()).animateLeftButtons(any())
        verify(topBar, never()).animateLeftButtons(any())
    }

    @Test
    fun applyChildOptions_shouldPassAnimateLeftRightButtonBar() {
        val options = Options().apply {
            topBar.buttons.right = ArrayList(listOf(componentBtn1))
            topBar.buttons.left = ArrayList(listOf(componentBtn2))
            topBar.animateLeftButtons = Bool(false)
            topBar.animateRightButtons = Bool(true)
        }

        uut.applyChildOptions(options, parent, child)
        verify(topBar).animateRightButtons(true)
        verify(topBar).animateLeftButtons(false)
    }

    @Test
    fun onChildDestroyed_destroyedButtons() {
        val options = Options()
        options.topBar.buttons.right = ArrayList(listOf(componentBtn1))
        options.topBar.buttons.left = ArrayList(listOf(componentBtn2))
        uut.applyChildOptions(options, parent, child)
        val buttons = uut.getComponentButtons(child.view)
        buttons.forEach(ButtonController::ensureViewIsCreated)
        uut.onChildDestroyed(child)
        buttons.forEach { assertThat(it.isDestroyed).isTrue() }
        assertThat(uut.getComponentButtons(child.view, null)).isNull()
    }

    @Test
    fun onChildDestroyed_mergedRightButtonsAreDestroyed() {
        val options = Options()
        options.topBar.buttons.right = ArrayList(listOf(componentBtn1))
        uut.mergeChildOptions(options, Options.EMPTY, parent, child)
        val buttons = uut.getComponentButtons(child.view)
        assertThat(buttons).hasSize(1)
        uut.onChildDestroyed(child)
        assertThat(buttons[0].isDestroyed).isTrue()
    }

    @Test
    fun applyChildOptions_topBarShouldExtendBehindStatusBarWhenDrawBehind() {
        val statusBarHeight = 10
        val statusBarHeightDp = 20
        val topBarHeightDp = 100
        val options = Options().apply {
            statusBar.drawBehind = Bool(true)
        }
        Mockito.`when`(child.resolveCurrentOptions()).thenReturn(options)
        mockSystemUiUtils(statusBarHeight, statusBarHeightDp) {
            uut.applyChildOptions(Options.EMPTY.copy().apply {
                topBar.height = Number(topBarHeightDp)
            }, parent, child)
            assertThat(topBar.paddingTop).isEqualTo(statusBarHeight)
            assertThat(topBar.y).isEqualTo(0f)
            assertThat(topBar.layoutParams.height).isEqualTo(statusBarHeightDp + topBarHeightDp)
        }
    }

    @Test
    fun mergeChildOptions_topBarShouldExtendBehindStatusBarWhenDrawBehind() {
        val statusBarHeight = 10
        val statusBarHeightDp = 20
        val topBarHeightDp = 100

        mockSystemUiUtils(statusBarHeight, statusBarHeightDp) {
            uut.mergeChildOptions(Options.EMPTY.copy().apply {
                topBar.height = Number(topBarHeightDp)
                statusBar.drawBehind = Bool(true)
            }, Options.EMPTY, parent, child)
            assertThat(topBar.paddingTop).isEqualTo(statusBarHeight)
            assertThat(topBar.y).isEqualTo(0f)
            assertThat(topBar.layoutParams.height).isEqualTo(statusBarHeightDp + topBarHeightDp)
        }
    }

    @Test
    fun mergeChildOptions_topBarShouldNotExtendBehindStatusBarWhenNoDrawBehind() {
        val statusBarHeight = 10
        val statusBarHeightDp = 20
        val topBarHeightDp = 100

        mockSystemUiUtils(statusBarHeight, statusBarHeightDp) {
            uut.mergeChildOptions(Options.EMPTY.copy().apply {
                topBar.height = Number(topBarHeightDp)
                statusBar.drawBehind = Bool(false)
            }, Options.EMPTY, parent, child)
            assertThat(topBar.paddingTop).isEqualTo(0)
            assertThat(topBar.y).isEqualTo(0f)
            assertThat(topBar.layoutParams.height).isEqualTo( topBarHeightDp)
        }
    }

    @Test
    fun applyTopInsets_topBarIsDrawnUnderStatusBarIfDrawBehindIsTrue() {
        val options = Options()
        options.statusBar.drawBehind = Bool(true)
        uut.applyTopInsets(parent, child)
        assertThat(topBar.y).isEqualTo(0f)
    }

    @Test
    fun applyTopInsets_topBarIsDrawnUnderStatusBarIfStatusBarIsHidden() {
        val options = Options()
        options.statusBar.visible = Bool(false)
        uut.applyTopInsets(parent, Mocks.viewController())
        assertThat(topBar.y).isEqualTo(0f)
    }

    @Test
    fun applyTopInsets_delegatesToChild() {
        uut.applyTopInsets(parent, child)
        verify(child).applyTopInset()
    }

    @Test
    fun applyChildOptions_shouldNotChangeTopMargin() {
        val options = Options()
        (topBar.layoutParams as ViewGroup.MarginLayoutParams).topMargin = 20
        uut.applyChildOptions(options, parent, child)
        assertThat((topBar.layoutParams as ViewGroup.MarginLayoutParams).topMargin).isEqualTo(20)
    }

    @Test
    fun applyChildOptions_shouldChangeTopMargin() {
        val options = Options()
        (topBar.layoutParams as ViewGroup.MarginLayoutParams).topMargin = 20
        options.topBar.topMargin = Number(10)
        uut.applyChildOptions(options, parent, child)
        assertThat((topBar.layoutParams as ViewGroup.MarginLayoutParams).topMargin).isEqualTo(10)
    }

    private fun assertTopBarOptions(options: Options, t: Int) {
        if (options.topBar.title.component.hasValue()) {
            verify(topBar, never()).title = any()
            verify(topBar, never()).setSubtitle(any())
            verify(topBar, times(t)).setTitleComponent(any<View>(), any<Alignment>())
        } else if (options.topBar.title.text.hasValue()) {
            verify(topBar, times(t)).title = any()
            verify(topBar, times(t)).setSubtitle(any())
            verify(topBar, never()).setTitleComponent(any<View>())
        }
        verify(topBar, times(t)).setBackgroundColor(any())
        verify(topBar, times(t)).setTitleTextColor(any())
        verify(topBar, times(t)).setSubtitleFontSize(any())
        verify(topBar, times(t)).setTitleTypeface(any(), any())
        verify(topBar, times(t)).setSubtitleTypeface(any(), any())
        verify(topBar, times(t)).setSubtitleColor(any())
        verify(topBar, times(t)).setTestId(any())
        verify(topBarController, times(t)).hide()
    }

    private fun createTopBarController() {
        topBarController = spy(object : TopBarController() {
            override fun createTopBar(context: Context, stackLayout: StackLayout): TopBar {
                topBar = spy(super.createTopBar(context, stackLayout))
                topBar.layout(0, 0, 1000, UiUtils.getTopBarHeight(activity))
                return topBar
            }
        })
    }

    fun component(alignment: Alignment): ComponentOptions {
        val component = ComponentOptions()
        component.name = Text("myComp")
        component.alignment = alignment
        component.componentId = Text("compId")
        return component
    }

    private fun createTypeFaceLoader(): TypefaceLoaderMock {
        val map: MutableMap<String, Typeface> = HashMap()
        map[SOME_FONT_FAMILY] = SOME_TYPEFACE
        return TypefaceLoaderMock(map)
    }

    companion object {
        private val EMPTY_OPTIONS = Options()
        const val SOME_FONT_FAMILY = "someFontFamily"
        val SOME_TYPEFACE = mock<Typeface>()
    }
}

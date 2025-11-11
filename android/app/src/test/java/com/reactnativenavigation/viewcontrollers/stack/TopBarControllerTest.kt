package com.reactnativenavigation.viewcontrollers.stack

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import org.mockito.kotlin.*
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.RNNFeatureToggles
import com.reactnativenavigation.RNNToggles
import com.reactnativenavigation.options.BackButton
import com.reactnativenavigation.options.ButtonOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.react.Constants
import com.reactnativenavigation.react.ReactView
import com.reactnativenavigation.utils.CollectionUtils
import com.reactnativenavigation.utils.OptionHelper
import com.reactnativenavigation.utils.TitleBarHelper
import com.reactnativenavigation.utils.resetViewProperties
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarAppearanceAnimator
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarController
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController
import com.reactnativenavigation.views.animations.ColorAnimator
import com.reactnativenavigation.views.stack.StackLayout
import com.reactnativenavigation.views.stack.topbar.TopBar
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import java.util.*

private const val BKG_COLOR = 0x010203

@Ignore("New architecture - WIP")
class TopBarControllerTest : BaseTest() {
    private lateinit var uut: TopBarController
    private lateinit var activity: Activity
    private lateinit var leftButton: ButtonOptions
    private lateinit var backButton: BackButton
    private lateinit var textButton1: ButtonOptions
    private lateinit var textButton2: ButtonOptions
    private lateinit var componentButton: ButtonOptions
    private lateinit var appearAnimator: TopBarAppearanceAnimator
    private lateinit var colorAnimator: ColorAnimator
    private val topBar: View
        get() = uut.view

    override fun beforeEach() {
        super.beforeEach()

        activity = newActivity()
        appearAnimator = spy(TopBarAppearanceAnimator())
        colorAnimator = mock<ColorAnimator>()

        uut = createTopBarController().apply {
            val stack = mock<StackLayout>()
            createView(activity, stack)
        }
        createButtons()
        initFeatureToggles()
    }

    @Test
    fun setButton_setsTextButton() {
        uut.applyRightButtons(rightButtons(textButton1)!!)
        uut.applyLeftButtons(leftButton(leftButton))
        assertThat(uut.getRightButton(0).title.toString()).isEqualTo(textButton1.text.get())
    }

    @Test
    fun setButton_setsCustomButton() {
        uut.applyLeftButtons(leftButton(leftButton))
        uut.applyRightButtons(rightButtons(componentButton)!!)
        val btnView = uut.getRightButton(0).actionView as ReactView
        assertThat(btnView.componentName).isEqualTo(componentButton.component.name.get())
    }

    @Test
    fun applyRightButtons_emptyButtonsListClearsRightButtons() {
        uut.applyLeftButtons(leftButton(leftButton))
        uut.applyRightButtons(rightButtons(componentButton, textButton1)!!)
        uut.applyLeftButtons(leftButton(leftButton))
        uut.applyRightButtons(ArrayList())
        assertThat(uut.rightButtonCount).isEqualTo(0)
    }

    @Test
    fun applyRightButtons_previousButtonsAreCleared() {
        uut.applyRightButtons(rightButtons(textButton1, componentButton)!!)
        assertThat(uut.rightButtonCount).isEqualTo(2)
        uut.applyRightButtons(rightButtons(textButton2)!!)
        assertThat(uut.rightButtonCount).isEqualTo(1)
    }

    @Test
    fun applyRightButtons_buttonsAreAddedInReversedOrderToMatchOrderOnIOs() {
        uut.applyLeftButtons(leftButton(leftButton))
        uut.applyRightButtons(rightButtons(textButton1, componentButton)!!)
        assertThat(uut.getRightButton(1).title.toString()).isEqualTo(textButton1.text.get())
    }

    @Test
    fun applyRightButtons_componentButtonIsReapplied() {
        val initialButtons = rightButtons(componentButton)
        uut.applyRightButtons(initialButtons!!)
        assertThat(uut.getRightButton(0).itemId).isEqualTo(componentButton.intId)
        uut.applyRightButtons(rightButtons(textButton1)!!)
        assertThat(uut.getRightButton(0).itemId).isEqualTo(textButton1.intId)
        uut.applyRightButtons(initialButtons)
        assertThat(uut.getRightButton(0).itemId).isEqualTo(componentButton.intId)
    }

    @Test
    fun mergeRightButtons_componentButtonIsNotAddedIfAlreadyAddedToMenu() {
        val initialButtons = rightButtons(componentButton)
        uut.applyRightButtons(initialButtons!!)
        uut.mergeRightButtons(initialButtons, emptyList())
    }

    @Test
    fun setLeftButtons_emptyButtonsListClearsLeftButton() {
        uut.applyLeftButtons(leftButton(leftButton))
        uut.applyRightButtons(rightButtons(componentButton)!!)
        assertThat(uut.leftButtonCount).isNotZero()
        uut.applyLeftButtons(emptyList())
        uut.applyRightButtons(rightButtons(textButton1)!!)
        assertThat(uut.leftButtonCount).isZero()
    }

    @Test
    fun setLeftButtons_clearsBackButton() {
        uut.view.setBackButton(TitleBarHelper.createButtonController(activity, backButton))
        assertThat(uut.view.navigationIcon).isNotNull()
        uut.applyLeftButtons(leftButton(leftButton))
        assertThat(uut.view.navigationIcon).isNull()
    }

    @Test
    fun setLeftButtons_emptyButtonsListClearsBackButton() {
        uut.view.setBackButton(TitleBarHelper.createButtonController(activity, backButton))
        assertThat(uut.view.navigationIcon).isNotNull()
        uut.applyLeftButtons(emptyList())
        assertThat(uut.view.navigationIcon).isNull()
    }

    @Test
    fun mergeLeftButtons_clearsBackButton() {
        uut.view.setBackButton(TitleBarHelper.createButtonController(activity, backButton))
        assertThat(uut.view.navigationIcon).isNotNull()
        uut.mergeLeftButtons(emptyList(), leftButton(leftButton))
        assertThat(uut.view.navigationIcon).isNull()
    }

    @Test
    fun mergeLeftButtons_emptyButtonsListClearsBackButton() {
        uut.view.setBackButton(TitleBarHelper.createButtonController(activity, backButton))
        assertThat(uut.view.navigationIcon).isNotNull()
        val initialButtons = leftButton(leftButton)
        uut.applyLeftButtons(initialButtons)
        uut.mergeLeftButtons(initialButtons, emptyList())
        assertThat(uut.view.navigationIcon).isNull()
    }

    @Test
    fun show() {
        uut.hide()
        assertGone(topBar)

        uut.show()
        verify(topBar).resetViewProperties()
        assertVisible(topBar)
    }

    @Test
    fun getPushAnimation_givenAnimDisabled_returnsNull() {
        val appearing = Options()
        appearing.topBar.animate = Bool(false)
        assertThat(uut.getPushAnimation(appearing)).isNull()
    }

    @Test
    fun getPushAnimation_returnsAppearanceAnimator() {
        val options = OptionHelper.emptyOptions()
        val appearAnimator = AnimatorSet()
        givenAppearAnimator(appearAnimator, options)

        val result = uut.getPushAnimation(options)
        assertThat(result).isInstanceOf(AnimatorSet::class.java)

        with(result as AnimatorSet) {
            assertThat(childAnimations).hasSize(1)
            assertThat(childAnimations[0]).isEqualTo(appearAnimator)
        }
    }

    @Test
    fun getPushAnimation_returnsColorAnimator() {
        val options = OptionHelper.builder().topBar().withColor(BKG_COLOR).build()
        val colorAnimator = ValueAnimator()
        givenColorAnimator(colorAnimator, BKG_COLOR)
        givenTopBarBackgroundAsColor()

        val result = uut.getPushAnimation(options)
        assertThat(result).isInstanceOf(AnimatorSet::class.java)

        with(result as AnimatorSet) {
            assertThat(childAnimations).hasSize(1)
            assertThat(childAnimations[0]).isEqualTo(colorAnimator)
        }
    }

    @Test
    fun getPushAnimation_givenFalseFeatureToggle_returnsNoColorAnimator() {
        val options = OptionHelper.builder().topBar().withColor(BKG_COLOR).build()
        val colorAnimator = ValueAnimator()
        givenColorAnimator(colorAnimator, BKG_COLOR)
        givenTopBarBackgroundAsColor()
        givenFeatureToggleOverrides(RNNToggles.TOP_BAR_COLOR_ANIMATION__PUSH to false)

        val result = uut.getPushAnimation(options)
        assertThat(result).isNull()
    }

    @Test
    fun getPushAnimation_givenNonStaticBkg_returnsNoColorAnimator() {
        val options = OptionHelper.builder().topBar().withColor(BKG_COLOR).build()
        val colorAnimator = ValueAnimator()
        givenColorAnimator(colorAnimator, BKG_COLOR)
        givenTopBarBackgroundAsDrawableResource()

        val result = uut.getPushAnimation(options)
        assertThat(result).isNull()
    }

    @Test
    fun getPushAnimation_returnsBothAnimators() {
        val options = OptionHelper.builder().topBar().withColor(BKG_COLOR).build()
        givenAppearAnimator(AnimatorSet(), options)
        givenColorAnimator(ValueAnimator(), BKG_COLOR)
        givenTopBarBackgroundAsColor()

        val result = uut.getPushAnimation(options)
        assertThat(result).isInstanceOf(AnimatorSet::class.java)
        assertThat((result as AnimatorSet).childAnimations).hasSize(2)
    }

    @Test
    fun getPopAnimation_returnsDisappearAnimator() {
        val disappearAnimator = AnimatorSet()
        val appearOpts = OptionHelper.emptyOptions()
        val disappearOpts = OptionHelper.emptyOptions()
        givenDisappearAnimator(disappearAnimator, appearOpts, disappearOpts)

        val result = uut.getPopAnimation(appearOpts, disappearOpts)
        assertThat(result).isInstanceOf(AnimatorSet::class.java)

        with(result as AnimatorSet) {
            assertThat(childAnimations).hasSize(1)
            assertThat(childAnimations[0]).isEqualTo(disappearAnimator)
        }
    }

    @Test
    fun getPopAnimation_returnsColorAnimator() {
        val colorAnimator = ValueAnimator()
        val appearOpts = OptionHelper.builder().topBar().withColor(BKG_COLOR).build()
        val disappearOpts = OptionHelper.emptyOptions()
        givenColorAnimator(colorAnimator, BKG_COLOR)
        givenTopBarBackgroundAsColor()

        val result = uut.getPopAnimation(appearOpts, disappearOpts)
        assertThat(result).isInstanceOf(AnimatorSet::class.java)

        with(result as AnimatorSet) {
            assertThat(childAnimations).hasSize(1)
            assertThat(childAnimations[0]).isEqualTo(colorAnimator)
        }
    }

    @Test
    fun getPopAnimation_givenFalseFeatureToggle_returnsNoColorAnimator() {
        val colorAnimator = ValueAnimator()
        val appearOpts = OptionHelper.builder().topBar().withColor(BKG_COLOR).build()
        val disappearOpts = OptionHelper.emptyOptions()
        givenColorAnimator(colorAnimator, BKG_COLOR)
        givenTopBarBackgroundAsColor()
        givenFeatureToggleOverrides(RNNToggles.TOP_BAR_COLOR_ANIMATION__PUSH to false)

        val result = uut.getPopAnimation(appearOpts, disappearOpts)
        assertThat(result).isNull()
    }

    @Test
    fun getPopAnimation_givenNonStaticBkg_returnsNoColorAnimator() {
        val colorAnimator = ValueAnimator()
        val appearOpts = OptionHelper.builder().topBar().withColor(BKG_COLOR).build()
        val disappearOpts = OptionHelper.emptyOptions()
        givenColorAnimator(colorAnimator, BKG_COLOR)
        givenTopBarBackgroundAsDrawableResource()

        val result = uut.getPopAnimation(appearOpts, disappearOpts)
        assertThat(result).isNull()
    }

    @Test
    fun getPopAnimation_givenAnimDisabled_returnsNull() {
        val appearing = Options()
        val disappearing = OptionHelper.builder().topBar().withDisabledAnim().build()
        assertThat(uut.getPopAnimation(appearing, disappearing)).isNull()
    }

    @Test
    fun getPopAnimation_returnsBothAnimators() {
        val appearOpts = OptionHelper.builder().topBar().withColor(BKG_COLOR).build()
        val disappearOpts = OptionHelper.emptyOptions()
        givenDisappearAnimator(AnimatorSet(), appearOpts, disappearOpts)
        givenColorAnimator(ValueAnimator(), BKG_COLOR)
        givenTopBarBackgroundAsColor()

        val result = uut.getPopAnimation(appearOpts, disappearOpts)
        assertThat(result).isInstanceOf(AnimatorSet::class.java)
        assertThat((result as AnimatorSet).childAnimations).hasSize(2)
    }

    @Test
    fun getSetStackRootAnimation_returnsNullIfAnimateFalse() {
        val appearing = Options()
        appearing.topBar.animate = Bool(false)
        assertThat(uut.getSetStackRootAnimation(appearing)).isNull()
    }

    @Test
    fun getSetStackRootAnimation_delegatesToAnimator() {
        val someAnimator = AnimatorSet()
        val options = Options.EMPTY
        doReturn(someAnimator).whenever(appearAnimator).getSetStackRootAnimation(
                options.animations.setStackRoot.topBar,
                options.topBar.visible,
                0f
        )
        val result = uut.getSetStackRootAnimation(options)
        assertThat(result).isEqualTo(someAnimator)
    }

    private fun createButtons() {
        leftButton = ButtonOptions()
        leftButton.id = Constants.BACK_BUTTON_ID
        backButton = BackButton.parse(activity, null)
        textButton1 = createTextButton("1")
        textButton2 = createTextButton("2")
        componentButton = ButtonOptions()
        componentButton.id = "customBtn"
        componentButton.component.name = Text("com.rnn.customBtn")
        componentButton.component.componentId = Text("component4")
    }

    private fun initFeatureToggles() {
        RNNFeatureToggles.clear()
        RNNFeatureToggles.init(
            RNNToggles.TOP_BAR_COLOR_ANIMATION__PUSH to true
        )
    }

    private fun createTextButton(id: String): ButtonOptions {
        val button = ButtonOptions()
        button.id = id
        button.text = Text("txt$id")
        return button
    }

    private fun leftButton(leftButton: ButtonOptions): List<ButtonController> {
        return listOf(TitleBarHelper.createButtonController(activity, leftButton))
    }

    private fun rightButtons(vararg buttons: ButtonOptions): List<ButtonController>? {
        return CollectionUtils.map(listOf(*buttons)) { button: ButtonOptions? -> TitleBarHelper.createButtonController(activity, button) }
    }

    private fun createTopBarController() = spy(object : TopBarController(appearAnimator, colorAnimator) {
        override fun createTopBar(context: Context, stackLayout: StackLayout): TopBar {
            return spy(super.createTopBar(context, stackLayout))
        }
    })

    private fun givenAppearAnimator(animator: Animator, options: Options) {
        doReturn(animator).whenever(appearAnimator).getPushAnimation(
            options.animations.push.topBar,
            options.topBar.visible,
            0f
        )
    }

    private fun givenDisappearAnimator(animator: Animator, appearOpts: Options, disappearOpts: Options) {
        doReturn(animator).whenever(appearAnimator).getPopAnimation(
            disappearOpts.animations.pop.topBar,
            appearOpts.topBar.visible,
            0f
        )
    }

    private fun givenColorAnimator(animator: ValueAnimator, color: Int) {
        whenever(colorAnimator.getAnimation(eq(topBar), any(), eq(color)))
            .thenReturn(animator)
    }

    private fun givenTopBarBackgroundAsColor() {
        topBar.setBackgroundColor(Color.BLACK)
    }

    private fun givenTopBarBackgroundAsDrawableResource() {
        topBar.setBackgroundResource(0)
    }

    private fun givenFeatureToggleOverrides(vararg overrides: Pair<RNNToggles, Boolean>) {
        RNNFeatureToggles.clear()
        RNNFeatureToggles.init(*overrides)
    }
}

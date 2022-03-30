package com.reactnativenavigation.viewcontrollers.stack

import android.animation.AnimatorSet
import android.app.Activity
import android.content.Context
import android.view.View
import org.mockito.kotlin.*
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.options.BackButton
import com.reactnativenavigation.options.ButtonOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.react.Constants
import com.reactnativenavigation.react.ReactView
import com.reactnativenavigation.utils.CollectionUtils
import com.reactnativenavigation.utils.TitleBarHelper
import com.reactnativenavigation.utils.resetViewProperties
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarAnimator
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarController
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController
import com.reactnativenavigation.views.stack.StackLayout
import com.reactnativenavigation.views.stack.topbar.TopBar
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import java.util.*

class TopBarControllerTest : BaseTest() {
    private lateinit var uut: TopBarController
    private lateinit var activity: Activity
    private lateinit var leftButton: ButtonOptions
    private lateinit var backButton: BackButton
    private lateinit var textButton1: ButtonOptions
    private lateinit var textButton2: ButtonOptions
    private lateinit var componentButton: ButtonOptions
    private lateinit var animator: TopBarAnimator
    private val topBar: View
        get() = uut.view

    override fun beforeEach() {
        activity = newActivity()
        animator = spy(TopBarAnimator())
        uut = createTopBarController()
        val stack = mock<StackLayout>()
        uut.createView(activity, stack)
        createButtons()
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
    fun getPushAnimation_returnsNullIfAnimateFalse() {
        val appearing = Options()
        appearing.topBar.animate = Bool(false)
        assertThat(uut.getPushAnimation(appearing)).isNull()
    }

    @Test
    fun getPushAnimation_delegatesToAnimator() {
        val someAnimator = AnimatorSet()
        val options = Options.EMPTY
        doReturn(someAnimator).whenever(animator).getPushAnimation(
                options.animations.push.topBar,
                options.topBar.visible,
                0f
        )
        val result = uut.getPushAnimation(options)
        assertThat(result).isEqualTo(someAnimator)
    }

    @Test
    fun getPopAnimation_returnsNullIfAnimateFalse() {
        val appearing = Options()
        val disappearing = Options()
        disappearing.topBar.animate = Bool(false)
        assertThat(uut.getPopAnimation(appearing, disappearing)).isNull()
    }

    @Test
    fun getPopAnimation_delegatesToAnimator() {
        val someAnimator = AnimatorSet()
        val appearing = Options.EMPTY
        val disappearing = Options.EMPTY
        doReturn(someAnimator).whenever(animator).getPopAnimation(
                disappearing.animations.pop.topBar,
                appearing.topBar.visible,
                0f
        )
        val result = uut.getPopAnimation(appearing, disappearing)
        assertThat(result).isEqualTo(someAnimator)
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
        doReturn(someAnimator).whenever(animator).getSetStackRootAnimation(
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

    private fun createTopBarController() = spy(object : TopBarController(animator) {
        override fun createTopBar(context: Context, stackLayout: StackLayout): TopBar {
            return spy(super.createTopBar(context, stackLayout))
        }
    })
}
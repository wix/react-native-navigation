package com.reactnativenavigation.viewcontrollers.stack

import android.animation.AnimatorSet
import android.app.Activity
import android.content.Context
import android.view.View
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.fakes.IconResolverFake
import com.reactnativenavigation.mocks.TitleBarButtonCreatorMock
import com.reactnativenavigation.options.BackButton
import com.reactnativenavigation.options.ButtonOptions
import com.reactnativenavigation.options.ComponentOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.react.Constants
import com.reactnativenavigation.react.ReactView
import com.reactnativenavigation.utils.TitleBarHelper
import com.reactnativenavigation.utils.resetViewProperties
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarAnimator
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarController
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonPresenter
import com.reactnativenavigation.views.stack.StackLayout
import com.reactnativenavigation.views.stack.topbar.TopBar
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.*

class TopBarControllerTest : BaseTest() {
    private lateinit var uut: TopBarController
    private lateinit var activity: Activity
    private lateinit var leftButton: ButtonOptions
    private lateinit var backButton: BackButton
    private lateinit var textButton1: ButtonOptions
    private lateinit var textButton2: ButtonOptions
    private lateinit var componentButton: ButtonOptions
    private lateinit var animator: TopBarAnimator
    private lateinit var leftButtonControllers: MutableMap<String,ButtonController>
    private lateinit var rightButtonControllers:  MutableMap<String,ButtonController>


    private val topBar: View
        get() = uut.view

    override fun beforeEach() {
        leftButtonControllers= mutableMapOf()
        rightButtonControllers= mutableMapOf()
        activity = newActivity()
        animator = spy(TopBarAnimator())
        uut = createTopBarController()
        val stack = mock<StackLayout>()
        uut.createView(activity, stack)
        createButtons()
    }

    @Test
    fun setButton_setsTextButton() {
        uut.applyRightButtonsOptions(rightButtonControllers, listOf(textButton1)){
            createButtonController(it)
        }
        uut.applyLeftButtonsOptions(leftButtonControllers, listOf(leftButton)){
            createButtonController(it)
        }
        assertThat(uut.getRightButton(0).title.toString()).isEqualTo(textButton1.text.get())
    }

    @Test
    fun setButton_setsCustomButton() {
        uut.applyLeftButtonsOptions(leftButtonControllers, listOf(leftButton)){
            createButtonController(it)
        }
        uut.applyRightButtonsOptions(rightButtonControllers, listOf(componentButton)){
            createButtonController(it)
        }
        val btnView = uut.getRightButton(0).actionView as ReactView
        assertThat(btnView.componentName).isEqualTo(componentButton.component.name.get())
    }

    @Test
    fun applyRightButtons_emptyButtonsListClearsRightButtons() {
        uut.applyLeftButtonsOptions(leftButtonControllers, listOf(leftButton)){
            createButtonController(it)
        }
        uut.applyRightButtonsOptions(rightButtonControllers, listOf(componentButton, textButton1)){
            createButtonController(it)
        }
        uut.applyLeftButtonsOptions(leftButtonControllers, listOf(leftButton)){
            createButtonController(it)
        }
        uut.applyRightButtonsOptions(rightButtonControllers, listOf()){
            createButtonController(it)
        }
        assertThat(uut.rightButtonCount).isEqualTo(0)
    }

    @Test
    fun applyRightButtons_previousButtonsAreCleared() {
        uut.applyRightButtonsOptions(rightButtonControllers, listOf(textButton1, componentButton)){
            createButtonController(it)
        }
        assertThat(uut.rightButtonCount).isEqualTo(2)
        uut.applyRightButtonsOptions(rightButtonControllers, listOf(textButton2)){
            createButtonController(it)
        }
        assertThat(uut.rightButtonCount).isEqualTo(1)
    }

    @Test
    fun applyRightButtons_sameButtonsAreUpdated() {
        uut.applyRightButtonsOptions(rightButtonControllers, listOf(textButton1)){
            createButtonController(it)
        }
        val copy = textButton1.copy()
        copy.enabled= Bool(false)
        uut.applyRightButtonsOptions(rightButtonControllers, listOf(copy)){
            createButtonController(it)
        }
        assertThat(rightButtonControllers[copy.id]?.button?.enabled?.get()).isFalse()
    }


    @Test
    fun applyRightButtons_buttonsAreAddedInReversedOrderToMatchOrderOnIOs() {
        uut.applyLeftButtonsOptions(leftButtonControllers, listOf(leftButton)){
            createButtonController(it)
        }
        uut.applyRightButtonsOptions(rightButtonControllers, listOf(textButton1, componentButton)){
            createButtonController(it)
        }
        assertThat(uut.getRightButton(1).title.toString()).isEqualTo(textButton1.text.get())
    }

    @Test
    fun applyRightButtons_componentButtonIsReapplied() {
        uut.applyRightButtonsOptions(rightButtonControllers, listOf( componentButton)){
            createButtonController(it)
        }
        assertThat(uut.getRightButton(0).itemId).isEqualTo(componentButton.intId)
        uut.applyRightButtonsOptions(rightButtonControllers, listOf( textButton1)){
            createButtonController(it)
        }
        assertThat(uut.getRightButton(0).itemId).isEqualTo(textButton1.intId)
        uut.applyRightButtonsOptions(rightButtonControllers, listOf( componentButton)){
            createButtonController(it)
        }
        assertThat(uut.getRightButton(0).itemId).isEqualTo(componentButton.intId)
    }

    @Test
    fun mergeRightButtonsOptions_componentButtonIsNotAddedIfAlreadyAddedToMenu() {
        val controllers = mutableMapOf<String,ButtonController>()
        uut.applyRightButtonsOptions(controllers, listOf(componentButton)){
            createButtonController(it)
        }
        verify(controllers[componentButton.id]!!, times(1)).addToMenu(any(), any())
        uut.mergeRightButtonsOptions(controllers, listOf(componentButton.copy())){
            createButtonController(it)
        }
        verify(controllers[componentButton.id]!!, times(1)).addToMenu(any(), any())
    }

    @Test
    fun setLeftButtons_emptyButtonsListClearsLeftButton() {
        uut.applyLeftButtonsOptions(leftButtonControllers, listOf(leftButton)){
            createButtonController(it)
        }
        uut.applyRightButtonsOptions(rightButtonControllers, listOf( componentButton)){
            createButtonController(it)
        }
        assertThat(uut.leftButtonCount).isNotZero()
        uut.applyLeftButtonsOptions(leftButtonControllers, listOf()){
            createButtonController(it)
        }
        uut.applyRightButtonsOptions(rightButtonControllers, listOf( textButton1)){
            createButtonController(it)
        }
        assertThat(uut.leftButtonCount).isZero()
    }

    @Test
    fun setLeftButtons_clearsBackButton() {
        uut.view.setBackButton(TitleBarHelper.createButtonController(activity, backButton))
        assertThat(uut.view.navigationIcon).isNotNull()
        uut.applyLeftButtonsOptions(leftButtonControllers, listOf(leftButton)){
            createButtonController(it)
        }
        assertThat(uut.view.navigationIcon).isNull()
    }

    @Test
    fun setLeftButtons_emptyButtonsListClearsBackButton() {
        uut.view.setBackButton(TitleBarHelper.createButtonController(activity, backButton))
        assertThat(uut.view.navigationIcon).isNotNull()
        uut.applyLeftButtonsOptions(leftButtonControllers, listOf()){
            createButtonController(it)
        }
        assertThat(uut.view.navigationIcon).isNull()
    }

    @Test
    fun mergeLeftButtons_clearsBackButton() {
        val controllers = mutableMapOf<String,ButtonController>()
        uut.view.setBackButton(TitleBarHelper.createButtonController(activity, backButton))
        assertThat(uut.view.navigationIcon).isNotNull()
        uut.mergeLeftButtonsOptions(controllers, listOf(leftButton)){
            createButtonController(it)
        }
        assertThat(uut.view.navigationIcon).isNull()
    }

    @Test
    fun mergeLeftButtons_emptyButtonsListClearsBackButton() {
        val controllers = mutableMapOf<String,ButtonController>()

        uut.view.setBackButton(TitleBarHelper.createButtonController(activity, backButton))
        assertThat(uut.view.navigationIcon).isNotNull()
        uut.applyLeftButtonsOptions(controllers, listOf(leftButton)){
            createButtonController(it)
        }
        uut.mergeLeftButtonsOptions(controllers, emptyList()){
            createButtonController(it)
        }
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

    @Test
    fun `mergeRightButtons - should add buttons`(){
        val controllers = spy(LinkedHashMap<String,ButtonController>())
        val controller = spy(ButtonController(activity, ButtonPresenter(activity, textButton1, IconResolverFake(activity)),
            textButton1, TitleBarButtonCreatorMock(), object : ButtonController.OnClickListener {
                override fun onPress(button: ButtonOptions) {

                }

            }))
        uut.mergeRightButtonsOptions(controllers, listOf(textButton1)) {
                controller
        }
        assertThat(uut.rightButtonCount).isEqualTo(1)
        verify(controllers, never()).remove(any())
        assertThat(controllers[textButton1.id]).isEqualTo(controller)
    }
    @Test
    fun `mergeRightOptions - should destroy all buttons that was removed`(){
       val componentButton2 = componentButton.copy()
        componentButton2.component = ComponentOptions().apply {
           this.name = componentButton.component.name
           this.componentId = Text("CustomNewComponent")
        }
        uut.mergeRightButtonsOptions(rightButtonControllers, listOf(textButton1, textButton2, componentButton)) {
            createButtonController(it)
        }
        val removedControllers = mutableMapOf<String, ButtonController>().apply {
            putAll(rightButtonControllers)
        }
        uut.mergeRightButtonsOptions(rightButtonControllers, listOf(componentButton2)) {
            createButtonController(it)
        }
        verify(removedControllers[textButton1.id]!!, times(1)).destroy()
        verify(removedControllers[textButton2.id]!!, times(1)).destroy()
        verify(removedControllers[componentButton.id]!!, times(1)).destroy()
    }
    @Test
    fun `mergeRightButtons - should remove all and re-add buttons in case of reorder, without destroy`(){
        uut.mergeRightButtonsOptions(rightButtonControllers, listOf(textButton1, textButton2)) {
            createButtonController(it)
        }
        assertThat(uut.getRightButton(1).itemId ).isEqualTo(textButton1.intId)
        assertThat(uut.getRightButton(0).itemId ).isEqualTo(textButton2.intId)
        val removedControllers = mutableMapOf<String, ButtonController>().apply { putAll(rightButtonControllers) }
        uut.mergeRightButtonsOptions(rightButtonControllers, listOf(textButton2.copy(), textButton1.copy())) {
            createButtonController(it)
        }
        assertThat(uut.getRightButton(1).itemId ).isEqualTo(textButton2.intId)
        assertThat(uut.getRightButton(0).itemId ).isEqualTo(textButton1.intId)

        verify(removedControllers[textButton1.id]!!, never()).destroy()
        verify(removedControllers[textButton2.id]!!, never()).destroy()

        verify(rightButtonControllers[textButton1.id]!!, times(2)).addToMenu(any(), any())
        verify(rightButtonControllers[textButton2.id]!!, times(2)).addToMenu(any(), any())
    }
    @Test
    fun `mergeRightButtons - should rebuild menu when adding menu items, existing should not be destroyed`(){
        val controllers = spy(LinkedHashMap<String,ButtonController>())
        uut.mergeRightButtonsOptions(controllers, listOf(textButton1)) {
            createButtonController(it)
        }
        assertThat(uut.rightButtonCount).isEqualTo(1)

        uut.mergeRightButtonsOptions(controllers, listOf(textButton1, textButton2)) {
            createButtonController(it)
        }
        assertThat(uut.rightButtonCount).isEqualTo(2)
        verify(controllers, never()).remove(any())
        verify(controllers[textButton1.id]!!, times(2)).addToMenu(any(), any())
    }
    @Test
    fun `mergeRightButtons - should modify changed buttons`(){
        val controllers = spy(LinkedHashMap<String,ButtonController>())
        uut.mergeRightButtonsOptions(controllers, listOf(textButton1.apply {
            this.enabled = Bool(true)
        })) {
            createButtonController(it)
        }
        assertThat(uut.rightButtonCount).isEqualTo(1)
        verify(controllers[textButton1.id]!!, times(1)).addToMenu(any(), any())

        uut.mergeRightButtonsOptions(controllers, listOf(textButton1.copy().apply { this.enabled= Bool(false) })) {
            createButtonController(it)
        }
        verify(controllers, never()).remove(any())
        verify(controllers[textButton1.id]!!, times(1)).mergeButtonOptions(any(), any())
        verify(controllers[textButton1.id]!!, times(1)).addToMenu(any(), any())
        verify(controllers[textButton1.id]!!, never()).destroy()
    }

    @Test
    fun `mergeRightButtons - reorder of same menu items should rebuild menu, not view recreation`(){
        val controllers = spy(LinkedHashMap<String,ButtonController>())
        uut.mergeRightButtonsOptions(controllers, listOf(textButton1, textButton2)) {
            createButtonController(it)
        }
        assertThat(uut.rightButtonCount).isEqualTo(2)
        verify(controllers[textButton1.id]!!, times(1)).addToMenu(any(), any())
        verify(controllers[textButton2.id]!!, times(1)).addToMenu(any(), any())

        uut.mergeRightButtonsOptions(controllers, listOf(textButton2.copy(), textButton1.copy())) {
            createButtonController(it)
        }
        verify(controllers[textButton1.id]!!, never()).mergeButtonOptions(any(), any())
        verify(controllers[textButton2.id]!!, never()).mergeButtonOptions(any(), any())
        verify(controllers[textButton1.id]!!, times(2)).addToMenu(any(), any())
        verify(controllers[textButton2.id]!!, times(2)).addToMenu(any(), any())
        verify(controllers[textButton1.id]!!, never()).destroy()
        verify(controllers[textButton2.id]!!, never()).destroy()
    }

    private fun createButtonController(it: ButtonOptions) =
        spy(ButtonController(activity, ButtonPresenter(activity, it, IconResolverFake(activity)),
            it, TitleBarButtonCreatorMock(), object : ButtonController.OnClickListener {
                override fun onPress(button: ButtonOptions) {

                }

            }))

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

    private fun createTopBarController() = spy(object : TopBarController(animator) {
        override fun createTopBar(context: Context, stackLayout: StackLayout): TopBar {
            return spy(super.createTopBar(context, stackLayout))
        }
    })

}
package com.reactnativenavigation.viewcontrollers.stack.topbar

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.view.MenuItem
import android.view.View
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.viewpager.widget.ViewPager
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.options.ButtonOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.resetViewProperties
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController
import com.reactnativenavigation.viewcontrollers.stack.topbar.title.TitleBarReactViewController
import com.reactnativenavigation.views.stack.StackLayout
import com.reactnativenavigation.views.stack.topbar.TopBar
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonBar

const val DEFAULT_BORDER_COLOR = Color.BLACK

open class TopBarController(private val animator: TopBarAnimator = TopBarAnimator()) {
    lateinit var view: TopBar
    private lateinit var leftButtonBar: ButtonBar
    private lateinit var rightButtonBar: ButtonBar
    private val buttonsTransition = AutoTransition()

    val height: Int
        get() = view.height
    val rightButtonCount: Int
        get() = rightButtonBar.buttonCount
    val leftButtonCount: Int
        get() = leftButtonBar.buttonCount

    fun getRightButton(index: Int): MenuItem = rightButtonBar.getButton(index)

    fun createView(context: Context, parent: StackLayout): TopBar {
        if (!::view.isInitialized) {
            view = createTopBar(context, parent)
            leftButtonBar = view.leftButtonBar
            rightButtonBar = view.rightButtonBar
            animator.bindView(view)
        }
        return view
    }

    protected open fun createTopBar(context: Context, stackLayout: StackLayout): TopBar {
        return TopBar(context)
    }

    fun initTopTabs(viewPager: ViewPager?) = view.initTopTabs(viewPager)

    fun clearTopTabs() = view.clearTopTabs()

    fun getPushAnimation(appearingOptions: Options, additionalDy: Float = 0f): Animator? {
        if (appearingOptions.topBar.animate.isFalse) return null
        return animator.getPushAnimation(
            appearingOptions.animations.push.topBar,
            appearingOptions.topBar.visible,
            additionalDy
        )
    }

    fun getPopAnimation(appearingOptions: Options, disappearingOptions: Options): Animator? {
        if (appearingOptions.topBar.animate.isFalse) return null
        return animator.getPopAnimation(
            disappearingOptions.animations.pop.topBar,
            appearingOptions.topBar.visible
        )
    }

    fun getSetStackRootAnimation(appearingOptions: Options, additionalDy: Float = 0f): Animator? {
        if (appearingOptions.topBar.animate.isFalse) return null
        return animator.getSetStackRootAnimation(
            appearingOptions.animations.setStackRoot.topBar,
            appearingOptions.topBar.visible,
            additionalDy
        )
    }

    fun show() {
        if (ViewUtils.isVisible(view) || animator.isAnimatingShow()) return
        view.resetViewProperties()
        view.visibility = View.VISIBLE
    }

    fun showAnimate(options: AnimationOptions, additionalDy: Float) {
        if (ViewUtils.isVisible(view) || animator.isAnimatingShow()) return
        animator.show(options, additionalDy)
    }

    fun hide() {
        if (!animator.isAnimatingHide()) view.visibility = View.GONE
    }

    fun hideAnimate(options: AnimationOptions, additionalDy: Float) {
        if (!ViewUtils.isVisible(view) || animator.isAnimatingHide()) return
        animator.hide(options, additionalDy)
    }

    fun setTitleComponent(component: TitleBarReactViewController) {
        view.setTitleComponent(component.view, component.component?.alignment ?: Alignment.Default)
    }

    fun alignTitleComponent(alignment: Alignment) {
        view.alignTitleComponent(alignment)
    }

    fun clearRightButtons() {
        view.clearRightButtons()
    }

    fun clearLeftButtons() {
        view.clearLeftButtons()
    }

    fun clearBackButton() {
        view.clearBackButton()
    }

    fun setBackButton(backButton: ButtonController?) {
        backButton?.let { view.setBackButton(it) }
    }

    fun animateRightButtons(shouldAnimate: Boolean) {
        view.animateRightButtons(shouldAnimate)
    }

    fun animateLeftButtons(shouldAnimate: Boolean) {
        view.animateLeftButtons(shouldAnimate)
    }

    fun mergeRightButtonsOptions(
        btnControllers: MutableMap<String, ButtonController>,
        rightButtons: List<ButtonOptions>,
        controllerCreator: (ButtonOptions) -> ButtonController
    ) {
        mergeButtonOptions(btnControllers, rightButtons.reversed(), controllerCreator, rightButtonBar)
    }

    fun mergeLeftButtonsOptions(
        btnControllers: MutableMap<String, ButtonController>,
        leftButtons: List<ButtonOptions>,
        controllerCreator: (ButtonOptions) -> ButtonController
    ) {
        clearBackButton()
        mergeButtonOptions(btnControllers, leftButtons, controllerCreator, leftButtonBar)
    }

    fun applyRightButtonsOptions(
        btnControllers: MutableMap<String, ButtonController>,
        rightButtons: List<ButtonOptions>,
        controllerCreator: (ButtonOptions) -> ButtonController
    ) {
        applyButtonsOptions(
            btnControllers,
            rightButtons.reversed(),
            controllerCreator,
            rightButtonBar
        )
    }

    fun applyLeftButtonsOptions(
        btnControllers: MutableMap<String, ButtonController>,
        leftButtons: List<ButtonOptions>,
        controllerCreator: (ButtonOptions) -> ButtonController
    ) {
        applyButtonsOptions(btnControllers, leftButtons, controllerCreator, leftButtonBar)
    }

    private fun applyButtonsOptions(
        btnControllers: MutableMap<String, ButtonController>,
        buttons: List<ButtonOptions>,
        controllerCreator: (ButtonOptions) -> ButtonController,
        buttonBar: ButtonBar
    ) {
        if (buttonBar.shouldAnimate)
            TransitionManager.beginDelayedTransition(buttonBar, buttonsTransition)

        buttonBar.clearButtons()
        buttons.forEachIndexed { index, it ->
            val order = index * 10
            val newController = if (btnControllers.containsKey(it.id)) {
                btnControllers.remove(it.id)
            } else {
                controllerCreator(it)
            }!!

            newController.addToMenu(buttonBar, order)
            btnControllers[it.id] = newController
        }
    }


    private fun mergeButtonOptions(
        btnControllers: MutableMap<String, ButtonController>,
        buttons: List<ButtonOptions>,
        controllerCreator: (ButtonOptions) -> ButtonController,
        buttonBar: ButtonBar
    ) {
        fun hasChangedOrder(): Boolean {
            val values = btnControllers.values
            return buttons.filterIndexed { index, buttonOptions ->
                val buttonController = btnControllers[buttonOptions.id]
                values.indexOf(buttonController) == index
            }.size != buttons.size
        }

        fun sameIdDifferentCompId(
            toUpdate: MutableMap<String, Int>,
            ctrl: Map.Entry<String, ButtonController>,
            buttons: List<ButtonOptions>
        ) = if (toUpdate.containsKey(ctrl.key)
            && ctrl.value.button.hasComponent()
            && buttons[toUpdate[ctrl.key]!!].component.componentId != ctrl.value.button.component.componentId
        ) {
            toUpdate.remove(ctrl.key)
            true
        } else false

        val requestedButtons = buttons.mapIndexed { index, buttonOptions -> buttonOptions.id to index }.toMap()
        var toUpdate = requestedButtons.filter {
            btnControllers[it.key]?.areButtonOptionsChanged(buttons[it.value]) ?: false
        }.toMutableMap()
        var toAdd = requestedButtons.filter { !btnControllers.containsKey(it.key) }
        var toRemove = btnControllers.filter { ctrl -> !requestedButtons.containsKey(ctrl.key) }
        val toDestroy = btnControllers.filter { ctrl -> sameIdDifferentCompId(toUpdate, ctrl, buttons) }
            .toMutableMap().apply { this.putAll(toRemove) }

        fun  needsRebuild(): Boolean {
           return if (toUpdate.size == buttons.size) {
                hasChangedOrder()
            } else toAdd.isNotEmpty() || toRemove.isNotEmpty()
        }

        if (needsRebuild()) {
            toUpdate = mutableMapOf()
            toAdd = requestedButtons
            toRemove = btnControllers.toMap()
            if (buttonBar.shouldAnimate)
                TransitionManager.beginDelayedTransition(buttonBar, buttonsTransition)
        }

        toUpdate.forEach {
            val button = buttons[it.value]
            btnControllers[button.id]?.mergeButtonOptions(button, buttonBar)
        }
        toRemove.forEach {
            buttonBar.removeButton(it.value.buttonIntId)
            btnControllers.remove(it.key)
        }
        toDestroy.values.forEach {
            it.destroy()
        }
        toAdd.forEach {
            val button = buttons[it.value]
            val order = it.value * 10
            val newController = btnControllers[button.id] ?: controllerCreator(button)
            newController.addToMenu(buttonBar, order)
            btnControllers[button.id] = newController
        }
    }


    fun onConfigurationChanged(
        options: Options,
        leftBtnControllers: MutableMap<String, ButtonController>?,
        rightBtnControllers: MutableMap<String, ButtonController>?
    ) {
        leftBtnControllers?.values?.forEach {
            it.onConfigurationChanged(leftButtonBar)
        }
        rightBtnControllers?.values?.forEach {
            it.onConfigurationChanged(rightButtonBar)
        }

        view.setOverflowButtonColor(options.topBar.rightButtonColor.get(Color.BLACK)!!)
        view.applyTopTabsColors(
            options.topTabs.selectedTabColor,
            options.topTabs.unselectedTabColor
        )
        view.setBorderColor(options.topBar.borderColor.get(DEFAULT_BORDER_COLOR)!!)
        view.setBackgroundColor(options.topBar.background.color.get(Color.WHITE)!!)
        view.setTitleTextColor(options.topBar.title.color.get(TopBar.DEFAULT_TITLE_COLOR)!!)
        view.setSubtitleColor(options.topBar.subtitle.color.get(TopBar.DEFAULT_TITLE_COLOR)!!)
    }
}
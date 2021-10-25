package com.reactnativenavigation.viewcontrollers.stack.topbar

import android.animation.Animator
import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.viewpager.widget.ViewPager
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.options.ButtonOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.utils.CollectionUtils.forEachIndexed
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.logd
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

    fun applyRightButtons(toAdd: List<ButtonController>) {
        view.clearRightButtons()
        toAdd.reversed().forEachIndexed { i, b -> b.addToMenu(rightButtonBar, i * 10) }
    }

    open fun applyLeftButtons(toAdd: List<ButtonController>) {
        view.clearBackButton()
        view.clearLeftButtons()
        forEachIndexed(toAdd) { b: ButtonController, i: Int -> b.addToMenu(leftButtonBar, i * 10) }
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
        applyButtonsOptions(btnControllers, rightButtons.reversed(), controllerCreator, rightButtonBar)
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
        val intersect = buttons.map { it.id }.intersect(btnControllers.values.map { it.button.id })
        logd(
            "Apply ButtonsOptions ${if (rightButtonBar == buttonBar) "Right" else "Left"} intersect ${intersect.size}",
            "NewBarMerge"
        )

        if (intersect.size != buttons.size) {
            if (buttonBar.shouldAnimate)
                TransitionManager.beginDelayedTransition(buttonBar, buttonsTransition)
            buttonBar.clearButtons()
            btnControllers.map { it.key }.forEach {
                btnControllers.remove(it)?.destroy()
            }
            buttons.forEachIndexed { index, it ->
                val order = (btnControllers.size + index) * 10
                val newController = controllerCreator(it)
                newController.addToMenu(buttonBar, order)
                btnControllers[it.id] = newController
            }
        }
    }

    private fun mergeButtonOptions(
        btnControllers: MutableMap<String, ButtonController>,
        buttons: List<ButtonOptions>,
        controllerCreator: (ButtonOptions) -> ButtonController,
        buttonBar: ButtonBar
    ) {

        var updatedButtons = buttons.filter { btnControllers[it.id]?.areButtonOptionsChanged(it) ?: false }
        var added = buttons.filterNot { btnControllers.containsKey(it.id) }
        var removed = btnControllers.filterNot { ctrl ->
            buttons.any {
                ctrl.key == it.id
            }
        }.map { it.key to it.value.buttonIntId }

        val rebuildMenu = if (updatedButtons.size == buttons.size) {
            val values = btnControllers.values
            buttons.filterIndexed { index, buttonOptions ->
                val buttonController = btnControllers[buttonOptions.id]
                values.indexOf(buttonController) == index
            }.size != buttons.size
        } else added.isNotEmpty() || removed.isNotEmpty()

        if (rebuildMenu) {
            updatedButtons = emptyList()
            added = buttons
            removed = btnControllers.map { it.key to it.value.buttonIntId }
            if (buttonBar.shouldAnimate)
                TransitionManager.beginDelayedTransition(buttonBar, buttonsTransition)
        }
        logd("rebuildMenu:$rebuildMenu ${if (rightButtonBar == buttonBar) "Right" else "Left"}", "NewBarMerge")

        updatedButtons.forEach {
            logd(
                "Updated ${if (rightButtonBar == buttonBar) "Right" else "Left"} Button ${it.id} with $it",
                "NewBarMerge"
            )
            btnControllers[it.id]?.mergeButtonOptions(it, buttonBar)
        }
        removed.forEach {
            val (key, buttonIntId) = it
            logd("Remove ${if (rightButtonBar == buttonBar) "Right" else "Left"} Button ${key}", "NewBarMerge")
            buttonBar.removeButton(buttonIntId)
            btnControllers.remove(key)?.destroy()
        }
        added.forEach {
            logd("Add ${if (rightButtonBar == buttonBar) "Right" else "Left"} Button ${it.id}", "NewBarMerge")
            val order = buttons.indexOf(it) * 10
            val newController = controllerCreator(it)
            newController.addToMenu(buttonBar, order)
            btnControllers[it.id] = newController
        }
    }
}
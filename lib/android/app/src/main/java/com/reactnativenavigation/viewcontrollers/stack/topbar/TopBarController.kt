package com.reactnativenavigation.viewcontrollers.stack.topbar

import android.animation.Animator
import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.utils.CollectionUtils.forEachIndexed
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.resetViewProperties
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController
import com.reactnativenavigation.viewcontrollers.stack.topbar.title.TitleBarReactViewController
import com.reactnativenavigation.views.stack.StackLayout
import com.reactnativenavigation.views.stack.topbar.TopBar
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonBar


open class TopBarController(private val animator: TopBarAnimator = TopBarAnimator()) {
    lateinit var view: TopBar
    private lateinit var leftButtonBar: ButtonBar
    private lateinit var rightButtonBar: ButtonBar


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

    fun mergeRightButtons(toAdd: List<ButtonController>, toRemove: List<ButtonController>) {
        toRemove.forEach { view.removeRightButton(it) }
        toAdd.reversed().forEachIndexed { i, b -> b.addToMenu(rightButtonBar, i * 10) }
    }

    open fun applyLeftButtons(toAdd: List<ButtonController>) {
        view.clearBackButton()
        view.clearLeftButtons()
        forEachIndexed(toAdd) { b: ButtonController, i: Int -> b.addToMenu(leftButtonBar, i * 10) }
    }

    open fun mergeLeftButtons(toAdd: List<ButtonController>, toRemove: List<ButtonController>) {
        view.clearBackButton()
        toRemove.forEach { view.removeLeftButton(it) }
        forEachIndexed(toAdd) { b: ButtonController, i: Int -> b.addToMenu(leftButtonBar, i * 10) }
    }
}
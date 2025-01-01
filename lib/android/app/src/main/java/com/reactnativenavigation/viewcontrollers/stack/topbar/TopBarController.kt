package com.reactnativenavigation.viewcontrollers.stack.topbar

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.MenuItem
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.options.ColorAnimationOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.TopBarOptions
import com.reactnativenavigation.options.animations.ViewAnimationOptions
import com.reactnativenavigation.utils.CollectionUtils.forEachIndexed
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.resetViewProperties
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController
import com.reactnativenavigation.viewcontrollers.stack.topbar.title.TitleBarReactViewController
import com.reactnativenavigation.views.stack.StackLayout
import com.reactnativenavigation.views.stack.topbar.TopBar
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonBar


open class TopBarController(private val appearAnimator: TopBarAppearanceAnimator = TopBarAppearanceAnimator()) {
    lateinit var view: TopBar
    private lateinit var leftButtonBar: ButtonBar
    private lateinit var rightButtonBar: ButtonBar

    private var isBkgAnimated = false

    val height: Int
        get() = view.height
    val rightButtonCount: Int
        get() = rightButtonBar.buttonCount
    val leftButtonCount: Int
        get() = leftButtonBar.buttonCount

    fun createView(context: Context, parent: StackLayout): TopBar {
        if (!::view.isInitialized) {
            view = createTopBar(context, parent)
            leftButtonBar = view.leftButtonBar
            rightButtonBar = view.rightButtonBar
            appearAnimator.bindView(view)
        }
        return view
    }

    fun initTopTabs(viewPager: ViewPager?) = view.initTopTabs(viewPager)

    fun clearTopTabs() = view.clearTopTabs()

    fun setBackgroundColor(topBarOptions: TopBarOptions, defaultColor: Int) {
        val color = topBarOptions.background.color

        if (!isBkgAnimated) {
            view.setBackgroundColor(color.get(defaultColor)!!)
        }
    }

    fun setBackgroundColor(topBarOptions: TopBarOptions) {
        val color = topBarOptions.background.color

        if (color.hasValue() && !isBkgAnimated) {
            view.setBackgroundColor(color.get())
        }
    }

    fun getRightButton(index: Int): MenuItem = rightButtonBar.getButton(index)

    fun getPushAnimation(appearingOptions: Options, additionalDy: Float = 0f): Animator? {
        val topBarOptions = appearingOptions.topBar
        val topBarAnimOptions = appearingOptions.animations.push.topBar

        isBkgAnimated = false

        mutableListOf(
            getAppearancePushAnimation(topBarOptions, topBarAnimOptions, additionalDy),
            getBkgColorAnimation(topBarOptions, topBarAnimOptions.enter)?.also {
                isBkgAnimated = true
            },
        ).filterNotNull().let {
            return if (it.isNotEmpty()) {
                AnimatorSet().apply { playTogether(it) }
            } else {
                null
            }
        }
    }

    fun getPopAnimation(appearingOptions: Options, disappearingOptions: Options): Animator? {
        val topBarOptions = appearingOptions.topBar
        val topBarAnimOptions = disappearingOptions.animations.pop.topBar

        isBkgAnimated = false

        mutableListOf(
            getAppearancePopAnimation(topBarOptions, topBarAnimOptions),
            getBkgColorAnimation(topBarOptions, topBarAnimOptions.exit)?.also {
                isBkgAnimated = true
            },
        ).filterNotNull().let {
            return if (it.isNotEmpty()) {
                AnimatorSet().apply { playTogether(it) }
            } else {
                null
            }
        }
    }

    fun getSetStackRootAnimation(appearingOptions: Options, additionalDy: Float = 0f): Animator? {
        if (appearingOptions.topBar.animate.isFalse) return null
        return appearAnimator.getSetStackRootAnimation(
                appearingOptions.animations.setStackRoot.topBar,
                appearingOptions.topBar.visible,
                additionalDy
        )
    }

    fun show() {
        if (ViewUtils.isVisible(view) || appearAnimator.isAnimatingShow()) return
        view.resetViewProperties()
        view.visibility = View.VISIBLE
    }

    fun showAnimate(options: AnimationOptions, additionalDy: Float) {
        if (ViewUtils.isVisible(view) || appearAnimator.isAnimatingShow()) return
        appearAnimator.show(options, additionalDy)
    }

    fun hide() {
        if (!appearAnimator.isAnimatingHide()) view.visibility = View.GONE
    }

    fun hideAnimate(options: AnimationOptions, additionalDy: Float) {
        if (!ViewUtils.isVisible(view) || appearAnimator.isAnimatingHide()) return
        appearAnimator.hide(options, additionalDy)
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

    protected open fun createTopBar(context: Context, stackLayout: StackLayout): TopBar {
        return TopBar(context)
    }

    private fun getBkgColorAnimation(topBarOptions: TopBarOptions, topBarAnimOptions: AnimationOptions): Animator? {
        val targetColor = topBarOptions.background.color
        val colorAnimOptions = topBarAnimOptions.colorAnimOptions

        if (topBarAnimOptions.enabled.isTrue &&
            targetColor.hasValue() &&
            view.background is ColorDrawable) {

            return colorAnimOptions.getAnimation(
                view,
                (view.background as ColorDrawable).color,
                targetColor.get()
            )
        }
        return null
    }

    private fun getAppearancePushAnimation(topBarOptions: TopBarOptions, topBarAnimOptions: ViewAnimationOptions, additionalDy: Float) =
        if (!topBarOptions.animate.isFalse) {
            appearAnimator.getPushAnimation(
                topBarAnimOptions,
                topBarOptions.visible,
                additionalDy,
            )
        } else {
            null
        }

    private fun getAppearancePopAnimation(topBarOptions: TopBarOptions, topBarAnimOptions: ViewAnimationOptions) =
        if (!topBarOptions.animate.isFalse) {
            appearAnimator.getPopAnimation(
                topBarAnimOptions,
                topBarOptions.visible,
            )
        } else {
            null
        }
}

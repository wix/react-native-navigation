package com.reactnativenavigation.viewcontrollers.stack.topbar

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.MenuItem
import android.view.View
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.viewpager.widget.ViewPager
import com.reactnativenavigation.RNNFeatureToggles
import com.reactnativenavigation.RNNToggles
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.AnimationOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.TopBarOptions
import com.reactnativenavigation.options.animations.ViewAnimationOptions
import com.reactnativenavigation.utils.CollectionUtils.forEachIndexed
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.resetViewProperties
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController
import com.reactnativenavigation.viewcontrollers.stack.topbar.title.TitleBarReactViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.TopBarVisibilityInfo
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.animations.ColorAnimator
import com.reactnativenavigation.views.stack.StackLayout
import com.reactnativenavigation.views.stack.topbar.TopBar
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonBar


open class TopBarController(
    private val appearAnimator: TopBarAppearanceAnimator = TopBarAppearanceAnimator(),
    private val colorAnimator: ColorAnimator = ColorAnimator(),
) {
    lateinit var view: TopBar
    private lateinit var leftButtonBar: ButtonBar
    private lateinit var rightButtonBar: ButtonBar

    private var hasPendingColorAnim = false

    val height: Int
        get() = view.height
    val rightButtonCount: Int
        get() = rightButtonBar.buttonCount
    val leftButtonCount: Int
        get() = leftButtonBar.buttonCount

    val visibilityInfo: TopBarVisibilityInfo
        get() = TopBarVisibilityInfo(view.isShown, getBackgroundColor())

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

        if (!hasPendingColorAnim) {
            view.setBackgroundColor(color.get(defaultColor)!!)
        }
    }

    fun setBackgroundColor(topBarOptions: TopBarOptions) {
        val color = topBarOptions.background.color

        if (color.hasValue() && !hasPendingColorAnim) {
            view.setBackgroundColor(color.get())
        }
    }

    fun getBackgroundColor(): Int? {
        return if (view.background is ColorDrawable) {
            (view.background as ColorDrawable).color
        } else {
            null
        }
    }

    fun getRightButton(index: Int): MenuItem = rightButtonBar.getButton(index)

    fun getPushAnimation(appearingOptions: Options, additionalDy: Float = 0f): Animator? {
        val topBarOptions = appearingOptions.topBar
        val topBarAnimOptions = appearingOptions.animations.push.topBar

        hasPendingColorAnim = false

        mutableListOf(
            getAppearancePushAnimation(topBarOptions, topBarAnimOptions, additionalDy),
            getBkgColorAnimation(topBarOptions)?.apply {
                hasPendingColorAnim = true

                addListener(onEnd = {
                    hasPendingColorAnim = false
                })
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

        hasPendingColorAnim = false

        mutableListOf(
            getAppearancePopAnimation(topBarOptions, topBarAnimOptions),
            getBkgColorAnimation(topBarOptions)?.apply {
                hasPendingColorAnim = true

                addListener(onEnd = {
                    hasPendingColorAnim = false
                })
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

    fun bindNewViewController(previousVC: ViewController<*>?, newVC: ViewController<*>?) {
        if (!RNNFeatureToggles.isEnabled(RNNToggles.TOP_BAR_COLOR_ANIMATION__TABS)) {
            return
        }

        previousVC?.visibilityInfo?.topBarVisibilityInfo?.solidColor?.let { fromColor ->
            newVC?.visibilityInfo?.topBarVisibilityInfo?.solidColor?.let { toColor ->
                colorAnimator.getAnimation(view, fromColor, toColor).apply {
                    doOnEnd {
                        hasPendingColorAnim = false
                    }
                    hasPendingColorAnim = true
                    start()
                }
            }
        }
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

    private fun getBkgColorAnimation(topBarOptions: TopBarOptions): Animator? {
        val targetColor = topBarOptions.background.color

        if (targetColor.hasValue()
            && view.background is ColorDrawable
            && RNNFeatureToggles.isEnabled(RNNToggles.TOP_BAR_COLOR_ANIMATION__PUSH)) {
            return colorAnimator.getAnimation(
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

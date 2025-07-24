package com.reactnativenavigation.viewcontrollers.bottomtabs

import android.animation.Animator
import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.IntRange
import androidx.core.view.updateMargins
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation.TitleState
import com.reactnativenavigation.RNNFeatureToggles
import com.reactnativenavigation.RNNToggles.TAB_BAR_TRANSLUCENCE
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.BottomTabsLayoutStyle
import com.reactnativenavigation.options.params.Fraction
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.bottomtabs.BottomTabs
import com.reactnativenavigation.views.bottomtabs.BottomTabsContainer
import com.reactnativenavigation.views.bottomtabs.BottomTabsLayout
import kotlin.math.max
import kotlin.math.roundToInt

class BottomTabsPresenter(
    private val tabs: List<ViewController<*>>,
    private var defaultOptions: Options,
    val animator: BottomTabsAnimator
) {
    private val bottomTabFinder: BottomTabFinder = BottomTabFinder(tabs)
    private lateinit var bottomTabsContainer: BottomTabsContainer
    private lateinit var bottomTabsLayout: BottomTabsLayout
    private lateinit var bottomTabs: BottomTabs
    private lateinit var tabSelector: TabSelector
    private val defaultTitleState: TitleState
        get() {
            for (i in 0 until bottomTabs.itemsCount) {
                if (bottomTabs.getItem(i)?.hasIcon() == true) return TitleState.SHOW_WHEN_ACTIVE
            }
            return TitleState.ALWAYS_SHOW
        }

    fun setDefaultOptions(defaultOptions: Options) {
        this.defaultOptions = defaultOptions
    }

    fun bindView(bottomTabsContainer: BottomTabsContainer, bottomTabsLayout: BottomTabsLayout, tabSelector: TabSelector) {
        this.bottomTabsContainer = bottomTabsContainer
        this.bottomTabsLayout = bottomTabsLayout
        this.bottomTabs = bottomTabsContainer.bottomTabs
        this.tabSelector = tabSelector
        animator.bindView(this.bottomTabs)
    }

    fun mergeOptions(options: Options, view: ViewController<*>) {
        mergeBottomTabsOptions(options, view)
    }

    fun applyOptions(options: Options) {
        applyBottomTabsOptions(options.copy().withDefaultOptions(defaultOptions))
    }

    fun applyChildOptions(options: Options, child: ViewController<*>) {
        val tabIndex = bottomTabFinder.findByControllerId(child.id)
        if (tabIndex >= 0) {
            applyBottomTabsOptions(options.copy().withDefaultOptions(defaultOptions))
            applyDrawBehind(tabIndex)
        }
    }

    fun mergeChildOptions(options: Options, child: ViewController<*>) {
        mergeBottomTabsOptions(options, child)
        val tabIndex = bottomTabFinder.findByControllerId(child.id)
        if (tabIndex >= 0) mergeDrawBehind(tabIndex)
    }

    private fun mergeBottomTabsOptions(options: Options, view: ViewController<*>) {
        val bottomTabsOptions = options.bottomTabsOptions

        if (bottomTabsOptions.layoutStyle == BottomTabsLayoutStyle.COMPACT) {
            bottomTabs.layoutParams.width = WRAP_CONTENT
            bottomTabs.refresh()
        }

        if (bottomTabsOptions.bottomMargin.hasValue()) {
            val margin = extractBottomMarginPx(bottomTabsOptions.bottomMargin)
            bottomTabsLayout.setBottomMargin(margin)
        }

        if (bottomTabsOptions.cornerRadius.hasValue()) {
            val radius = extractCornerRadius(bottomTabsOptions.cornerRadius)
            bottomTabsContainer.setRoundedCorners(radius)
        }

        // Keep this before the translucent check below
        if (bottomTabsOptions.backgroundColor.hasValue()) {
            bottomTabsContainer.setBackgroundColor(bottomTabsOptions.backgroundColor.get())
        }

        if (RNNFeatureToggles.isEnabled(TAB_BAR_TRANSLUCENCE)) {
            if (bottomTabsOptions.translucent.isTrue) {
                if (bottomTabsOptions.blurRadius.hasValue()) {
                    bottomTabsContainer.setBlurRadius(bottomTabsOptions.blurRadius.get().toFloat())
                }
                if (bottomTabsOptions.backgroundColor.hasValue()) {
                    bottomTabsContainer.setBlurColor(bottomTabsOptions.backgroundColor.get())
                }
                bottomTabsContainer.enableBackgroundBlur()
            } else if (bottomTabsOptions.translucent.isFalse) {
                bottomTabsContainer.disableBackgroundBlur()
            }
        }

        if (bottomTabsOptions.elevation.hasValue()) {
            bottomTabsContainer.setElevation(bottomTabsOptions.elevation)
        }

        if (options.layout.direction.hasValue()) bottomTabs.setLayoutDirection(options.layout.direction)
        if (bottomTabsOptions.preferLargeIcons.hasValue()) bottomTabs.setPreferLargeIcons(bottomTabsOptions.preferLargeIcons.get())
        if (bottomTabsOptions.titleDisplayMode.hasValue()) {
            bottomTabs.setTitleState(bottomTabsOptions.titleDisplayMode.toState())
        }

        if (bottomTabsOptions.animateTabSelection.hasValue()) {
            bottomTabs.setAnimateTabSelection(bottomTabsOptions.animateTabSelection.get())
        }
        if (bottomTabsOptions.currentTabIndex.hasValue()) {
            val tabIndex = bottomTabsOptions.currentTabIndex.get()
            if (tabIndex >= 0) tabSelector.selectTab(tabIndex)
        }
        if (bottomTabsOptions.testId.hasValue()) {
            bottomTabs.tag = bottomTabsOptions.testId.get()
        }
        if (bottomTabsOptions.currentTabId.hasValue()) {
            val tabIndex = bottomTabFinder.findByControllerId(bottomTabsOptions.currentTabId.get())
            if (tabIndex >= 0) tabSelector.selectTab(tabIndex)
        }
        if (bottomTabsOptions.hideOnScroll.hasValue()) {
            bottomTabs.setBehaviorTranslationEnabled(bottomTabsOptions.hideOnScroll.get())
        }

        if (bottomTabsOptions.borderColor.hasValue()) {
            bottomTabsContainer.setTopOutLineColor(bottomTabsOptions.borderColor.get())
            bottomTabsContainer.showTopLine()
        }
        if (bottomTabsOptions.borderWidth.hasValue()) {
            bottomTabsContainer.setTopOutlineWidth(bottomTabsOptions.borderWidth.get().roundToInt())
            bottomTabsContainer.showTopLine()
        }
        if (bottomTabsOptions.shadowOptions.hasValue()) {
            if (bottomTabsOptions.shadowOptions.color.hasValue())
                bottomTabsContainer.shadowColor = bottomTabsOptions.shadowOptions.color.get()
            if (bottomTabsOptions.shadowOptions.radius.hasValue())
                bottomTabsContainer.shadowRadius = bottomTabsOptions.shadowOptions.radius.get().toFloat()
            if (bottomTabsOptions.shadowOptions.opacity.hasValue())
                bottomTabsContainer.setShadowOpacity(bottomTabsOptions.shadowOptions.opacity.get().toFloat())
            bottomTabsContainer.showShadow()
        }

        if (view.isViewShown) {
            if (bottomTabsOptions.visible.isTrue) {
                if (bottomTabsOptions.animate.isTrueOrUndefined) {
                    animator.show()
                } else {
                    bottomTabs.restoreBottomNavigation(false)
                }
                bottomTabsContainer.revealTopOutline()
            }
            if (bottomTabsOptions.visible.isFalse) {
                if (bottomTabsOptions.animate.isTrueOrUndefined) {
                    animator.hide()
                } else {
                    bottomTabs.hideBottomNavigation(false)
                }
                bottomTabsContainer.hideTopOutLine()

            }
        }
    }

    private fun applyDrawBehind(@IntRange(from = 0) tabIndex: Int) {
        tabs[tabIndex].applyBottomInset()
    }

    private fun mergeDrawBehind(tabIndex: Int) {
        tabs[tabIndex].applyBottomInset()
    }

    private fun applyBottomTabsOptions(options: Options) {
        val bottomTabsOptions = options.bottomTabsOptions
        if (bottomTabsOptions.layoutStyle == BottomTabsLayoutStyle.COMPACT) {
            bottomTabs.layoutParams.width = WRAP_CONTENT
        }

        if (bottomTabsOptions.bottomMargin.hasValue()) {
            val margin = extractBottomMarginPx(bottomTabsOptions.bottomMargin)
            bottomTabsLayout.setBottomMargin(margin)
        }

        if (bottomTabsOptions.cornerRadius.hasValue()) {
            val radius = extractCornerRadius(bottomTabsOptions.cornerRadius)
            bottomTabsContainer.setRoundedCorners(radius)
        } else {
            bottomTabsContainer.clearRoundedCorners()
        }

        if (RNNFeatureToggles.isEnabled(TAB_BAR_TRANSLUCENCE) && bottomTabsOptions.translucent.isTrue) {
            if (bottomTabsOptions.blurRadius.hasValue()) {
                bottomTabsContainer.setBlurRadius(bottomTabsOptions.blurRadius.get().toFloat())
            }
            if (bottomTabsOptions.backgroundColor.hasValue()) {
                bottomTabsContainer.setBlurColor(bottomTabsOptions.backgroundColor.get())
            }
            bottomTabsContainer.enableBackgroundBlur()
        } else {
            bottomTabsContainer.disableBackgroundBlur()
            bottomTabsContainer.setBackgroundColor(bottomTabsOptions.backgroundColor.get(Color.WHITE)!!)
        }

        bottomTabs.setLayoutDirection(options.layout.direction)
        bottomTabs.setPreferLargeIcons(options.bottomTabsOptions.preferLargeIcons[false])
        bottomTabs.setTitleState(bottomTabsOptions.titleDisplayMode[defaultTitleState])

        bottomTabs.setAnimateTabSelection(bottomTabsOptions.animateTabSelection.get(true))
        if (bottomTabsOptions.currentTabIndex.hasValue()) {
            val tabIndex = bottomTabsOptions.currentTabIndex.get()
            if (tabIndex >= 0) {
                bottomTabsOptions.currentTabIndex.consume()
                tabSelector.selectTab(tabIndex)
            }
        }
        if (bottomTabsOptions.testId.hasValue()) bottomTabs.tag = bottomTabsOptions.testId.get()
        if (bottomTabsOptions.currentTabId.hasValue()) {
            val tabIndex = bottomTabFinder.findByControllerId(bottomTabsOptions.currentTabId.get())
            if (tabIndex >= 0) {
                bottomTabsOptions.currentTabId.consume()
                tabSelector.selectTab(tabIndex)
            }
        }
        if (bottomTabsOptions.visible.isTrueOrUndefined) {
            if (bottomTabsOptions.animate.isTrueOrUndefined) {
                animator.show()
            } else {
                bottomTabs.restoreBottomNavigation(false)
            }
        }
        if (bottomTabsOptions.visible.isFalse) {
            if (bottomTabsOptions.animate.isTrueOrUndefined) {
                animator.hide()
            } else {
                bottomTabs.hideBottomNavigation(false)
            }
        }

        if (bottomTabsOptions.elevation.hasValue()) {
            bottomTabsContainer.setElevation(bottomTabsOptions.elevation)
        }

        when {
            bottomTabsOptions.borderColor.hasValue() -> {
                bottomTabsContainer.setTopOutLineColor(bottomTabsOptions.borderColor.get())
                bottomTabsContainer.showTopLine()
            }
            bottomTabsOptions.borderWidth.hasValue() -> {
                bottomTabsContainer.setTopOutlineWidth(bottomTabsOptions.borderWidth.get().roundToInt())
                bottomTabsContainer.showTopLine()
            }
            else -> {
                bottomTabsContainer.clearTopOutline()
            }
        }

        if (bottomTabsOptions.shadowOptions.hasValue()) {
            if (bottomTabsOptions.shadowOptions.color.hasValue())
                bottomTabsContainer.shadowColor = bottomTabsOptions.shadowOptions.color.get()
            if (bottomTabsOptions.shadowOptions.radius.hasValue())
                bottomTabsContainer.shadowRadius = bottomTabsOptions.shadowOptions.radius.get().toFloat()
            if (bottomTabsOptions.shadowOptions.opacity.hasValue())
                bottomTabsContainer.setShadowOpacity(bottomTabsOptions.shadowOptions.opacity.get().toFloat())
            bottomTabsContainer.showShadow()
        } else {
            bottomTabsContainer.clearShadow()
        }
        bottomTabs.setBehaviorTranslationEnabled(bottomTabsOptions.hideOnScroll[false])
    }

    fun applyBottomInset(bottomInset: Int) {
        (bottomTabsContainer.layoutParams as ViewGroup.MarginLayoutParams).updateMargins(bottom = bottomInset)
        bottomTabsContainer.requestLayout()
    }

    fun getBottomInset(resolvedOptions: Options): Int {
        return if (resolvedOptions.withDefaultOptions(defaultOptions).bottomTabsOptions.isHiddenOrDrawBehind) 0 else bottomTabs.height
    }

    fun getPushAnimation(appearingOptions: Options): Animator? {
        if (appearingOptions.bottomTabsOptions.animate.isFalse) return null
        return animator.getPushAnimation(
            appearingOptions.animations.push.bottomTabs,
            appearingOptions.bottomTabsOptions.visible
        )
    }

    fun getPopAnimation(appearingOptions: Options, disappearingOptions: Options): Animator? {
        if (appearingOptions.bottomTabsOptions.animate.isFalse) return null
        return animator.getPopAnimation(
            disappearingOptions.animations.pop.bottomTabs,
            appearingOptions.bottomTabsOptions.visible
        )
    }

    fun getSetStackRootAnimation(appearingOptions: Options): Animator? {
        if (appearingOptions.bottomTabsOptions.animate.isFalse) return null
        return animator.getSetStackRootAnimation(
            appearingOptions.animations.setStackRoot.bottomTabs,
            appearingOptions.bottomTabsOptions.visible
        )
    }

    fun findTabIndexByTabId(id: String?): Int {
        val index = bottomTabFinder.findByControllerId(id)
        return max(index, 0)
    }

    fun onConfigurationChanged(options: Options) {
        val bottomTabsOptions = options.withDefaultOptions(defaultOptions).bottomTabsOptions

        if (bottomTabsOptions.layoutStyle == BottomTabsLayoutStyle.COMPACT) {
            bottomTabs.layoutParams.width = WRAP_CONTENT
        }

        if (bottomTabsOptions.bottomMargin.hasValue()) {
            val margin = extractBottomMarginPx(bottomTabsOptions.bottomMargin)
            bottomTabsLayout.setBottomMargin(margin)
        }

        if (bottomTabsOptions.cornerRadius.hasValue()) {
            val radius = extractCornerRadius(bottomTabsOptions.cornerRadius)
            bottomTabsContainer.setRoundedCorners(radius)
        } else {
            bottomTabsContainer.clearRoundedCorners()
        }

        if (RNNFeatureToggles.isEnabled(TAB_BAR_TRANSLUCENCE) && bottomTabsOptions.translucent.isTrue) {
            if (bottomTabsOptions.blurRadius.hasValue()) {
                bottomTabsContainer.setBlurRadius(bottomTabsOptions.blurRadius.get().toFloat())
            }
            if (bottomTabsOptions.backgroundColor.hasValue()) {
                bottomTabsContainer.setBlurColor(bottomTabsOptions.backgroundColor.get())
            }
            bottomTabsContainer.enableBackgroundBlur()
        } else {
            bottomTabsContainer.disableBackgroundBlur()

            // TODO Change to bottomTabsContainer.setBackgroundColor()?
            bottomTabs.setBackgroundColor(bottomTabsOptions.backgroundColor.get(Color.WHITE)!!)
        }

        if (bottomTabsOptions.shadowOptions.hasValue()) {
            if (bottomTabsOptions.shadowOptions.color.hasValue())
                bottomTabsContainer.shadowColor = bottomTabsOptions.shadowOptions.color.get()
        }

        if (bottomTabsOptions.borderColor.hasValue()) {
            bottomTabsContainer.setTopOutLineColor(bottomTabsOptions.borderColor.get())
            bottomTabsContainer.showTopLine()
        }
    }

    private fun extractCornerRadius(cornerRadius: Fraction): Float {
        val radiusDp = cornerRadius.get()
        val radius = UiUtils.dpToPx(bottomTabsContainer.context, radiusDp.toFloat())
        return radius
    }

    private fun extractBottomMarginPx(bottomMargin: Fraction): Int {
        val marginDp = bottomMargin.get()
        val margin = UiUtils.dpToPx(bottomTabsContainer.context, marginDp.toFloat()).roundToInt()
        return margin
    }
}
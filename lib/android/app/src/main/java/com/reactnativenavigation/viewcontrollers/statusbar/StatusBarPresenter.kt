package com.reactnativenavigation.viewcontrollers.statusbar

import android.animation.Animator
import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.Window
import com.reactnativenavigation.RNNFeatureToggles.isEnabled
import com.reactnativenavigation.RNNToggles
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.StatusBarOptions
import com.reactnativenavigation.options.StatusBarOptions.TextColorScheme
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.utils.ColorUtils.isColorLight
import com.reactnativenavigation.utils.StubAnimationListener.Companion.onAnimatorEnd
import com.reactnativenavigation.utils.SystemUiUtils.clearStatusBarTranslucency
import com.reactnativenavigation.utils.SystemUiUtils.getStatusBarColor
import com.reactnativenavigation.utils.SystemUiUtils.hideStatusBar
import com.reactnativenavigation.utils.SystemUiUtils.isTranslucent
import com.reactnativenavigation.utils.SystemUiUtils.setStatusBarColor
import com.reactnativenavigation.utils.SystemUiUtils.setStatusBarColorScheme
import com.reactnativenavigation.utils.SystemUiUtils.setStatusBarTranslucent
import com.reactnativenavigation.utils.SystemUiUtils.showStatusBar
import com.reactnativenavigation.viewcontrollers.viewcontroller.StatusBarColorAnimator
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import java.lang.ref.WeakReference

class StatusBarPresenter private constructor(
        activity: Activity,
        private val sbColorAnimator: StatusBarColorAnimator = StatusBarColorAnimator(activity)) {

    private val window = WeakReference(activity.window)
    private var hasPendingColorAnim = false

    fun applyOptions(viewController: ViewController<*>, options: StatusBarOptions) {
        if (!hasPendingColorAnim) {
            setStatusBarBackgroundColor(options)
            setTranslucent(options)
        }
        setTextColorScheme(options)
        setStatusBarVisible(viewController, options.visible)
    }

    fun mergeOptions(view: View, statusBar: StatusBarOptions) {
        mergeStatusBarBackgroundColor(statusBar)
        mergeTextColorScheme(statusBar)
        mergeTranslucent(statusBar)
        mergeStatusBarVisible(view, statusBar.visible)
    }

    fun onConfigurationChanged(options: StatusBarOptions) {
        setStatusBarBackgroundColor(options)
        setTextColorScheme(options)
    }

    fun bindViewController(newOptions: StatusBarOptions) {
        if (!isEnabled(RNNToggles.TOP_BAR_COLOR_ANIMATION__TABS)) {
            return
        }

        if (newOptions.backgroundColor.canApplyValue()) {
            val currentColor = getCurrentStatusBarBackgroundColor() ?: return
            val newColor = getStatusBarBackgroundColor(newOptions)
            createStatusBarColorAnimation(
                from = currentColor,
                to = newColor,
                translucent = newOptions.translucent.isTrue,
            ).start()
        }
    }

    fun getStatusBarPushAnimation(appearingOptions: Options): Animator? =
        if (isEnabled(RNNToggles.TOP_BAR_COLOR_ANIMATION__PUSH)) {
            getStatusBarColorAnimation(appearingOptions.statusBar)
        } else null

    fun getStatusBarPopAnimation(appearingOptions: Options, disappearingOptions: Options): Animator? =
        if (isEnabled(RNNToggles.TOP_BAR_COLOR_ANIMATION__PUSH)) {
            getStatusBarColorAnimation(appearingOptions.statusBar)
        } else null

    private fun setStatusBarBackgroundColor(statusBar: StatusBarOptions) {
        if (statusBar.backgroundColor.canApplyValue()) {
            val statusBarBackgroundColor: Int = getStatusBarBackgroundColor(statusBar)
            setStatusBarBackgroundColor(statusBarBackgroundColor, statusBar.translucent.isTrue)
        }
    }

    private fun setStatusBarBackgroundColor(color: Int, translucent: Boolean) {
        setStatusBarColor(window.get(), color, translucent)
    }

    private fun getStatusBarBackgroundColor(statusBar: StatusBarOptions): Int {
        val defaultColor =
            if (statusBar.visible.isTrueOrUndefined) Color.BLACK else Color.TRANSPARENT
        return statusBar.backgroundColor.get(defaultColor)!!
    }

    private fun setTextColorScheme(statusBar: StatusBarOptions) {
        val view = window.get()?.decorView
        //View.post is a Workaround, added to solve internal Samsung
        //Android 9 issues. For more info see https://github.com/wix/react-native-navigation/pull/7231
        view?.post {
            setStatusBarColorScheme(
                window.get(),
                view,
                isDarkTextColorScheme(statusBar)
            )
        }
    }

    private fun setTranslucent(options: StatusBarOptions) {
        val window = window.get()
        if (options.translucent.isTrue) {
            setStatusBarTranslucent(window)
        } else if (isTranslucent(window)) {
            clearStatusBarTranslucency(window)
        }
    }

    private fun setStatusBarVisible(viewController: ViewController<*>, visible: Bool) {
        val window = window.get() ?: return
        val view = if (viewController.view != null) viewController.view else window.decorView
        if (visible.isFalse) {
            hideStatusBar(window, view)
        } else {
            showStatusBar(window, view)
        }
    }

    private fun mergeStatusBarBackgroundColor(statusBar: StatusBarOptions) {
        if (statusBar.backgroundColor.hasValue()) {
            val statusBarBackgroundColor = getStatusBarBackgroundColor(statusBar)
            setStatusBarColor(
                window.get(), statusBarBackgroundColor,
                statusBar.translucent.isTrue
            )
        }
    }

    private fun mergeTextColorScheme(statusBar: StatusBarOptions) {
        if (!statusBar.textColorScheme.hasValue()) return
        setTextColorScheme(statusBar)
    }

    private fun mergeTranslucent(options: StatusBarOptions) {
        val window: Window = window.get() ?: return
        if (options.translucent.isTrue) {
            setStatusBarTranslucent(window)
        } else if (options.translucent.isFalse && isTranslucent(window)) {
            clearStatusBarTranslucency(window)
        }
    }

    private fun mergeStatusBarVisible(view: View, visible: Bool) {
        if (visible.hasValue()) {
            if (visible.isTrue) {
                showStatusBar(window.get(), view)
            } else {
                hideStatusBar(window.get(), view)
            }
        }
    }

    private fun isDarkTextColorScheme(statusBar: StatusBarOptions): Boolean {
        if (statusBar.textColorScheme == TextColorScheme.Dark) {
            return true
        }

        if (statusBar.textColorScheme == TextColorScheme.Light) {
            return false
        }
        return isColorLight(getStatusBarBackgroundColor(statusBar))
    }

    private fun getStatusBarColorAnimation(statusBarOptions: StatusBarOptions): Animator? {
        if (isEnabled(RNNToggles.TOP_BAR_COLOR_ANIMATION__TABS)) {
            getCurrentStatusBarBackgroundColor()?.let { currentColor ->
                val targetColor = statusBarOptions.backgroundColor

                if (targetColor.hasValue()) {
                    val translucent = statusBarOptions.translucent.isTrue
                    return createStatusBarColorAnimation(
                        from = currentColor,
                        to = targetColor.get(),
                        translucent = translucent,
                    )
                }
            }
        }
        return null
    }

    private fun createStatusBarColorAnimation(from: Int, to: Int, translucent: Boolean): Animator =
        sbColorAnimator.getAnimator(from, to, translucent).apply {
            addListener(onAnimatorEnd {
                hasPendingColorAnim = false
            })
            hasPendingColorAnim = true
        }

    private fun getCurrentStatusBarBackgroundColor() =
        getStatusBarColor(window.get())

    companion object {
        lateinit var instance: StatusBarPresenter

        fun init(activity: Activity) {
            instance = StatusBarPresenter(activity)
        }
    }
}

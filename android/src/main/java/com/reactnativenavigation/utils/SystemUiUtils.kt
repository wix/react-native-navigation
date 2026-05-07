package com.reactnativenavigation.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlin.math.abs
import kotlin.math.ceil

object SystemUiUtils {
    private const val STATUS_BAR_HEIGHT_M = 24
    internal const val STATUS_BAR_HEIGHT_TRANSLUCENCY = 0.65f
    private var statusBarHeight = -1

    const val DEFAULT_NAV_BAR_COLOR = Color.BLACK
    private const val THREE_BUTTON_NAV_BAR_OPACITY = 0.8f

    private var statusBarBackgroundView: View? = null
    private var statusBarBackgroundActivity: java.lang.ref.WeakReference<Activity>? = null
    private var navBarBackgroundView: View? = null
    @JvmStatic
    var isEdgeToEdgeActive = false
        private set
    private var isThreeButtonNav = false
    private var lastExplicitNavBarColor: Int? = null

    @JvmStatic
    fun getStatusBarHeight(activity: Activity?): Int {
        val res = if (statusBarHeight > 0) {
            statusBarHeight
        } else {
            statusBarHeight = activity?.let {
                val rectangle = Rect()
                val window: Window = activity.window
                window.decorView.getWindowVisibleDisplayFrame(rectangle)
                val statusBarHeight: Int = rectangle.top
                val contentView = window.findViewById<View>(Window.ID_ANDROID_CONTENT)
                contentView?.let {
                    val contentViewTop = contentView.top
                    abs(contentViewTop - statusBarHeight)
                }
            } ?: STATUS_BAR_HEIGHT_M
            statusBarHeight
        }
        return res
    }

    @JvmStatic
    fun saveStatusBarHeight(height: Int) {
        statusBarHeight = height
    }

    @JvmStatic
    fun getStatusBarHeightDp(activity: Activity?): Int {
        return UiUtils.pxToDp(activity, getStatusBarHeight(activity).toFloat()).toInt()
    }

    // region Setup

    /**
     * Initializes view-based system bar backgrounds for edge-to-edge.
     * Call from Activity.onPostCreate after the navigator content layout is set.
     *
     * Status bar: reuses the system's android:id/statusBarBackground DecorView child
     * when available. On API 35+ with EdgeToEdge, this view may not exist, so a
     * manual view is created in the content layout, sized by status bar insets.
     * Navigation bar: creates a view in [contentLayout] sized by WindowInsets,
     * since the system's navigationBarBackground is not available with EdgeToEdge.
     */
    @JvmStatic
    fun setupSystemBarBackgrounds(activity: Activity, contentLayout: ViewGroup) {
        setupStatusBarBackground(activity)
        setupNavigationBarBackground(contentLayout)
    }

    private fun setupStatusBarBackground(activity: Activity) {
        if (statusBarBackgroundView != null) return
        val sbView = activity.window.decorView.findViewById<View>(android.R.id.statusBarBackground)
        if (sbView != null) {
            statusBarBackgroundView = sbView
        } else {
            statusBarBackgroundActivity = java.lang.ref.WeakReference(activity)
        }
    }

    private fun ensureStatusBarBackgroundView(): View? {
        statusBarBackgroundView?.let { return it }
        val activity = statusBarBackgroundActivity?.get() ?: return null
        val contentLayout = activity.findViewById<ViewGroup>(android.R.id.content) ?: return null
        val view = View(activity)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, 0, Gravity.TOP
        )
        contentLayout.addView(view, params)
        statusBarBackgroundView = view
        statusBarBackgroundActivity = null

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val sbHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val lp = v.layoutParams
            if (lp.height != sbHeight) {
                lp.height = sbHeight
                v.layoutParams = lp
            }
            insets
        }
        view.requestApplyInsets()
        return view
    }

    private fun setupNavigationBarBackground(contentLayout: ViewGroup) {
        if (navBarBackgroundView != null) return
        val view = View(contentLayout.context).apply {
            setBackgroundColor(Color.BLACK)
        }
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, 0, Gravity.BOTTOM
        )
        contentLayout.addView(view, params)
        navBarBackgroundView = view

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            val tappableHeight = insets.getInsets(WindowInsetsCompat.Type.tappableElement()).bottom
            val wasThreeButton = isThreeButtonNav
            isThreeButtonNav = tappableHeight > 0
            if (isThreeButtonNav != wasThreeButton) {
                val color = lastExplicitNavBarColor ?: getDefaultNavBarColor()
                v.setBackgroundColor(color)
            }
            val lp = v.layoutParams
            if (lp.height != navBarHeight) {
                lp.height = navBarHeight
                v.layoutParams = lp
            }
            insets
        }
        view.requestApplyInsets()
    }

    /**
     * Returns the default navigation bar color, applying 80% opacity for 3-button navigation.
     * Gesture navigation gets a fully opaque color since the bar is minimal.
     */
    @JvmStatic
    fun getDefaultNavBarColor(): Int {
        if (!isThreeButtonNav) return DEFAULT_NAV_BAR_COLOR
        val alpha = (THREE_BUTTON_NAV_BAR_OPACITY * 255).toInt()
        return Color.argb(alpha, Color.red(DEFAULT_NAV_BAR_COLOR), Color.green(DEFAULT_NAV_BAR_COLOR), Color.blue(DEFAULT_NAV_BAR_COLOR))
    }

    /**
     * Returns true when the system statusBarBackground view was not found during setup,
     * meaning a manual view will be lazily created on the first setStatusBarColor call.
     * Use this to decide whether to apply a theme-based initial status bar color.
     */
    @JvmStatic
    fun needsManualStatusBarBackground(): Boolean = statusBarBackgroundActivity != null

    /**
     * Marks edge-to-edge as active. Call after EdgeToEdge.enable() in the activity.
     * This flag controls whether navigation bar insets are forwarded to SafeAreaView
     * and whether the view-based nav bar background is used for color changes.
     */
    @JvmStatic
    fun activateEdgeToEdge() {
        isEdgeToEdgeActive = true
    }

    /**
     * Clears references to system bar background views.
     * Call from Activity.onDestroy to avoid leaking views across activity recreation.
     */
    @JvmStatic
    fun tearDown() {
        statusBarBackgroundView = null
        statusBarBackgroundActivity = null
        navBarBackgroundView = null
        isEdgeToEdgeActive = false
        isThreeButtonNav = false
        lastExplicitNavBarColor = null
        statusBarHeight = -1
    }

    // endregion

    // region Status Bar

    @JvmStatic
    fun setStatusBarColorScheme(window: Window?, view: View, isDark: Boolean) {
        window?.let {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = isDark
        }
    }

    @JvmStatic
    fun setStatusBarTranslucent(window: Window?) {
        getStatusBarColor(window)?.let { currentColor ->
            setStatusBarColor(window, currentColor, true)
        }
    }

    @JvmStatic
    fun isTranslucent(window: Window?): Boolean {
        val color = getStatusBarColor(window) ?: return false
        return Color.alpha(color) < 255
    }

    @JvmStatic
    fun clearStatusBarTranslucency(window: Window?) {
        getStatusBarColor(window)?.let { currentColor ->
            setStatusBarColor(window, currentColor, false)
        }
    }

    @JvmStatic
    fun setStatusBarColor(window: Window?, @ColorInt color: Int, translucent: Boolean) {
        val colorAlpha = Color.alpha(color)
        val alpha = if (translucent && colorAlpha == 255) STATUS_BAR_HEIGHT_TRANSLUCENCY else colorAlpha / 255.0f
        val opaqueColor = Color.argb(
            ceil(alpha * 255).toInt(),
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
        applyStatusBarColor(window, opaqueColor)
    }

    /**
     * Sets the status bar background color, lazily creating a manual view on API 35+
     * if the system view wasn't available at setup time. Use this for explicit app-level
     * color requests (e.g. from MainActivity or NavigationActivity).
     */
    @JvmStatic
    fun setStatusBarColor(window: Window?, color: Int) {
        val view = ensureStatusBarBackgroundView()
        if (view != null) {
            view.setBackgroundColor(color)
        } else {
            @Suppress("DEPRECATION")
            window?.statusBarColor = color
        }
    }

    private fun applyStatusBarColor(window: Window?, color: Int) {
        statusBarBackgroundView?.setBackgroundColor(color) ?: run {
            @Suppress("DEPRECATION")
            window?.statusBarColor = color
        }
    }

    /**
     * Gets the current status bar background color.
     * Reads from the view-based background when available,
     * falls back to the deprecated window API on older configurations.
     */
    @JvmStatic
    fun getStatusBarColor(window: Window?): Int? {
        statusBarBackgroundView?.let { view ->
            (view.background as? ColorDrawable)?.let { return it.color }
        }
        @Suppress("DEPRECATION")
        return window?.statusBarColor
    }

    @JvmStatic
    fun hideStatusBar(window: Window?, view: View) {
        window?.let {
            WindowInsetsControllerCompat(window, view).let { controller ->
                controller.hide(WindowInsetsCompat.Type.statusBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    @JvmStatic
    fun showStatusBar(window: Window?, view: View) {
        window?.let {
            WindowInsetsControllerCompat(window, view).show(WindowInsetsCompat.Type.statusBars())
        }
    }

    // endregion

    // region Navigation Bar

    @JvmStatic
    fun hideNavigationBar(window: Window?, view: View) {
        window?.let {
            WindowInsetsControllerCompat(window, view).let { controller ->
                controller.hide(WindowInsetsCompat.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    @JvmStatic
    fun showNavigationBar(window: Window?, view: View) {
        window?.let {
            WindowInsetsControllerCompat(window, view).show(WindowInsetsCompat.Type.navigationBars())
        }
    }

    /**
     * Sets the navigation bar background color and icon appearance.
     * Uses the view-based background when available (edge-to-edge),
     * falls back to the deprecated window API on older configurations.
     */
    @JvmStatic
    fun setNavigationBarBackgroundColor(window: Window?, color: Int, lightColor: Boolean) {
        lastExplicitNavBarColor = color
        window?.let {
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = lightColor
        }
        if (isEdgeToEdgeActive) {
            navBarBackgroundView?.setBackgroundColor(color)
        } else {
            @Suppress("DEPRECATION")
            window?.navigationBarColor = color
        }
    }

    // endregion
}

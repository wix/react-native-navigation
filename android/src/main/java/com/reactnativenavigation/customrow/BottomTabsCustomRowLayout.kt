package com.reactnativenavigation.customrow

import android.app.Activity
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.reactnativenavigation.views.bottomtabs.BottomTabs

/**
 * Resolves how the floating custom row should anchor and whether the bottom
 * system-bar inset belongs inside the row or was already applied by RNN's
 * [com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsController].
 *
 * On most devices (including Pixel with gesture/3-button nav) RNN pads the
 * bottom-tabs controller by `systemBars().bottom`, so the native bar already
 * sits above the nav area — adding the same inset again creates a visible gap.
 *
 * With edge-to-edge (API 35+ theme opt-in) content can extend behind the nav
 * bar; the row must pin to the overlay host bottom and reserve inset inside.
 */
internal object BottomTabsCustomRowLayout {

    enum class AnchorMode {
        /** Native bar bottom is already above system bars (RNN bottom padding). */
        NATIVE_BAR_ABOVE_SYSTEM_BARS,
        /** Row extends to the host bottom; inset is applied inside the row. */
        EDGE_TO_EDGE,
    }

    data class Placement(
        val anchorMode: AnchorMode,
        /** Inset applied inside the row for cell/chrome layout (0 when native bar already cleared it). */
        val rowSafeBottomInsetPx: Int,
        val left: Int,
        val top: Int,
        val width: Int,
        val height: Int,
    )

    fun resolvePlacement(
        activity: Activity,
        row: BottomTabsCustomRow,
        bottomTabs: BottomTabs,
        overlayHost: ViewGroup,
        navBarInsetPx: Int,
    ): Placement? {
        val nativeHeight = bottomTabs.height
        if (nativeHeight <= 0) return null

        val horizontalMargin = row.effectiveHorizontalMarginPx()
        val bottomMargin = row.effectiveBottomMarginPx()

        val tabLeftInHost = tabLeftRelativeToHost(bottomTabs, overlayHost)
        val tabRightInHost = tabLeftInHost + bottomTabs.width

        // Row is hosted on `android.R.id.content`; anchor to `BottomTabs` bottom
        // (above RNN's nav-bar padding). Never use decor.height — that draws over
        // the system navigation buttons.
        val anchorMode = resolveAnchorMode(activity, bottomTabs, overlayHost, navBarInsetPx)
        val rowSafeBottom = 0

        val contentHeight = row.effectiveContentHeightPx(nativeHeight)
        val totalHeight = contentHeight + bottomMargin

        val left = tabLeftInHost + horizontalMargin
        val width = (tabRightInHost - horizontalMargin) - left

        // RNN already lays out `BottomTabs` above its bottom padding — match that
        // edge. Do not subtract `navBarInsetPx` again (that was lifting the bar).
        val bottom = tabBottomRelativeToHost(bottomTabs, overlayHost) - bottomMargin
        val top = bottom - totalHeight

        return Placement(anchorMode, rowSafeBottom, left, top, width, totalHeight)
    }

    fun resolveAnchorMode(
        activity: Activity,
        bottomTabs: BottomTabs,
        overlayHost: ViewGroup,
        @Suppress("UNUSED_PARAMETER") navBarInsetPx: Int,
    ): AnchorMode {
        // RNN's bottom-tabs host ends at `android.R.id.content` bottom while the
        // system nav bar sits below that — never treat "flush with content" as
        // edge-to-edge or we reserve a phantom inset and leave a white gap.
        if (!isEdgeToEdgeEnabled(activity)) {
            return AnchorMode.NATIVE_BAR_ABOVE_SYSTEM_BARS
        }
        val decor = activity.window?.decorView ?: return AnchorMode.NATIVE_BAR_ABOVE_SYSTEM_BARS
        val tolerancePx = dpToPx(activity, 4f)
        val tabBottomOnScreen = screenBottom(bottomTabs)
        val decorBottomOnScreen = screenBottom(decor)
        return if (kotlin.math.abs(tabBottomOnScreen - decorBottomOnScreen) <= tolerancePx) {
            AnchorMode.EDGE_TO_EDGE
        } else {
            AnchorMode.NATIVE_BAR_ABOVE_SYSTEM_BARS
        }
    }

    private fun screenBottom(view: android.view.View): Int {
        val loc = IntArray(2).also(view::getLocationOnScreen)
        return loc[1] + view.height
    }

    fun isEdgeToEdgeEnabled(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            return false
        }
        val typedValue = TypedValue()
        val resolved = activity.theme.resolveAttribute(
            android.R.attr.windowOptOutEdgeToEdgeEnforcement,
            typedValue,
            true
        )
        return resolved &&
            typedValue.type == TypedValue.TYPE_INT_BOOLEAN &&
            typedValue.data == 0
    }

    private fun tabLeftRelativeToHost(bottomTabs: BottomTabs, overlayHost: ViewGroup): Int {
        val tabLoc = IntArray(2).also(bottomTabs::getLocationOnScreen)
        val hostLoc = IntArray(2).also(overlayHost::getLocationOnScreen)
        return tabLoc[0] - hostLoc[0]
    }

    private fun tabBottomRelativeToHost(bottomTabs: BottomTabs, overlayHost: ViewGroup): Int {
        val tabLoc = IntArray(2).also(bottomTabs::getLocationOnScreen)
        val hostLoc = IntArray(2).also(overlayHost::getLocationOnScreen)
        val tabTopInHost = tabLoc[1] - hostLoc[1]
        return tabTopInHost + bottomTabs.height
    }

    private fun dpToPx(activity: Activity, dp: Float): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            activity.resources.displayMetrics
        ).toInt()
}

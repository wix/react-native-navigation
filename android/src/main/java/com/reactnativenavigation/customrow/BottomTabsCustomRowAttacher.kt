package com.reactnativenavigation.customrow

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.widget.FrameLayout
import com.reactnativenavigation.views.bottomtabs.BottomTabs

/** Tracks only custom tabs that are actually attached; never scans an activity's view tree. */
internal object BottomTabsCustomRowAttacher {
    private data class Attachment(
        val row: BottomTabsCustomRow,
        val observer: ViewTreeObserver,
        val listener: ViewTreeObserver.OnGlobalLayoutListener,
        val originalAlpha: Float,
        val originalElevation: Float,
    )

    private data class LastPlacement(
        val left: Int,
        val top: Int,
        val width: Int,
        val height: Int,
        val safeBottomInsetPx: Int,
    )

    @JvmStatic
    fun attach(bottomTabs: BottomTabs) {
        if (!bottomTabs.hasCustomItemViews()) return
        if (bottomTabs.getTag(TAG_ATTACHMENT) != null) return
        val activity = activityFrom(bottomTabs.context) ?: return
        val decor = activity.window?.decorView as? ViewGroup ?: return
        val overlayHost = decor.findViewById<View>(android.R.id.content) as? ViewGroup ?: decor
        val originalAlpha = bottomTabs.alpha
        val originalElevation = bottomTabs.elevation
        bottomTabs.setExternalCustomItemViewHost(true)
        val row = BottomTabsCustomRow(overlayHost.context, bottomTabs)
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            row.visibility = if (bottomTabs.isShown) View.VISIBLE else View.GONE
            positionRow(row, bottomTabs, overlayHost, activity)
        }
        val observer = decor.viewTreeObserver
        bottomTabs.setTag(TAG_ATTACHMENT, Attachment(row, observer, listener, originalAlpha, originalElevation))
        overlayHost.addView(row, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
        ))
        bottomTabs.alpha = 0f
        bottomTabs.elevation = 0f
        observer.addOnGlobalLayoutListener(listener)
        listener.onGlobalLayout()
    }

    @JvmStatic
    fun detach(bottomTabs: BottomTabs) {
        val attachment = bottomTabs.getTag(TAG_ATTACHMENT) as? Attachment ?: return
        // Select View.setTag: AHBottomNavigation also overloads setTag(Int, String?).
        (bottomTabs as View).setTag(TAG_ATTACHMENT, null)
        if (attachment.observer.isAlive) {
            attachment.observer.removeOnGlobalLayoutListener(attachment.listener)
        }
        (attachment.row.parent as? ViewGroup)?.removeView(attachment.row)
        bottomTabs.setExternalCustomItemViewHost(false)
        bottomTabs.alpha = attachment.originalAlpha
        bottomTabs.elevation = attachment.originalElevation
    }

    @JvmStatic
    fun onCustomItemsChanged(bottomTabs: BottomTabs) {
        if (!bottomTabs.hasCustomItemViews()) {
            detach(bottomTabs)
            return
        }
        if (!bottomTabs.isAttachedToWindow) return
        val attachment = bottomTabs.getTag(TAG_ATTACHMENT) as? Attachment
        if (attachment == null) attach(bottomTabs)
        else {
            attachment.row.rebuildCells()
            attachment.listener.onGlobalLayout()
        }
    }

    private fun activityFrom(context: Context): Activity? {
        var current = context
        while (current is ContextWrapper) {
            if (current is Activity) return current
            val base = current.baseContext
            if (base === current) return null
            current = base
        }
        return current as? Activity
    }

    private fun positionRow(
        row: BottomTabsCustomRow,
        bottomTabs: BottomTabs,
        overlayHost: ViewGroup,
        activity: Activity,
    ) {
        val navBarInsetPx = systemBottomInsetPx(overlayHost, bottomTabs)
        val placement = BottomTabsCustomRowLayout.resolvePlacement(
            activity,
            row,
            bottomTabs,
            overlayHost,
            navBarInsetPx,
        ) ?: return

        val next = LastPlacement(
            placement.left,
            placement.top,
            placement.width,
            placement.height,
            placement.rowSafeBottomInsetPx,
        )
        if (row.getTag(TAG_LAST_PLACEMENT) == next) {
            return
        }
        row.setTag(TAG_LAST_PLACEMENT, next)
        row.setSafeBottomInsetPx(placement.rowSafeBottomInsetPx)

        val lp = (row.layoutParams as? FrameLayout.LayoutParams)
            ?: FrameLayout.LayoutParams(placement.width, placement.height)
        lp.width = placement.width
        lp.height = placement.height
        lp.leftMargin = placement.left
        lp.topMargin = placement.top
        row.layoutParams = lp
        row.bringToFront()
    }

    private fun systemBottomInsetPx(overlayHost: View, bottomTabs: View): Int {
        for (source in listOf(bottomTabs, overlayHost)) {
            val insets = source.rootWindowInsets ?: continue
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val navBars = insets.getInsets(WindowInsets.Type.navigationBars()).bottom
                if (navBars > 0) return navBars
            } else {
                @Suppress("DEPRECATION")
                val legacy = insets.systemWindowInsetBottom
                if (legacy > 0) return legacy
            }
        }
        return 0
    }

    private val TAG_ATTACHMENT = "rnnCustomRowAttachment".hashCode()
    private val TAG_LAST_PLACEMENT = "rnnCustomRowLastPlacement".hashCode()
}

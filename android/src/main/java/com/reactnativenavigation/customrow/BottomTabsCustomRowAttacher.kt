package com.reactnativenavigation.customrow

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.widget.FrameLayout
import com.reactnativenavigation.views.bottomtabs.BottomTabs

/**
 * Activity-lifecycle observer that watches every started activity for
 * `BottomTabs` instances using the existing custom-tab path and injects a
 * [BottomTabsCustomRow] above them, hiding the native chrome via public
 * `View` APIs (`alpha = 0f`).
 *
 * Layout listeners are registered once per activity (on the decor view) and
 * placement updates are deduplicated so Espresso / Detox can reach idle.
 */
internal object BottomTabsCustomRowAttacher : Application.ActivityLifecycleCallbacks {

    @Volatile private var registered: Boolean = false
    @Volatile private var lastResumedActivity: Activity? = null

    private data class LastPlacement(
        val left: Int,
        val top: Int,
        val width: Int,
        val height: Int,
        val safeBottomInsetPx: Int,
    )

    fun registerOnce(application: Application, currentActivity: Activity? = null) {
        if (!registered) {
            registered = true
            application.registerActivityLifecycleCallbacks(this)
        }
        if (currentActivity != null && lastResumedActivity == null) {
            lastResumedActivity = currentActivity
            ensureLayoutObserver(currentActivity)
            tryAttach(currentActivity)
        }
    }

    fun rescan() {
        val activity = lastResumedActivity ?: return
        tryAttach(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ensureLayoutObserver(activity)
        tryAttach(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        ensureLayoutObserver(activity)
        tryAttach(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        lastResumedActivity = activity
        ensureLayoutObserver(activity)
        tryAttach(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        if (lastResumedActivity === activity) lastResumedActivity = null
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        activity.window?.decorView?.setTag(TAG_OBSERVING, null)
    }

    private fun ensureLayoutObserver(activity: Activity) {
        val decor = activity.window?.decorView as? ViewGroup ?: return
        if (decor.getTag(TAG_OBSERVING) == true) return
        decor.setTag(TAG_OBSERVING, true)
        decor.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    tryAttach(activity)
                }
            }
        )
    }

    private fun tryAttach(activity: Activity) {
        val scanRoot = activity.window?.decorView as? ViewGroup
            ?: activity.findViewById<View>(android.R.id.content) as? ViewGroup
            ?: return
        val overlayHost = activity.findViewById<View>(android.R.id.content) as? ViewGroup
            ?: scanRoot

        forEachBottomTabs(scanRoot) { bottomTabs ->
            if (!bottomTabs.hasCustomItemViews()) return@forEachBottomTabs

            val existing = bottomTabs.getTag(TAG_ATTACHED_ROW_ID) as? BottomTabsCustomRow
            if (existing != null) {
                ensureRowHostedOn(existing, overlayHost)
                positionRow(existing, bottomTabs, overlayHost, activity)
                return@forEachBottomTabs
            }

            bottomTabs.setExternalCustomItemViewHost(true)
            val row = BottomTabsCustomRow(overlayHost.context, bottomTabs)
            overlayHost.addView(
                row,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
            )
            bottomTabs.setTag(TAG_ATTACHED_ROW_ID, row)
            bottomTabs.alpha = 0f
            bottomTabs.elevation = 0f
            positionRow(row, bottomTabs, overlayHost, activity)
        }
    }

    private fun ensureRowHostedOn(row: BottomTabsCustomRow, overlayHost: ViewGroup) {
        if (row.parent === overlayHost) return
        (row.parent as? ViewGroup)?.removeView(row)
        overlayHost.addView(
            row,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        )
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

    private fun forEachBottomTabs(view: View, block: (BottomTabs) -> Unit) {
        if (view is BottomTabs) {
            block(view)
            return
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                forEachBottomTabs(view.getChildAt(i), block)
            }
        }
    }

    private val TAG_ATTACHED_ROW_ID = "rnnBottomTabsCustomRow".hashCode()
    private val TAG_OBSERVING = "rnnCustomRowObserving".hashCode()
    private val TAG_LAST_PLACEMENT = "rnnCustomRowLastPlacement".hashCode()
}

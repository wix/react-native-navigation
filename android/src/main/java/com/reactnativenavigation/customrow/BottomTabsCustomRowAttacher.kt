package com.reactnativenavigation.customrow

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
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
 * Strict zero-touch: never instantiates, casts to, or modifies any
 * package-private state of the existing tabs implementation. Only the
 * public methods of `BottomTabs` (`hasCustomItemViews`, `getCustomItemView`,
 * `getItemsCount`, `setCurrentItem`, and standard `View` getters/setters)
 * are used.
 */
internal object BottomTabsCustomRowAttacher : Application.ActivityLifecycleCallbacks {

    @Volatile private var registered: Boolean = false
    @Volatile private var lastResumedActivity: Activity? = null

    private const val TAG = "RNNCustomRow"

    fun registerOnce(application: Application, currentActivity: Activity? = null) {
        if (!registered) {
            registered = true
            Log.d(TAG, "registerOnce: attacher installed")
            application.registerActivityLifecycleCallbacks(this)
        }
        // Bootstrap the case where the attacher is installed AFTER the
        // current activity has already passed onResume — lifecycle callbacks
        // wouldn't otherwise fire for it.
        if (currentActivity != null && lastResumedActivity == null) {
            lastResumedActivity = currentActivity
            attachWhenReady(currentActivity)
        }
    }

    /**
     * Manually re-run the attach scan against the currently-resumed activity,
     * if any. Called when the JS-side config changes or when a new
     * `Navigation` command is forwarded by [BottomTabsCustomRowModule.notifyCommand].
     */
    fun rescan() {
        val activity = lastResumedActivity ?: return
        attachWhenReady(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        attachWhenReady(activity)
    }

    override fun onActivityStarted(activity: Activity) { attachWhenReady(activity) }
    override fun onActivityResumed(activity: Activity) {
        lastResumedActivity = activity
        attachWhenReady(activity)
    }
    override fun onActivityPaused(activity: Activity) {
        if (lastResumedActivity === activity) lastResumedActivity = null
    }
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    private fun attachWhenReady(activity: Activity) {
        val root = activity.findViewById<View>(android.R.id.content) as? ViewGroup ?: return
        val rootObserver = root.viewTreeObserver
        rootObserver.addOnGlobalLayoutListener {
            tryAttach(activity, root)
        }
        // Also observe the decor view so we catch modals/overlays that may
        // attach to the activity's window instead of `android.R.id.content`.
        val decor = activity.window?.decorView as? ViewGroup
        decor?.viewTreeObserver?.addOnGlobalLayoutListener {
            tryAttach(activity, decor)
        }
        tryAttach(activity, root)
    }

    private fun tryAttach(activity: Activity, scanRoot: ViewGroup) {
        val overlayHost = (activity.findViewById<View>(android.R.id.content) as? ViewGroup)
            ?: scanRoot
        forEachBottomTabs(scanRoot) { bottomTabs ->
            if (!bottomTabs.hasCustomItemViews()) return@forEachBottomTabs

            val existing = bottomTabs.getTag(TAG_ATTACHED_ROW_ID) as? BottomTabsCustomRow
            if (existing != null) {
                // Tug-of-war is over once `externalCustomItemViewHost` is on,
                // so we only need to keep the row positioned correctly as the
                // native bar resizes.
                positionRow(existing, bottomTabs, overlayHost)
                return@forEachBottomTabs
            }

            Log.d(
                TAG,
                "attaching custom row over BottomTabs ${bottomTabs.hashCode()} " +
                        "items=${bottomTabs.itemsCount} overlayHost=$overlayHost"
            )
            // Tell the existing bar to stop hosting the React item views so we
            // can own them in our floating row.
            bottomTabs.setExternalCustomItemViewHost(true)

            val row = BottomTabsCustomRow(overlayHost.context, bottomTabs)
            // Add to the overlay host so the row can extend above the native
            // bar without being clipped by the bar's immediate parent.
            overlayHost.addView(
                row,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
            )
            bottomTabs.setTag(TAG_ATTACHED_ROW_ID, row)
            bottomTabs.alpha = 0f      // public View API, hides visuals only
            bottomTabs.elevation = 0f
            positionRow(row, bottomTabs, overlayHost)
        }
    }

    private fun positionRow(row: BottomTabsCustomRow, bottomTabs: BottomTabs, overlayHost: ViewGroup) {
        val nativeHeight = bottomTabs.height
        if (nativeHeight <= 0) return

        row.setSafeBottomInsetPx(systemBottomInsetPx(overlayHost, bottomTabs))

        val totalHeight = row.effectiveTotalHeightPx(nativeHeight)
        val horizontalMargin = row.effectiveHorizontalMarginPx()

        val tabLoc = IntArray(2).also(bottomTabs::getLocationOnScreen)
        val hostLoc = IntArray(2).also(overlayHost::getLocationOnScreen)
        val tabLeftInHost = tabLoc[0] - hostLoc[0]
        val tabTopInHost = tabLoc[1] - hostLoc[1]
        val tabRightInHost = tabLeftInHost + bottomTabs.width
        val tabBottomInHost = tabTopInHost + nativeHeight

        val left = tabLeftInHost + horizontalMargin
        val right = tabRightInHost - horizontalMargin
        val bottom = tabBottomInHost
        val top = bottom - totalHeight

        val lp = (row.layoutParams as? FrameLayout.LayoutParams)
            ?: FrameLayout.LayoutParams(right - left, totalHeight)
        lp.width = right - left
        lp.height = totalHeight
        lp.leftMargin = left
        lp.topMargin = top
        row.layoutParams = lp
        row.bringToFront()
        row.requestLayout()
    }

    private fun systemBottomInsetPx(overlayHost: View, bottomTabs: View): Int {
        // Prefer the bottomTabs' own insets; fall back to the overlay host's.
        val sources = listOf(bottomTabs, overlayHost)
        for (source in sources) {
            val insets = source.rootWindowInsets ?: continue
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val sysBars = insets.getInsets(WindowInsets.Type.systemBars()).bottom
                val ime = insets.getInsets(WindowInsets.Type.ime()).bottom
                if (sysBars > 0 || ime > 0) return maxOf(sysBars, 0)
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
}

package com.reactnativenavigation.customrow

import android.content.Context
import android.widget.FrameLayout
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.views.bottomtabs.BottomTabs
import org.junit.Assert.assertEquals
import org.junit.Test

class BottomTabsCustomRowTest : BaseTest() {
    @Test
    fun reattachedRowContinuesReceivingConfigurationUpdates() {
        val activity = newActivity()
        val container = FrameLayout(activity)
        val row = BottomTabsCustomRow(activity, BottomTabs(activity))
        activity.setContentView(container)

        try {
            container.addView(row)
            BottomTabsCustomRowConfigStore.update(
                BottomTabsCustomRowOptions(horizontalMargin = 24f)
            )
            idleMainLooper()
            assertEquals(dp(activity, 24f), row.effectiveHorizontalMarginPx())

            container.removeView(row)
            BottomTabsCustomRowConfigStore.update(
                BottomTabsCustomRowOptions(horizontalMargin = 32f)
            )
            idleMainLooper()
            assertEquals(dp(activity, 24f), row.effectiveHorizontalMarginPx())

            container.addView(row)
            assertEquals(dp(activity, 32f), row.effectiveHorizontalMarginPx())

            BottomTabsCustomRowConfigStore.update(
                BottomTabsCustomRowOptions(horizontalMargin = 40f)
            )
            idleMainLooper()
            assertEquals(dp(activity, 40f), row.effectiveHorizontalMarginPx())
        } finally {
            container.removeView(row)
            BottomTabsCustomRowConfigStore.update(BottomTabsCustomRowOptions())
        }
    }

    private fun dp(context: Context, value: Float): Int =
        (value * context.resources.displayMetrics.density).toInt()
}

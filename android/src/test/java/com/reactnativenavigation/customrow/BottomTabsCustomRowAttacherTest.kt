package com.reactnativenavigation.customrow

import android.view.ViewGroup
import android.widget.FrameLayout
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.views.bottomtabs.BottomTabs
import com.reactnativenavigation.views.bottomtabs.CustomBottomTabItemView
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class BottomTabsCustomRowAttacherTest : BaseTest() {
    @Test
    fun attachesOnlyForCustomItemsAndCleansUpOnDetachAndReattach() {
        val activity = newActivity()
        val root = FrameLayout(activity)
        val tabs = BottomTabs(activity)
        root.addView(tabs, FrameLayout.LayoutParams(400, 60))
        activity.setContentView(root)
        val host = activity.findViewById<ViewGroup>(android.R.id.content)
        fun rows() = (0 until host.childCount).map { host.getChildAt(it) }.filterIsInstance<BottomTabsCustomRow>()
        assertThat(rows()).isEmpty()
        tabs.setCustomItemViews(listOf(CustomBottomTabItemView(activity, "tab", "Tab", 0, true, null)))
        assertThat(rows()).hasSize(1)
        BottomTabsCustomRowAttacher.attach(tabs)
        assertThat(rows()).hasSize(1)
        root.removeView(tabs)
        assertThat(rows()).isEmpty()
        assertThat(tabs.alpha).isEqualTo(1f)
        root.addView(tabs)
        assertThat(rows()).hasSize(1)
        tabs.setCustomItemViews(emptyList())
        assertThat(rows()).isEmpty()
        assertThat(tabs.alpha).isEqualTo(1f)
        root.removeView(tabs)
    }
}

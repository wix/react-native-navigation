package com.reactnativenavigation.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.reactnativenavigation.BaseRobolectricTest
import com.reactnativenavigation.utils.SystemUiUtils.STATUS_BAR_HEIGHT_TRANSLUCENCY
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify
import org.robolectric.Robolectric
import kotlin.math.ceil

class SystemUiUtilsTest : BaseRobolectricTest() {

    @After
    fun afterEach() {
        SystemUiUtils.tearDown()
    }

    @Test
    fun `setStatusBarColor - should change color considering alpha`() {
        val window = Mockito.mock(Window::class.java)
        val alphaColor = Color.argb(44, 22, 255, 255)
        val color = Color.argb(255, 22, 255, 255)
        SystemUiUtils.setStatusBarColor(window, alphaColor, false)

        verify(window).statusBarColor = alphaColor

        SystemUiUtils.setStatusBarColor(window, color, true)

        verify(window).statusBarColor = Color.argb(ceil(STATUS_BAR_HEIGHT_TRANSLUCENCY*255).toInt(), 22, 255, 255)
    }

    @Test
    fun `setupSystemBarBackgrounds - initializes navigation bar background from resolved color`() {
        val activity = Robolectric.setupActivity(AppCompatActivity::class.java)
        val contentLayout = FrameLayout(activity)
        val initialColor = Color.RED
        SystemUiUtils.setNavigationBarBackgroundColor(activity.window, initialColor, false)

        SystemUiUtils.setupSystemBarBackgrounds(activity, contentLayout)

        assertThat(getBackgroundColor(getNavigationBarBackground(contentLayout))).isEqualTo(initialColor)
    }

    @Test
    fun `setNavigationBarBackgroundColor - updates view and window color when edge-to-edge is inactive`() {
        val activity = Robolectric.setupActivity(AppCompatActivity::class.java)
        val contentLayout = FrameLayout(activity)
        @Suppress("DEPRECATION")
        activity.window.navigationBarColor = Color.BLACK
        SystemUiUtils.setupSystemBarBackgrounds(activity, contentLayout)

        SystemUiUtils.setNavigationBarBackgroundColor(activity.window, Color.WHITE, true)

        assertThat(getBackgroundColor(getNavigationBarBackground(contentLayout))).isEqualTo(Color.WHITE)
        assertThat(activity.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
            .isEqualTo(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
    }

    @Test
    fun `setNavigationBarBackgroundColor - updates view and icon appearance when edge-to-edge is active`() {
        val activity = Robolectric.setupActivity(AppCompatActivity::class.java)
        val contentLayout = FrameLayout(activity)
        val initialColor = Color.RED
        SystemUiUtils.setNavigationBarBackgroundColor(activity.window, initialColor, false)
        SystemUiUtils.setupSystemBarBackgrounds(activity, contentLayout)
        SystemUiUtils.activateEdgeToEdge()

        SystemUiUtils.setNavigationBarBackgroundColor(activity.window, Color.WHITE, true)

        assertThat(getBackgroundColor(getNavigationBarBackground(contentLayout))).isEqualTo(Color.WHITE)
        assertThat(activity.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
            .isEqualTo(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
    }

    private fun getNavigationBarBackground(contentLayout: FrameLayout): View {
        return contentLayout.getChildAt(contentLayout.childCount - 1)
    }

    private fun getBackgroundColor(view: View): Int {
        return (view.background as ColorDrawable).color
    }
}

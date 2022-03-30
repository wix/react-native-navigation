package com.reactnativenavigation.utils

import android.graphics.Color
import android.view.Window
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.utils.SystemUiUtils.STATUS_BAR_HEIGHT_TRANSLUCENCY
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify
import kotlin.math.ceil

class SystemUiUtilsTest : BaseTest() {

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
}
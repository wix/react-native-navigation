package com.reactnativenavigation.react

import com.facebook.react.bridge.ReactApplicationContext
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.options.LayoutFactory
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class NavigationTurboModuleTest : BaseTest() {
    @Test
    fun command_handlesNonNavigationActivity() {
        val reactContext = mock<ReactApplicationContext>()
        whenever(reactContext.getCurrentActivity()).thenReturn(newActivity())
        val uut = NavigationTurboModule(reactContext, mock<LayoutFactory>())

        uut.setDefaultOptions(null)
        idleMainLooper()
    }
}

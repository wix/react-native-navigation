package com.reactnativenavigation

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
abstract class BaseRobolectricTest {

    val context: Context = RuntimeEnvironment.getApplication()
    lateinit var mockConfiguration: Configuration


    @Before
    open fun beforeEach() {
        NavigationApplication.instance = mock(NavigationApplication::class.java)
        mockConfiguration = mock(Configuration::class.java)
        val res: Resources = mock(Resources::class.java)
        mockConfiguration.uiMode = Configuration.UI_MODE_NIGHT_NO
        whenever(res.getConfiguration()).thenReturn(mockConfiguration)
        whenever(NavigationApplication.instance.resources).thenReturn(res)
        whenever(res.getColor(ArgumentMatchers.anyInt())).thenReturn(0x00000)
        whenever(res.getColor(ArgumentMatchers.anyInt(), any())).thenReturn(0x00000)
    }
}
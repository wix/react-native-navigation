package com.reactnativenavigation.utils

import android.app.Application
import android.content.Context
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.devsupport.interfaces.DevSupportManager
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest = Config.NONE)
class ContextTest {
    @Test
    fun readsDeveloperSupportFromReactHostWithoutAccessingLegacyHost() {
        val manager = mock<DevSupportManager>()
        val host = mock<ReactHost>()
        whenever(host.devSupportManager).thenReturn(manager)
        val application = object : Application(), ReactApplication {
            override val reactHost = host
            override val reactNativeHost: ReactNativeHost
                get() = error("ReactHost-only applications have no legacy host")
        }
        val context = mock<Context>()
        whenever(context.applicationContext).thenReturn(application)

        whenever(manager.devSupportEnabled).thenReturn(true)
        assertThat(context.isDebug()).isTrue()
        whenever(manager.devSupportEnabled).thenReturn(false)
        assertThat(context.isDebug()).isFalse()
        whenever(host.devSupportManager).thenReturn(null)
        assertThat(context.isDebug()).isFalse()
    }
}

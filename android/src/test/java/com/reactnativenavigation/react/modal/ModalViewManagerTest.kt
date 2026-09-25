package com.reactnativenavigation.react.modal

import com.facebook.react.bridge.ReactContext
import com.reactnativenavigation.BaseTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ModalViewManagerTest : BaseTest() {
    @Test
    fun dropRemovesLifecycleListenerWithoutActiveNavigator() {
        val reactContext = mock<ReactContext>()
        whenever(reactContext.currentActivity).thenReturn(null)
        val modal = mock<ModalHostLayout>()

        ModalViewManager(reactContext).onDropViewInstance(modal)

        verify(modal).onDropInstance()
    }
}

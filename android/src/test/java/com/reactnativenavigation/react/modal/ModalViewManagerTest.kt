package com.reactnativenavigation.react.modal

import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.JavaOnlyMap
import com.facebook.react.uimanager.ReactStylesDiffMap
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.NavigationActivity
import com.reactnativenavigation.viewcontrollers.navigator.Navigator
import org.junit.Test
import org.mockito.kotlin.*

class ModalViewManagerTest : BaseTest() {
    @Test
    fun updatingModalPropsDoesNotPresentTheSameControllerTwice() {
        val navigator = mock<Navigator>()
        val activity = mock<NavigationActivity>()
        whenever(activity.navigator).thenReturn(navigator)
        val context = mock<ReactContext>()
        whenever(context.currentActivity).thenReturn(activity)
        val modal = mock<ModalHostLayout>()
        val controller = mock<ModalLayoutController>()
        whenever(modal.viewController).thenReturn(controller)
        whenever(modal.isPresented).thenCallRealMethod()
        doCallRealMethod().whenever(modal).isPresented = true
        val manager = ModalViewManager(context)
        manager.updateProperties(modal, ReactStylesDiffMap(JavaOnlyMap()))
        manager.updateProperties(modal, ReactStylesDiffMap(JavaOnlyMap()))
        verify(navigator, times(1)).showModal(eq(controller), any())
    }
}

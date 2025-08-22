package com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.modes

import androidx.test.espresso.Espresso
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.any

class AfterInitialTabTest : AttachModeTest() {

    @Before
    override fun setup() {
        super.setup()
        uut = AfterInitialTab(parent, tabs, presenter, options)
    }

    @Test
    fun attach_initialTabIsAttached() {
        uut.attach()
        assertIsChild(parent, tab2)
    }

    @Test
    fun attach_otherTabsAreAttachedAfterInitialTab() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            uut.attach()

            assertNotChildOf(parent, *otherTabs())

            initialTab().onViewWillAppear()
        }
        Espresso.onIdle()
        assertIsChild(parent, *otherTabs())
    }

    @Test
    fun destroy() {
        uut.destroy()
        verify(initialTab()).removeOnAppearedListener(any())
    }
} 
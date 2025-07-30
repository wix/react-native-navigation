package com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.modes

import org.junit.Before
import org.junit.Test

class OnSwitchToTabTest : AttachModeTest() {

    @Before
    override fun setup() {
        super.setup()
        uut = OnSwitchToTab(parent, tabs, presenter, options)
    }

    @Test
    fun attach_onlyInitialTabIsAttached() {
        uut.attach()
        assertIsChild(parent, initialTab())
        assertNotChildOf(parent, *otherTabs())
    }

    @Test
    fun onTabSelected_initialTabIsNotHandled() {
        uut.onTabSelected(initialTab())
        assertNotChildOf(parent, initialTab())
    }

    @Test
    fun onTabSelected_otherTabIsAttached() {
        uut.onTabSelected(tab1)
        assertIsChild(parent, tab1)
    }
} 
package com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.modes

import org.junit.Before
import org.junit.Test

class TogetherTest : AttachModeTest() {

    @Before
    override fun setup() {
        super.setup()
        uut = Together(parent, tabs, presenter, options)
    }

    @Test
    fun attach_allTabsAreAttached() {
        uut.attach()
        assertIsChild(parent, *tabs.toTypedArray())
    }
} 
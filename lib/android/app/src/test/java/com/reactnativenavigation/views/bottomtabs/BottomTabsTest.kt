package com.reactnativenavigation.views.bottomtabs

import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.reactnativenavigation.BaseTest
import org.junit.Test

class BottomTabsTest : BaseTest() {
    private lateinit var uut: BottomTabs

    override fun beforeEach() {
        uut = spy(BottomTabs(newActivity()))
    }

    @Test
    fun createItems_triggersSuperCall() {
        uut.createItems()
        verify(uut).superCreateItems()
    }

    @Test
    fun createItems_superNotInvokedWhenDisabled() {
        uut.disableItemsCreation()
        uut.createItems()
        verify(uut, never()).superCreateItems()
    }

    @Test
    fun createItems_superInvokedAfterItemCreationIsEnabledOnlyOnce() {
        uut.disableItemsCreation()
        uut.createItems()
        verify(uut, never()).superCreateItems()

        uut.enableItemsCreation()
        uut.enableItemsCreation()
        verify(uut, times(1)).superCreateItems()
    }
}
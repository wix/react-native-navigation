package com.reactnativenavigation.views.bottomtabs

import org.mockito.kotlin.never
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
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
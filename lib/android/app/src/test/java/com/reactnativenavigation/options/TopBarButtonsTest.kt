package com.reactnativenavigation.options

import com.reactnativenavigation.BaseTest
import org.assertj.core.api.Java6Assertions
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import kotlin.test.assertTrue

class TopBarButtonsTest : BaseTest() {
    private lateinit var uut: TopBarButtons

    override fun beforeEach() {
        uut = TopBarButtons()
    }

    @Test
    fun mergeWith_rightButtonsAreCopiedByValue() {
        val right = arrayListOf(ButtonOptions(), ButtonOptions())
        val other = TopBarButtons(right)

        uut.mergeWith(other)
        assertThat(uut.right).hasSize(2)
        right.forEachIndexed { index, buttonOptions -> assertThat(buttonOptions).isNotEqualTo(uut.right!![index]) }
    }
}
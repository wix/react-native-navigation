package com.reactnativenavigation.options

import android.graphics.Color
import com.reactnativenavigation.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.Test

class BadgeOptionsTest : BaseTest() {
    lateinit var uut: BadgeOptions

    @Test
    fun `parse - should parse json options`() {
        uut = parseBadgeOptions(newActivity(), JSONObject().apply {
            put("text", "hello")
            put("textColor", Color.RED)
            put("backgroundColor", Color.RED)
        })

        assertThat(uut.text.get()).isEqualTo("hello")
        assertThat(uut.textColor.get()).isEqualTo(Color.RED)
        assertThat(uut.backgroundColor.get()).isEqualTo(Color.RED)
    }

    @Test
    fun `parse - should parse null for missing json options`() {
        uut = parseBadgeOptions(newActivity(), JSONObject().apply {
            put("text", "hello")
            put("backgroundColor", Color.RED)
        })

        assertThat(uut.text.get()).isEqualTo("hello")
        assertThat(uut.textColor.hasValue()).isFalse()
        assertThat(uut.backgroundColor.get()).isEqualTo(Color.RED)

        uut = parseBadgeOptions(newActivity(), JSONObject().apply {
        })

        assertThat(uut.text.hasValue()).isFalse()
        assertThat(uut.textColor.hasValue()).isFalse()
        assertThat(uut.backgroundColor.hasValue()).isFalse()
    }

    @Test
    fun `hasValue - true when one of the props has value false otherwhise`() {
        uut = parseBadgeOptions(newActivity(), JSONObject().apply {
            put("text", "hello")
            put("backgroundColor", Color.RED)
        })
        assertThat(uut.hasValue()).isTrue()

        uut = parseBadgeOptions(newActivity(), JSONObject().apply {
            put("backgroundColor", Color.RED)
        })
        assertThat(uut.hasValue()).isTrue()

        uut = parseBadgeOptions(newActivity(), JSONObject().apply {
        })
        assertThat(uut.hasValue()).isFalse()
    }

    @Test
    fun `mergeWith - should merge changes only`() {
        uut = parseBadgeOptions(newActivity(), JSONObject().apply {
            put("text", "hello")
            put("backgroundColor", Color.RED)
        })

        assertThat(uut.textColor.hasValue()).isFalse()
        val uut2 = parseBadgeOptions(newActivity(), JSONObject().apply {
            put("backgroundColor", Color.YELLOW)
            put("textColor", Color.BLUE)
        })
        uut.mergeWith(uut2)

        assertThat(uut.textColor.hasValue()).isTrue()
        assertThat(uut.backgroundColor.get()).isEqualTo(Color.YELLOW)
    }
}
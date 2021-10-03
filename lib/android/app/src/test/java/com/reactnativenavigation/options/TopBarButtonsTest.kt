package com.reactnativenavigation.options

import android.app.Activity
import com.reactnativenavigation.BaseTest
import org.assertj.core.api.Java6Assertions
import org.assertj.core.api.Java6Assertions.assertThat
import org.json.JSONObject
import org.junit.Test
import java.lang.Exception
import kotlin.test.assertTrue

class TopBarButtonsTest : BaseTest() {
    private final var ACCESSIBILITY_LABEL_AS_DEFAUL_OPTIONS: String = "accessibilityLabel as default option"
    private final var ACCESSIBILITY_LABEL_AS_CUSTOM_OPTIONS: String = "My accessibilityLabel explicitly given"
    private final var OTHER_ACCESSIBILITY_LABEL: String = "Other accessibilityLabel"
    private lateinit var uut: TopBarButtons
    private var activity: Activity? = null

    override fun beforeEach() {
        uut = TopBarButtons()
        activity = newActivity()
    }

    fun createBackButtonJson(label: String?): JSONObject {
        return JSONObject().put("accessibilityLabel", label);
    }

    fun createTopBarOptionsJson(label: String?): JSONObject {
        return JSONObject().put("backButton", createBackButtonJson(label))
    }

    @Test
    fun mergeWith_rightButtonsAreCopiedByValue() {
        val right = arrayListOf(ButtonOptions(), ButtonOptions())
        val other = TopBarButtons(right)

        uut.mergeWith(other)
        assertThat(uut.right).hasSize(2)
        right.forEachIndexed { index, buttonOptions -> assertThat(buttonOptions).isNotEqualTo(uut.right!![index]) }
    }

    @Test
    fun mergeWithDefault_dontOverrideCustomAccessibilityLabel() {
        val default: TopBarButtons = TopBarButtons.parse(activity, createTopBarOptionsJson(ACCESSIBILITY_LABEL_AS_DEFAUL_OPTIONS))
        val current: TopBarButtons = TopBarButtons.parse(activity, createTopBarOptionsJson(ACCESSIBILITY_LABEL_AS_CUSTOM_OPTIONS))

        current.mergeWithDefault(default)
        assertThat(current.back.accessibilityLabel.get()).isEqualTo(ACCESSIBILITY_LABEL_AS_CUSTOM_OPTIONS)
    }

    @Test
    fun mergeWithDefault_overrideDefaultAccessibilityLabel() {
        val default: TopBarButtons = TopBarButtons.parse(activity, createTopBarOptionsJson(ACCESSIBILITY_LABEL_AS_DEFAUL_OPTIONS))
        val current: TopBarButtons = TopBarButtons.parse(activity, createTopBarOptionsJson(BackButton.DEFAULT_ACCESSIBILITY_LABEL))

        current.mergeWithDefault(default)
        assertThat(current.back.accessibilityLabel.get()).isEqualTo(ACCESSIBILITY_LABEL_AS_DEFAUL_OPTIONS)
    }

    @Test
    fun mergeWithDefault_overrideEmptyAccessibilityLabel() {
        val default: TopBarButtons = TopBarButtons.parse(activity, createTopBarOptionsJson(ACCESSIBILITY_LABEL_AS_DEFAUL_OPTIONS))
        val current: TopBarButtons = TopBarButtons.parse(activity, createTopBarOptionsJson(null))

        current.mergeWithDefault(default)
        assertThat(current.back.accessibilityLabel.get()).isEqualTo(ACCESSIBILITY_LABEL_AS_DEFAUL_OPTIONS)
    }

    @Test
    fun mergeWith_dontOverrideCurrentAccessibilityLabelWhenPassEmptyValue() {
        val current: TopBarButtons = TopBarButtons.parse(activity, createTopBarOptionsJson(ACCESSIBILITY_LABEL_AS_CUSTOM_OPTIONS))
        val other: TopBarButtons = TopBarButtons.parse(activity, createTopBarOptionsJson(null))

        current.mergeWith(other)
        assertThat(current.back.accessibilityLabel.get()).isEqualTo(ACCESSIBILITY_LABEL_AS_CUSTOM_OPTIONS)
    }

    @Test
    fun mergeWith_dontOverrideCurrentAccessibilityLabelWhenPassDefaultValue() {
        val current: TopBarButtons = TopBarButtons.parse(activity, createTopBarOptionsJson(ACCESSIBILITY_LABEL_AS_CUSTOM_OPTIONS))
        val other: TopBarButtons = TopBarButtons.parse(activity, createTopBarOptionsJson(BackButton.DEFAULT_ACCESSIBILITY_LABEL))

        current.mergeWith(other)
        assertThat(current.back.accessibilityLabel.get()).isEqualTo(ACCESSIBILITY_LABEL_AS_CUSTOM_OPTIONS)
    }

    @Test
    fun mergeWith_overridePreviousAccessibilityLabelWithNewValue() {
        val current: TopBarButtons = TopBarButtons.parse(activity, createTopBarOptionsJson(ACCESSIBILITY_LABEL_AS_CUSTOM_OPTIONS))
        val other: TopBarButtons = TopBarButtons.parse(activity, createTopBarOptionsJson(OTHER_ACCESSIBILITY_LABEL))

        current.mergeWith(other)
        assertThat(current.back.accessibilityLabel.get()).isEqualTo(OTHER_ACCESSIBILITY_LABEL)
    }
}
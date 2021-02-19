package com.reactnativenavigation.options

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.reactnativenavigation.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONException
import org.json.JSONObject
import org.junit.Test


class ModalAnimationOptionsTest : BaseTest() {
    lateinit var uut: ModalAnimationOptions

    @Test
    fun `parse - empty for invalid payload`() {
        uut = parseModalAnimationOptions(JSONObject())
        assertThat(uut.hasValue()).isFalse()
        assertThat(uut.enter.hasValue()).isFalse()
        assertThat(uut.exit.hasValue()).isFalse()

        uut = parseModalAnimationOptions(JSONObject().apply { put("some", "value") })
        assertThat(uut.hasValue()).isFalse()
    }

    @Test
    fun `parse - should parse enabled animation options from valid payload`() {
        uut = parseModalAnimationOptions(newModalAnimationJson(true))
        assertThat(uut.hasValue()).isTrue()
        assertThat(uut.enter.hasValue()).isTrue()
        assertThat(uut.exit.hasValue()).isTrue()
        assertThat(uut.exit.enabled.isTrueOrUndefined).isTrue()
        assertThat(uut.enter.enabled.isTrueOrUndefined).isTrue()
    }

    @Test
    fun `parse - should parse disabled animation options from valid payload`() {
        uut = parseModalAnimationOptions(newModalAnimationJson(false))
        assertThat(uut.hasValue()).isTrue()
        assertThat(uut.enter.hasValue()).isTrue()
        assertThat(uut.exit.hasValue()).isTrue()
        assertThat(uut.exit.enabled.isTrueOrUndefined).isFalse()
        assertThat(uut.enter.enabled.isTrueOrUndefined).isFalse()
    }

    @Test
    fun `hasValue should return true if one of enter, exit, sharedElements, elementTransitions has value `() {
        uut = ModalAnimationOptions()
        assertThat(uut.hasValue()).isFalse()

        uut = ModalAnimationOptions(enter = AnimationOptions(newAnimationOptionsJson(false)))
        assertThat(uut.hasValue()).isTrue()

        uut = ModalAnimationOptions(exit = AnimationOptions(newAnimationOptionsJson(false)))
        assertThat(uut.hasValue()).isTrue()


        val mockSharedElements: SharedElements = mock { }
        whenever(mockSharedElements.hasValue()).thenReturn(true)
        uut = ModalAnimationOptions(sharedElements = mockSharedElements)
        assertThat(uut.hasValue()).isTrue()

        val mockElementsTransitions: ElementTransitions = mock { }
        whenever(mockElementsTransitions.hasValue()).thenReturn(true)
        uut = ModalAnimationOptions(elementTransitions = mockElementsTransitions)
        assertThat(uut.hasValue()).isTrue()

    }


    @Test
    fun `hasElementTransition should return true if one of shared elements, element Transitions has value`() {
        uut = ModalAnimationOptions()
        assertThat(uut.hasElementTransitions()).isFalse()

        val mockSharedElements: SharedElements = mock { }
        whenever(mockSharedElements.hasValue()).thenReturn(true)
        uut = ModalAnimationOptions(sharedElements = mockSharedElements)
        assertThat(uut.hasElementTransitions()).isTrue()

        val mockElementsTransitions: ElementTransitions = mock { }
        whenever(mockElementsTransitions.hasValue()).thenReturn(true)
        uut = ModalAnimationOptions(elementTransitions = mockElementsTransitions)
        assertThat(uut.hasElementTransitions()).isTrue()

    }

}

fun newAnimationOptionsJson(enabled: Boolean) =
        JSONObject().apply {
            put("enabled", enabled)
            put("translationY", newBasicValueAnimationJson())
        }

fun newBasicValueAnimationJson() =
        JSONObject().apply {
            put("from", 0.0)
            put("to", 1.0)
            put("duration", 100)
            put("interpolation", JSONObject().apply { put("type", "decelerate") })
        }


fun newModalAnimationJson(enabled: Boolean = true) =
        JSONObject().apply {
            put("enter", newAnimationOptionsJson(enabled))
            put("exit", newAnimationOptionsJson(enabled))
        }

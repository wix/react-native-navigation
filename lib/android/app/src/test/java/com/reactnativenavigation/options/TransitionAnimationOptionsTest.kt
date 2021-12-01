package com.reactnativenavigation.options
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.reactnativenavigation.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test


class TransitionAnimationOptionsTest : BaseTest() {
    lateinit var uut: TransitionAnimationOptions

    @Test
    fun `parse - empty for invalid payload`() {
        uut = parseTransitionAnimationOptions(JSONObject())
        assertThat(uut.hasValue()).isFalse()
        assertThat(uut.enter.hasValue()).isFalse()
        assertThat(uut.exit.hasValue()).isFalse()

        uut = parseTransitionAnimationOptions(JSONObject().apply { put("some", "value") })
        assertThat(uut.hasValue()).isFalse()
    }

    @Test
    fun `parse - should parse enabled animation options from valid payload`() {
        uut = parseTransitionAnimationOptions(newModalAnimationJson(true))
        assertThat(uut.hasValue()).isTrue()
        assertThat(uut.enter.hasValue()).isTrue()
        assertThat(uut.exit.hasValue()).isTrue()
        assertThat(uut.exit.enabled.isTrueOrUndefined).isTrue()
        assertThat(uut.enter.enabled.isTrueOrUndefined).isTrue()
    }

    @Test
    fun `parse - should parse disabled animation options from valid payload`() {
        uut = parseTransitionAnimationOptions(newModalAnimationJson(false))
        assertThat(uut.hasValue()).isTrue()
        assertThat(uut.enter.hasValue()).isTrue()
        assertThat(uut.exit.hasValue()).isTrue()
        assertThat(uut.exit.enabled.isTrueOrUndefined).isFalse()
        assertThat(uut.enter.enabled.isTrueOrUndefined).isFalse()
    }

    @Test
    fun `hasValue should return true if one of enter, exit, sharedElements, elementTransitions has value `() {
        uut = TransitionAnimationOptions()
        assertThat(uut.hasValue()).isFalse()

        uut = TransitionAnimationOptions(enter = AnimationOptions(newAnimationOptionsJson(false)))
        assertThat(uut.hasValue()).isTrue()

        uut = TransitionAnimationOptions(exit = AnimationOptions(newAnimationOptionsJson(false)))
        assertThat(uut.hasValue()).isTrue()


        val mockSharedElements: SharedElements = mock { }
        whenever(mockSharedElements.hasValue()).thenReturn(true)
        uut = TransitionAnimationOptions(sharedElements = mockSharedElements)
        assertThat(uut.hasValue()).isTrue()

        val mockElementsTransitions: ElementTransitions = mock { }
        whenever(mockElementsTransitions.hasValue()).thenReturn(true)
        uut = TransitionAnimationOptions(elementTransitions = mockElementsTransitions)
        assertThat(uut.hasValue()).isTrue()

    }


    @Test
    fun `hasElementTransition should return true if one of shared elements, element Transitions has value`() {
        uut = TransitionAnimationOptions()
        assertThat(uut.hasElementTransitions()).isFalse()

        val mockSharedElements: SharedElements = mock { }
        whenever(mockSharedElements.hasValue()).thenReturn(true)
        uut = TransitionAnimationOptions(sharedElements = mockSharedElements)
        assertThat(uut.hasElementTransitions()).isTrue()

        val mockElementsTransitions: ElementTransitions = mock { }
        whenever(mockElementsTransitions.hasValue()).thenReturn(true)
        uut = TransitionAnimationOptions(elementTransitions = mockElementsTransitions)
        assertThat(uut.hasElementTransitions()).isTrue()

    }

}

fun newSharedElementAnimationOptionsJson() =
        JSONArray().apply {
            put(JSONObject().apply {
                put("fromId", "1")
                put("toId", "2")
                put("duration", "30")
            })
            put(JSONObject().apply {
                put("fromId", "3")
                put("toId", "4")
                put("duration", "30")
            })
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

package com.reactnativenavigation.options

import androidx.annotation.RestrictTo
import com.reactnativenavigation.options.params.Bool
import org.json.JSONObject

fun parseModalAnimationOptions(jsonObject: JSONObject?): ModalAnimationOptions {
    return jsonObject?.let {
        val modalAnimationOptions = ModalAnimationOptions(
                AnimationOptions(jsonObject.optJSONObject("enter")),
                AnimationOptions(jsonObject.optJSONObject("exit"))
        )
        if (jsonObject.has("sharedElementTransitions")) {
            val json = jsonObject.getJSONObject("sharedElementTransitions")
            modalAnimationOptions.sharedElements = SharedElements.parse(json)
        }
        if (jsonObject.has("elementTransitions")) {
            val json = jsonObject.getJSONObject("elementTransitions")
            modalAnimationOptions.elementTransitions = ElementTransitions.parse(json)
        }
        modalAnimationOptions
    } ?: ModalAnimationOptions()
}

open class ModalAnimationOptions(
        val enter: AnimationOptions = AnimationOptions(),
        val exit: AnimationOptions = AnimationOptions(),
        override var sharedElements: SharedElements = SharedElements(),
        override var elementTransitions: ElementTransitions = ElementTransitions()
) : LayoutAnimation {
    open fun hasValue() = enter.hasValue() || exit.hasValue() || sharedElements.hasValue() || elementTransitions.hasValue()
    open fun mergeWith(other: ModalAnimationOptions) {
        this.enter.mergeWith(other.enter)
        this.exit.mergeWith(other.exit)
        this.sharedElements.mergeWith(other.sharedElements)
        this.elementTransitions.mergeWith(other.elementTransitions)
    }

    open fun mergeWithDefault(other: ModalAnimationOptions) {
        if (!this.enter.hasValue()) this.enter.mergeWithDefault(other.enter)
        if (!this.exit.hasValue()) this.exit.mergeWithDefault(other.exit)
        if (!this.sharedElements.hasValue()) this.sharedElements.mergeWithDefault(other.sharedElements)
        if (!this.elementTransitions.hasValue()) this.elementTransitions.mergeWithDefault(other.elementTransitions)
    }

    open fun hasElementTransitions() = sharedElements.hasValue() || elementTransitions.hasValue()

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun setWaitForRender(waitForRender: Bool) {
        this.enter.waitForRender = waitForRender
        this.exit.waitForRender = waitForRender
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun toggle(enabled: Bool) {
        this.enter.enabled = enabled
        this.exit.enabled = enabled
    }
}


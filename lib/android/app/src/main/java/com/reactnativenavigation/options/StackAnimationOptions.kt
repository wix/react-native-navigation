package com.reactnativenavigation.options

import com.reactnativenavigation.options.animations.ViewAnimationOptions
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.NullBool
import com.reactnativenavigation.options.parsers.BoolParser
import org.json.JSONObject

open class StackAnimationOptions(json: JSONObject? = null) : LayoutAnimation {

    @JvmField var enabled: Bool = NullBool()
    @JvmField var waitForRender: Bool = NullBool()
    @JvmField var content = ViewAnimationOptions()
    @JvmField var bottomTabs = ViewAnimationOptions()
    @JvmField var topBar = ViewAnimationOptions()
    override var sharedElements = SharedElements()
    override var elementTransitions = ElementTransitions()

    init {
        parse(json)
    }

    fun mergeWith(other: StackAnimationOptions) {
        topBar.mergeWith(other.topBar)
        content.mergeWith(other.content)
        bottomTabs.mergeWith(other.bottomTabs)
        sharedElements.mergeWith(other.sharedElements)
        elementTransitions.mergeWith(other.elementTransitions)
        if (other.enabled.hasValue()) enabled = other.enabled
        if (other.waitForRender.hasValue()) waitForRender = other.waitForRender
    }

    fun mergeWithDefault(defaultOptions: StackAnimationOptions) {
        content.mergeWithDefault(defaultOptions.content)
        bottomTabs.mergeWithDefault(defaultOptions.bottomTabs)
        topBar.mergeWithDefault(defaultOptions.topBar)
        sharedElements.mergeWithDefault(defaultOptions.sharedElements)
        elementTransitions.mergeWithDefault(defaultOptions.elementTransitions)
        if (!enabled.hasValue()) enabled = defaultOptions.enabled
        if (!waitForRender.hasValue()) waitForRender = defaultOptions.waitForRender
    }

    fun hasEnterValue(): Boolean {
        return topBar.enter.hasValue() || content.enter.hasValue() || bottomTabs.enter.hasValue() || waitForRender.hasValue()
    }

    fun hasExitValue(): Boolean {
        return topBar.exit.hasValue() || content.exit.hasValue() || bottomTabs.exit.hasValue() || waitForRender.hasValue()
    }

    private fun parse(json: JSONObject?) {
        json ?: return
        content = ViewAnimationOptions(json.optJSONObject("content"))
        bottomTabs = ViewAnimationOptions(json.optJSONObject("bottomTabs"))
        topBar = ViewAnimationOptions(json.optJSONObject("topBar"))
        enabled = BoolParser.parseFirst(json, "enabled", "enable")
        waitForRender = BoolParser.parse(json, "waitForRender")
        sharedElements = SharedElements.parse(json)
        elementTransitions = ElementTransitions.parse(json)
    }
}
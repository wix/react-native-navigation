package com.reactnativenavigation.options

import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.NullBool
import com.reactnativenavigation.options.parsers.BoolParser
import org.json.JSONObject

open class NestedAnimationsOptions : LayoutAnimation {
    @JvmField var enabled: Bool = NullBool()
    @JvmField var waitForRender: Bool = NullBool()
    @JvmField var content = AnimationOptions()
    @JvmField var bottomTabs = AnimationOptions()
    @JvmField var topBar = AnimationOptions()
    override var sharedElements = SharedElements()
    override var elementTransitions = ElementTransitions()

    fun mergeWith(other: NestedAnimationsOptions) {
        topBar.mergeWith(other.topBar)
        content.mergeWith(other.content)
        bottomTabs.mergeWith(other.bottomTabs)
        sharedElements.mergeWith(other.sharedElements)
        elementTransitions.mergeWith(other.elementTransitions)
        if (other.enabled.hasValue()) enabled = other.enabled
        if (other.waitForRender.hasValue()) waitForRender = other.waitForRender
    }

    fun mergeWithDefault(defaultOptions: NestedAnimationsOptions) {
        content.mergeWithDefault(defaultOptions.content)
        bottomTabs.mergeWithDefault(defaultOptions.bottomTabs)
        topBar.mergeWithDefault(defaultOptions.topBar)
        sharedElements.mergeWithDefault(defaultOptions.sharedElements)
        elementTransitions.mergeWithDefault(defaultOptions.elementTransitions)
        if (!enabled.hasValue()) enabled = defaultOptions.enabled
        if (!waitForRender.hasValue()) waitForRender = defaultOptions.waitForRender
    }

    fun hasValue(): Boolean {
        return topBar.hasValue() || content.hasValue() || bottomTabs.hasValue() || waitForRender.hasValue()
    }

    fun hasElementsTransition(): Boolean {
        return sharedElements.hasValue() || elementTransitions.hasValue()
    }

    companion object {
        @JvmStatic
        fun parse(json: JSONObject?): NestedAnimationsOptions {
            val options = NestedAnimationsOptions()
            if (json == null) return options
            options.content = AnimationOptions(json.optJSONObject("content"))
            options.bottomTabs = AnimationOptions(json.optJSONObject("bottomTabs"))
            options.topBar = AnimationOptions(json.optJSONObject("topBar"))
            options.enabled = BoolParser.parseFirst(json, "enabled", "enable")
            options.waitForRender = BoolParser.parse(json, "waitForRender")
            options.sharedElements = SharedElements.parse(json)
            options.elementTransitions = ElementTransitions.parse(json)
            return options
        }
    }
}
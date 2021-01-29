package com.reactnativenavigation.options

import android.content.Context
import com.reactnativenavigation.options.params.*
import com.reactnativenavigation.options.params.Number
import com.reactnativenavigation.options.parsers.*
import org.json.JSONObject

class BottomTabsOptions {
    var backgroundColor: Colour = NullColor
    var hideOnScroll: Bool = NullBool()
    var visible: Bool = NullBool()
    var drawBehind: Bool = NullBool()
    var animate: Bool = NullBool()
    var animateTabSelection: Bool = NullBool()
    var preferLargeIcons: Bool = NullBool()
    var currentTabIndex: Number = NullNumber
    var elevation: Fraction = NullFraction
    var currentTabId: Text = NullText()
    var testId: Text = NullText()
    var borderColor: Colour = NullColor
    var borderWidth: Number = NullNumber
    var shadowOptions: ShadowOptions = NullShadowOptions
    var titleDisplayMode = TitleDisplayMode.UNDEFINED
    var tabsAttachMode = TabsAttachMode.UNDEFINED
    fun mergeWith(other: BottomTabsOptions) {
        if (other.currentTabId.hasValue()) currentTabId = other.currentTabId
        if (other.currentTabIndex.hasValue()) currentTabIndex = other.currentTabIndex
        if (other.hideOnScroll.hasValue()) hideOnScroll = other.hideOnScroll
        if (other.visible.hasValue()) visible = other.visible
        if (other.drawBehind.hasValue()) drawBehind = other.drawBehind
        if (other.animate.hasValue()) animate = other.animate
        if (other.animateTabSelection.hasValue()) animateTabSelection = other.animateTabSelection
        if (other.preferLargeIcons.hasValue()) preferLargeIcons = other.preferLargeIcons
        if (other.elevation.hasValue()) elevation = other.elevation
        if (other.backgroundColor.hasValue()) backgroundColor = other.backgroundColor
        if (other.testId.hasValue()) testId = other.testId
        if (other.titleDisplayMode.hasValue()) titleDisplayMode = other.titleDisplayMode
        if (other.tabsAttachMode.hasValue()) tabsAttachMode = other.tabsAttachMode
        if (other.borderColor.hasValue()) this.borderColor = other.borderColor
        if (other.borderWidth.hasValue()) this.borderWidth = other.borderWidth
        if (other.shadowOptions.hasValue()) this.shadowOptions.mergeWith(other.shadowOptions)
    }

    fun mergeWithDefault(defaultOptions: BottomTabsOptions) {
        if (!currentTabId.hasValue()) currentTabId = defaultOptions.currentTabId
        if (!currentTabIndex.hasValue()) currentTabIndex = defaultOptions.currentTabIndex
        if (!hideOnScroll.hasValue()) hideOnScroll = defaultOptions.hideOnScroll
        if (!visible.hasValue()) visible = defaultOptions.visible
        if (!drawBehind.hasValue()) drawBehind = defaultOptions.drawBehind
        if (!animate.hasValue()) animate = defaultOptions.animate
        if (!animateTabSelection.hasValue()) animateTabSelection = defaultOptions.animateTabSelection
        if (!preferLargeIcons.hasValue()) preferLargeIcons = defaultOptions.preferLargeIcons
        if (!elevation.hasValue()) elevation = defaultOptions.elevation
        if (!backgroundColor.hasValue()) backgroundColor = defaultOptions.backgroundColor
        if (!titleDisplayMode.hasValue()) titleDisplayMode = defaultOptions.titleDisplayMode
        if (!tabsAttachMode.hasValue()) tabsAttachMode = defaultOptions.tabsAttachMode
        if (!borderColor.hasValue()) this.borderColor = defaultOptions.borderColor
        if (!borderWidth.hasValue()) borderWidth = defaultOptions.borderWidth
        if (!shadowOptions.hasValue()) this.shadowOptions.mergeWithDefaults(defaultOptions.shadowOptions)

    }

    val isHiddenOrDrawBehind: Boolean
        get() = visible.isFalse || drawBehind.isTrue

    fun clearOneTimeOptions() {
        currentTabId = NullText()
        currentTabIndex = NullNumber
    }

    companion object {
        fun parse(context: Context?, json: JSONObject?): BottomTabsOptions {
            val options = BottomTabsOptions()
            if (json == null || context == null) return options
            options.backgroundColor = ColorParser.parse(context, json, "backgroundColor")
            options.currentTabId = TextParser.parse(json, "currentTabId")
            options.currentTabIndex = NumberParser.parse(json, "currentTabIndex")
            options.hideOnScroll = BoolParser.parse(json, "hideOnScroll")
            options.visible = BoolParser.parse(json, "visible")
            options.drawBehind = BoolParser.parse(json, "drawBehind")
            options.preferLargeIcons = BoolParser.parse(json, "preferLargeIcons")
            options.animate = BoolParser.parse(json, "animate")
            options.animateTabSelection = BoolParser.parse(json, "animateTabSelection")
            options.elevation = FractionParser.parse(json, "elevation")
            options.borderColor = ColorParser.parse(context, json, "borderColor")
            options.borderWidth = NumberParser.parse(json, "borderWidth")
            options.shadowOptions = parseShadowOptions(context, json.optJSONObject("shadow"))
            options.testId = TextParser.parse(json, "testID")
            options.titleDisplayMode = TitleDisplayMode.fromString(json.optString("titleDisplayMode"))
            options.tabsAttachMode = TabsAttachMode.fromString(json.optString("tabsAttachMode"))
            return options
        }
    }
}
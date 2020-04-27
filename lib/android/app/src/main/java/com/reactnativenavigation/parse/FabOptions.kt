package com.reactnativenavigation.parse

import com.reactnativenavigation.parse.params.*
import com.reactnativenavigation.parse.params.Number
import com.reactnativenavigation.parse.parsers.BoolParser
import com.reactnativenavigation.parse.parsers.ColorParser
import com.reactnativenavigation.parse.parsers.NumberParser
import com.reactnativenavigation.parse.parsers.TextParser
import org.json.JSONObject

class FabOptions {
    @JvmField
    var id: Text = NullText()
    @JvmField
    var backgroundColor: Colour = NullColor()
    @JvmField
    var clickColor: Colour = NullColor()
    @JvmField
    var icon: Text = NullText()
    @JvmField
    var iconColor: Colour = NullColor()
    @JvmField
    var visible: Bool = NullBool()
    @JvmField
    var alignHorizontally: Text = NullText()
    @JvmField
    var hideOnScroll: Bool = NullBool()
    @JvmField
    var size: Text = NullText()
    @JvmField
    var customSize: Number = NullNumber()
    @JvmField
    var text: Text = NullText()
    @JvmField
    var setExtended: Bool = NullBool()

    fun mergeWith(other: FabOptions) {
        if (other.id.hasValue()) {
            id = other.id
        }
        if (other.backgroundColor.hasValue()) {
            backgroundColor = other.backgroundColor
        }
        if (other.clickColor.hasValue()) {
            clickColor = other.clickColor
        }
        if (other.visible.hasValue()) {
            visible = other.visible
        }
        if (other.icon.hasValue()) {
            icon = other.icon
        }
        if (other.iconColor.hasValue()) {
            iconColor = other.iconColor
        }
        if (other.alignHorizontally.hasValue()) {
            alignHorizontally = other.alignHorizontally
        }
        if (other.hideOnScroll.hasValue()) {
            hideOnScroll = other.hideOnScroll
        }
        if (other.size.hasValue()) {
            size = other.size
        }
        if (other.customSize.hasValue()) {
            customSize = other.customSize
        }
        if (other.text.hasValue()) {
            text = other.text
        }
        if (other.setExtended.hasValue()) {
            setExtended = other.setExtended
        }
    }

    fun mergeWithDefault(defaultOptions: FabOptions) {
        if (!id.hasValue()) {
            id = defaultOptions.id
        }
        if (!backgroundColor.hasValue()) {
            backgroundColor = defaultOptions.backgroundColor
        }
        if (!clickColor.hasValue()) {
            clickColor = defaultOptions.clickColor
        }
        if (!visible.hasValue()) {
            visible = defaultOptions.visible
        }
        if (!icon.hasValue()) {
            icon = defaultOptions.icon
        }
        if (!iconColor.hasValue()) {
            iconColor = defaultOptions.iconColor
        }
        if (!alignHorizontally.hasValue()) {
            alignHorizontally = defaultOptions.alignHorizontally
        }
        if (!hideOnScroll.hasValue()) {
            hideOnScroll = defaultOptions.hideOnScroll
        }
        if (!size.hasValue()) {
            size = defaultOptions.size
        }
        if (!customSize.hasValue()) {
            customSize = defaultOptions.customSize
        }
        if (!text.hasValue()) {
            text = defaultOptions.text
        }
        if (!setExtended.hasValue()) {
            setExtended = defaultOptions.setExtended
        }
    }

    fun hasValue(): Boolean {
        return id.hasValue() || icon.hasValue()
    }

    companion object {
        @JvmStatic
        fun parse(json: JSONObject?): FabOptions {
            val options = FabOptions()
            if (json == null) return options
            options.id = TextParser.parse(json, "id")
            options.backgroundColor = ColorParser.parse(json, "backgroundColor")
            options.clickColor = ColorParser.parse(json, "clickColor")
            options.visible = BoolParser.parse(json, "visible")
            if (json.has("icon")) {
                options.icon = TextParser.parse(json.optJSONObject("icon"), "uri")
            }
            options.iconColor = ColorParser.parse(json, "iconColor")
            options.alignHorizontally = TextParser.parse(json, "alignHorizontally")
            options.hideOnScroll = BoolParser.parse(json, "hideOnScroll")
            options.size = TextParser.parse(json, "size")
            options.customSize = NumberParser.parse(json, "customSize")
            options.text = TextParser.parse(json, "text")
            options.setExtended = BoolParser.parse(json, "setExtended")
            return options
        }
    }
}
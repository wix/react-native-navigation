package com.reactnativenavigation.options

import com.reactnativenavigation.options.params.NullText
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.options.parsers.TextParser
import org.json.JSONObject
class OverlayAttachOptions{

    @JvmField
    var layoutId :Text = NullText()
    @JvmField
    var anchorId :Text = NullText()
    @JvmField
    var gravity:Text = NullText()

    fun hasValue() = layoutId.hasValue()
    override fun toString(): String {
        return "OverlayAttachOptions(layoutId=$layoutId, anchorId=$anchorId, gravity=$gravity)"
    }

    companion object{
        @JvmStatic
        fun parse(json: JSONObject?): OverlayAttachOptions {
            val overlayAttachOptions = OverlayAttachOptions()
            overlayAttachOptions.layoutId = TextParser.parse(json,"layoutId")
            overlayAttachOptions.anchorId = TextParser.parse(json?.optJSONObject("anchor"),"id")
            overlayAttachOptions.gravity = TextParser.parse(json?.optJSONObject("anchor"),"gravity")
            return overlayAttachOptions
        }
    }
}
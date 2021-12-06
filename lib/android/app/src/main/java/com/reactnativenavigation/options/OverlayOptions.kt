package com.reactnativenavigation.options

import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.NullBool
import com.reactnativenavigation.options.params.NullText
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.options.parsers.BoolParser
import com.reactnativenavigation.options.parsers.TextParser
import org.json.JSONObject
class OverlayAttachOptions{

    var layoutId :Text = NullText()
    var anchorId :Text = NullText()
    var gravity:Text = NullText()

    fun hasValue() = layoutId.hasValue()&&anchorId.hasValue()&&gravity.hasValue()
    override fun toString(): String {
        return "OverlayAttachOptions(layoutId=$layoutId, anchorId=$anchorId, gravity=$gravity)"
    }

    companion object{
        fun parse(json: JSONObject?): OverlayAttachOptions {
            val overlayAttachOptions = OverlayAttachOptions()
            overlayAttachOptions.layoutId = TextParser.parse(json,"layoutId")
            overlayAttachOptions.anchorId = TextParser.parse(json?.optJSONObject("anchor"),"id")
            overlayAttachOptions.gravity = TextParser.parse(json?.optJSONObject("anchor"),"gravity")
            return overlayAttachOptions
        }
    }


}
class OverlayOptions {
    @JvmField
    var interceptTouchOutside: Bool = NullBool()

    var overlayAttachOptions: OverlayAttachOptions = OverlayAttachOptions()
    companion object {
        @JvmStatic
        fun parse(json: JSONObject?): OverlayOptions {
            val options = OverlayOptions()
            if (json == null) return options
            options.overlayAttachOptions = OverlayAttachOptions.parse(json.optJSONObject("attach"))
            options.interceptTouchOutside = BoolParser.parse(json, "interceptTouchOutside")
            return options
        }
    }
}
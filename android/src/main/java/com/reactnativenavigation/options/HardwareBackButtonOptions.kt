package com.reactnativenavigation.options

import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.NullBool
import com.reactnativenavigation.options.parsers.BoolParser
import org.json.JSONObject


sealed class HwBackBottomTabsBehaviour {
    object Undefined : HwBackBottomTabsBehaviour() {
        override fun hasValue(): Boolean = false
    }

    object Exit : HwBackBottomTabsBehaviour()
    object PrevSelection : HwBackBottomTabsBehaviour()
    object JumpToFirst : HwBackBottomTabsBehaviour()

    open fun hasValue(): Boolean = true

    companion object {
        private const val BEHAVIOUR_EXIT = "exit"
        private const val BEHAVIOUR_PREV = "previous"
        private const val BEHAVIOUR_FIRST = "first"
        fun fromString(behaviour: String?): HwBackBottomTabsBehaviour {
            return when (behaviour) {
                BEHAVIOUR_PREV -> PrevSelection
                BEHAVIOUR_FIRST -> JumpToFirst
                BEHAVIOUR_EXIT -> Exit
                else -> Undefined
            }
        }
    }
}

open class HardwareBackButtonOptions(json: JSONObject? = null) {

    @JvmField
    var dismissModalOnPress: Bool = NullBool()

    @JvmField
    var popStackOnPress: Bool = NullBool()
    var bottomTabOnPress: HwBackBottomTabsBehaviour = HwBackBottomTabsBehaviour.Undefined

    init {
        parse(json)
    }

    fun mergeWith(other: HardwareBackButtonOptions) {
        if (other.dismissModalOnPress.hasValue()) dismissModalOnPress = other.dismissModalOnPress
        if (other.popStackOnPress.hasValue()) popStackOnPress = other.popStackOnPress
        if (other.bottomTabOnPress.hasValue()) bottomTabOnPress = other.bottomTabOnPress
    }

    fun mergeWithDefault(defaultOptions: HardwareBackButtonOptions) {
        if (!dismissModalOnPress.hasValue()) dismissModalOnPress = defaultOptions.dismissModalOnPress
        if (!popStackOnPress.hasValue()) popStackOnPress = defaultOptions.popStackOnPress
        if (!bottomTabOnPress.hasValue()) bottomTabOnPress = defaultOptions.bottomTabOnPress
    }

    private fun parse(json: JSONObject?) {
        json ?: return
        dismissModalOnPress = BoolParser.parse(json, "dismissModalOnPress")
        popStackOnPress = BoolParser.parse(json, "popStackOnPress")
        bottomTabOnPress = HwBackBottomTabsBehaviour.fromString(json.optString("bottomTabsOnPress"))
    }
}
package com.reactnativenavigation.options.params

enum class BottomTabsLayoutStyle {
    STRETCH,
    COMPACT,
    LAYOUT_STYLE_UNDEFINED;

    fun hasValue(): Boolean = (this != LAYOUT_STYLE_UNDEFINED)

    companion object {
        @JvmStatic
        fun fromString(mode: String?): BottomTabsLayoutStyle {
            return when (mode?.lowercase()) {
                "stretch" -> STRETCH
                "compact" -> COMPACT
                else -> LAYOUT_STYLE_UNDEFINED
            }
        }
    }
}

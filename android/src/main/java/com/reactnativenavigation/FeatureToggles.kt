package com.reactnativenavigation

import androidx.annotation.VisibleForTesting

enum class RNNToggles {
    TOP_BAR_COLOR_ANIMATION__PUSH,
    TOP_BAR_COLOR_ANIMATION__TABS,
    TAB_BAR_TRANSLUCENCE,
}

private val ToggleDefaults = mapOf(
    RNNToggles.TOP_BAR_COLOR_ANIMATION__PUSH to false,
    RNNToggles.TOP_BAR_COLOR_ANIMATION__TABS to false,
    RNNToggles.TAB_BAR_TRANSLUCENCE to false,
)

object RNNFeatureToggles {
    private var init = false
    private var toggles = mutableMapOf<RNNToggles, Boolean>()

    @JvmStatic
    fun init() {
        assertNotInitialized()

        init = true
        toggles = ToggleDefaults.toMutableMap()
    }

    @JvmStatic
    fun init(overrides: Map<RNNToggles, Boolean>) {
        init()
        this.toggles.putAll(overrides)
    }

    @JvmStatic
    fun init(vararg overrides: Pair<RNNToggles, Boolean>) {
        init(mapOf(*overrides))
    }

    @JvmStatic
    fun isEnabled(toggleName: RNNToggles): Boolean {
        assertInitialized()
        return toggles.getOrElse(toggleName) { false }
    }

    @VisibleForTesting
    @JvmStatic
    fun clear() {
        init = false
        toggles.clear()
    }

    private fun assertNotInitialized() {
        if (init) {
            throw IllegalStateException("FeatureToggles already initialized")
        }
    }

    private fun assertInitialized() {
        if (!init) {
            throw IllegalStateException("FeatureToggles not initialized")
        }
    }
}

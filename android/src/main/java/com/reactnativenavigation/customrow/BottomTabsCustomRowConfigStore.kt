package com.reactnativenavigation.customrow

/**
 * Process-wide singleton holding the latest custom-row configuration
 * pushed from JS via the `RNNBottomTabsCustomRowModule` native module.
 *
 * The attacher reads from here when it needs to apply chrome to a freshly
 * detected `BottomTabs` instance.
 */
object BottomTabsCustomRowConfigStore {
    @Volatile
    private var current: BottomTabsCustomRowOptions = BottomTabsCustomRowOptions()

    private val listeners = mutableSetOf<(BottomTabsCustomRowOptions) -> Unit>()

    fun update(options: BottomTabsCustomRowOptions) {
        current = options
        synchronized(listeners) {
            listeners.toList().forEach { it.invoke(options) }
        }
    }

    fun get(): BottomTabsCustomRowOptions = current

    fun addListener(listener: (BottomTabsCustomRowOptions) -> Unit) {
        synchronized(listeners) { listeners.add(listener) }
    }

    fun removeListener(listener: (BottomTabsCustomRowOptions) -> Unit) {
        synchronized(listeners) { listeners.remove(listener) }
    }
}

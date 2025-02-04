package com.reactnativenavigation.utils

import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.options.params.ThemeColour
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

object OptionHelper {
    @JvmStatic
    fun createBottomTabOptions(): Options {
        val options = Options()
        options.topBar.buttons.left = ArrayList()
        options.bottomTabOptions.text = Text("Tab")
        options.bottomTabOptions.icon = Text("http://127.0.0.1/icon.png")
        return options
    }

    fun builder() = OptionsBuilder()
    fun emptyOptions(): Options = builder().build()
}

class OptionsBuilder internal constructor() {
    private val options = Options.EMPTY.copy()

    fun topBar() = TopBarBuilder()
    fun build(): Options = options

    inner class TopBarBuilder {
        fun withColor(color: Int): OptionsBuilder {
            options.topBar.background.color = aThemeColor(color)
            return this@OptionsBuilder
        }
        fun withDisabledAnim(): OptionsBuilder {
            options.topBar.animate = Bool(false)
            return this@OptionsBuilder
        }
    }
}

private fun aThemeColor(color: Int) = mock<ThemeColour> {
    on { hasValue() } doReturn true
    on { get() } doReturn color
}

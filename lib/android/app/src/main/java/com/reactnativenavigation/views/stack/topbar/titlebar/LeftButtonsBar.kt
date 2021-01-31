package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController


class LeftButtonsBar(context: Context) : TitleBar(context) {
    init {
        setContentInsetsAbsolute(0, 0)
        contentInsetStartWithNavigation = 0
    }

    fun setBackButton(button: ButtonController) {
        button.applyNavigationIcon(this)
    }

    fun clearBackButton() {
        navigationIcon = null
    }

    override fun clearButtons() {
        super.clearButtons()
        clearBackButton()
    }

    override fun setTitle(title: CharSequence?) {

    }

    override fun setSubtitle(title: CharSequence?) {

    }
}
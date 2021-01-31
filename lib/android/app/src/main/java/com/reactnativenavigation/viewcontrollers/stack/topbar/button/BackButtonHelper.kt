package com.reactnativenavigation.viewcontrollers.stack.topbar.button

import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController

class BackButtonHelper {
    fun clear(child: ViewController<*>) {
        if (!child.options.topBar.buttons.back.hasValue()) {
            child.options.topBar.buttons.back.visible = Bool(false)
        }
    }

    fun addToPushedChild(child: ViewController<*>) {
        if (child.options.topBar.buttons.left != null || child.options.topBar.buttons.back.visible.isFalse) return
        val options = Options()
        options.topBar.buttons.back.setVisible()
        child.mergeOptions(options)
    }
}
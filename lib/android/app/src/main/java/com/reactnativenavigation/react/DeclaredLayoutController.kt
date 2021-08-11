package com.reactnativenavigation.react

import android.app.Activity
import com.facebook.react.bridge.ReactContext
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.YellowBoxDelegate
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.ViewControllerOverlay

class DeclaredLayoutController(
    val reactContext: ReactContext,
    activity: Activity?,
    id: String?,
    yellowBoxDelegate: YellowBoxDelegate?,
    initialOptions: Options?,
    overlay: ViewControllerOverlay?
) : ViewController<DeclaredLayout>(activity, id, yellowBoxDelegate, initialOptions, overlay) {
    override fun isViewShown(): Boolean {
        return !isDestroyed && view != null && view!!.isShown
    }

    override fun isRendered(): Boolean {
        return isViewCreated
    }

    override fun getCurrentComponentName(): String = "GenericDeclaredLayoutController"


    override fun createView(): DeclaredLayout {
        return DeclaredLayout(reactContext)
    }

    override fun sendOnNavigationButtonPressed(buttonId: String?) {
        TODO("Not yet implemented")
    }

}
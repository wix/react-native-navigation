package com.reactnativenavigation.react

import android.app.Activity
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.YellowBoxDelegate
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.ViewControllerOverlay

class DeclaredLayoutController(
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

    override fun getView(): DeclaredLayout {
        return createView()
    }
    override fun createView(): DeclaredLayout {
        return DeclaredLayout(activity)
    }

    override fun sendOnNavigationButtonPressed(buttonId: String?) {
        TODO("Not yet implemented")
    }

}
package com.reactnativenavigation.react.modal

import android.app.Activity
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.UIManagerModule
import com.facebook.react.uimanager.common.UIManagerType
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.react.Constants
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.YellowBoxDelegate
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.ViewControllerOverlay

class ModalLayoutController(
    val reactContext: ReactContext,
    activity: Activity?,
    id: String?,
    yellowBoxDelegate: YellowBoxDelegate?,
    initialOptions: Options?,
    overlay: ViewControllerOverlay?,
    val getHostId: () -> Int
) : ViewController<ModalFrameLayout>(activity, id, yellowBoxDelegate, initialOptions, overlay) {
    override fun isViewShown(): Boolean {
        return !isDestroyed && view != null && view!!.isShown
    }

    override fun isRendered(): Boolean {
        return isViewCreated
    }

    override fun getCurrentComponentName(): String = "ModalLayoutController"


    override fun createView(): ModalFrameLayout {
        return ModalFrameLayout(reactContext)
    }

    override fun sendOnNavigationButtonPressed(buttonId: String?) {
        if (buttonId == Constants.HARDWARE_BACK_BUTTON_ID) {
            val dispatcher = UIManagerHelper.getEventDispatcher(reactContext, UIManagerType.FABRIC)
            dispatcher?.dispatchEvent(RequestCloseModalEvent(getHostId()))
        }
    }

    fun sendShowEvent() {
        val dispatcher = UIManagerHelper.getEventDispatcher(reactContext, UIManagerType.FABRIC)
        dispatcher?.dispatchEvent(ShowModalEvent(getHostId()))
    }
}

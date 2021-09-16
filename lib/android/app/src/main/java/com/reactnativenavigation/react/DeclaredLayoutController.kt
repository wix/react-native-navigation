package com.reactnativenavigation.react

import android.app.Activity
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.UIManagerModule
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.react.modal.ModalDismissEvent
import com.reactnativenavigation.react.modal.RequestDismissModalEvent
import com.reactnativenavigation.react.modal.ShowModalEvent
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.YellowBoxDelegate
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.ViewControllerOverlay

class DeclaredLayoutController(
    val reactContext: ReactContext,
    activity: Activity?,
    id: String?,
    yellowBoxDelegate: YellowBoxDelegate?,
    initialOptions: Options?,
    overlay: ViewControllerOverlay?,
    val getHostId:()->Int
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
        if(buttonId == Constants.HARDWARE_BACK_BUTTON_ID ){
                val dispatcher = reactContext.getNativeModule(
                    UIManagerModule::class.java
                ).eventDispatcher
                dispatcher.dispatchEvent(RequestDismissModalEvent(getHostId()))
        }
    }

    fun sendDismissEvent(){
        val dispatcher = reactContext.getNativeModule(
            UIManagerModule::class.java
        ).eventDispatcher
        dispatcher.dispatchEvent(ModalDismissEvent(getHostId()))
    }

    fun sendShowEvent(){
        val dispatcher = reactContext.getNativeModule(
            UIManagerModule::class.java
        ).eventDispatcher
        dispatcher.dispatchEvent(ShowModalEvent(getHostId()))
    }

}
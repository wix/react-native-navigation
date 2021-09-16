package com.reactnativenavigation.react

import android.view.View
import com.facebook.react.common.MapBuilder
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.reactnativenavigation.react.modal.ModalDismissEvent
import com.reactnativenavigation.react.modal.RequestDismissModalEvent
import com.reactnativenavigation.react.modal.ShowModalEvent
import com.reactnativenavigation.viewcontrollers.navigator.Navigator

private const val MODAL_MANAGER_NAME = "RNNModalViewManager"

@ReactModule(name = MODAL_MANAGER_NAME)
    class RNNModalViewManager(private val navigator: Navigator) : ViewGroupManager<DeclaredLayoutHost>() {

        override fun getName(): String = MODAL_MANAGER_NAME

        override fun createViewInstance(reactContext: ThemedReactContext): DeclaredLayoutHost {
            return DeclaredLayoutHost(reactContext)
        }

        override fun onDropViewInstance(modal: DeclaredLayoutHost) {
            super.onDropViewInstance(modal)
            navigator.dismissModal(modal.viewController.id, CommandListenerAdapter(object: CommandListener {
                override fun onSuccess(childId: String?) {
                    modal.viewController.sendDismissEvent()
                }

                override fun onError(message: String?) {
                }

            }))
            modal.onDropInstance()
        }

        override fun onAfterUpdateTransaction(modal: DeclaredLayoutHost) {
            super.onAfterUpdateTransaction(modal)
            navigator.showModal(modal.viewController, CommandListenerAdapter(object : CommandListener {
                override fun onSuccess(childId: String?) {
                    modal.viewController.sendShowEvent()
                }

                override fun onError(message: String?) {
                }

            }))
        }

        override fun addView(parent: DeclaredLayoutHost?, child: View?, index: Int) {
            super.addView(parent, child, index)
        }

        override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any>? {
            return MapBuilder.builder<String, Any>()
                .put(RequestDismissModalEvent.EVENT_NAME, MapBuilder.of("registrationName", "onRequestDismiss"))
                .put(ModalDismissEvent.EVENT_NAME, MapBuilder.of("registrationName", "onDismiss"))
                .put(ShowModalEvent.EVENT_NAME, MapBuilder.of("registrationName", "onShow"))
                .build()
        }

        @ReactProp(name = "visible")
        fun setVisible(modal: DeclaredLayoutHost, visible: Boolean) {
        }

    }
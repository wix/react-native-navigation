package com.reactnativenavigation.viewcontrollers.overlay

import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import com.reactnativenavigation.options.OverlayAttachOptions
import com.reactnativenavigation.options.OverlayOptions
import com.reactnativenavigation.react.CommandListener
import com.reactnativenavigation.utils.CoordinatorLayoutUtils
import com.reactnativenavigation.utils.removeFromParent
import com.reactnativenavigation.viewcontrollers.component.ComponentViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.BehaviourDelegate
import com.reactnativenavigation.views.overlay.ViewTooltip

private fun View.closeNow() {
    if (this is ViewTooltip.TooltipView) {
        this.closeNow()
    } else {
        removeFromParent()
    }
}

private class OverlayEntry(
    var overlayView: View,
    val viewController: ViewController<*>
)

class OverlayManager {
    private val overlayRegistry = mutableMapOf<String, OverlayEntry>()
    var mainOverlayContainer: ViewGroup? = null
    var findController: (String) -> ViewController<*>? = { null }
    var findAnchorView: (OverlayAttachOptions) -> View? = { null }

    fun show(overlayController: ViewController<*>, overlayOptions: OverlayOptions, listener: CommandListener) {
        val overlayAttachOptions = overlayOptions.overlayAttachOptions
        if (overlayAttachOptions.anchorId.hasValue()) {
            anchorOverlayInParent(overlayAttachOptions, overlayController, listener)
        } else {
            attachOverlayToParent(overlayAttachOptions, overlayController, listener)
        }
    }

    private fun attachOverlayToParent(
        overlayAttachOptions: OverlayAttachOptions,
        overlayController: ViewController<*>,
        listener: CommandListener
    ) {
        val parent = if (overlayAttachOptions.layoutId.hasValue()) {
            findController(overlayAttachOptions.layoutId.get())?.view
        } else mainOverlayContainer

        parent?.let {
            it.visibility = View.VISIBLE
            registerOverlay(overlayController.view,overlayController, listener)
            it.addView(
                overlayController.view,
                CoordinatorLayoutUtils.matchParentWithBehaviour(BehaviourDelegate(overlayController))
            )
        } ?: listener.onError("Cannot find layout with id " + overlayAttachOptions.layoutId)
    }

    private fun anchorOverlayInParent(
        overlayAttachOptions: OverlayAttachOptions,
        overlayController: ViewController<*>,
        listener: CommandListener
    ) {
        val hostController = findController(overlayAttachOptions.layoutId.get())
        if (hostController != null) {
            val anchorView: View? = findAnchorView(overlayAttachOptions)
            if (anchorView != null) {
                if(overlayController is ComponentViewController){
                    overlayController.ignoreInsets(true)
                }
                val anchoredView =
                    hostController.showAnchoredOverlay(anchorView, overlayAttachOptions, overlayController)
                anchoredView?.let {
                    registerOverlay(it,overlayController, listener)
                } ?: listener.onError("Parent could not create anchored view, it could be null parent")
            } else {
                listener.onError("Cannot find anchor view with id " + overlayAttachOptions.anchorId)
            }
        } else {
            listener.onError("Cannot find layout with id " + overlayAttachOptions.layoutId)
        }
    }

    private fun registerOverlay(
        view:View,
        viewController: ViewController<*>,
        listener: CommandListener
    ) {
        overlayRegistry[viewController.id] = OverlayEntry(view, viewController)
        viewController.onViewDidAppear()
        listener.onSuccess(viewController.id)
    }

    fun onConfigurationChanged(configuration: Configuration?) {
        overlayRegistry.values.forEach { entry -> entry.viewController.onConfigurationChanged(configuration) }
    }

    fun dismiss(componentId: String, listener: CommandListener) {
        val overlay = overlayRegistry.remove(componentId)
        if (overlay == null) {
            listener.onError("Could not dismiss Overlay. Overlay with id $componentId was not found.")
        } else {
            destroyOverlay(overlay)
            listener.onSuccess(componentId)
        }
    }

    fun dismissAll() {
        destroy()
    }

    fun destroy() {
        val entries = overlayRegistry.entries
        while (entries.isNotEmpty()) {
            val first = entries.first()
            val attachOverlayEntry = first.value
            attachOverlayEntry.overlayView.closeNow()
            attachOverlayEntry.viewController.destroy()
            overlayRegistry.remove(first.key)
        }
        mainOverlayContainer?.visibility = View.GONE
    }

    fun size() = overlayRegistry.size

    fun findControllerById(id: String?): ViewController<*>? {
        return overlayRegistry[id]?.viewController
    }

    private fun destroyOverlay(overlay: OverlayEntry?) {
        overlay?.overlayView?.closeNow()
        overlay?.viewController?.destroy()
        if (overlayRegistry.isEmpty()) mainOverlayContainer?.visibility = View.GONE
    }

    fun onHostPause() {
        overlayRegistry.values.forEach { it.viewController.onViewDisappear() }
    }

    fun onHostResume() {
        overlayRegistry.values.forEach { it.viewController.onViewDidAppear() }
    }
}
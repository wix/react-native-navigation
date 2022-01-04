package com.reactnativenavigation.viewcontrollers

import android.content.res.Configuration
import android.view.View
import com.reactnativenavigation.options.OverlayAttachOptions
import com.reactnativenavigation.react.CommandListener
import com.reactnativenavigation.utils.removeFromParent
import com.reactnativenavigation.viewcontrollers.component.ComponentViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.overlay.ViewTooltip

private fun View.closeNow() {
    if (this is ViewTooltip.TooltipView) {
        this.closeNow()
    } else {
        removeFromParent()
    }
}

private class AttachOverlayEntry(
    var overlayView: View,
    val viewController: ViewController<*>
)

class AttachedOverlayManager(
    private val findController: (String) -> ViewController<*>?,
    private val findAnchorView: (OverlayAttachOptions) -> View?
) {
    private val registry = mutableMapOf<String, AttachOverlayEntry>()


    fun show(
        overlayController: ViewController<*>, overlayAttachOptions: OverlayAttachOptions,
        listener: CommandListener
    ) {
        if (overlayController !is ComponentViewController) {
            listener.onError("Cannot show anchored view with non component layout")
            return
        }
        overlayController.ignoreInsets(true)
        val hostController: ViewController<*>? = findController(overlayAttachOptions.layoutId.get())
        if (hostController != null) {
            if (overlayAttachOptions.anchorId.hasValue())
                showAnchoredOverlay(overlayAttachOptions, hostController, overlayController, listener)
            else
                showAttachedOverlay(hostController, overlayController, listener)
        } else {
            listener.onError("Cannot find layout with id " + overlayAttachOptions.layoutId)
        }
    }

    private fun showAttachedOverlay(
        hostController: ViewController<*>,
        overlayController: ComponentViewController,
        listener: CommandListener
    ) {
        val view = hostController.showOverlay(overlayController)
        view?.let {
            registerOverlay(overlayController, view)
            listener.onSuccess(overlayController.id)
        } ?: listener.onError("Provided overlay has null view")
    }

    private fun showAnchoredOverlay(
        overlayAttachOptions: OverlayAttachOptions,
        hostController: ViewController<*>,
        overlayController: ViewController<*>,
        listener: CommandListener
    ) {
        val anchorView: View? = findAnchorView(overlayAttachOptions)
        if (anchorView != null) {
            val anchoredView = hostController.showAnchoredOverlay(anchorView, overlayAttachOptions, overlayController)
            anchoredView?.let {
                registerOverlay(overlayController, anchoredView)
                listener.onSuccess(overlayController.id)
            } ?: listener.onError("Parent could not create anchored view, it could be null parent")
        } else {
            listener.onError("Cannot find anchor view with id " + overlayAttachOptions.anchorId)
        }
    }

    private fun registerOverlay(
        overlayController: ViewController<*>,
        anchoredView: View
    ) {
        registry[overlayController.id] = AttachOverlayEntry(anchoredView, overlayController)
        overlayController.onViewDidAppear()
    }

    operator fun contains(id: String): Boolean {
        return registry.containsKey(id)
    }

    fun dismissAll(id: String, listener: CommandListener) {
        registry.remove(id)?.let {
            it.overlayView.closeNow()
            it.viewController.destroy()
            listener.onSuccess(id)
        } ?: listener.onError("Can't dismiss non-shown anchored view of id: $id")
    }

    fun dismissAll() {
        val entries = registry.entries
        while (entries.isNotEmpty()) {
            val first = entries.first()
            val attachOverlayEntry = first.value
            attachOverlayEntry.overlayView.closeNow()
            attachOverlayEntry.viewController.destroy()
            registry.remove(first.key)
        }
    }

    fun onConfigurationChanged(newConfig: Configuration) {

    }

}
package com.reactnativenavigation.viewcontrollers.overlay

import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import com.reactnativenavigation.react.CommandListener
import com.reactnativenavigation.utils.CoordinatorLayoutUtils
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.BehaviourDelegate

class OverlayManager {
    private val overlayRegistry = mutableMapOf<String, ViewController<*>>()
    fun show(overlaysContainer: ViewGroup, overlay: ViewController<*>, listener: CommandListener) {
        overlaysContainer.visibility = View.VISIBLE
        overlayRegistry[overlay.id] = overlay
        overlay.addOnAppearedListener {
            overlay.onViewDidAppear()
            listener.onSuccess(overlay.id)
        }
        overlaysContainer.addView(
            overlay.view,
            CoordinatorLayoutUtils.matchParentWithBehaviour(BehaviourDelegate(overlay))
        )
    }

    fun onConfigurationChanged(configuration: Configuration?) {
        overlayRegistry.values.forEach { controller -> controller.onConfigurationChanged(configuration) }
    }

    fun dismiss(overlaysContainer: ViewGroup, componentId: String, listener: CommandListener) {
        val overlay = overlayRegistry.remove(componentId)
        if (overlay == null) {
            listener.onError("Could not dismiss Overlay. Overlay with id $componentId was not found.")
        } else {
            destroyOverlay(overlaysContainer, overlay)
            listener.onSuccess(componentId)
        }
    }

    fun dismissAll(overlaysContainer: ViewGroup, listener: CommandListener) {
        destroy(overlaysContainer)
        listener.onSuccess("")
    }

    fun destroy(overlaysContainer: ViewGroup) {
        val removedOverlays = overlayRegistry.values.map { overlay ->
            destroyOverlay(overlaysContainer, overlay)
            overlay.id
        }.toList()
        removedOverlays.forEach {
            overlayRegistry.remove(it)
        }
    }

    fun size() = overlayRegistry.size

    fun findControllerById(id: String?): ViewController<*>? {
        return overlayRegistry[id]
    }

    private fun destroyOverlay(overlaysContainer: ViewGroup, overlay: ViewController<*>) {
        overlay.destroy()
        if (isEmpty) overlaysContainer.visibility = View.GONE
    }

    private val isEmpty: Boolean
        get() = size() == 0

    fun onHostPause() {
        overlayRegistry.values.forEach(ViewController<*>::onViewDisappear)
    }

    fun onHostResume() {
        overlayRegistry.values.forEach(ViewController<*>::onViewDidAppear)
    }
}
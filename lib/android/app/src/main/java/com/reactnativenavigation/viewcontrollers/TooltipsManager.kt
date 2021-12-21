package com.reactnativenavigation.viewcontrollers

import android.view.View
import com.reactnativenavigation.options.OverlayAttachOptions
import com.reactnativenavigation.react.CommandListener
import com.reactnativenavigation.viewcontrollers.component.ComponentViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.ViewTooltip

class TooltipsManager(
    private val findController: (String) -> ViewController<*>?,
    private val findTooltipAnchorView: (OverlayAttachOptions) -> View?
) : View.OnAttachStateChangeListener {
    private val registry = mutableMapOf<String, Pair<ViewTooltip.TooltipView, ViewController<*>>>()
    private val anchorsTooltipsRegistry = mutableMapOf<View, String>()

    fun showTooltip(
        tooltipController: ViewController<*>, overlayAttachOptions: OverlayAttachOptions,
        listener: CommandListener
    ) {
        if (tooltipController !is ComponentViewController) {
            listener.onError("Cannot show Tooltip with non component layout")
            return
        }
        tooltipController.ignoreInsets(true)
        val hostController: ViewController<*>? = findController(overlayAttachOptions.layoutId.get())
        if (hostController != null) {
            val tooltipAnchorView: View? = findTooltipAnchorView(overlayAttachOptions)
            if (tooltipAnchorView != null) {
                val tooltipView = hostController.showTooltip(tooltipAnchorView, overlayAttachOptions, tooltipController)
                tooltipView?.let {
                    tooltipAnchorView.addOnAttachStateChangeListener(this)
                    anchorsTooltipsRegistry[tooltipAnchorView] = tooltipController.id
                    registry[tooltipController.id] = tooltipView to tooltipController
                    tooltipController.onViewDidAppear()
                    listener.onSuccess(tooltipController.id)
                } ?: listener.onError("Parent could not create tooltip, it could be null parent")
            } else {
                listener.onError("Cannot find anchor view with id " + overlayAttachOptions.anchorId)
            }
        } else {
            listener.onError("Cannot find layout with id " + overlayAttachOptions.layoutId)
        }
    }

    operator fun contains(id: String): Boolean {
        return registry.containsKey(id)
    }

    fun dismissTooltip(id: String, listener: CommandListener) {
        registry.remove(id)?.let {
            val (tpView, controller) = it
            tpView.closeNow()
            controller.destroy()
            listener.onSuccess(id)
        } ?: listener.onError("Can't dismiss non-shown tooltip of id: $id")
    }

    fun dismissAllTooltip(listener: CommandListener) {
        while (registry.isNotEmpty()) {
            val first = registry.entries.first()
            val (tpView, controller) = first.value
            tpView.close()
            tpView.post {
                controller.destroy()
            }
            registry.remove(first.key)
        }
        listener.onSuccess("")

    }

    override fun onViewAttachedToWindow(v: View?) {

    }

    override fun onViewDetachedFromWindow(v: View?) {
        anchorsTooltipsRegistry[v]?.let {
            v?.removeOnAttachStateChangeListener(this)
            dismissTooltip(it, NOOPCommandListener)
            anchorsTooltipsRegistry.remove(v)
        }
    }

}

private object NOOPCommandListener : CommandListener {
    override fun onSuccess(childId: String?) {

    }

    override fun onError(message: String?) {
    }
}
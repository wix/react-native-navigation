package com.reactnativenavigation.viewcontrollers

import android.view.View
import com.reactnativenavigation.options.OverlayAttachOptions
import com.reactnativenavigation.react.CommandListener
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.MaViewTooltip

class TooltipsManager(
    val findController: (String) -> ViewController<*>?,
    val findTooltipAnchorView: (OverlayAttachOptions) -> View?
) {
    private val registry = mutableMapOf<String, Pair<MaViewTooltip.TooltipView, ViewController<*>>>()

    fun showTooltip(
        tooltipController: ViewController<*>, overlayAttachOptions: OverlayAttachOptions,
        listener: CommandListener
    ) {
        val hostController: ViewController<*>? = findController(overlayAttachOptions.layoutId.get())
        if (hostController != null) {
            val tooltipAnchorView: View? = findTooltipAnchorView(overlayAttachOptions)
            if (tooltipAnchorView != null) {
                val tooltipView = hostController.showTooltip(tooltipAnchorView, overlayAttachOptions, tooltipController)
                registry[tooltipController.id] = tooltipView to tooltipController
                listener.onSuccess(tooltipController.id)
            } else {
                listener.onError("Cannot find anchor view with id" + overlayAttachOptions.anchorId)
            }
        } else {
            listener.onError("Cannot find layout with id" + overlayAttachOptions.layoutId)
        }
    }

    fun contains(id:String): Boolean {
        return registry.containsKey(id)
    }
    fun dismissTooltip(id: String, listener: CommandListener) {
        registry.remove(id)?.let {
            val (tpView, controller) = it
            tpView.close()
            tpView.post {
                controller.destroy()
                listener.onSuccess(id)
            }
        }?:listener.onError("Can't dismiss non-shown tooltip of id: $id")
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

}
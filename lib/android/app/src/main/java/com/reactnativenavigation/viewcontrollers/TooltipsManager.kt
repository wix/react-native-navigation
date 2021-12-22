package com.reactnativenavigation.viewcontrollers

import android.view.View
import com.reactnativenavigation.options.OverlayAttachOptions
import com.reactnativenavigation.react.CommandListener
import com.reactnativenavigation.viewcontrollers.component.ComponentViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.ViewTooltip

private class TooltipDataEntry(
    var tooltipView: ViewTooltip.TooltipView,
    val tooltipController: ViewController<*>
)

class TooltipsManager(
    private val findController: (String) -> ViewController<*>?,
    private val findTooltipAnchorView: (OverlayAttachOptions) -> View?
){
    private val registry = mutableMapOf<String, TooltipDataEntry>()

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
                    registry[tooltipController.id] = TooltipDataEntry(tooltipView, tooltipController)
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
            it.tooltipView.closeNow()
            it.tooltipController.destroy()
            listener.onSuccess(id)
        } ?: listener.onError("Can't dismiss non-shown tooltip of id: $id")
    }

    fun dismissAllTooltips() {
        val entries = registry.entries
        while (entries.isNotEmpty()) {
            val first = entries.first()
            val tooltipDataEntry = first.value
            tooltipDataEntry.tooltipView.closeNow()
            tooltipDataEntry.tooltipController.destroy()
            registry.remove(first.key)
        }
    }

}
package com.reactnativenavigation.react.modal

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import com.facebook.infer.annotation.Assertions
import com.facebook.react.common.MapBuilder
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.LayoutShadowNode
import com.facebook.react.uimanager.ReactShadowNodeImpl
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.reactnativenavigation.options.ModalPresentationStyle
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.react.CommandListener
import com.reactnativenavigation.react.CommandListenerAdapter
import com.reactnativenavigation.utils.StatusBarUtils
import com.reactnativenavigation.viewcontrollers.navigator.Navigator

private const val MODAL_MANAGER_NAME = "RNNModalViewManager"

@ReactModule(name = MODAL_MANAGER_NAME)
class RNNModalViewManager(private val navigator: Navigator) : ViewGroupManager<ModalHostLayout>() {

    override fun getName(): String = MODAL_MANAGER_NAME

    override fun createViewInstance(reactContext: ThemedReactContext): ModalHostLayout {
        return ModalHostLayout(reactContext)
    }

    override fun createShadowNodeInstance(): LayoutShadowNode {
        return ModalHostShadowNode()
    }

    override fun getShadowNodeClass(): Class<out LayoutShadowNode> {
        return ModalHostShadowNode::class.java
    }

    override fun onDropViewInstance(modal: ModalHostLayout) {
        super.onDropViewInstance(modal)
        navigator.dismissModal(modal.viewController.id, CommandListenerAdapter())
        modal.onDropInstance()
    }

    override fun onAfterUpdateTransaction(modal: ModalHostLayout) {
        super.onAfterUpdateTransaction(modal)
        navigator.showModal(modal.viewController, CommandListenerAdapter(object : CommandListener {
            override fun onSuccess(childId: String?) {
                modal.viewController.sendShowEvent()
            }

            override fun onError(message: String?) {
            }

        }))
    }

    override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any>? {
        return MapBuilder.builder<String, Any>()
            .put(RequestDismissModalEvent.EVENT_NAME, MapBuilder.of("registrationName", "onRequestDismiss"))
            .put(ShowModalEvent.EVENT_NAME, MapBuilder.of("registrationName", "onShow"))
            .build()
    }

    @ReactProp(name = "visible")
    fun setVisible(modal: ModalHostLayout, visible: Boolean) {
        //should be empty, we need at least one prop to be implemented via this
        //annotation, which enables RN to call onAfterUpdateTransaction to show and
        //onDropViewInstance to hide (to prevent taking space).
    }

    @ReactProp(name = "blurOnUnmount")
    fun setBlurOnUnmount(modal: ModalHostLayout, blurOnUnmount: Boolean) {
       modal.viewController.mergeOptions(Options().apply {
           this.modal.blurOnUnmount = Bool(blurOnUnmount)
       })
    }
    @ReactProp(name = "presentationStyle")
    fun setPresentationStyle(modal: ModalHostLayout, presentationStyle: String) {
        modal.viewController.mergeOptions(Options().apply {
            this.modal.presentationStyle = ModalPresentationStyle.fromString(presentationStyle)
        })
    }
}

private fun getModalHostSize(context: Context): Point {
    val MIN_POINT = Point()
    val MAX_POINT = Point()
    val SIZE_POINT = Point()
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = Assertions.assertNotNull(wm).defaultDisplay
    // getCurrentSizeRange will return the min and max width and height that the window can be
    display.getCurrentSizeRange(MIN_POINT, MAX_POINT)
    // getSize will return the dimensions of the screen in its current orientation
    display.getSize(SIZE_POINT)
    val attrs = intArrayOf(android.R.attr.windowFullscreen)
    val theme = context.theme
    val ta = theme.obtainStyledAttributes(attrs)
    val windowFullscreen = ta.getBoolean(0, false)

    // We need to add the status bar height to the height if we have a fullscreen window,
    // because Display.getCurrentSizeRange doesn't include it.
    var statusBarHeight = 0
    if (windowFullscreen) {
        statusBarHeight = StatusBarUtils.getStatusBarHeight(context)
    }
    return if (SIZE_POINT.x < SIZE_POINT.y) {
        // If we are vertical the width value comes from min width and height comes from max height
        Point(MIN_POINT.x, MAX_POINT.y + statusBarHeight)
    } else {
        // If we are horizontal the width value comes from max width and height comes from min height
        Point(MAX_POINT.x, MIN_POINT.y + statusBarHeight)
    }
}

private class ModalHostShadowNode : LayoutShadowNode() {
    override fun addChildAt(child: ReactShadowNodeImpl, i: Int) {
        super.addChildAt(child, i)
        val modalSize = getModalHostSize(themedContext)
        child.setStyleWidth(modalSize.x.toFloat())
        child.setStyleHeight(modalSize.y.toFloat())
    }
}

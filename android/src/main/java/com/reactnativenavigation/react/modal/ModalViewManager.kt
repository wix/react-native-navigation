package com.reactnativenavigation.react.modal

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import com.facebook.infer.annotation.Assertions
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.LayoutShadowNode
import com.facebook.react.uimanager.ReactShadowNodeImpl
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.reactnativenavigation.NavigationActivity
import com.reactnativenavigation.options.ModalPresentationStyle
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.parseTransitionAnimationOptions
import com.reactnativenavigation.options.parsers.JSONParser
import com.reactnativenavigation.react.CommandListener
import com.reactnativenavigation.react.CommandListenerAdapter
import com.reactnativenavigation.utils.SystemUiUtils
import com.reactnativenavigation.viewcontrollers.navigator.Navigator

private const val MODAL_MANAGER_NAME = "RNNModalViewManager"

@ReactModule(name = MODAL_MANAGER_NAME)
class ModalViewManager(val reactContext: ReactContext) : ViewGroupManager<ModalHostLayout>() {

    private val navigator: Navigator?
        get() {
            val navigationActivity = reactContext.currentActivity as? NavigationActivity
            return navigationActivity?.let {
                if (it.isFinishing || it.isDestroyed) null else it.navigator
            }
        }
    private val jsonParser = JSONParser()
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
        navigator?.let {
            it.dismissModal(modal.viewController.id, CommandListenerAdapter())
            modal.onDropInstance()
        }
    }

    override fun onAfterUpdateTransaction(modal: ModalHostLayout) {
        super.onAfterUpdateTransaction(modal)
        navigator?.showModal(modal.viewController, CommandListenerAdapter(object : CommandListener {
            override fun onSuccess(childId: String?) {
                modal.viewController.sendShowEvent()
            }

            override fun onError(message: String?) {
            }

        }))
    }

    override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any>? {
        return MapBuilder.builder<String, Any>()
            .put(RequestCloseModalEvent.EVENT_NAME, MapBuilder.of("registrationName", "onRequestClose"))
            .put(ShowModalEvent.EVENT_NAME, MapBuilder.of("registrationName", "onShow"))
            .build()
    }

    @ReactProp(name = "animation")
    fun setAnimation(modal: ModalHostLayout, animation: ReadableMap) {
        modal.viewController.mergeOptions(Options().apply {
            val animationJson = jsonParser.parse(animation)
            val showModal = parseTransitionAnimationOptions(animationJson.optJSONObject("showModal"))
            val dismissModal = parseTransitionAnimationOptions(animationJson.optJSONObject("dismissModal"))
            this.animations.showModal = showModal
            this.animations.dismissModal = dismissModal
        })
    }

    @ReactProp(name = "blurOnUnmount")
    fun setBlurOnUnmount(modal: ModalHostLayout, blurOnUnmount: Boolean) {
        modal.viewController.mergeOptions(Options().apply {
            this.modal.blurOnUnmount = Bool(blurOnUnmount)
        })
    }

    @ReactProp(name = "transparent")
    fun setTransparent(modal: ModalHostLayout, transparent: Boolean) {
        modal.viewController.mergeOptions(Options().apply {
            this.modal.presentationStyle =
                if (transparent) ModalPresentationStyle.OverCurrentContext else ModalPresentationStyle.None
        })
    }
}

private fun getModalHostSize(activity: Activity): Point {
    val MIN_POINT = Point()
    val MAX_POINT = Point()
    val SIZE_POINT = Point()
    val wm = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = Assertions.assertNotNull(wm).defaultDisplay
    // getCurrentSizeRange will return the min and max width and height that the window can be
    display.getCurrentSizeRange(MIN_POINT, MAX_POINT)
    // getSize will return the dimensions of the screen in its current orientation
    display.getSize(SIZE_POINT)
    val attrs = intArrayOf(android.R.attr.windowFullscreen)
    val theme = activity.theme
    val ta = theme.obtainStyledAttributes(attrs)
    val windowFullscreen = ta.getBoolean(0, false)

    // We need to add the status bar height to the height if we have a fullscreen window,
    // because Display.getCurrentSizeRange doesn't include it.
    var statusBarHeight = 0
    if (windowFullscreen) {
        statusBarHeight = SystemUiUtils.getStatusBarHeight(activity)
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
        themedContext?.currentActivity?.let {
            val modalSize = getModalHostSize(it)
            child.setStyleWidth(modalSize.x.toFloat())
            child.setStyleHeight(modalSize.y.toFloat())
        }
    }
}

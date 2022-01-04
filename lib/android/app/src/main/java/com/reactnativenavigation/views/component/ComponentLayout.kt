package com.reactnativenavigation.views.component

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.reactnativenavigation.options.ButtonOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.react.ReactView
import com.reactnativenavigation.react.events.ComponentType
import com.reactnativenavigation.utils.CoordinatorLayoutUtils
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController
import com.reactnativenavigation.viewcontrollers.viewcontroller.ScrollEventListener
import com.reactnativenavigation.views.tooltips.AttachedOverlayContainer
import com.reactnativenavigation.views.touch.OverlayTouchDelegate

@SuppressLint("ViewConstructor")
open class ComponentLayout(
    context: Context,
    private val reactView: ReactView
) : CoordinatorLayout(context), ReactComponent, ButtonController.OnClickListener {
    private var willAppearSent = false
    private val touchDelegate: OverlayTouchDelegate
    val attachedOverlayContainer: AttachedOverlayContainer = AttachedOverlayContainer(context, "Component")

    init {
        addView(reactView.asView(), CoordinatorLayoutUtils.matchParentLP())
        addView(attachedOverlayContainer, CoordinatorLayoutUtils.matchParentLP())
        touchDelegate = OverlayTouchDelegate(this, reactView)
    }

    override fun isReady() = reactView.isReady

    override fun asView(): ViewGroup = this

    override fun destroy() = reactView.destroy()

    fun start() = reactView.start()

    open fun sendComponentWillStart() {
        if (!willAppearSent) reactView.sendComponentWillStart(ComponentType.Component)
        willAppearSent = true
    }

    open fun sendComponentStart() {
        reactView.sendComponentStart(ComponentType.Component)
    }

    open fun sendComponentStop() {
        willAppearSent = false
        reactView.sendComponentStop(ComponentType.Component)
    }

    open fun applyOptions(options: Options) {
        touchDelegate.interceptTouchOutside = options.overlayOptions.interceptTouchOutside
    }

    fun setInterceptTouchOutside(interceptTouchOutside: Bool) {
        touchDelegate.interceptTouchOutside = interceptTouchOutside
    }

    override fun sendOnNavigationButtonPressed(buttonId: String) = reactView.sendOnNavigationButtonPressed(buttonId)

    override fun getScrollEventListener(): ScrollEventListener = reactView.scrollEventListener

    override fun dispatchTouchEventToJs(event: MotionEvent) = reactView.dispatchTouchEventToJs(event)

    override fun isRendered() = reactView.isRendered

    override fun onPress(button: ButtonOptions) = reactView.sendOnNavigationButtonPressed(button.id)

    override fun onInterceptTouchEvent(ev: MotionEvent) = touchDelegate.onInterceptTouchEvent(ev)

    fun superOnInterceptTouchEvent(event: MotionEvent?) = super.onInterceptTouchEvent(event)
}
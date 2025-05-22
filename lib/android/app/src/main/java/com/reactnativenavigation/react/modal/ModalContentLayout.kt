package com.reactnativenavigation.react.modal

import android.content.Context
import android.view.MotionEvent
import android.view.View
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.JSTouchDispatcher
import com.facebook.react.uimanager.RootView
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.common.UIManagerType
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.views.view.ReactViewGroup


class ModalContentLayout(context: Context?) : ReactViewGroup(context), RootView{

    private val mJSTouchDispatcher = JSTouchDispatcher(this)

    override fun onChildStartedNativeGesture(child: View, androidEvent: MotionEvent) {
        mJSTouchDispatcher.onChildStartedNativeGesture(androidEvent, this.getEventDispatcher())
    }
    override fun onChildStartedNativeGesture(androidEvent: MotionEvent) {
        mJSTouchDispatcher.onChildStartedNativeGesture(androidEvent, this.getEventDispatcher())
    }
    override fun onChildEndedNativeGesture(child: View, androidEvent: MotionEvent) {
        mJSTouchDispatcher.onChildEndedNativeGesture(androidEvent, this.getEventDispatcher())
    }
    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    private fun getEventDispatcher(): EventDispatcher {
        val reactContext: ReactContext = this.getReactContext()
        return UIManagerHelper.getEventDispatcher(reactContext, UIManagerType.FABRIC) ?: throw IllegalStateException("EventDispatcher for Fabric UI Manager is not found")
    }

    override fun handleException(t: Throwable?) {
        getReactContext().handleException(RuntimeException(t))
    }

    private fun getReactContext(): ReactContext {
        return this.context as ReactContext
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        mJSTouchDispatcher.handleTouchEvent(event, getEventDispatcher())
        return super.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mJSTouchDispatcher.handleTouchEvent(event, getEventDispatcher())
        super.onTouchEvent(event)
        return true
    }

}

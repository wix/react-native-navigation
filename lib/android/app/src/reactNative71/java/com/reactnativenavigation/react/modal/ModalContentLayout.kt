package com.reactnativenavigation.react.modal

import android.content.Context
import android.view.MotionEvent
import android.view.View
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.*
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.views.view.ReactViewGroup


class ModalContentLayout(context: Context?) : ReactViewGroup(context), RootView{
    private var hasAdjustedSize = false
    private var viewWidth = 0
    private var viewHeight = 0
    private val mJSTouchDispatcher = JSTouchDispatcher(this)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
        this.updateFirstChildView()
    }
    private fun updateFirstChildView() {
        if (this.childCount > 0) {
            hasAdjustedSize = false
            val viewTag = getChildAt(0).id
            val reactContext: ReactContext = this.getReactContext()
            reactContext.runOnNativeModulesQueueThread(object : GuardedRunnable(reactContext) {
                override fun runGuarded() {
                    val uiManager = this@ModalContentLayout.getReactContext().getNativeModule(
                            UIManagerModule::class.java
                    ) as UIManagerModule
                    uiManager.updateNodeSize(
                            viewTag,
                            this@ModalContentLayout.viewWidth,
                            this@ModalContentLayout.viewHeight
                    )
                }
            })
        } else {
            hasAdjustedSize = true
        }
    }

    override fun addView(child: View?, index: Int, params: LayoutParams?) {
        super.addView(child, index, params)
        if (hasAdjustedSize) {
            updateFirstChildView()
        }
    }
    override fun onChildStartedNativeGesture(child: View, androidEvent: MotionEvent?) {
        mJSTouchDispatcher.onChildStartedNativeGesture(androidEvent, this.getEventDispatcher())
    }
    override fun onChildStartedNativeGesture(androidEvent: MotionEvent?) {
        mJSTouchDispatcher.onChildStartedNativeGesture(androidEvent, this.getEventDispatcher())
    }
    override fun onChildEndedNativeGesture(child: View, androidEvent: MotionEvent?) {
        mJSTouchDispatcher.onChildEndedNativeGesture(androidEvent, this.getEventDispatcher())
    }
    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    private fun getEventDispatcher(): EventDispatcher? {
        val reactContext: ReactContext = this.getReactContext()
        return reactContext.getNativeModule(UIManagerModule::class.java)!!.eventDispatcher
    }


    override fun handleException(t: Throwable?) {
        getReactContext().handleException(RuntimeException(t))
    }

    private fun getReactContext(): ReactContext {
        return this.context as ReactContext
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        mJSTouchDispatcher.handleTouchEvent(event, getEventDispatcher())
        return super.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mJSTouchDispatcher.handleTouchEvent(event, getEventDispatcher())
        super.onTouchEvent(event)
        return true
    }

}
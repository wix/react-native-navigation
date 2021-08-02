package com.reactnativenavigation.react

import android.content.Context
import android.view.MotionEvent
import android.view.View
import androidx.annotation.UiThread
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.*
import com.facebook.react.uimanager.FabricViewStateManager.HasFabricViewStateManager
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.views.view.ReactViewGroup

class DeclaredLayout(context: Context?) : ReactViewGroup(context), RootView,
    HasFabricViewStateManager{
    private var hasAdjustedSize = false
    private var viewWidth = 0
    private var viewHeight = 0
    private val mFabricViewStateManager = FabricViewStateManager()
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
            if (mFabricViewStateManager.hasStateWrapper()) {
                updateState(viewWidth, viewHeight)
            } else {
                val reactContext: ReactContext = this.getReactContext()
                reactContext.runOnNativeModulesQueueThread(object : GuardedRunnable(reactContext) {
                    override fun runGuarded() {
                        val uiManager = this@DeclaredLayout.getReactContext().getNativeModule<UIManagerModule>(
                            UIManagerModule::class.java
                        ) as UIManagerModule
                        uiManager.updateNodeSize(
                            viewTag,
                            this@DeclaredLayout.viewWidth,
                            this@DeclaredLayout.viewHeight
                        )
                    }
                })
            }
        } else {
            hasAdjustedSize = true
        }
    }

    @UiThread
    fun updateState(width: Int, height: Int) {
        val realWidth = PixelUtil.toDIPFromPixel(width.toFloat())
        val realHeight = PixelUtil.toDIPFromPixel(height.toFloat())
        val currentState = this.fabricViewStateManager.state
        if (currentState != null) {
            val delta = 0.9f
            val stateScreenHeight = if (currentState.hasKey("screenHeight")) currentState.getDouble("screenHeight")
                .toFloat() else 0.0f
            val stateScreenWidth = if (currentState.hasKey("screenWidth")) currentState.getDouble("screenWidth")
                .toFloat() else 0.0f
            if (Math.abs(stateScreenWidth - realWidth) < delta && Math.abs(stateScreenHeight - realHeight) < delta) {
                return
            }
        }
        mFabricViewStateManager.setState {
            val map: WritableMap = WritableNativeMap()
            map.putDouble("screenWidth", realWidth.toDouble())
            map.putDouble("screenHeight", realHeight.toDouble())
            map
        }
    }
    override fun addView(child: View?, index: Int, params: LayoutParams?) {
        super.addView(child, index, params)
        if (hasAdjustedSize) {
            updateFirstChildView()
        }
    }
    override fun onChildStartedNativeGesture(androidEvent: MotionEvent?) {
        mJSTouchDispatcher.onChildStartedNativeGesture(androidEvent, this.getEventDispatcher())
    }
    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    private fun getEventDispatcher(): EventDispatcher? {
        val reactContext: ReactContext = this.getReactContext()
        return reactContext.getNativeModule(UIManagerModule::class.java)!!.eventDispatcher
    }

    override fun getFabricViewStateManager(): FabricViewStateManager {
        return mFabricViewStateManager
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
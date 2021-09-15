package com.reactnativenavigation.react

import android.graphics.Point
import android.view.View
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.LayoutShadowNode
import com.facebook.react.uimanager.ReactShadowNodeImpl
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.reactnativenavigation.viewcontrollers.navigator.Navigator

private const val MODAL_MANAGER_NAME = "RNNModalViewManager"

class DeclaredLayoutShadowNode : LayoutShadowNode() {
    override fun addChildAt(child: ReactShadowNodeImpl, i: Int) {
        super.addChildAt(child, i)
        val modalSize = Point(screenWidth, screenHeight)
        child.setStyleWidth(100f)
        child.setStyleHeight(100f)
    }

}


@ReactModule(name = MODAL_MANAGER_NAME)
class RNNModalViewManager(private val navigator: Navigator) : ViewGroupManager<DeclaredLayoutHost>() {

    override fun getName(): String = MODAL_MANAGER_NAME

    override fun createViewInstance(reactContext: ThemedReactContext): DeclaredLayoutHost {
        return DeclaredLayoutHost(reactContext)
    }

    override fun createShadowNodeInstance(): LayoutShadowNode {
        return DeclaredLayoutShadowNode()
    }

    override fun getShadowNodeClass(): Class<out LayoutShadowNode> {
        return DeclaredLayoutShadowNode::class.java
    }

    override fun onDropViewInstance(modal: DeclaredLayoutHost) {
        super.onDropViewInstance(modal)
        modal.onDropInstance()
        navigator.dismissModal(modal.viewController.id, CommandListenerAdapter())
    }

    override fun onAfterUpdateTransaction(modal: DeclaredLayoutHost) {
        super.onAfterUpdateTransaction(modal)
        navigator.showModal(modal.viewController, CommandListenerAdapter())
    }

    override fun addView(parent: DeclaredLayoutHost?, child: View?, index: Int) {
        super.addView(parent, child, index)
    }

    @ReactProp(name = "visible")
    fun setVisible(modal: DeclaredLayoutHost, visible: Boolean) {
    }

}
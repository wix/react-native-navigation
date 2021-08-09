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
        val modalSize = Point(screenWidth,screenHeight)
        child.setStyleWidth(0f)
        child.setStyleHeight(0f)
    }

}


@ReactModule(name = MODAL_MANAGER_NAME)
class RNNModalViewManager(private val navigator: Navigator) : ViewGroupManager<DeclaredLayoutHost>() {

    override fun getName(): String = MODAL_MANAGER_NAME

    override fun createViewInstance(reactContext: ThemedReactContext): DeclaredLayoutHost {
        val declaredLayoutHost = DeclaredLayoutHost(reactContext)
        return declaredLayoutHost
    }

    override fun createShadowNodeInstance(): LayoutShadowNode {
        return DeclaredLayoutShadowNode()
    }

    override fun getShadowNodeClass(): Class<out LayoutShadowNode> {
        return DeclaredLayoutShadowNode::class.java
    }
    override fun onDropViewInstance(view: DeclaredLayoutHost) {
        super.onDropViewInstance(view)
        view.onDropInstance()
    }

     override fun onAfterUpdateTransaction(view: DeclaredLayoutHost) {
        super.onAfterUpdateTransaction(view)
        view.showOrUpdate()
    }
    override fun addView(parent: DeclaredLayoutHost?, child: View?, index: Int) {
        super.addView(parent, child, index)
    }

    @ReactProp(name = "visible")
    fun setVisible(modal: DeclaredLayoutHost, visible: Boolean) {
        if (visible) {
            modal.viewController.view.visibility = View.VISIBLE
            modal.visibility = View.VISIBLE
            navigator.showModal(modal.viewController, CommandListenerAdapter())
        }
//        else {
//            if(modal.viewController.isViewShown){
//                navigator.dismissModal(modal.viewController.id,object : CommandListener {
//                    override fun onSuccess(childId: String?) {
//                        modal.viewController.view.visibility = View.GONE
//                    }
//
//                    override fun onError(message: String?) {
//                        modal.viewController.view.visibility = View.GONE
//                    }
//                })
//            }
//            modal.visibility = View.GONE
//        }
    }

}
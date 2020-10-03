package com.reactnativenavigation.views.element.finder

import android.view.View
import com.facebook.react.uimanager.util.ReactFindViewUtil
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController

class ExistingViewFinder : ViewFinder {
    override fun find(root: ViewController<*>, nativeId: String, onViewFound: (View) -> Unit) {
        ReactFindViewUtil.findView(root.view, nativeId)?.let { onViewFound(it) }
    }
}
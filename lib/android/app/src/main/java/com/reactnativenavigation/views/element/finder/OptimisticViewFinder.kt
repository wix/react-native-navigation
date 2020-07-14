package com.reactnativenavigation.views.element.finder

import android.view.View
import androidx.core.view.doOnLayout
import com.facebook.react.uimanager.util.ReactFindViewUtil
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController

class OptimisticViewFinder : ViewFinder {
    override fun find(root: ViewController<*>, nativeId: String, onViewFound: (View) -> Unit) {
        ReactFindViewUtil.findView(root.view, nativeId)
                ?.let { onViewFound(it) }
                ?: run {
                    ReactFindViewUtil.findView(root.view, object : ReactFindViewUtil.OnViewFoundListener {
                        override fun getNativeId() = nativeId
                        override fun onViewFound(view: View) = view.doOnLayout { onViewFound(view) }
                    })
                }
    }
}
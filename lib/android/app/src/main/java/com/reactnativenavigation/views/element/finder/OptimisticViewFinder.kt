package com.reactnativenavigation.views.element.finder

import android.view.View
import androidx.core.view.doOnLayout
import com.facebook.react.uimanager.util.ReactFindViewUtil
import com.reactnativenavigation.utils.doOnLayoutCompat
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class OptimisticViewFinder : ViewFinder {
    override suspend fun find(root: ViewController<*>, nativeId: String) = suspendCancellableCoroutine<View>() { cont ->
        val onViewFoundListener = object : ReactFindViewUtil.OnViewFoundListener {
            override fun getNativeId() = nativeId
            override fun onViewFound(view: View) = view.doOnLayout { cont.resume(it) }
        }

        ReactFindViewUtil.findView(root.view, nativeId)
                ?.let { view -> view.doOnLayoutCompat { cont.resume(view) } }
                ?: run { ReactFindViewUtil.findView(root.view, onViewFoundListener) }

        cont.invokeOnCancellation { ReactFindViewUtil.removeViewListener(onViewFoundListener) }
    }
}
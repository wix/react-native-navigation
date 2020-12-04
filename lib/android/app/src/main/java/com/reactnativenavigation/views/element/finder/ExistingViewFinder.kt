package com.reactnativenavigation.views.element.finder

import android.view.View
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import com.facebook.drawee.generic.RootDrawable
import com.facebook.react.uimanager.util.ReactFindViewUtil
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ExistingViewFinder : ViewFinder {
    private val bailoutTime = 100L
    private var timeElapsedWaitingForActualImageToLoad = 0L

    override suspend fun find(root: ViewController<*>, nativeId: String) = suspendCoroutine<View?> { cont ->
        when (val view = ReactFindViewUtil.findView(root.view, nativeId)) {
            null -> cont.resume(null)
            is ImageView -> {
                if (hasMeasuredDrawable(view)) {
                    resume(view, cont)
                } else {
                    resumeOnImageLoad(view, cont)
                }
            }
            else -> cont.resume(view)
        }
    }

    private fun resume(view: ImageView, cont: Continuation<View?>) {
        if (view.drawable is RootDrawable) {
            view.post { cont.resume(view) }
        } else {
            cont.resume(view)
        }
    }

    private fun resumeOnImageLoad(view: ImageView, cont: Continuation<View?>) {
        val t1 = System.currentTimeMillis()
        view.doOnPreDraw {
            if (hasMeasuredDrawable(view)) {
                view.post {
                    cont.resume(view)
                }
            } else {
                timeElapsedWaitingForActualImageToLoad += (System.currentTimeMillis() - t1)
                if (timeElapsedWaitingForActualImageToLoad < bailoutTime) {
                    resumeOnImageLoad(view, cont)
                } else {
                    cont.resume(null)
                }
            }
        }
    }

    private fun hasMeasuredDrawable(view: ImageView) = when (view.drawable) {
        is RootDrawable -> true
        else -> with(view.drawable) { intrinsicWidth != -1 && intrinsicHeight != -1 }
    }
}
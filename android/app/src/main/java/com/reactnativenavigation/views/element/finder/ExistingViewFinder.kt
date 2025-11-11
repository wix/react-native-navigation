package com.reactnativenavigation.views.element.finder

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import com.facebook.drawee.generic.RootDrawable
import com.facebook.react.uimanager.util.ReactFindViewUtil
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.min

class ExistingViewFinder : ViewFinder {

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
        view.doOnPreDraw {
            if (hasMeasuredDrawable(view)) {
                view.post {
                    cont.resume(view)
                }
            } else {
                resumeOnImageLoad(view, cont)
            }
        }
    }

    private fun hasMeasuredDrawable(view: ImageView) = when (view.drawable) {
        is RootDrawable -> true
        else -> checkIfFastImageIsMeasured(view)
    }

    private fun checkIfFastImageIsMeasured(view: ImageView) = with(view.drawable) {
        this != null && intrinsicWidth != -1 && intrinsicHeight != -1 && isImageScaledToFit(view)
    }

    private fun Drawable.isImageScaledToFit(view: ImageView): Boolean {
        val scaleX = view.width / intrinsicWidth.toFloat()
        val scaleY = view.height / intrinsicHeight.toFloat()
        return min(scaleX, scaleY) >= 1f
    }
}

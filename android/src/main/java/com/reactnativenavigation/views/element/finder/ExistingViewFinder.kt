package com.reactnativenavigation.views.element.finder

import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.facebook.drawee.generic.RootDrawable
import com.facebook.react.uimanager.util.ReactFindViewUtil
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

class ExistingViewFinder : ViewFinder {
    override suspend fun find(root: ViewController<*>, nativeId: String): View? {
        if (root.isDestroyed) return null
        val view = ReactFindViewUtil.findView(root.view, nativeId)
        return if (view is ImageView) awaitImage(view) else view
    }

    internal suspend fun awaitImage(view: ImageView): View? = withTimeoutOrNull(IMAGE_LOAD_TIMEOUT_MS) {
        suspendCancellableCoroutine { cont ->
            val observer = view.viewTreeObserver
            lateinit var preDraw: ViewTreeObserver.OnPreDrawListener
            lateinit var attachment: View.OnAttachStateChangeListener
            var pendingResume: Runnable? = null

            fun cleanup() {
                if (observer.isAlive) observer.removeOnPreDrawListener(preDraw)
                view.removeOnAttachStateChangeListener(attachment)
                pendingResume?.let(view::removeCallbacks)
            }

            fun finish(result: View?) {
                cleanup()
                if (cont.isActive) cont.resume(result)
            }

            fun checkImage() {
                if (!hasMeasuredDrawable(view) || pendingResume != null) return
                // Fresco updates drawable bounds during drawing; preserve the deferred resume.
                if (view.drawable is RootDrawable) {
                    pendingResume = Runnable { finish(view) }.also { view.post(it) }
                } else {
                    finish(view)
                }
            }

            preDraw = ViewTreeObserver.OnPreDrawListener { checkImage(); true }
            attachment = object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {}
                override fun onViewDetachedFromWindow(v: View) { finish(null) }
            }
            observer.addOnPreDrawListener(preDraw)
            view.addOnAttachStateChangeListener(attachment)
            cont.invokeOnCancellation { cleanup() }
            if (cont.isActive) checkImage()
        }
    }

    private fun hasMeasuredDrawable(view: ImageView): Boolean {
        val drawable = view.drawable ?: return false
        return drawable is RootDrawable ||
            (view.width > 0 && view.height > 0 && drawable.intrinsicWidth > 0 && drawable.intrinsicHeight > 0)
    }

    companion object {
        internal const val IMAGE_LOAD_TIMEOUT_MS = 1000L
    }
}

package com.reactnativenavigation.viewcontrollers.modal

import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.reactnativenavigation.options.ModalPresentationStyle
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.react.CommandListener
import com.reactnativenavigation.utils.CoordinatorLayoutUtils
import com.reactnativenavigation.utils.ScreenAnimationListener
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController

class ModalPresenter internal constructor(private val animator: ModalAnimator) {
    private var rootLayout: ViewGroup? = null
    private var modalsLayout: CoordinatorLayout? = null
    private var defaultOptions = Options()
    fun setRootLayout(rootLayout: ViewGroup?) {
        this.rootLayout = rootLayout
    }

    fun setModalsLayout(modalsLayout: CoordinatorLayout?) {
        this.modalsLayout = modalsLayout
    }

    fun setDefaultOptions(defaultOptions: Options) {
        this.defaultOptions = defaultOptions
    }

    fun showModal(toAdd: ViewController<*>, toRemove: ViewController<*>, listener: CommandListener) {
        if (modalsLayout == null) {
            listener.onError("Can not show modal before activity is created")
            return
        }
        val options = toAdd.resolveCurrentOptions(defaultOptions)
        toAdd.setWaitForRender(options.animations.showModal.waitForRender)
        modalsLayout!!.visibility = View.VISIBLE
        modalsLayout!!.addView(toAdd.view, CoordinatorLayoutUtils.matchParentLP())
        if (options.animations.showModal.enabled.isTrueOrUndefined) {
            toAdd.view.alpha = 0f
            if (options.animations.showModal.waitForRender.isTrue) {
                toAdd.addOnAppearedListener { animateShow(toAdd, toRemove, listener, options) }
            } else {
                animateShow(toAdd, toRemove, listener, options)
            }
        } else {
            if (options.animations.showModal.waitForRender.isTrue) {
                toAdd.addOnAppearedListener { onShowModalEnd(toAdd, toRemove, listener) }
            } else {
                onShowModalEnd(toAdd, toRemove, listener)
            }
        }
    }

    private fun animateShow(toAdd: ViewController<*>, toRemove: ViewController<*>, listener: CommandListener, options: Options) {
        animator.show(toAdd.view, options.animations.showModal, object : ScreenAnimationListener() {
            override fun onStart() {
                toAdd.view.alpha = 1f
            }

            override fun onEnd() {
                onShowModalEnd(toAdd, toRemove, listener)
            }

            override fun onCancel() {
                listener.onSuccess(toAdd.id)
            }
        })
    }

    private fun onShowModalEnd(toAdd: ViewController<*>, toRemove: ViewController<*>?, listener: CommandListener) {
        toAdd.onViewDidAppear()
        if (toRemove != null && toAdd.resolveCurrentOptions(defaultOptions).modal.presentationStyle != ModalPresentationStyle.OverCurrentContext) {
            toRemove.detachView()
        }
        listener.onSuccess(toAdd.id)
    }

    fun dismissModal(toDismiss: ViewController<*>, toAdd: ViewController<*>?, root: ViewController<*>, listener: CommandListener) {
        if (modalsLayout == null) {
            listener.onError("Can not dismiss modal before activity is created")
            return
        }
        if (toAdd != null) {
            toAdd.attachView(if (toAdd === root) rootLayout else modalsLayout, 0)
            toAdd.onViewDidAppear()
        }
        val options = toDismiss.resolveCurrentOptions(defaultOptions)
        if (options.animations.dismissModal.enabled.isTrueOrUndefined) {
            animator.dismiss(toDismiss.view, options.animations.dismissModal, object : ScreenAnimationListener() {
                override fun onEnd() {
                    onDismissEnd(toDismiss, listener)
                }
            })
        } else {
            onDismissEnd(toDismiss, listener)
        }
    }

    private fun onDismissEnd(toDismiss: ViewController<*>, listener: CommandListener) {
        listener.onSuccess(toDismiss.id)
        toDismiss.destroy()
        if (isEmpty) modalsLayout!!.visibility = View.GONE
    }

    private val isEmpty: Boolean
        private get() = modalsLayout!!.childCount == 0
}
package com.reactnativenavigation.viewcontrollers.modal

import android.app.Activity
import android.view.ViewGroup
import androidx.annotation.RestrictTo
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.react.CommandListener
import com.reactnativenavigation.react.CommandListenerAdapter
import com.reactnativenavigation.react.events.EventEmitter
import com.reactnativenavigation.utils.ObjectUtils
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import java.util.*

class ModalStack {
    private val modals: MutableList<ViewController<*>> = ArrayList()
    private val presenter: ModalPresenter
    private var eventEmitter: EventEmitter? = null
    fun setEventEmitter(eventEmitter: EventEmitter?) {
        this.eventEmitter = eventEmitter
    }

    constructor(activity: Activity?) {
        presenter = ModalPresenter(ModalAnimator(activity))
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    internal constructor(presenter: ModalPresenter) {
        this.presenter = presenter
    }

    fun setModalsLayout(modalsLayout: CoordinatorLayout?) {
        presenter.setModalsLayout(modalsLayout)
    }

    fun setRootLayout(rootLayout: ViewGroup?) {
        presenter.setRootLayout(rootLayout)
    }

    fun setDefaultOptions(defaultOptions: Options) {
        presenter.setDefaultOptions(defaultOptions)
    }

    fun showModal(viewController: ViewController<*>, root: ViewController<*>, listener: CommandListener) {
        val toRemove = if (isEmpty) root else peek()
        modals.add(viewController)
        presenter.showModal(viewController, toRemove, listener)
    }

    fun dismissModal(componentId: String, root: ViewController<*>, listener: CommandListener): Boolean {
        val toDismiss = findModalByComponentId(componentId)
        return if (toDismiss != null) {
            val isDismissingTopModal = isTop(toDismiss)
            modals.remove(toDismiss)
            val toAdd = if (isEmpty) root else if (isDismissingTopModal) get(size() - 1) else null
            if (isDismissingTopModal) {
                if (toAdd == null) {
                    listener.onError("Could not dismiss modal")
                    return false
                }
            }
            presenter.dismissModal(toDismiss, toAdd, root, object : CommandListenerAdapter(listener) {
                override fun onSuccess(childId: String) {
                    eventEmitter!!.emitModalDismissed(toDismiss.id, toDismiss.currentComponentName, 1)
                    super.onSuccess(toDismiss.id)
                }
            })
            true
        } else {
            listener.onError("Nothing to dismiss")
            false
        }
    }

    fun dismissAllModals(root: ViewController<*>?, mergeOptions: Options?, listener: CommandListener) {
        if (modals.isEmpty() || root == null) {
            listener.onSuccess(ObjectUtils.perform<ViewController<*>?, String>(root, "", { obj: ViewController<*>? -> obj!!.id }))
            return
        }
        val topModalId = peek().id
        val topModalName = peek().currentComponentName
        val modalsDismissed = size()
        peek().mergeOptions(mergeOptions)
        while (!modals.isEmpty()) {
            if (modals.size == 1) {
                dismissModal(modals[0].id, root, object : CommandListenerAdapter(listener) {
                    override fun onSuccess(childId: String) {
                        eventEmitter!!.emitModalDismissed(topModalId, topModalName, modalsDismissed)
                        super.onSuccess(childId)
                    }
                })
            } else {
                modals[0].destroy()
                modals.removeAt(0)
            }
        }
    }

    fun handleBack(listener: CommandListener, root: ViewController<*>): Boolean {
        if (isEmpty) return false
        return if (peek().handleBack(listener)) {
            true
        } else dismissModal(peek().id, root, listener)
    }

    fun peek(): ViewController<*> {
        if (modals.isEmpty()) throw EmptyStackException()
        return modals[modals.size - 1]
    }

    operator fun get(index: Int): ViewController<*> {
        return modals[index]
    }

    val isEmpty: Boolean
        get() = modals.isEmpty()

    fun size(): Int {
        return modals.size
    }

    private fun isTop(modal: ViewController<*>): Boolean {
        return !isEmpty && peek() == modal
    }

    private fun findModalByComponentId(componentId: String): ViewController<*>? {
        for (modal in modals) {
            if (modal.findController(componentId) != null) {
                return modal
            }
        }
        return null
    }

    fun findControllerById(componentId: String?): ViewController<*>? {
        for (modal in modals) {
            val controllerById = modal.findController(componentId)
            if (controllerById != null) {
                return controllerById
            }
        }
        return null
    }

    fun destroy() {
        for (modal in modals) {
            modal.destroy()
        }
        modals.clear()
    }
}
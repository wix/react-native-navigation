package com.reactnativenavigation.viewcontrollers

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import com.reactnativenavigation.parse.Options
import com.reactnativenavigation.parse.params.Button
import com.reactnativenavigation.parse.params.Text
import com.reactnativenavigation.react.events.ComponentType
import com.reactnativenavigation.utils.*
import com.reactnativenavigation.utils.ImageLoader.ImagesLoadingListener
import com.reactnativenavigation.viewcontrollers.button.IconResolver
import com.reactnativenavigation.viewcontrollers.viewcontrolleroverlay.ViewControllerOverlay
import com.reactnativenavigation.views.titlebar.TitleBar
import com.reactnativenavigation.views.titlebar.TitleBarButtonCreator
import com.reactnativenavigation.views.titlebar.TitleBarReactButtonView

class TitleBarButtonController(activity: Activity?,
                               private val navigationIconResolver: IconResolver,
                               private val presenter: ButtonPresenter,
                               val button: Button,
                               private val viewCreator: TitleBarButtonCreator,
                               private val onPressListener: OnClickListener) : ViewController<TitleBarReactButtonView?>(activity, button.id, YellowBoxDelegate(activity!!), Options(), ViewControllerOverlay(activity)), MenuItem.OnMenuItemClickListener {
    private var menuItem: MenuItem? = null

    interface OnClickListener {
        fun onPress(buttonId: String?)
    }

    private var icon: Drawable? = null

    val buttonInstanceId: String
        get() = button.instanceId

    val buttonIntId: Int
        get() = button.intId

    @SuppressLint("MissingSuperCall")
    override fun onViewAppeared() {
        getView()!!.sendComponentStart(ComponentType.Button)
    }

    @SuppressLint("MissingSuperCall")
    override fun onViewDisappear() {
        getView()!!.sendComponentStop(ComponentType.Button)
    }

    override fun isRendered(): Boolean {
        return !button.component.componentId.hasValue() || super.isRendered()
    }

    override fun sendOnNavigationButtonPressed(buttonId: String) {
        getView()!!.sendOnNavigationButtonPressed(buttonId)
    }

    override fun getCurrentComponentName(): String? {
        return button.component.name.get()
    }

    override fun createView(): TitleBarReactButtonView {
        return viewCreator.create(activity, button.component).apply {
            view = this
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        onPressListener.onPress(button.id)
        return true
    }

    override fun equals(other: Any?): Boolean {
        if(other !is TitleBarButtonController) return false
        if (other === this) return true
        return if (other.id != id) false else button.equals(other.button)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    fun applyNavigationIcon(toolbar: Toolbar) {
        navigationIconResolver.resolve(button) { icon: Drawable ->
            setIconColor(icon)
            toolbar.setNavigationOnClickListener { onPressListener.onPress(button.id) }
            toolbar.navigationIcon = icon
            setLeftButtonTestId(toolbar)
            if (button.accessibilityLabel.hasValue()) toolbar.navigationContentDescription = button.accessibilityLabel.get()
        }
    }

    private fun setLeftButtonTestId(toolbar: Toolbar) {
        if (!button.testId.hasValue()) return
        toolbar.post {
            val leftButton = ViewUtils.findChildByClass(toolbar, ImageButton::class.java)
            if (leftButton != null) {
                leftButton.tag = button.testId.get()
            }
        }
    }

    fun addToMenu(titleBar: TitleBar, order: Int) {
        if (button.component.hasValue() && titleBar.containsRightButton(menuItem, order)) return
        titleBar.menu.removeItem(button.intId)
        menuItem = titleBar.menu.add(Menu.NONE, button.intId, order, presenter.styledText)
        applyButtonOptions(titleBar, menuItem)
    }

    private fun applyButtonOptions(titleBar: TitleBar, menuItem: MenuItem?) {
        if (button.showAsAction.hasValue()) menuItem!!.setShowAsAction(button.showAsAction.get())
        menuItem!!.isEnabled = button.enabled.isTrueOrUndefined
        menuItem.setOnMenuItemClickListener(this)
        if (button.hasComponent()) {
            menuItem.actionView = getView()
            if (button.accessibilityLabel.hasValue()) getView()!!.contentDescription = button.accessibilityLabel.get()
        } else {
            if (button.accessibilityLabel.hasValue()) MenuItemCompat.setContentDescription(menuItem, button.accessibilityLabel.get())
            if (button.hasIcon()) {
                loadIcon(object : ImageLoadingListenerAdapter() {
                    override fun onComplete(drawable: Drawable) {
                        this@TitleBarButtonController.icon = drawable
                        setIconColor(drawable)
                        menuItem.icon = drawable
                    }
                })
            }
        }
        setTestId(titleBar, button.testId)
    }

    private fun loadIcon(callback: ImagesLoadingListener) {
        navigationIconResolver.resolve(button) { drawable: Drawable? -> callback.onComplete(drawable!!) }
    }

    private fun setIconColor(icon: Drawable) {
        if (button.disableIconTint.isTrue) return
        if (button.enabled.isTrueOrUndefined && button.color.hasValue()) {
            presenter.tint(icon, button.color.get())
        } else if (button.enabled.isFalse) {
            presenter.tint(icon, button.disabledColor[Color.LTGRAY])
        }
    }

    private fun setTestId(toolbar: Toolbar, testId: Text) {
        if (!testId.hasValue()) return
        UiUtils.runOnPreDrawOnce(toolbar) {
            if (button.hasComponent() && view != null) {
                view!!.tag = testId.get()
            }
            val buttonsLayout = ViewUtils.findChildByClass(toolbar, ActionMenuView::class.java)
            val buttons = ViewUtils.findChildrenByClass(buttonsLayout, TextView::class.java)
            for (view in buttons) {
                if (button.text.hasValue() && button.text.get() == view.text.toString()) {
                    view.tag = testId.get()
                } else if (button.icon.hasValue() && ArrayUtils.contains(view.compoundDrawables, icon)) {
                    view.tag = testId.get()
                }
            }
        }
    }

}
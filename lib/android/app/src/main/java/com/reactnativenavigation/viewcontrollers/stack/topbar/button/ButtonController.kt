package com.reactnativenavigation.viewcontrollers.stack.topbar.button

import android.annotation.SuppressLint
import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.reactnativenavigation.options.ButtonOptions
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.ThemeColour
import com.reactnativenavigation.react.events.ComponentType
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.viewcontrollers.viewcontroller.YellowBoxDelegate
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.ViewControllerOverlay
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonBar
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarButtonCreator
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarReactButtonView

open class ButtonController(activity: Activity,
                            private val presenter: ButtonPresenter,
                            val button: ButtonOptions,
                            private val viewCreator: TitleBarButtonCreator,
                            private val onPressListener: OnClickListener) : ViewController<TitleBarReactButtonView>(activity, button.id, YellowBoxDelegate(activity), Options(), ViewControllerOverlay(activity)), MenuItem.OnMenuItemClickListener {

    private var menuItem: MenuItem? = null

    interface OnClickListener {
        fun onPress(button: ButtonOptions)
    }

    val buttonInstanceId: String
        get() = button.instanceId

    val buttonIntId: Int
        get() = button.intId

    @SuppressLint("MissingSuperCall")
    override fun onViewWillAppear() {
        getView()?.sendComponentWillStart(ComponentType.Button)
        getView()?.sendComponentStart(ComponentType.Button)
    }

    @SuppressLint("MissingSuperCall")
    override fun onViewDisappear() {
        getView()?.sendComponentStop(ComponentType.Button)
    }

    override fun isRendered(): Boolean {
        return !button.component.componentId.hasValue() || super.isRendered()
    }

    override fun sendOnNavigationButtonPressed(buttonId: String) {
        getView()!!.sendOnNavigationButtonPressed(buttonId)
    }

    override fun getCurrentComponentName(): String = button.component.name.get()

    override fun createView(): TitleBarReactButtonView {
        return viewCreator.create(activity, button.component).apply {
            view = this
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        onPressListener.onPress(button)
        return true
    }

    fun areButtonsEqual(other: ButtonController): Boolean {
        if (other === this) return true
        return if (other.id != id) false else button.equals(other.button)
    }

    fun applyNavigationIcon(toolbar: Toolbar) {
        presenter.applyNavigationIcon(toolbar) {
            onPressListener.onPress(it)
        }
    }

    open fun applyColor(toolbar: Toolbar, color: ThemeColour) = this.menuItem?.let { presenter.applyColor(toolbar, it, color) }

    open fun applyDisabledColor(toolbar: Toolbar, disabledColour: ThemeColour) = this.menuItem?.let { presenter.applyDisabledColor(toolbar, it, disabledColour) }

    fun addToMenu(buttonBar: ButtonBar, order: Int) {
        if (button.component.hasValue() && buttonBar.containsButton(menuItem, order)) return
        buttonBar.menu.removeItem(button.intId)
        menuItem = buttonBar.addButton(Menu.NONE,
                button.intId,
                order,
                presenter.styledText)?.also { menuItem ->
            menuItem.setOnMenuItemClickListener(this@ButtonController)
            presenter.applyOptions(buttonBar, menuItem, this@ButtonController::getView)
        }
    }
}
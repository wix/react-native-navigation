package com.reactnativenavigation.viewcontrollers

import android.annotation.SuppressLint
import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import com.reactnativenavigation.parse.Options
import com.reactnativenavigation.parse.params.Button
import com.reactnativenavigation.react.events.ComponentType
import com.reactnativenavigation.utils.*
import com.reactnativenavigation.viewcontrollers.viewcontrolleroverlay.ViewControllerOverlay
import com.reactnativenavigation.views.titlebar.TitleBar
import com.reactnativenavigation.views.titlebar.TitleBarButtonCreator
import com.reactnativenavigation.views.titlebar.TitleBarReactButtonView

class TitleBarButtonController(activity: Activity,
                               private val presenter: ButtonPresenter,
                               val button: Button,
                               private val viewCreator: TitleBarButtonCreator,
                               private val onPressListener: OnClickListener) : ViewController<TitleBarReactButtonView>(activity, button.id, YellowBoxDelegate(activity), Options(), ViewControllerOverlay(activity)), MenuItem.OnMenuItemClickListener {
    private var menuItem: MenuItem? = null

    interface OnClickListener {
        fun onPress(buttonId: String?)
    }

    val buttonInstanceId: String
        get() = button.instanceId

    val buttonIntId: Int
        get() = button.intId

    @SuppressLint("MissingSuperCall")
    override fun onViewWillAppear() {
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

    override fun getCurrentComponentName(): String = button.component.name.get()

    override fun createView(): TitleBarReactButtonView {
        return viewCreator.create(activity, button.component).apply {
            view = this
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        onPressListener.onPress(button.id)
        return true
    }

    fun areButtonsEqual(other: TitleBarButtonController): Boolean {
        if (other === this) return true
        return if (other.id != id) false else button.equals(other.button)
    }

    fun applyNavigationIcon(titleBar: TitleBar) {
        presenter.applyNavigationIcon(titleBar) {
            onPressListener.onPress(it)
        }
    }

    fun addToMenu(titleBar: TitleBar, order: Int) {
        if (button.component.hasValue() && titleBar.containsRightButton(menuItem, order)) return
        titleBar.menu.removeItem(button.intId)
        createAndAddButtonToTitleBar(titleBar, order).apply {
            menuItem = this
            setOnMenuItemClickListener(this@TitleBarButtonController)
            presenter.applyOptions(titleBar, this, this@TitleBarButtonController::getView)
        }
    }

    fun createAndAddButtonToTitleBar(titleBar: TitleBar, order: Int): MenuItem = titleBar.menu.add(Menu.NONE, button.intId, order, presenter.styledText)
}
package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.SpannableString
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar
import com.reactnativenavigation.utils.ObjectUtils
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController

open class ButtonBar internal constructor(context: Context) : Toolbar(context) {
    init {
        super.setContentInsetsAbsolute(0, 0)
        this.contentInsetStartWithNavigation = 0
    }

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        enableOverflowForReactButtonViews(child)
    }

    private fun enableOverflowForReactButtonViews(child: View) {
        if (child is ActionMenuView) {
            (child as ViewGroup).clipChildren = false
        }
    }

    override fun setLayoutDirection(layoutDirection: Int) {
        ObjectUtils.perform(ViewUtils.findChildByClass(this, ActionMenuView::class.java), { buttonsContainer: ActionMenuView -> buttonsContainer.layoutDirection = layoutDirection })
        super.setLayoutDirection(layoutDirection)
    }

    fun addButton(menuItem: Int, intId: Int, order: Int, styledText: SpannableString): MenuItem? {
        return this.menu?.add(menuItem,
                intId,
                order,
                styledText)
    }

    val buttonCount: Int
        get() = menu.size()

    open fun clearButtons() {
        clearBackButton()
        if (menu.size() > 0) menu.clear()
    }

    fun getButton(index: Int): MenuItem {
        return menu.getItem(index)
    }

    fun containsButton(menuItem: MenuItem?, order: Int): Boolean {
        return menuItem != null && menu.findItem(menuItem.itemId) != null && menuItem.order == order
    }

    fun removeButton(buttonId: Int) {
        menu.removeItem(buttonId)
    }

    fun setBackButton(button: ButtonController) {
        button.applyNavigationIcon(this)
    }

    fun clearBackButton() {
        navigationIcon = null
    }

    fun setOverflowButtonColor(color: Int) {
        val actionMenuView = ViewUtils.findChildByClass(this, ActionMenuView::class.java)
        if (actionMenuView != null) {
            val overflowIcon = actionMenuView.overflowIcon
            if (overflowIcon != null) {
                overflowIcon.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
            }
        }
    }
}
package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.SpannableString
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar
import com.reactnativenavigation.R
import com.reactnativenavigation.utils.ObjectUtils
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController


class RightButtonsBar constructor(context: Context) : ButtonsToolbar(context)

class LeftButtonsBar constructor(context: Context) : ButtonsToolbar(context) {
    fun setBackButton(button: ButtonController) {
        button.applyNavigationIcon(this)
    }

    fun clearBackButton() {
        navigationIcon = null
    }

    override fun clearButtons() {
        super.clearButtons()
        clearBackButton()
    }
}

open class ButtonsToolbar internal constructor(context: Context) : Toolbar(context) {
    init {
        super.setTitleTextAppearance(context, R.style.TitleBarTitle)
        super.setSubtitleTextAppearance(context, R.style.TitleBarSubtitle)
        //space that was reserved for system stuff, we want it for us!
        super.setContentInsetsAbsolute(0, 0)
        this.contentInsetStartWithNavigation = 0
        //to make sure menu items are laid out right aways
        this.menu
    }

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        //enable overflow for react button views
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

    fun setHeight(height: Int) {
        layoutParams?.let {
            val pixelHeight = UiUtils.dpToPx(context, height)
            if (pixelHeight == layoutParams.height) return
            val lp = layoutParams
            lp.height = pixelHeight
            layoutParams = lp
        }
    }

    fun setTopMargin(topMargin: Int) {
        val pixelTopMargin = UiUtils.dpToPx(context, topMargin)
        if (layoutParams is MarginLayoutParams) {
            val lp = layoutParams as MarginLayoutParams
            if (lp.topMargin == pixelTopMargin) return
            lp.topMargin = pixelTopMargin
            layoutParams = lp
        }
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

    val buttonsCount: Int
        get() = menu.size()

    open fun clearButtons() {
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

    override fun setTitle(title: CharSequence?) {

    }

    override fun setSubtitle(title: CharSequence?) {

    }
}
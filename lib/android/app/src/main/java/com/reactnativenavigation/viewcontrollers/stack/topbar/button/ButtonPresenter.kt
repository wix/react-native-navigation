package com.reactnativenavigation.viewcontrollers.stack.topbar.button

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.core.view.doOnPreDraw
import com.reactnativenavigation.options.ButtonOptions
import com.reactnativenavigation.utils.*
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBar

open class ButtonPresenter(private val context: Context, private val button: ButtonOptions, private val iconResolver: IconResolver) {
    companion object {
        const val DISABLED_COLOR = Color.LTGRAY
    }

    val styledText: SpannableString
        get() {
            return SpannableString(button.text.get("")).apply {
                setSpan(ButtonSpan(context, button), 0, button.text.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            }
        }

    open fun tint(drawable: Drawable, tint: Int) {
        drawable.colorFilter = PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN)
    }

    fun applyOptions(titleBar: TitleBar, menuItem: MenuItem, viewCreator: () -> View) {
        applyShowAsAction(menuItem)
        applyEnabled(menuItem)
        applyComponent(menuItem, viewCreator)
        applyAccessibilityLabel(menuItem)
        applyIcon(menuItem)

        applyOptionsDirectlyOnView(titleBar, menuItem) {
            applyTestId(it)
            applyTextColor(it)
            applyAllCaps(it)
        }
    }

    private fun applyAccessibilityLabel(menuItem: MenuItem) {
        if (button.accessibilityLabel.hasValue()) {
            if (button.component.hasValue()) {
                menuItem.actionView.contentDescription = button.accessibilityLabel.get()
            } else {
                MenuItemCompat.setContentDescription(menuItem, button.accessibilityLabel.get())
            }
        }
    }

    private fun applyComponent(menuItem: MenuItem, viewCreator: () -> View) {
        if (button.hasComponent()) {
            menuItem.actionView = viewCreator()
        }
    }

    private fun applyEnabled(menuItem: MenuItem) {
        menuItem.isEnabled = button.enabled.isTrueOrUndefined
    }

    private fun applyIcon(menuItem: MenuItem) {
        if (button.hasIcon()) {
            loadIcon(object : ImageLoadingListenerAdapter() {
                override fun onComplete(drawable: Drawable) {
                    setIconColor(drawable)
                    menuItem.icon = drawable
                }
            })
        }
    }

    private fun applyShowAsAction(menuItem: MenuItem) {
        if (button.showAsAction.hasValue()) menuItem.setShowAsAction(button.showAsAction.get())
    }

    private fun applyTestId(view: View) {
        if (button.testId.hasValue()) view.tag = button.testId.get()
    }

    private fun applyTextColor(view: View) {
        if (view is TextView) {
            if (button.enabled.isTrueOrUndefined) {
                if (button.color.hasValue()) view.setTextColor(button.color.get())
            } else {
                view.setTextColor(button.disabledColor.get(DISABLED_COLOR))
            }
        }
    }

    private fun applyAllCaps(view: View) {
        if (view is TextView) view.isAllCaps = button.allCaps.get(true)
    }

    private fun applyOptionsDirectlyOnView(titleBar: TitleBar, menuItem: MenuItem, onViewFound: (View) -> Unit) {
        titleBar.doOnPreDraw {
            if (button.hasComponent()) onViewFound(menuItem.actionView!!)
            val buttonsLayout = ViewUtils.findChildByClass(titleBar, ActionMenuView::class.java)
            val buttons = ViewUtils.findChildrenByClass(buttonsLayout, TextView::class.java)
            for (view in buttons) {
                if (isTextualButtonView(view) || isIconButtonView(view, menuItem)) {
                    onViewFound(view)
                }
            }
        }
    }

    private fun isIconButtonView(view: TextView, menuItem: MenuItem) = button.icon.hasValue() && ArrayUtils.contains(view.compoundDrawables, menuItem.icon)
    private fun isTextualButtonView(view: TextView) = button.text.hasValue() && button.text.get() == view.text.toString()
    private fun loadIcon(callback: ImageLoader.ImagesLoadingListener) {
        iconResolver.resolve(button) { drawable: Drawable? -> callback.onComplete(drawable!!) }
    }

    fun applyNavigationIcon(titleBar: TitleBar, onPress: (String) -> Unit) {
        iconResolver.resolve(button) { icon: Drawable ->
            setIconColor(icon)
            titleBar.setNavigationOnClickListener { onPress(button.id) }
            titleBar.navigationIcon = icon
            setLeftButtonTestId(titleBar)
            if (button.accessibilityLabel.hasValue()) titleBar.navigationContentDescription = button.accessibilityLabel.get()
        }
    }

    private fun setIconColor(icon: Drawable) {
        if (button.disableIconTint.isTrue) return
        if (button.enabled.isTrueOrUndefined && button.color.hasValue()) {
            tint(icon, button.color.get())
        } else if (button.enabled.isFalse) {
            tint(icon, button.disabledColor[Color.LTGRAY])
        }
    }

    private fun setLeftButtonTestId(toolbar: Toolbar) {
        if (!button.testId.hasValue()) return
        toolbar.post {
            ViewUtils.findChildByClass(toolbar, ImageButton::class.java)?.let {
                it.tag = button.testId.get()
            }
        }
    }
}
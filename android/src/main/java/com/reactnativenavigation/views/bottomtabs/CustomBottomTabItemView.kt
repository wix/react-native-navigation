package com.reactnativenavigation.views.bottomtabs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.widget.FrameLayout
import com.reactnativenavigation.react.ReactView

/**
 * Hosts a [ReactView] that renders a user-supplied React component as a
 * bottom tab item. The view sits on top of the native AHBottomNavigation tab
 * cell and forwards touches through to the underlying cell so native
 * selection, ripple and `selectTabOnPress: false` keep working.
 *
 * The hosted component receives the following props at creation:
 * `componentId`, `tabIndex`, `selected`, `badge`. Selection updates are
 * pushed via [setSelected]; badge updates via [setBadge].
 */
@SuppressLint("ViewConstructor")
class CustomBottomTabItemView(
    context: Context,
    val componentId: String,
    val componentName: String,
    val tabIndex: Int,
    initialSelected: Boolean,
    initialBadge: String?
) : FrameLayout(context) {

    val reactView: ReactView = ReactView(context, componentId, componentName)
    private var isCurrentlySelected: Boolean = initialSelected
    private var badge: String? = initialBadge

    init {
        addView(reactView)
        reactView.isClickable = false
        reactView.isFocusable = false
        isClickable = false
        isFocusable = false
        pushProps()
    }

    /**
     * Touches must always reach the underlying AHBottomNavigation cell so
     * that native selection, ripple, accessibility focus and
     * `selectTabOnPress: false` keep working. Returning false here makes
     * this view completely transparent to touch input and prevents any
     * `Touchable*` rendered inside the React tree from swallowing taps.
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean = false

    fun setItemSelected(selected: Boolean) {
        if (this.isCurrentlySelected == selected) return
        this.isCurrentlySelected = selected
        pushProps()
    }

    fun setBadge(badge: String?) {
        if (this.badge == badge) return
        this.badge = badge
        pushProps()
    }

    private fun pushProps() {
        val bundle = Bundle().apply {
            putString("componentId", componentId)
            putInt("tabIndex", tabIndex)
            putBoolean("selected", isCurrentlySelected)
            if (badge != null) putString("badge", badge)
        }
        reactView.setProps(bundle)
    }
}

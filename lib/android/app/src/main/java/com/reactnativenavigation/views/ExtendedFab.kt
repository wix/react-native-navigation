package com.reactnativenavigation.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.reactnativenavigation.anim.FabAnimator
import com.reactnativenavigation.anim.FabCollapseBehaviour
import com.reactnativenavigation.interfaces.FloatingButton
import com.reactnativenavigation.interfaces.ScrollEventListener
import com.reactnativenavigation.parse.params.Colour
import com.reactnativenavigation.utils.ImageLoader
import com.reactnativenavigation.utils.ImageLoadingListenerAdapter

class ExtendedFab(context: Context?, id: String) : ExtendedFloatingActionButton(context!!), FabAnimator, FloatingButton {
    override var fabId = ""
    private val collapseBehaviour: FabCollapseBehaviour
    override fun applyIcon(icon: String, color: Colour) {
        ImageLoader().loadIcons(context, listOf(icon), object : ImageLoadingListenerAdapter() {
            override fun onComplete(drawable: List<Drawable>) {
                if (color.hasValue()) drawable[0].colorFilter = PorterDuffColorFilter(color.get(), PorterDuff.Mode.SRC_IN)
                setIcon(drawable[0])
            }

            override fun onError(error: Throwable?) {
                error!!.printStackTrace()
            }
        })
    }

    override fun show() {
        super<ExtendedFloatingActionButton>.show()
    }

    override fun bringToFront() {
        super<ExtendedFloatingActionButton>.bringToFront()
    }

    override fun setRippleColor(color: ColorStateList?) {
        super<ExtendedFloatingActionButton>.setRippleColor(color)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val fab = other as Fab
        return fabId == fab.fabId
    }

    override fun hide() {
        super<ExtendedFloatingActionButton>.hide()
    }

    override fun hashCode(): Int {
        return fabId.hashCode()
    }

    override fun enableCollapse(scrollEventListener: ScrollEventListener) {
        collapseBehaviour.enableCollapse(scrollEventListener)
    }

    override fun disableCollapse() {
        collapseBehaviour.disableCollapse()
    }

    override fun setBackgroundColor(get: Int) {
        super<ExtendedFloatingActionButton>.setBackgroundColor(get)
    }

    override fun setText(string: String?) {
        super<ExtendedFloatingActionButton>.setText(string)
    }

    override fun setExtended(status : Boolean) {
        super<ExtendedFloatingActionButton>.setExtended(status)
    }

    override fun setOnClickListener(function: (View?) -> Unit) {
        super<ExtendedFloatingActionButton>.setOnClickListener(function)
    }

    init {
        collapseBehaviour = FabCollapseBehaviour(this)
        fabId = id
    }
}
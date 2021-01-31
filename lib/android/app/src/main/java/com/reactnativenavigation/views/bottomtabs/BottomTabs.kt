package com.reactnativenavigation.views.bottomtabs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.reactnativenavigation.R
import com.reactnativenavigation.options.LayoutDirection
import com.reactnativenavigation.utils.CollectionUtils
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.utils.ViewUtils
import java.util.*

@SuppressLint("ViewConstructor")
open class BottomTabs(context: Context?) : AHBottomNavigation(context) {
    private var itemsCreationEnabled = true
    private var shouldCreateItems = true
    private val onItemCreationEnabled: MutableList<Runnable> = ArrayList()
    private val shadowLayer: Drawable? = null
    private val borderView = View(context).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
            gravity = Gravity.TOP
        }
        setBackgroundColor(Color.BLUE)
    }

    init {
        id = R.id.bottomTabs
        this.addView(borderView, 0)
    }

    fun disableItemsCreation() {
        itemsCreationEnabled = false
    }

    fun enableItemsCreation() {
        itemsCreationEnabled = true
        if (shouldCreateItems) {
            shouldCreateItems = false
            createItems()
            CollectionUtils.forEach(onItemCreationEnabled) { obj: Runnable -> obj.run() }
            onItemCreationEnabled.clear()
        }
    }

    public override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (hasItemsAndIsMeasured(w, h, oldw, oldh)) createItems()
    }

    public override fun createItems() {
        if (itemsCreationEnabled) {
            superCreateItems()
        } else {
            shouldCreateItems = true
        }
    }

    open fun superCreateItems() {
        super.createItems()
    }

    override fun setCurrentItem(@IntRange(from = 0) position: Int) {
        setCurrentItem(position, true)
    }

    override fun setCurrentItem(@IntRange(from = 0) position: Int, useCallback: Boolean) {
        if (itemsCreationEnabled) {
            super.setCurrentItem(position, useCallback)
        } else {
            onItemCreationEnabled.add(Runnable { super.setCurrentItem(position, useCallback) })
        }
    }

    override fun setTitleState(titleState: TitleState) {
        if (getTitleState() != titleState) super.setTitleState(titleState)
    }

    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(color)
        if (defaultBackgroundColor != color) defaultBackgroundColor = color
    }

    override fun hideBottomNavigation(withAnimation: Boolean) {
        super.hideBottomNavigation(withAnimation)
        if (!withAnimation) visibility = View.GONE
    }

    fun setText(index: Int, text: String) {
        val item = getItem(index)
        if (item.getTitle(context) != text) {
            item.setTitle(text)
            refresh()
        }
    }

    fun setIcon(index: Int, icon: Drawable) {
        val item = getItem(index)
        if (item.getDrawable(context) != icon) {
            item.setIcon(icon)
            refresh()
        }
    }

    fun setSelectedIcon(index: Int, icon: Drawable) {
        val item = getItem(index)
        if (item.getDrawable(context) != icon) {
            item.setSelectedIcon(icon)
            refresh()
        }
    }

    fun setLayoutDirection(direction: LayoutDirection) {
        val tabsContainer = ViewUtils.findChildByClass(this, LinearLayout::class.java)
        if (tabsContainer != null) tabsContainer.layoutDirection = direction.get()
    }

    private fun hasItemsAndIsMeasured(w: Int, h: Int, oldw: Int, oldh: Int): Boolean {
        return w != 0 && h != 0 && (w != oldw || h != oldh) && itemsCount > 0
    }


    companion object {

    }


}
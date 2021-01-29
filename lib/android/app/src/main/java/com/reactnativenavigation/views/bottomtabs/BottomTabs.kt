package com.reactnativenavigation.views.bottomtabs

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.reactnativenavigation.R
import com.reactnativenavigation.options.LayoutDirection
import com.reactnativenavigation.utils.CollectionUtils
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
        visibility = View.GONE
    }

    init {
        id = R.id.bottomTabs
        addView(borderView,0)
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

    override fun createItems() {
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

    fun setBorderColor(@ColorInt color: Int) {
        this.borderView.setBackgroundColor(color)
    }
    fun setBorderWidth(width: Int) {
        if(width<=0){
            this.borderView.visibility = View.GONE
        }else{
            this.borderView.updateLayoutParams<LayoutParams> {
                topMargin = -height
            }
        }
    }

    companion object {
        fun generateBackgroundWithShadow(view: View, @ColorInt backgroundColor: Int,
                                         cornerRadius: Float,
                                         shadowColor: Int,
                                         elevation: Int,
                                         shadowGravity: Int): Drawable {
            val outerRadius = floatArrayOf(cornerRadius, cornerRadius, cornerRadius,
                    cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                    cornerRadius)
            val backgroundPaint = Paint()
            backgroundPaint.style = Paint.Style.FILL
            backgroundPaint.setShadowLayer(cornerRadius, 0f, 0f, 0)
            val shapeDrawablePadding = Rect()
            shapeDrawablePadding.left = elevation
            shapeDrawablePadding.right = elevation
            val DY: Int
            when (shadowGravity) {
                Gravity.CENTER -> {
                    shapeDrawablePadding.top = elevation
                    shapeDrawablePadding.bottom = elevation
                    DY = 0
                }
                Gravity.TOP -> {
                    shapeDrawablePadding.top = elevation * 2
                    shapeDrawablePadding.bottom = elevation
                    DY = -1 * elevation / 3
                }
                Gravity.BOTTOM -> {
                    shapeDrawablePadding.top = elevation
                    shapeDrawablePadding.bottom = elevation * 2
                    DY = elevation / 3
                }
                else -> {
                    shapeDrawablePadding.top = elevation
                    shapeDrawablePadding.bottom = elevation * 2
                    DY = elevation / 3
                }
            }
            val shapeDrawable = ShapeDrawable()
            shapeDrawable.setPadding(shapeDrawablePadding)
            shapeDrawable.paint.color = backgroundColor
            shapeDrawable.paint.setShadowLayer(cornerRadius / 3, 0f, DY.toFloat(), shadowColor)
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, shapeDrawable.paint)
            shapeDrawable.shape = RoundRectShape(outerRadius, null, null)
            val drawable = LayerDrawable(arrayOf<Drawable>(shapeDrawable))
            drawable.setLayerInset(0, elevation, elevation * 2, elevation, elevation * 2)
            return drawable
        }
    }


}
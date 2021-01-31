package com.reactnativenavigation.views.bottomtabs

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import com.reactnativenavigation.options.ShadowOptions
import com.reactnativenavigation.utils.UiUtils

class BottomTabsContainer(context: Context) : LinearLayout(context) {
    var bottomTabs = BottomTabs(context)
        @RestrictTo(RestrictTo.Scope.TESTS) set(value) {
            this.removeView(field)
            addView(value, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
            field = value
        }

    private val topOutLineView = View(context)

    init {
        orientation = VERTICAL
        this.topOutLineView.setBackgroundColor(Color.CYAN)
        addView(topOutLineView, LayoutParams(LayoutParams.MATCH_PARENT, 0))
        addView(bottomTabs, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }

    fun setTopOutlineWidth(width: Int) {
        this.topOutLineView.layoutParams = (this.topOutLineView.layoutParams as LayoutParams).apply {
            height = width
        }
    }

    fun setTopOutlineColor(@ColorInt color: Int) {
        this.topOutLineView.setBackgroundColor(color)
    }

    fun setShadowOptions(shadowOptions: ShadowOptions) {
        val color = shadowOptions.color.get()
        val radius = shadowOptions.radius.get().toFloat()
        val opacity = shadowOptions.opacity.get(0.0)
     this.background = generateBackgroundWithShadow(this,color,radius,color,10)
    }

}


private fun generateBackgroundWithShadow(view: View, @ColorInt backgroundColor: Int,
                                         cornerRadius: Float,
                                         shadowColor: Int,
                                         elevation: Int): Drawable {
    val outerRadius = floatArrayOf(cornerRadius, cornerRadius, cornerRadius,
            cornerRadius, cornerRadius, cornerRadius, cornerRadius,
            cornerRadius)
    val shapeDrawablePadding = Rect()
    shapeDrawablePadding.top = elevation * 2
    val DY = UiUtils.dpToPx(view.context,-2).toFloat()
    val shapeDrawable = ShapeDrawable()
    shapeDrawable.setPadding(shapeDrawablePadding)
    shapeDrawable.paint.color = shadowColor
    shapeDrawable.paint.setShadowLayer(cornerRadius, 0f, DY, shadowColor)
    shapeDrawable.shape = RoundRectShape(outerRadius, null, null)
    val drawable = LayerDrawable(arrayOf<Drawable>(shapeDrawable))
    drawable.setLayerInset(0, 0, 0, 0, 0)
    return drawable
}

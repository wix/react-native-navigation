package com.reactnativenavigation.views.bottomtabs

import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RestrictTo
import com.reactnativenavigation.utils.UiUtils
import kotlin.math.roundToInt

internal const val DEFAULT_SHADOW_COLOR = Color.BLACK
internal const val DEFAULT_SHADOW_OPACITY = 0.85f
internal const val DEFAULT_SHADOW_RADIUS = 5f
private const val SHADOW_DY = 3f
internal const val SHADOW_HEIGHT_DP = 4

internal const val DEFAULT_TOP_OUTLINE_SIZE_PX = 1
internal const val DEFAULT_TOP_OUTLINE_COLOR = Color.DKGRAY

class BottomTabsContainer(context: Context) : LinearLayout(context) {
    var bottomTabs = BottomTabs(context)
        @RestrictTo(RestrictTo.Scope.TESTS) set(value) {
            this.removeView(field)
            addView(value, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
            field = value
        }
    open var topOutLineView = TopOutlineView(context)
        @RestrictTo(RestrictTo.Scope.TESTS, RestrictTo.Scope.SUBCLASSES) get
        @RestrictTo(RestrictTo.Scope.TESTS) set(value) {
            this.removeView(field)
            addView(value, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
            field = value
        }
    open var shadowRectView = ShadowRectView(context)
        @RestrictTo(RestrictTo.Scope.TESTS, RestrictTo.Scope.SUBCLASSES) get
        @RestrictTo(RestrictTo.Scope.TESTS) set(value) {
            this.removeView(field)
            addView(value, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
            field = value
        }

    init {
        this.id = View.generateViewId()
        this.clipChildren = false
        this.clipToPadding = false
        orientation = VERTICAL
        setTopOutLineColor(DEFAULT_TOP_OUTLINE_COLOR)
        this.shadowRectView.visibility = View.GONE
        this.topOutLineView.visibility = View.GONE

        addView(shadowRectView, LayoutParams(LayoutParams.MATCH_PARENT, UiUtils.dpToPx(context, SHADOW_HEIGHT_DP)))
        addView(topOutLineView, LayoutParams(LayoutParams.MATCH_PARENT, DEFAULT_TOP_OUTLINE_SIZE_PX))
        addView(bottomTabs, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }

    fun setTopOutlineWidth(width: Int) {
        this.topOutLineView.layoutParams = (this.topOutLineView.layoutParams as LayoutParams).apply {
            height = width
        }
    }

    fun setTopOutLineColor(color: Int) {
        this.topOutLineView.setBackgroundColor(color)
    }

    fun setShadowColor(color: Int) {
        this.shadowRectView.setColor(color)
    }

    fun setShadowRadius(radius: Float) {
        this.shadowRectView.setRadius(radius)
    }

    fun setShadowOpacity(opacity: Float) {
        this.shadowRectView.setOpacity(opacity)
    }

    fun showShadow() {
        this.shadowRectView.visibility = View.VISIBLE
        this.shadowRectView.invalidate()
    }

    fun clearShadow() {
        if (shadowRectView.visibility != View.GONE) {
            this.shadowRectView.reset()
            this.shadowRectView.visibility = View.GONE
        }
    }

    fun showTopLine() {
        this.topOutLineView.visibility = View.VISIBLE
    }

    fun clearTopOutline() {
        this.topOutLineView.visibility = View.GONE
    }

    fun revealTopLineAndShadow() {
        topOutLineView.alpha = 1f
        shadowRectView.alpha = 1f
    }

    fun hideTopLineAndShadow() {
        topOutLineView.alpha = 0f
        shadowRectView.alpha = 0f
    }
}

class TopOutlineView(context: Context) : View(context)


class ShadowRectView(context: Context) : View(context) {
    private val path = Path()

    private val corners = floatArrayOf(
            0f, 0f,   // Top left radius in px
            0f, 0f,   // Top right radius in px
            0f, 0f,     // Bottom right radius in px
            0f, 0f      // Bottom left radius in px
    )
    private val shadowPaint: Paint = Paint().apply {
        this.style = Paint.Style.FILL_AND_STROKE
        this.isAntiAlias = true
    }
    private var shadowLayerRadius: Float = DEFAULT_SHADOW_RADIUS
    private var shadowLayerColor: Int = DEFAULT_SHADOW_COLOR

    private val mainRect = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint)
        reset()
    }

    fun setColor(color: Int) {
        this.shadowLayerColor = color
        this.shadowPaint.color = color
        this.shadowPaint.setShadowLayer(
                shadowLayerRadius,
                0f,
                SHADOW_DY,
                color
        )
    }

    fun setRadius(radius: Float) {
        this.shadowLayerRadius = radius
        this.shadowPaint.setShadowLayer(
                radius,
                0f,
                SHADOW_DY,
                this.shadowLayerColor
        )

        this.shadowPaint.maskFilter = BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)

        corners[0] = radius
        corners[1] = radius
        corners[2] = radius
        corners[3] = radius
    }


    fun setOpacity(opacity: Float) {
        this.shadowPaint.alpha = (opacity * 255).roundToInt()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        path.reset()
        mainRect.right = this.width.toFloat()
        val mheight = this.height.toFloat()
        mainRect.top = mheight - SHADOW_DY
        mainRect.bottom = mheight
        path.addRoundRect(mainRect, corners, Path.Direction.CW)
        canvas?.drawPath(path, shadowPaint)
    }

    fun reset() {
        setColor(DEFAULT_SHADOW_COLOR)
        setOpacity(DEFAULT_SHADOW_OPACITY)
        setRadius(DEFAULT_SHADOW_RADIUS)
    }


}

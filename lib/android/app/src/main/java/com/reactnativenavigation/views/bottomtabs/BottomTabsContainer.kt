package com.reactnativenavigation.views.bottomtabs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.LinearLayout
import androidx.annotation.RestrictTo
import androidx.core.graphics.ColorUtils
import com.reactnativenavigation.options.params.Fraction
import com.reactnativenavigation.utils.UiUtils.dpToPx
import eightbitlab.com.blurview.BlurTarget
import eightbitlab.com.blurview.BlurView
import kotlin.math.roundToInt


internal const val DEFAULT_SHADOW_COLOR = Color.BLACK
internal const val DEFAULT_SHADOW_RADIUS = 10
internal const val DEFAULT_SHADOW_DISTANCE = 15f
internal const val DEFAULT_SHADOW_ANGLE = 270f

internal const val DEFAULT_TOP_OUTLINE_SIZE_PX = 1
internal const val DEFAULT_TOP_OUTLINE_COLOR = Color.DKGRAY

class TopOutlineView(context: Context) : View(context)

@SuppressLint("ViewConstructor")
class BottomTabsContainer(context: Context, val bottomTabs: BottomTabs) : ShadowLayout(context) {

    private val blurringView: BlurView
    private var blurEnabled: Boolean? = null

    var topOutLineView = TopOutlineView(context)
        @RestrictTo(RestrictTo.Scope.TESTS, RestrictTo.Scope.SUBCLASSES) get
        @RestrictTo(RestrictTo.Scope.TESTS) set(value) {
            this.removeView(field)
            addView(value, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
            field = value
        }

    init {
        this.isShadowed = false
        this.id = View.generateViewId()
        shadowAngle = DEFAULT_SHADOW_ANGLE
        shadowDistance = DEFAULT_SHADOW_DISTANCE
        shadowColor = DEFAULT_SHADOW_COLOR
        val linearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            addView(topOutLineView, LayoutParams(LayoutParams.MATCH_PARENT, DEFAULT_TOP_OUTLINE_SIZE_PX))
            addView(bottomTabs, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        }

        this.clipChildren = false
        this.clipToPadding = false
        setTopOutLineColor(DEFAULT_TOP_OUTLINE_COLOR)
        this.topOutLineView.visibility = View.GONE

        blurringView = BlurView(context).apply {
            setBlurRadius(25f)
            setBlurEnabled(false)
            addView(linearLayout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        }
        addView(blurringView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override var shadowRadius: Float
        get() = super.shadowRadius
        set(value) {
            super.shadowRadius = DEFAULT_SHADOW_RADIUS + value
        }

    fun setTopOutlineWidth(width: Int) {
        this.topOutLineView.layoutParams = (this.topOutLineView.layoutParams as LinearLayout.LayoutParams).apply {height = width}
    }

    fun setTopOutLineColor(color: Int) {
        this.topOutLineView.setBackgroundColor(color)
    }

    fun setShadowOpacity(opacity: Float) {
        shadowColor = ColorUtils.setAlphaComponent(shadowColor, (opacity * 0xFF).roundToInt())
    }

    fun setRoundedCorners(radius: Float) {
        this.outlineProvider = object: ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, radius)
            }
        }
        this.clipToOutline = true
    }

    fun setBlurSurface(blurSurface: BlurTarget) {
        blurringView.setupWith(blurSurface)

        if (blurEnabled != true) {
            blurringView.setBlurEnabled(false)
        }
    }

    fun enableBackgroundBlur() {
        blurringView.setBlurEnabled(true)
        blurEnabled = true
    }

    fun disableBackgroundBlur() {
        blurringView.setBlurEnabled(false)
        blurEnabled = false
    }

    fun clearRoundedCorners() {
        this.outlineProvider = null
        this.clipToOutline = true
    }

    fun showShadow() {
        isShadowed = true
    }

    fun clearShadow() {
        isShadowed = false
    }

    fun showTopLine() {
        this.topOutLineView.visibility = View.VISIBLE
    }

    fun clearTopOutline() {
        this.topOutLineView.visibility = View.GONE
    }

    fun revealTopOutline() {
        topOutLineView.alpha = 1f
    }

    fun hideTopOutLine() {
        topOutLineView.alpha = 0f
    }

    fun setElevation(elevation: Fraction) {
        setElevation(dpToPx(context, elevation.get().toFloat()))
    }
}

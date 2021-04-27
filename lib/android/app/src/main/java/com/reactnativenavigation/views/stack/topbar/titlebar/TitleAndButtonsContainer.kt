package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.FontOptions
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.parsers.TypefaceLoader
import com.reactnativenavigation.utils.CompatUtils
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.utils.ViewUtils

const val DEFAULT_LEFT_MARGIN = 16
const val SYSTEM_ACTION_BUTTON_SIZE_DP = 48


class TitleAndButtonsContainer(context: Context) : RelativeLayout(context) {

    private val titleMeasurer = TitleComponentMeasurer(context)

    private var component: View? = null

    private var titleComponentAlignment: Alignment = Alignment.Default
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private val titleSubTitleBar = TitleSubTitleLayout(context).apply {
        id = CompatUtils.generateViewId()
//        setBackgroundColor(Color.GREEN)
    }

    val leftToolbar = RNNToolbar(context).apply {
        this.id = CompatUtils.generateViewId()
//        setBackgroundColor(Color.RED)
    }
    val rightToolbar = RNNToolbar(context).apply {
        this.id = CompatUtils.generateViewId()
    }

    init {
        this.addView(leftToolbar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            addRule(ALIGN_PARENT_START)
            addRule(CENTER_VERTICAL)
        })
        this.addView(titleSubTitleBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        this.addView(rightToolbar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            addRule(ALIGN_PARENT_END)
            addRule(CENTER_VERTICAL)
        })
    }

    fun setComponent(component: View, alignment: Alignment = Alignment.Default) {
        if (this.component == component) return
        clear()
        this.component = component
        this.addView(this.component, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        titleComponentAlignment = alignment
    }

    fun setTitle(title: CharSequence?) {
        clearComponent()
        this.titleSubTitleBar.visibility = View.VISIBLE
        this.titleSubTitleBar.setTitle(title)
    }

    fun setSubtitle(title: CharSequence?) {
        clearComponent()
        this.titleSubTitleBar.visibility = View.VISIBLE
        this.titleSubTitleBar.setSubtitle(title)
    }

    fun setTitleBarAlignment(alignment: Alignment) {
        titleComponentAlignment = alignment
    }

    override fun setLayoutDirection(layoutDirection: Int) {
        this.titleSubTitleBar.layoutDirection = layoutDirection
        this.rightToolbar.layoutDirection = layoutDirection
        this.leftToolbar.layoutDirection = layoutDirection
        super.setLayoutDirection(layoutDirection)
    }

    fun setSubTitleTextAlignment(alignment: Alignment) = this.titleSubTitleBar.setSubTitleAlignment(alignment)

    fun setTitleTextAlignment(alignment: Alignment) = this.titleSubTitleBar.setTitleAlignment(alignment)

    fun setBackgroundColor(color: Colour) = if (color.hasValue()) setBackgroundColor(color.get()) else Unit

    fun setTitleFontSize(size: Float) = this.titleSubTitleBar.setTitleFontSize(size)

    fun setTitleTypeface(typefaceLoader: TypefaceLoader, font: FontOptions) = this.titleSubTitleBar.setTitleTypeface(typefaceLoader, font)

    fun setSubtitleTypeface(typefaceLoader: TypefaceLoader, font: FontOptions) = this.titleSubTitleBar.setSubtitleTypeface(typefaceLoader, font)

    fun setSubtitleFontSize(size: Float) = this.titleSubTitleBar.setSubtitleFontSize(size)

    fun setSubtitleColor(@ColorInt color: Int) = this.titleSubTitleBar.setSubtitleTextColor(color)

    fun setTitleColor(@ColorInt color: Int) = this.titleSubTitleBar.setTitleTextColor(color)

    fun getTitle(): String = this.titleSubTitleBar.getTitle()

    fun setHeight(height: Int) {
        val pixelHeight = UiUtils.dpToPx(context, height)
        if (this.layoutParams != null) {
            if (pixelHeight == layoutParams.height) return
            val lp = layoutParams
            lp.height = pixelHeight
            this@TitleAndButtonsContainer.layoutParams = lp
        } else {
            this.layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, pixelHeight)
        }
    }

    fun setTopMargin(topMargin: Int) {
        val pixelTopMargin = UiUtils.dpToPx(context, topMargin)
        if (layoutParams != null) {
            if (layoutParams is MarginLayoutParams) {
                val lp = layoutParams as MarginLayoutParams
                if (lp.topMargin == pixelTopMargin) return
                lp.topMargin = pixelTopMargin
                this@TitleAndButtonsContainer.layoutParams = lp
            }
        }
    }

    fun clear() {
        if (this.childCount > 0 && this.component == null) {
            this.titleSubTitleBar.visibility = View.INVISIBLE
        }
        clearComponent()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        layoutChildren(l, r, t, b)
    }

    private fun layoutChildren(l: Int, r: Int, t: Int, b: Int) {
        val titleComponent = getTitleComponent()
        val isCenter = titleComponentAlignment == Alignment.Center
        val parentWidth = r - l
        val leftBarMeasuredWidth = leftToolbar.measuredWidth
        val rightBarMeasuredWidth = rightToolbar.measuredWidth
        titleMeasurer.setRawMeasurements(parentWidth, titleComponent.measuredWidth, leftBarMeasuredWidth,
                rightBarMeasuredWidth, isCenter)
        if (isCenter) {
            val titleLeft = titleMeasurer.resolveTitleLeft()
            val titleRight = titleMeasurer.resolveTitleRight()
            titleComponent.layout(titleLeft, t, titleRight, b)
        } else {
            titleComponent.layout(leftToolbar.right, t, rightToolbar.left, b)
        }
        leftToolbar.layout(l, t, leftBarMeasuredWidth, b)
        rightToolbar.layout(r - rightBarMeasuredWidth, t, r, b)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            measureTitleComponentExact(parentWidth, heightMeasureSpec)
        }
    }

    private fun measureTitleComponentExact(parentWidth: Int, heightMeasureSpec: Int) {
        val titleComponent = getTitleComponent()

        rightToolbar.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), heightMeasureSpec)
        leftToolbar.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), heightMeasureSpec)
        titleComponent.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), heightMeasureSpec)

        titleMeasurer.setRawMeasurements(parentWidth, titleComponent.measuredWidth, leftToolbar.measuredWidth,
                rightToolbar.measuredWidth, titleComponentAlignment == Alignment.Center)

        val titleLeft = titleMeasurer.resolveTitleLeft()
        val titleRight = titleMeasurer.resolveTitleRight()
        val titleMeasuredWidth = titleRight - titleLeft


        titleComponent.measure(MeasureSpec.makeMeasureSpec(titleMeasuredWidth, MeasureSpec.EXACTLY), heightMeasureSpec)
        leftToolbar.measure(MeasureSpec.makeMeasureSpec(titleMeasurer.leftBarWidth, MeasureSpec.EXACTLY), heightMeasureSpec)
        rightToolbar.measure(MeasureSpec.makeMeasureSpec(titleMeasurer.rightBarWidth, MeasureSpec.EXACTLY), heightMeasureSpec)
    }


    private fun clearComponent() = this.component?.let { ViewUtils.removeFromParent(it); this.component = null; }

    @RestrictTo(RestrictTo.Scope.TESTS, RestrictTo.Scope.LIBRARY)
    fun getTitleComponent() = this.component ?: this.titleSubTitleBar

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getComponent() = this.component

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getTitleSubtitleBar() = this.titleSubTitleBar
}
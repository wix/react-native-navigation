package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.FontOptions
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.parsers.TypefaceLoader
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.utils.ViewUtils
import kotlin.math.roundToInt


class TitleAndButtonsContainer(context: Context) : ViewGroup(context) {
    private var component: View? = null
    private var titleComponentAlignment: Alignment = Alignment.Default
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private var titleSubTitleBar = TitleSubTitleLayout(context)
    var leftButtonsBar = ButtonsBar(context)
        private set
    var rightButtonsBar = ButtonsBar(context)
        private set

    init {
        this.addView(leftButtonsBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
        this.addView(titleSubTitleBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
        this.addView(rightButtonsBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
    }

    fun setComponent(component: View, alignment: Alignment = Alignment.Default) {
        if (this.component == component) return
        clear()
        this.component = component
        this.addView(this.component, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT))
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
        super.setLayoutDirection(layoutDirection)
        this.component?.layoutDirection = layoutDirection
        this.titleSubTitleBar.layoutDirection = layoutDirection
        this.rightButtonsBar.layoutDirection = layoutDirection
        this.leftButtonsBar.layoutDirection = layoutDirection
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
        layoutChildren(l, r, t, b)
    }

    private fun layoutChildren(l: Int, r: Int, t: Int, b: Int) {
        val titleComponent = getTitleComponent()
        val isCenter = titleComponentAlignment == Alignment.Center
        val parentWidth = r - l
        val parentHeight = b - t
        val isRTL = layoutDirection == View.LAYOUT_DIRECTION_RTL
        val (titleLeft, titleRight) = resolveTitleBoundsLimit(
                parentWidth,
                titleComponent.measuredWidth,
                leftButtonsBar.measuredWidth,
                rightButtonsBar.measuredWidth,
                isCenter,
                isRTL
        )
        val (leftButtonsLeft, leftButtonsRight) = resolveLeftToolbarBounds(parentWidth, leftButtonsBar.measuredWidth, isRTL)
        val (rightButtonsLeft, rightButtonsRight) = resolveRightToolbarBounds(parentWidth, rightButtonsBar.measuredWidth,
                isRTL)

        leftButtonsBar.layout(leftButtonsLeft, t, leftButtonsRight, b)
        rightButtonsBar.layout(rightButtonsLeft, t, rightButtonsRight, b)
        titleComponent.layout(titleLeft, (parentHeight / 2f - titleComponent.measuredHeight / 2f).roundToInt(), titleRight,
                (parentHeight / 2f + titleComponent.measuredHeight / 2f).roundToInt())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            measureTitleComponentExact(parentWidth, parentHeight)
        }
        setMeasuredDimension(parentWidth, parentHeight)
    }


    private fun measureTitleComponentExact(parentWidth: Int, parentHeight: Int) {
        val titleComponent = this.getTitleComponent()
        component?.layoutDirection = View.LAYOUT_DIRECTION_LTR
        rightButtonsBar.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), MeasureSpec
                .makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY))
        leftButtonsBar.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), MeasureSpec
                .makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY))

        titleComponent.measure(makeTitleWidthMeasureSpec(parentWidth, titleComponentAlignment == Alignment.Center),
                MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.AT_MOST))
    }

    private fun makeTitleWidthMeasureSpec(parentWidth: Int, isCenter: Boolean): Int {
        return if (isCenter) {
            MeasureSpec.makeMeasureSpec(parentWidth,
            MeasureSpec.AT_MOST)
        } else {
            MeasureSpec.makeMeasureSpec(parentWidth - rightButtonsBar.measuredWidth - leftButtonsBar.measuredWidth - 2 * DEFAULT_LEFT_MARGIN_PX, MeasureSpec.AT_MOST)
        }
    }

    private fun clearComponent() = this.component?.let { ViewUtils.removeFromParent(it); this.component = null; }

    @RestrictTo(RestrictTo.Scope.TESTS, RestrictTo.Scope.LIBRARY)
    fun getTitleComponent() = this.component ?: this.titleSubTitleBar

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getComponent() = this.component

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun setTitleSubtitleLayout(layout: TitleSubTitleLayout) {
        this.removeView(this.titleSubTitleBar)
        this.titleSubTitleBar = layout
        this.addView(this.titleSubTitleBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun setButtonsBars(left: ButtonsBar, right: ButtonsBar) {
        val leftLp = LayoutParams(leftButtonsBar.layoutParams)
        val rightLp = LayoutParams(rightButtonsBar.layoutParams)
        this.removeView(leftButtonsBar)
        this.removeView(rightButtonsBar)
        this.addView(left, leftLp)
        this.addView(right, rightLp)
        this.leftButtonsBar = left
        this.rightButtonsBar = right
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getTitleSubtitleBar() = this.titleSubTitleBar
}
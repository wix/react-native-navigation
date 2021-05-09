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
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.isRTL


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
    var leftButtonBar = ButtonBar(context)
        private set
    var rightButtonBar = ButtonBar(context)
        private set

    init {
        this.addView(leftButtonBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
        this.addView(titleSubTitleBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
        this.addView(rightButtonBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
    }

    fun setComponent(component: View, alignment: Alignment = Alignment.Default) {
        if (this.component == component) return
        clearCurrentTitle()
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
        super.setLayoutDirection(layoutDirection)
        this.component?.layoutDirection = layoutDirection
        this.titleSubTitleBar.layoutDirection = layoutDirection
        this.rightButtonBar.layoutDirection = layoutDirection
        this.leftButtonBar.layoutDirection = layoutDirection
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

    fun clearCurrentTitle() {
        this.titleSubTitleBar.clear()
        clearComponent()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val titleComponent = getTitleComponent()
        val isCenter = titleComponentAlignment == Alignment.Center
        val parentWidth = r - l
        val parentHeight = b - t
        val isRTL = isRTL()
        val titleWidth = titleComponent.measuredWidth
        val leftBarWidth = leftButtonBar.measuredWidth
        val rightBarWidth = rightButtonBar.measuredWidth

        val (titleLeft, titleRight) = resolveHorizontalTitleBoundsLimit(parentWidth, titleWidth, leftBarWidth, rightBarWidth, isCenter, isRTL)
        val (titleTop, titleBottom) = resolveVerticalTitleBoundsLimit(parentHeight, titleWidth)
        val (leftButtonsLeft, leftButtonsRight) = resolveLeftButtonsBounds(parentWidth, leftBarWidth, isRTL)
        val (rightButtonsLeft, rightButtonsRight) = resolveRightButtonsBounds(parentWidth, rightButtonBar.measuredWidth, isRTL)

        leftButtonBar.layout(leftButtonsLeft, t, leftButtonsRight, b)
        rightButtonBar.layout(rightButtonsLeft, t, rightButtonsRight, b)
        measureTextTitleEnsureTruncatedEndIfNeeded(titleRight, titleLeft, titleComponent, isCenter)
        titleComponent.layout(titleLeft, titleTop, titleRight, titleBottom)
    }

    private fun measureTextTitleEnsureTruncatedEndIfNeeded(titleRight: TitleRight, titleLeft: TitleLeft, titleComponent: View, isCenter: Boolean) {
        if (component == null && titleRight - titleLeft != titleComponent.measuredWidth) {
            val margin = if (isCenter) 0 else 2 * DEFAULT_LEFT_MARGIN_PX
            titleComponent.measure(MeasureSpec.makeMeasureSpec(titleRight - titleLeft + margin, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(titleComponent.measuredHeight, MeasureSpec.EXACTLY))
        }
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
        rightButtonBar.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), MeasureSpec
                .makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY))
        leftButtonBar.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), MeasureSpec
                .makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY))

        titleComponent.measure(makeTitleWidthMeasureSpec(parentWidth, titleComponentAlignment == Alignment.Center),
                MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.AT_MOST))
    }

    private fun makeTitleWidthMeasureSpec(parentWidth: Int, isCenter: Boolean): Int {
        return if (isCenter) {
            MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST)
        } else {
            MeasureSpec.makeMeasureSpec(parentWidth - rightButtonBar.measuredWidth - leftButtonBar.measuredWidth - 2 * DEFAULT_LEFT_MARGIN_PX, MeasureSpec.AT_MOST)
        }
    }

    private fun clearComponent() = this.component?.let { ViewUtils.removeFromParent(it); this.component = null; }

    internal fun getTitleComponent() = this.component ?: this.titleSubTitleBar

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getComponent() = this.component

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun setTitleSubtitleLayout(layout: TitleSubTitleLayout) {
        this.removeView(this.titleSubTitleBar)
        this.titleSubTitleBar = layout
        this.addView(this.titleSubTitleBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun setButtonBars(left: ButtonBar, right: ButtonBar) {
        val leftLp = LayoutParams(leftButtonBar.layoutParams)
        val rightLp = LayoutParams(rightButtonBar.layoutParams)
        this.removeView(leftButtonBar)
        this.removeView(rightButtonBar)
        this.addView(left, leftLp)
        this.addView(right, rightLp)
        this.leftButtonBar = left
        this.rightButtonBar = right
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getTitleSubtitleBar() = this.titleSubTitleBar
}

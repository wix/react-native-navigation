package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.FontOptions
import com.reactnativenavigation.options.params.ThemeColour
import com.reactnativenavigation.options.parsers.TypefaceLoader
import com.reactnativenavigation.utils.isRTL
import com.reactnativenavigation.utils.removeFromParent


class TitleAndButtonsContainer(context: Context) : ViewGroup(context) {
    private var component: View? = null
    private var titleComponentAlignment: Alignment = Alignment.Default
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    fun animateLeftButtons(animate:Boolean) {
        leftButtonBar.shouldAnimate = animate
    }
    fun animateRightButtons(animate:Boolean) {
        rightButtonBar.shouldAnimate = animate
    }

    private var titleSubTitleBar = TitleSubTitleLayout(context)
    var leftButtonBar = ButtonBar(context)
        private set
    var rightButtonBar = ButtonBar(context)
        private set

    init {
        addView(leftButtonBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
        addView(titleSubTitleBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
        addView(rightButtonBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
    }

    fun setComponent(component: View, alignment: Alignment = Alignment.Default) {
        if (this.component == component) return
        clearTitle()
        this.component = component
        addView(this.component, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        titleComponentAlignment = alignment
    }

    fun setTitle(title: CharSequence?) {
        clearComponent()
        titleSubTitleBar.visibility = View.VISIBLE
        titleSubTitleBar.setTitle(title)
    }

    fun setSubtitle(subtitle: CharSequence?) {
        clearComponent()
        titleSubTitleBar.visibility = View.VISIBLE
        titleSubTitleBar.setSubtitle(subtitle)
    }

    fun setTitleBarAlignment(alignment: Alignment) {
        titleComponentAlignment = alignment
    }

    override fun setLayoutDirection(layoutDirection: Int) {
        super.setLayoutDirection(layoutDirection)
        component?.layoutDirection = layoutDirection
        titleSubTitleBar.layoutDirection = layoutDirection
        rightButtonBar.layoutDirection = layoutDirection
        leftButtonBar.layoutDirection = if(isRTL()) View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL
    }

    fun setSubTitleTextAlignment(alignment: Alignment) = titleSubTitleBar.setSubTitleAlignment(alignment)

    fun setTitleTextAlignment(alignment: Alignment) = titleSubTitleBar.setTitleAlignment(alignment)

    fun setBackgroundColor(color: ThemeColour) = if (color.hasValue()) setBackgroundColor(color.get()) else Unit

    fun setTitleFontSize(size: Float) = titleSubTitleBar.setTitleFontSize(size)

    fun setTitleTypeface(typefaceLoader: TypefaceLoader, font: FontOptions) = titleSubTitleBar.setTitleTypeface(typefaceLoader, font)

    fun setSubtitleTypeface(typefaceLoader: TypefaceLoader, font: FontOptions) = titleSubTitleBar.setSubtitleTypeface(typefaceLoader, font)

    fun setSubtitleFontSize(size: Float) = titleSubTitleBar.setSubtitleFontSize(size)

    fun setSubtitleColor(@ColorInt color: Int) = titleSubTitleBar.setSubtitleTextColor(color)

    fun setTitleColor(@ColorInt color: Int) = titleSubTitleBar.setTitleTextColor(color)

    fun getTitle(): String = titleSubTitleBar.getTitle()

    fun clearTitle() {
        titleSubTitleBar.clear()
        clearComponent()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val titleComponent = getTitleComponent()
        val isCenter = titleComponentAlignment == Alignment.Center
        val containerWidth = r - l
        val containerHeight = b - t
        val isRTL = isRTL()
        val titleWidth = titleComponent.measuredWidth
        val titleHeight = titleComponent.measuredHeight
        val leftBarWidth = leftButtonBar.measuredWidth
        val rightBarWidth = rightButtonBar.measuredWidth

        val (titleLeft, titleRight) = resolveHorizontalTitleBoundsLimit(containerWidth, titleWidth, leftBarWidth, rightBarWidth, isCenter, isRTL)
        val (titleTop, titleBottom) = resolveVerticalTitleBoundsLimit(containerHeight, titleHeight)
        val (leftButtonsLeft, leftButtonsRight) = resolveLeftButtonsBounds(containerWidth, leftBarWidth, isRTL)
        val (rightButtonsLeft, rightButtonsRight) = resolveRightButtonsBounds(containerWidth, rightButtonBar.measuredWidth, isRTL)

        leftButtonBar.layout(leftButtonsLeft, t, leftButtonsRight, b)
        rightButtonBar.layout(rightButtonsLeft, t, rightButtonsRight, b)
        titleComponent.layout(titleLeft, titleTop, titleRight, titleBottom)

        measureTextTitleEnsureTruncatedEndIfNeeded(titleRight, titleLeft, titleComponent, isCenter)
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
        val containerWidth = MeasureSpec.getSize(widthMeasureSpec)
        val containerHeight = MeasureSpec.getSize(heightMeasureSpec)
        measureTitleComponentExact(mode, containerWidth, containerHeight)
        setMeasuredDimension(containerWidth, containerHeight)
    }


    private fun measureTitleComponentExact(mode: Int, containerWidth: Int, containerHeight: Int) {
        if (mode != MeasureSpec.EXACTLY) return
        val titleComponent = getTitleComponent()
        rightButtonBar.measure(MeasureSpec.makeMeasureSpec(containerWidth, MeasureSpec.AT_MOST), MeasureSpec
                .makeMeasureSpec(containerHeight, MeasureSpec.EXACTLY))
        leftButtonBar.measure(MeasureSpec.makeMeasureSpec(containerWidth, MeasureSpec.AT_MOST), MeasureSpec
                .makeMeasureSpec(containerHeight, MeasureSpec.EXACTLY))

        val rightBarWidth = rightButtonBar.measuredWidth
        val leftBarWidth = leftButtonBar.measuredWidth
        val isCenter = titleComponentAlignment == Alignment.Center
        val titleHeightMeasureSpec = MeasureSpec.makeMeasureSpec(containerHeight, MeasureSpec.AT_MOST)
        val titleWidthMeasureSpec = makeTitleAtMostWidthMeasureSpec(containerWidth, rightBarWidth, leftBarWidth, isCenter)
        titleComponent.measure(titleWidthMeasureSpec, titleHeightMeasureSpec)
    }

    private fun clearComponent() = component?.let { it.removeFromParent(); component = null; }

    internal fun getTitleComponent() = component ?: titleSubTitleBar

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getComponent() = component

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun setTitleSubtitleLayout(layout: TitleSubTitleLayout) {
        removeView(titleSubTitleBar)
        titleSubTitleBar = layout
        addView(titleSubTitleBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun setButtonBars(left: ButtonBar, right: ButtonBar) {
        val leftLp = LayoutParams(leftButtonBar.layoutParams)
        val rightLp = LayoutParams(rightButtonBar.layoutParams)
        removeView(leftButtonBar)
        removeView(rightButtonBar)
        addView(left, leftLp)
        addView(right, rightLp)
        leftButtonBar = left
        rightButtonBar = right
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getTitleSubtitleBar() = titleSubTitleBar
}

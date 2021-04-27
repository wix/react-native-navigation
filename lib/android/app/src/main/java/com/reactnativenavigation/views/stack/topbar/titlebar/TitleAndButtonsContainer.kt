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
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.utils.ViewUtils


class TitleAndButtonsContainer(context: Context) : RelativeLayout(context) {
    private var component: View? = null

    private var titleComponentAlignment: Alignment = Alignment.Default
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private val titleSubTitleBar = TitleSubTitleLayout(context)
    val leftButtonsBar = ButtonsBar(context)
    val rightButtonsBar = ButtonsBar(context)

    init {
        this.addView(leftButtonsBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            addRule(ALIGN_PARENT_START)
            addRule(CENTER_VERTICAL)
        })
        this.addView(titleSubTitleBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        this.addView(rightButtonsBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            addRule(ALIGN_PARENT_END)
            addRule(CENTER_VERTICAL)
        })
    }

    fun setComponent(component: View, alignment: Alignment = Alignment.Default) {
        if (this.component == component) return
        clear()
        this.component = component
        this.addView(this.component, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            addRule(CENTER_VERTICAL)
        })
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
        this.rightButtonsBar.layoutDirection = layoutDirection
        this.leftButtonsBar.layoutDirection = layoutDirection
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
        val isRTL = layoutDirection == View.LAYOUT_DIRECTION_RTL
        val (titleLeft, titleRight) = resolveTitleBoundsLimit(
                parentWidth,
                titleComponent.measuredWidth,
                leftButtonsBar.measuredWidth,
                rightButtonsBar.measuredWidth,
                isCenter,
                isRTL
        )
        if (!isCenter && isRTL) {
            titleComponent.layout(titleRight, t, titleLeft, b)
        } else {
            titleComponent.layout(titleLeft, t, titleRight, b)
        }
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

        rightButtonsBar.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), heightMeasureSpec)
        leftButtonsBar.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), heightMeasureSpec)
        titleComponent.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), heightMeasureSpec)

        val (titleLeft, titleRight) = resolveTitleBoundsLimit(
                parentWidth,
                titleComponent.measuredWidth,
                leftButtonsBar.measuredWidth,
                rightButtonsBar.measuredWidth,
                titleComponentAlignment == Alignment.Center,
                layoutDirection == View.LAYOUT_DIRECTION_RTL
        )

        titleComponent.measure(MeasureSpec.makeMeasureSpec(titleRight - titleLeft, MeasureSpec.EXACTLY), heightMeasureSpec)
    }


    private fun clearComponent() = this.component?.let { ViewUtils.removeFromParent(it); this.component = null; }

    @RestrictTo(RestrictTo.Scope.TESTS, RestrictTo.Scope.LIBRARY)
    fun getTitleComponent() = this.component ?: this.titleSubTitleBar

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getComponent() = this.component

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getTitleSubtitleBar() = this.titleSubTitleBar
}
package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import androidx.core.view.iterator
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.FontOptions
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.parsers.TypefaceLoader
import com.reactnativenavigation.utils.CompatUtils
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.logd
import kotlin.math.max
import kotlin.math.min

const val DEFAULT_LEFT_MARGIN = 16
const val SYSTEM_ACTION_BUTTON_SIZE_DP = 48

class MainToolBar(context: Context) : RelativeLayout(context) {

    private var component: View? = null

    private var titleComponentAlignment: Alignment = Alignment.Default
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    private val defaultMargin = UiUtils.dpToPx(context, DEFAULT_LEFT_MARGIN)
    private val minToolBarSize = UiUtils.dpToPx(context, SYSTEM_ACTION_BUTTON_SIZE_DP)

    private val titleSubTitleBar = TitleSubTitleLayout(context).apply {
        id = CompatUtils.generateViewId()
    }

    val leftButtonsBar = LeftButtonsBar(context).apply {
        this.id = CompatUtils.generateViewId()
    }
    val rightButtonsBar: RightButtonsBar = RightButtonsBar(context).apply {
        this.id = CompatUtils.generateViewId()
    }

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
        logd("setComponent with this.component:$${this.component?.id ?: -999}, newComponent:${component.id}, alignment:${alignment}")
        if (this.component == component) return
        clear()
        this.component = component
        this.addView(this.component,  LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
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
        logd("setTitleBarAlignment $alignment on ${if (this.component == null) "titleSubTitle" else "component"} $id")
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
            this@MainToolBar.layoutParams = lp
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
                this@MainToolBar.layoutParams = lp
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
        // Bypass regular layout for the titleComponent to make final placements based on the measurement
        val titleComponent = getTitleComponent()
        if (titleComponentAlignment == Alignment.Center) {
            val titleLeft = max(l + leftButtonsBar.measuredWidth, (r - l) / 2 - titleComponent.measuredWidth / 2)
            val titleRight = min(r - rightButtonsBar.measuredWidth, (r - l) / 2 + titleComponent.measuredWidth / 2)
            titleComponent.layout(titleLeft, t, titleRight, b)
        } else {
            titleComponent.layout(l + leftButtonsBar.measuredWidth + defaultMargin, t, r - rightButtonsBar.measuredWidth, b)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        logd("OnMeasure: parentWidth: $parentWidth, parentHeight: $parentHeight widthMeasureSpec:" +
                " ${MeasureSpec.toString(widthMeasureSpec)}, heightMeasureSpec: ${MeasureSpec.toString(heightMeasureSpec)}", "MainToolBar")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // exact measurement of the parent
        if (mode == MeasureSpec.EXACTLY) {
            val titleComponent = getTitleComponent()
            // measure all 3 child with AT_MOST to get what space they require
            rightButtonsBar.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), heightMeasureSpec)
            leftButtonsBar.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), heightMeasureSpec)
            titleComponent.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), heightMeasureSpec)

            val titleComponentWidth = titleComponent.measuredWidth

            // hide buttons title bypass the right end, otherwise show
            var leftBarWidth = leftButtonsBar.measuredWidth
            val titleLeft = parentWidth / 2 - (titleComponentWidth / 2)
            if (titleLeft < this.left + leftBarWidth + defaultMargin) {
                leftBarWidth = minToolBarSize
                for (item in leftButtonsBar.menu) {
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                }
            }else{
                for (item in leftButtonsBar.menu) {
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                }
            }

            // hide buttons title bypass the left end, otherwise show
            var rightBarWidth = rightButtonsBar.measuredWidth
            val titleRight = parentWidth / 2 + (titleComponentWidth / 2)
            if (titleRight > this.right - rightBarWidth - defaultMargin) {
                rightBarWidth = minToolBarSize
                for (item in rightButtonsBar.menu) {
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                }
            }else{
                for (item in rightButtonsBar.menu) {
                    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                }
            }

            // calculate and measure the title component for the last time.
            val measuredTitleWidth = min(parentWidth - rightBarWidth - leftBarWidth, titleComponentWidth)
            titleComponent.measure(MeasureSpec.makeMeasureSpec(measuredTitleWidth, MeasureSpec.EXACTLY), heightMeasureSpec)
        }
    }

    private fun clearComponent() = this.component?.let { ViewUtils.removeFromParent(it); this.component = null; }

    @RestrictTo(RestrictTo.Scope.TESTS, RestrictTo.Scope.LIBRARY)
    fun getTitleComponent() = this.component ?: this.titleSubTitleBar


    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getComponent() = this.component

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getTitleSubtitleBar() = this.titleSubTitleBar

}
package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import androidx.constraintlayout.widget.ConstraintLayout
import com.reactnativenavigation.options.FontOptions
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.parsers.TypefaceLoader
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.utils.ViewUtils


open class TitleBar constructor(context: Context) : ConstraintLayout(context) {
    private var component: View? = null
    private val titleSubTitleBar = TitleSubTitleLayout(context).apply {
        this.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            this.startToStart = LayoutParams.PARENT_ID
            this.endToEnd = LayoutParams.PARENT_ID
            this.topToTop = LayoutParams.PARENT_ID
            this.bottomToBottom = LayoutParams.PARENT_ID
            horizontalBias = 0f
            verticalBias = 0.5f
        }
        this.visibility = GONE
        this@TitleBar.addView(this)
    }
    private val componentLp = LayoutParams(titleSubTitleBar.layoutParams as LayoutParams)

    fun setComponent(component: View) {
        Log.d("XCXCXC", "set component called with $component, this.component=${this.component}")
        if (this.component == component) return
        clear()
        this.component = component
        this.component?.layoutParams = componentLp
        this.addView(component)
        Log.d("XCXCXC", "set component added $component, this.component=${this.component}")
    }

    //////Switching from tab A to tab B when changing normal title bar with custom one and back with the normal
    fun setTitle(title: CharSequence?) {
        Log.d("XCXCXC", "set title called with $title, clearing component make title visible")
        clearComponent()
        this.titleSubTitleBar.visibility = View.VISIBLE
        this.titleSubTitleBar.setTitle(title)
        Log.d("XCXCXC", "set title changed to $title")

    }

    fun setSubtitle(title: CharSequence?) {
        Log.d("XCXCXC", "set subtitle called with $title, clearing component make title visible")
        clearComponent()
        this.titleSubTitleBar.visibility = View.VISIBLE
        this.titleSubTitleBar.setSubtitle(title)
        Log.d("XCXCXC", "set subtitle changed to $title")

    }

    fun setBackgroundColor(color: Colour) = if (color.hasValue()) setBackgroundColor(color.get()) else Unit

    fun setTitleFontSize(size: Float) = this.titleSubTitleBar.setTitleFontSize(size)

    fun setTitleTypeface(typefaceLoader: TypefaceLoader, font: FontOptions) = this.titleSubTitleBar.setTitleTypeface(typefaceLoader, font)

    fun setSubtitleTypeface(typefaceLoader: TypefaceLoader, font: FontOptions) = this.titleSubTitleBar.setSubtitleTypeface(typefaceLoader, font)

    fun setSubtitleFontSize(size: Float) = this.titleSubTitleBar.setSubtitleFontSize(size)

    fun setSubtitleColor(@ColorInt color: Int) = this.titleSubTitleBar.setSubtitleTextColor(color)

    fun setTitleColor(@ColorInt color: Int) = this.titleSubTitleBar.setTitleTextColor(color)

    fun setHeight(height: Int) {
        val pixelHeight = UiUtils.dpToPx(context, height)
        if (this.layoutParams != null) {
            if (pixelHeight == layoutParams.height) return
            val lp = layoutParams
            lp.height = pixelHeight
            this@TitleBar.layoutParams = lp
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
                this@TitleBar.layoutParams = lp
            }
        }
    }

    fun getTitle(): String = this.titleSubTitleBar.getTitle()

    fun clear() {
        Log.d("XCXCXC", "Clear called, titles gone, component removed")
        clearTitleSubTitle()
        clearComponent()
        Log.d("XCXCXC", "Clear Done")

    }

    private fun clearTitleSubTitle() {
        Log.d("XCXCXC", "clearTitleSubTitle called, childCount:$childCount, component:$component,childCount > 0 && component == null = ${childCount > 0 && component == null}")
        if (childCount > 0 && component == null) {
            titleSubTitleBar.clear()
            this.titleSubTitleBar.visibility = View.GONE
        }
    }

    private fun clearComponent() {
        Log.d("XCXCXC", "clearComponent called, component:$component")
        if (component != null) {
            ViewUtils.removeFromParent(this.component)
            component = null
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getTitleSubtitleBarView() = this.titleSubTitleBar

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getTitleTxtView() = this.titleSubTitleBar.getTitleTxtView()

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getSubTitleTxtView() = this.titleSubTitleBar.getSubTitleTxtView()

}
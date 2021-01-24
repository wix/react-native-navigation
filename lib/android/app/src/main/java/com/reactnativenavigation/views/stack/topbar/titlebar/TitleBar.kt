package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
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
    private val titleSubTitleBar by lazy { TitleSubTitleLayout(context) }
    private val titleLP by lazy {
        LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            this.startToStart = LayoutParams.PARENT_ID
            this.endToEnd = LayoutParams.PARENT_ID
            this.topToTop = LayoutParams.PARENT_ID
            this.bottomToBottom = LayoutParams.PARENT_ID
            horizontalBias = 0f
            verticalBias = 0.5f
        }
    }

    private val componentLp by lazy { LayoutParams(titleLP) }


    fun setComponent(component: View) {
        if (this.component == component) return
        clear()
        this.component = component
        this.component?.layoutParams = componentLp
        this.addView(component)
    }

    fun setTitle(title: CharSequence?) {
        addTitleSubtitleBarIfNeeded()
        clearComponent()
        this.titleSubTitleBar.setTitle(title)
    }

    fun setSubtitle(title: CharSequence?) {
        addTitleSubtitleBarIfNeeded()
        this.titleSubTitleBar.setSubtitle(title)
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
        clearTitleSubTitle()
        clearComponent()
    }

    private fun clearTitleSubTitle() {
        if (childCount > 0 && component == null) {
            titleSubTitleBar.clear()
            ViewUtils.removeFromParent(titleSubTitleBar)
        }
    }

    private fun clearComponent() {
        if (component != null) {
            ViewUtils.removeFromParent(component)
            component = null
        }
    }

    private fun addTitleSubtitleBarIfNeeded() {
        if (component != null || childCount == 0) {
            this.titleSubTitleBar.layoutParams = titleLP;
            addView(this.titleSubTitleBar)
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getTitleTxtView() = this.titleSubTitleBar.getTitleTxtView()

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getSubTitleTxtView() = this.titleSubTitleBar.getSubTitleTxtView()

}
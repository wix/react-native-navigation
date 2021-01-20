package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import com.reactnativenavigation.options.FontOptions
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.parsers.TypefaceLoader
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.utils.ViewUtils

open class TitleBar constructor(context: Context) : ConstraintLayout(context) {
    private var component: View? = null
    private val titleSubTitleBar = TitleSubTitleLayout(context)

    fun setComponent(component: View) {
        if (this.component === component) return
        clear()
        this.component = component
        this.addView(component, LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            this.startToStart = LayoutParams.PARENT_ID
            this.endToEnd = LayoutParams.PARENT_ID
            this.topToTop = LayoutParams.PARENT_ID
            this.bottomToBottom = LayoutParams.PARENT_ID
            horizontalBias = 0f
            verticalBias = 0.5f
        })
    }

    fun setTitle(title: CharSequence?) {
        if (component != null || childCount == 0) {
            addView(this.titleSubTitleBar, LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                this.startToStart = LayoutParams.PARENT_ID
                this.endToEnd = LayoutParams.PARENT_ID
                this.topToTop = LayoutParams.PARENT_ID
                this.bottomToBottom = LayoutParams.PARENT_ID
                horizontalBias = 0f
                verticalBias = 0.5f
            })
        }
        clearComponent()
        this.titleSubTitleBar.setTitle(title)
    }

    fun getTitle(): String = this.titleSubTitleBar.getTitle()

    fun setSubtitle(title: CharSequence?) {
        this.titleSubTitleBar.setSubtitle(title)
    }

    override fun setLayoutDirection(layoutDirection: Int) {
        super.setLayoutDirection(layoutDirection)
    }

    fun setBackgroundColor(color: Colour) = if (color.hasValue()) setBackgroundColor(color.get()) else Unit

    fun setTitleFontSize(size: Float) = this.titleSubTitleBar.setTitleFontSize(size)

    fun setTitleTypeface(typefaceLoader: TypefaceLoader, font: FontOptions) = this.titleSubTitleBar.setTitleTypeface(typefaceLoader, font)

    fun setSubtitleTypeface(typefaceLoader: TypefaceLoader, font: FontOptions) = this.titleSubTitleBar.setSubtitleTypeface(typefaceLoader, font)

    fun setSubtitleFontSize(size: Float) = this.titleSubTitleBar.setSubtitleFontSize(size)

    fun setSubtitleColor(@ColorInt color: Int) = this.titleSubTitleBar.setSubtitleTextColor(color)

    fun setTitleColor(@ColorInt color: Int) = this.titleSubTitleBar.setTitleTextColor(color)


    fun setHeight(height: Int) {
        layoutParams?.let {
            val pixelHeight = UiUtils.dpToPx(context, height)
            if (pixelHeight == layoutParams.height) return
            val lp = layoutParams
            lp.height = pixelHeight
            layoutParams = lp
        }
    }


    fun setTopMargin(topMargin: Int) {
        val pixelTopMargin = UiUtils.dpToPx(context, topMargin)
        if (layoutParams is MarginLayoutParams) {
            val lp = layoutParams as MarginLayoutParams
            if (lp.topMargin == pixelTopMargin) return
            lp.topMargin = pixelTopMargin
            layoutParams = lp
        }
    }

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


    companion object {
        const val DEFAULT_LEFT_MARGIN = 16
    }


}
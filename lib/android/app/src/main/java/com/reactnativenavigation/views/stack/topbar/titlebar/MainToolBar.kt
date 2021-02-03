package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.FontOptions
import com.reactnativenavigation.options.LayoutDirection
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.parsers.TypefaceLoader
import com.reactnativenavigation.utils.CompatUtils
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.logd
import kotlin.math.roundToInt

const val DEFAULT_LEFT_MARGIN = 16

class MainToolBar(context: Context) : RelativeLayout(context) {

    private var component: View? = null
    private val componentViewId = CompatUtils.generateViewId() // keeping componentIds stable for constraint layout to perform well
    private var componentViewIdBackup = View.NO_ID

    private val titleSubTitleBar = TitleSubTitleLayout(context).apply {
        id = CompatUtils.generateViewId()
    }

    val leftButtonsBar = LeftButtonsBar(context).apply {
        this.id = CompatUtils.generateViewId()
    }
    val rightButtonsBar: RightButtonsBar = RightButtonsBar(context).apply { this.id = CompatUtils.generateViewId()
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


    fun setComponent(component: View) {
        if (this.component == component) return
        val componentLp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            addRule(ALIGN_START, titleSubTitleBar.id)
            addRule(ALIGN_END, titleSubTitleBar.id)
            addRule(CENTER_VERTICAL)
        }
        clear()
        this.component = component
        this.componentViewIdBackup = this.component?.id ?: View.NO_ID
        this.component?.id = componentViewId
        this.addView(this.component, componentLp)
    }

    fun setTitle(title: CharSequence?) {
        clearComponent()
        this.titleSubTitleBar.setTitle(title)
    }

    fun setSubtitle(title: CharSequence?) {
        clearComponent()
        this.titleSubTitleBar.setSubtitle(title)
    }

    fun setTitleBarAlignment(alignment: Alignment) {
        logd("setTitleBarAlignment $alignment on ${if (this.component == null) "titleSubTitle" else "component"} $id")
        val margin = if (alignment == Alignment.Center) 0 else UiUtils.dpToPx(context, DEFAULT_LEFT_MARGIN)

        this.titleSubTitleBar.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            if (alignment != Alignment.Center) {
                addRule(END_OF, leftButtonsBar.id)
                addRule(START_OF, rightButtonsBar.id)
                addRule(CENTER_VERTICAL)
            } else {
                addRule(CENTER_IN_PARENT)
            }
            marginStart = margin
            marginEnd = margin
        }
        this.component?.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            if (alignment != Alignment.Center) {
                addRule(ALIGN_START, titleSubTitleBar.id)
                addRule(ALIGN_END, titleSubTitleBar.id)
                addRule(CENTER_VERTICAL)
            } else {
                addRule(CENTER_IN_PARENT)
            }
        }
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
        //clearing title and sub title
        if (this.childCount > 0 && this.component == null) {
            this.titleSubTitleBar.clear()
        }
        clearComponent()
    }


    private fun clearComponent() = this.component?.let { it.id = View.NO_ID; ViewUtils.removeFromParent(it); this.component = null; }

    @RestrictTo(RestrictTo.Scope.TESTS, RestrictTo.Scope.LIBRARY)
    fun getTitleComponent() = this.component ?: this.titleSubTitleBar


    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getComponent() = this.component

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getTitleSubtitleBar() = this.titleSubTitleBar

}
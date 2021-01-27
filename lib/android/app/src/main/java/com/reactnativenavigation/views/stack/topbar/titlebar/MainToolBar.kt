package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.FontOptions
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.parsers.TypefaceLoader
import com.reactnativenavigation.utils.CompatUtils
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.utils.ViewUtils
import kotlin.math.roundToInt

const val DEFAULT_LEFT_MARGIN = 16

class MainToolBar(context: Context) : ConstraintLayout(context) {

    private var component: View? = null

    private val titleSubTitleBar = TitleSubTitleLayout(context).apply {
        id = CompatUtils.generateViewId()
        this.visibility = GONE
    }

    val leftButtonsBar = LeftButtonsBar(context).apply { this.id = CompatUtils.generateViewId() }
    val rightButtonsBar: RightButtonsBar = RightButtonsBar(context).apply { this.id = CompatUtils.generateViewId() }

    private val titleBarContentLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
        this.startToEnd = leftButtonsBar.id
        this.endToStart = rightButtonsBar.id
        this.topToTop = ConstraintSet.PARENT_ID
        this.bottomToBottom = ConstraintSet.PARENT_ID
        this.verticalBias = 0.5f
        this.horizontalBias = 0f
        this.marginStart = UiUtils.dpToPx(context, DEFAULT_LEFT_MARGIN)
    }

    init {
        this.addView(leftButtonsBar, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        this.addView(titleSubTitleBar, this.titleBarContentLayoutParams)
        this.addView(rightButtonsBar, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        val constraintSet = ConstraintSet().also { it.clone(this) }

        constraintSet.setHorizontalBias(leftButtonsBar.id, 0f)
        constraintSet.setHorizontalBias(rightButtonsBar.id, 0f)

        constraintSet.constrainMaxWidth(leftButtonsBar.id, (UiUtils.getWindowWidth(context) / 2f).roundToInt())
        constraintSet.constrainMaxWidth(rightButtonsBar.id, (UiUtils.getWindowWidth(context) / 2f).roundToInt())

        constraintSet.connect(leftButtonsBar.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(leftButtonsBar.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.connect(leftButtonsBar.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)

        constraintSet.connect(rightButtonsBar.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(rightButtonsBar.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.connect(rightButtonsBar.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)

        constraintSet.applyTo(this)
    }

    fun setComponent(component: View) {
        if (this.component == component) return
        clear()
        this.component = component
        this.component?.layoutParams = titleBarContentLayoutParams
        if (component.id == View.NO_ID) {
            component.id = CompatUtils.generateViewId()
        }
        this.addView(component)
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
        val constraintSet = ConstraintSet().also { it.clone(this) }
        val bias = if (alignment == Alignment.Center) 0.5f else 0f;
        val margin = if (alignment == Alignment.Center) 0 else UiUtils.dpToPx(context, DEFAULT_LEFT_MARGIN)
        val startToEndId = if (alignment == Alignment.Center) ConstraintSet.PARENT_ID else leftButtonsBar.id
        val startEndSide = if (alignment == Alignment.Center) ConstraintSet.START else ConstraintSet.END
        val endToStartId = if (alignment == Alignment.Center) ConstraintSet.PARENT_ID else rightButtonsBar.id
        val endStartSide = if (alignment == Alignment.Center) ConstraintSet.END else ConstraintSet.START

        constraintSet.connect(getTitleComponent().id, ConstraintSet.START, startToEndId, startEndSide)
        constraintSet.connect(getTitleComponent().id, ConstraintSet.END, endToStartId, endStartSide)
        constraintSet.setHorizontalBias(this.getTitleComponent().id, bias)
        constraintSet.setMargin(this.getTitleComponent().id, ConstraintSet.START, margin)

        constraintSet.applyTo(this)
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
            this.titleSubTitleBar.visibility = View.GONE
        }
        clearComponent()
    }

    private fun clearComponent() = this.component?.let { ViewUtils.removeFromParent(it); this.component = null }

    @RestrictTo(RestrictTo.Scope.TESTS, RestrictTo.Scope.LIBRARY)
    fun getTitleComponent() = this.component ?: this.titleSubTitleBar

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getComponent() = this.component

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getTitleSubtitleBar() = this.titleSubTitleBar

}
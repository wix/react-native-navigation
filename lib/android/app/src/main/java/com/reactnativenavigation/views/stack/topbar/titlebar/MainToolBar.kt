package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import androidx.core.view.doOnLayout
import androidx.core.view.marginEnd
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.FontOptions
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.parsers.TypefaceLoader
import com.reactnativenavigation.utils.CompatUtils
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.utils.logd
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController

const val DEFAULT_LEFT_MARGIN = 16

class MainToolBar(context: Context) : RelativeLayout(context) {

    private var component: View? = null
    private val centerFrameLayout = FrameLayout(context).apply {
        id = CompatUtils.generateViewId()
    }

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
        this.addView(titleSubTitleBar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            addRule(END_OF, leftButtonsBar.id)
            addRule(START_OF, rightButtonsBar.id)
            addRule(CENTER_VERTICAL)
            marginStart = UiUtils.dpToPx(context, DEFAULT_LEFT_MARGIN)
        })
        this.addView(centerFrameLayout, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
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
        if (alignment == Alignment.Center) {
            this.centerFrameLayout.addView(this.component, FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                this.gravity = Gravity.CENTER
            })
        } else {
            this.addView(this.component, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                addRule(END_OF, leftButtonsBar.id)
                addRule(START_OF, rightButtonsBar.id)
                addRule(CENTER_VERTICAL)
                marginStart = UiUtils.dpToPx(context, DEFAULT_LEFT_MARGIN)
            })
        }
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
//        this.titleSubTitleBar.setBackgroundColor(Color.GREEN)
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        if (alignment == Alignment.Center) {
            layoutParams.addRule(CENTER_IN_PARENT, TRUE)
        } else {
            titleSubTitleBar.setPadding(0, 0, 0, 0)
            layoutParams.apply {
                addRule(END_OF, leftButtonsBar.id)
                addRule(START_OF, rightButtonsBar.id)
                addRule(CENTER_VERTICAL)
                marginStart = UiUtils.dpToPx(context, DEFAULT_LEFT_MARGIN)
            }
        }
        this.titleSubTitleBar.layoutParams = layoutParams
        if(alignment==Alignment.Center){
            updateCenterTitlePadding()
        }
        logd("rightButtonsWidth: ${rightButtonsBar.width}, left: ${leftButtonsBar.width}", "OnMeasure-toolbar")
    }

    private fun updateCenterTitlePadding() {
        doOnLayout {
            if ((this.titleSubTitleBar.layoutParams as LayoutParams).rules[CENTER_IN_PARENT] != TRUE){
                return@doOnLayout
            }
            val totalStartPadding = if (leftButtonsBar.width != 0) {
                if (this.titleSubTitleBar.x <= (this.leftButtonsBar.width))
                    leftButtonsBar.width
                else 0
            } else 0
            val totalEndPadding = if (rightButtonsBar.width != 0) {
                if (totalStartPadding == 0) {
                    if (this.titleSubTitleBar.x + this.titleSubTitleBar.titleTextView.measuredWidth >= (this.width - this.rightButtonsBar.width))
                        rightButtonsBar.width + marginEnd
                    else 0
                } else {
                    if (totalStartPadding + this.titleSubTitleBar.titleTextView.measuredWidth >= (this.width - this.rightButtonsBar.width))
                        rightButtonsBar.width + marginEnd
                    else 0
                }
            } else {
                0
            }
            titleSubTitleBar.setPaddingRelative(totalStartPadding, 0, totalEndPadding, 0)
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
        if (this.childCount > 0 && this.component == null) {
            this.titleSubTitleBar.visibility = View.INVISIBLE
        }
        clearComponent()
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
//        val marginStart = (this.titleSubTitleBar.layoutParams as RelativeLayout.LayoutParams).marginStart
//        val maxWidth = measuredWidth - rightButtonsBar.measuredWidth - leftButtonsBar.measuredWidth -marginStart
//        this.titleSubTitleBar.maxWidth = maxWidth
//        logd("measured width: $measuredWidth, max width: $maxWidth, x :${this.titleSubTitleBar.translationX}", "OnMeasure-toolbar")
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//
//    }

    private fun clearComponent() = this.component?.let { ViewUtils.removeFromParent(it); this.component = null; }

    fun setBackButton(button: ButtonController) {
        this.leftButtonsBar.setBackButton(button)
    }

    @RestrictTo(RestrictTo.Scope.TESTS, RestrictTo.Scope.LIBRARY)
    fun getTitleComponent() = this.component ?: this.titleSubTitleBar


    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getComponent() = this.component

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getTitleSubtitleBar() = this.titleSubTitleBar
    fun clearLeftButtons() {
        this.leftButtonsBar.clearButtons()
        onButtonsChanged()
    }

    fun clearBackButton() {
        this.leftButtonsBar.clearBackButton()
        onButtonsChanged()
    }

    fun clearRightButtons() {
        this.rightButtonsBar.clearButtons()
        onButtonsChanged()
    }

    fun removeRightButton(buttonId: Int) {
        this.rightButtonsBar.removeButton(buttonId)
        onButtonsChanged()
    }

    fun removeLeftButton(buttonId: Int) {
        this.leftButtonsBar.removeButton(buttonId)
        onButtonsChanged()
    }

    fun onButtonsChanged() {
        updateCenterTitlePadding()
    }

}
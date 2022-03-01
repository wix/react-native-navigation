package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.FontOptions
import com.reactnativenavigation.options.parsers.TypefaceLoader

private const val DEFAULT_TITLE_FONT_SIZE_DP = 18f
private const val DEFAULT_SUBTITLE_FONT_SIZE_DP = 14f

class TitleSubTitleLayout(context: Context) : LinearLayout(context) {

    private val titleTextView = SingleLineTextView(context, DEFAULT_TITLE_FONT_SIZE_DP)
    private val subTitleTextView = SingleLineTextView(context, DEFAULT_SUBTITLE_FONT_SIZE_DP)

    init {
        this.orientation = VERTICAL
        this.setVerticalGravity(Gravity.CENTER_VERTICAL)
        this.addView(titleTextView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply { gravity = Gravity.START or Gravity.CENTER_VERTICAL })
        this.addView(subTitleTextView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
            weight = 1f
        })
    }

    fun setSubTitleAlignment(alignment: Alignment) {
        if (alignment == Alignment.Center) {
            (this.subTitleTextView.layoutParams as LayoutParams).gravity = Gravity.CENTER
        } else {
            (this.subTitleTextView.layoutParams as LayoutParams).gravity = Gravity.START or Gravity.CENTER_VERTICAL
        }
    }

    fun setTitleAlignment(alignment: Alignment) {
        if (alignment == Alignment.Center) {
            (this.titleTextView.layoutParams as LayoutParams).gravity = Gravity.CENTER
        } else {
            (this.titleTextView.layoutParams as LayoutParams).gravity = Gravity.START or Gravity.CENTER_VERTICAL
        }
    }

    fun setTitleFontSize(size: Float) = titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size)

    fun setSubtitleFontSize(size: Float) = subTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size)

    fun setSubtitleTextColor(@ColorInt color: Int) = this.subTitleTextView.setTextColor(color)

    fun setTitleTextColor(@ColorInt color: Int) = this.titleTextView.setTextColor(color)

    fun setTitleTypeface(typefaceLoader: TypefaceLoader, font: FontOptions) {
        titleTextView.typeface = font.getTypeface(typefaceLoader, titleTextView.typeface)
    }

    fun setSubtitleTypeface(typefaceLoader: TypefaceLoader, font: FontOptions) {
        subTitleTextView.typeface = font.getTypeface(typefaceLoader, subTitleTextView.typeface)
    }

    fun setTitle(title: CharSequence?) {
        this.titleTextView.text = title
    }

    fun setSubtitle(subTitle: CharSequence?) {
        this.subTitleTextView.text = subTitle
        if (subTitle.isNullOrEmpty()) {
            this.subTitleTextView.visibility = View.GONE
        } else {
            this.subTitleTextView.visibility = View.VISIBLE
        }
    }

    fun getTitle() = (this.titleTextView.text ?: "").toString()

    fun clear() {
        this.titleTextView.text = null
        setSubtitle(null)
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getTitleTxtView(): TextView {
        return this.titleTextView
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getSubTitleTxtView(): TextView {
        return this.subTitleTextView
    }
}
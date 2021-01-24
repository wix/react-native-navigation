package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import androidx.core.widget.TextViewCompat
import com.reactnativenavigation.R
import com.reactnativenavigation.options.FontOptions
import com.reactnativenavigation.options.parsers.TypefaceLoader

class TitleSubTitleLayout(context: Context) : LinearLayout(context) {
    private val titleTextView = TextView(context).apply { TextViewCompat.setTextAppearance(this, R.style.TitleBarTitle) }
    private val subTitleTextView = TextView(context).apply { TextViewCompat.setTextAppearance(this, R.style.TitleBarSubtitle) }

    init {
        this.setBackgroundColor(Color.parseColor("#FF8B61"))
        this.orientation = VERTICAL
        this.setVerticalGravity(Gravity.CENTER_VERTICAL)

        this.addView(titleTextView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            setBackgroundColor(Color.DKGRAY)
        })
        this.addView(subTitleTextView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            setBackgroundColor(Color.GRAY)
            weight = 1f
        })
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
        this.subTitleTextView.text = null
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
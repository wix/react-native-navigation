package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import androidx.core.widget.TextViewCompat
import com.reactnativenavigation.R
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.FontOptions
import com.reactnativenavigation.options.parsers.TypefaceLoader


class TitleSubTitleLayout(context: Context) : LinearLayout(context) {

    private val titleTextView = TextView(context).apply {
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
        TextViewCompat.setTextAppearance(this, R.style.TitleBarTitle)
    }
    private val subTitleTextView = TextView(context).apply {
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
        TextViewCompat.setTextAppearance(this, R.style.TitleBarSubtitle)
    }

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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

//        if (titleTextView.text.isNotEmpty() || subTitleTextView.text.isNotEmpty()) {
//            val titleTextWidth = this.titleTextView.paint.measureText(this.titleTextView.text.toString())
//            val subtitleTextWidth = this.subTitleTextView.paint.measureText(this.subTitleTextView.text.toString())
//            val mode = MeasureSpec.getMode(widthMeasureSpec)
//            val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
//            val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
//            val textWidth = max(titleTextWidth, subtitleTextWidth)
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec) // must call to meet layout constraints if any
//            logd("OnMeasure: parentHeight: $parentHeight widthMeasureSpec: ${MeasureSpec.toString(widthMeasureSpec)}, heightMeasureSpec: ${MeasureSpec.toString(heightMeasureSpec)}", "TitleSubTitleLayout")
//            if (mode == MeasureSpec.AT_MOST) {
//                //back + overflow limit
//                val maxAllowedWidth = parentWidth
//                val desiredWidth = min(textWidth, maxAllowedWidth)
//
//                logd("OnMeasure: parentWidth: $parentWidth, titleTextWidth: $titleTextWidth, subtitleTextWidth: $subtitleTextWidth, desiredWidth: $desiredWidth, availableWidth: $maxAllowedWidth", "TitleSubTitleLayout")
//                super.setMeasuredDimension(desiredWidth.roundToInt(), parentHeight)
//            } else if (mode == MeasureSpec.EXACTLY) {
//                //should check available space
//                super.setMeasuredDimension(textWidth.roundToInt(), parentHeight)
//            }
//        } else {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        }

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
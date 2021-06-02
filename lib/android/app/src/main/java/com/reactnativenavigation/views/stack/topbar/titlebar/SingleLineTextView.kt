package com.reactnativenavigation.views.stack.topbar.titlebar

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView

@SuppressLint("ViewConstructor")
class SingleLineTextView(context: Context, fontSizeDp:Float) : AppCompatTextView(context){
    init {
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
        setTextSize(TypedValue.COMPLEX_UNIT_DIP,fontSizeDp)
    }
}
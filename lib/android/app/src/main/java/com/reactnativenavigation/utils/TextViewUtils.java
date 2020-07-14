package com.reactnativenavigation.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.annotation.ColorInt;

public class TextViewUtils {
    @ColorInt
    public static int getTextColor(TextView view) {
        SpannedString text = new SpannedString(view.getText());
        ForegroundColorSpan[] spans = text.getSpans(0, text.length(), ForegroundColorSpan.class);
        return spans.length == 0 ? view.getCurrentTextColor() : spans[0].getForegroundColor();
    }

    public static float getTextSize(TextView view) {
        SpannedString text = new SpannedString(view.getText());
        AbsoluteSizeSpan[] spans = text.getSpans(0, text.length(), AbsoluteSizeSpan.class);
        return spans.length == 0 ? -1 : spans[0].getSize();
    }

    public static void setColor(SpannableString span, int color) {
        span.setSpan(new ForegroundColorSpan(color), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}

package com.reactnativenavigation.utils;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;

import com.reactnativenavigation.parse.params.Button;

import static android.text.Spanned.SPAN_EXCLUSIVE_INCLUSIVE;

public class ButtonPresenter {
    private Button button;

    public ButtonPresenter(Button button) {
        this.button = button;
    }

    public void tint(Drawable drawable, int tint) {
        drawable.setColorFilter(new PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN));
    }

    public SpannableString getStyledText() {
        SpannableString string = new SpannableString(button.text.get(""));
        string.setSpan(new ButtonSpan(button), 0, button.text.length(), SPAN_EXCLUSIVE_INCLUSIVE);
        return string;
    }
}

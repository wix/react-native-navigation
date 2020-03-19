package com.reactnativenavigation.utils;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.MenuItem;
import android.widget.TextView;

import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.views.titlebar.TitleBar;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static com.reactnativenavigation.utils.UiUtils.runOnPreDrawOnce;

public class ButtonPresenter {
    private Button button;

    public ButtonPresenter(Button button) {
        this.button = button;
    }

    public void tint(Drawable drawable, int tint) {
        drawable.setColorFilter(new PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN));
    }

    public void setTypeFace(TitleBar titleBar, Typeface typeface) {
        if (typeface == null) return;
        runOnPreDrawOnce(titleBar, () -> forEach(titleBar.findButtonTextView(button), b -> ((TextView) b).setTypeface(typeface)));
    }

    public void setFontSize(MenuItem menuItem) {
        SpannableString spanString = new SpannableString(button.text.get());
        if (this.button.fontSize.hasValue())
            spanString.setSpan(
                    new AbsoluteSizeSpan(button.fontSize.get(), true),
                    0,
                    button.text.get().length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
            );
        menuItem.setTitleCondensed(spanString);
    }

    public void setTextColor(TitleBar titleBar) {
        runOnPreDrawOnce(titleBar, () -> forEach(titleBar.findButtonTextView(button), btn -> {
            if (button.enabled.isTrueOrUndefined() && button.color.hasValue()) {
                setEnabledColor((TextView) btn);
            } else if (button.enabled.isFalse()) {
                setDisabledColor((TextView) btn, button.disabledColor.get(Color.LTGRAY));
            }
        }));
    }

    public void setDisabledColor(TextView btn, int color) {
        btn.setTextColor(color);
    }

    public void setEnabledColor(TextView btn) {
        btn.setTextColor(button.color.get());
    }
}

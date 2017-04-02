package com.reactnativenavigation.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reactnativenavigation.R;
import com.reactnativenavigation.utils.ViewUtils;

/**
 * Created by jsierra on 31/03/17.
 */

public class FloatingActionButtonLabel extends TextView {
    private int mVerticalPadding = (int) ViewUtils.convertDpToPixel(4);
    private int mHorizontalPadding = (int) ViewUtils.convertDpToPixel(8);

    public FloatingActionButtonLabel(Context context) {
        super(context);
        this.init(context);
    }

    private void init(Context context) {
        setDefaultStyles();
    }

    private void setDefaultStyles() {
        setMinHeight((int) ViewUtils.convertDpToPixel(24));
        ViewCompat.setElevation(this, (int) ViewUtils.convertDpToPixel(1));

        setBackgroundResource(R.drawable.label_corners);
        GradientDrawable drawable = (GradientDrawable) getBackground();
        drawable.setColor(Color.parseColor("#CC000000"));
        setTextColor(Color.WHITE);

        setGravity(Gravity.CENTER);
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
    }
}

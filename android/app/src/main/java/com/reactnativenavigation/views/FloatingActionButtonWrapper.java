package com.reactnativenavigation.views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.design.widget.FloatingActionButton;


import android.widget.TextView;

import com.reactnativenavigation.R;


public class FloatingActionButtonWrapper extends FloatingActionButton {
    String mTitle;

    public FloatingActionButtonWrapper(Context context) {
        super(context);
    }

    public void setTitle(String title) {
        mTitle = title;
        TextView label = getLabelView();
        if (label != null) {
            label.setText(title);
        }
    }

    FloatingActionButtonLabel getLabelView() {
        return (FloatingActionButtonLabel) getTag(R.id.fab_label);
    }

    public String getTitle() {
        return mTitle;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void setBackgroundCompat(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        TextView label = getLabelView();
        if (label != null) {
            label.setVisibility(visibility);
        }

        super.setVisibility(visibility);
    }
}
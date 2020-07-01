package com.reactnativenavigation.views.pip;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

public class PIPContainer extends FrameLayout {

    public PIPContainer(@NonNull Context context) {
        super(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, 0);
        setBackgroundColor(Color.TRANSPARENT);
        setLayoutParams(layoutParams);
    }
}

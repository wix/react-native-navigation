package com.reactnativenavigation.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class TranslucentTitleBarBackground extends TitleBarBackground {
    public TranslucentTitleBarBackground() {
        super(new TranslucentDrawable(), new ColorDrawable(Color.TRANSPARENT));
        setCrossFadeEnabled(true);
    }
}

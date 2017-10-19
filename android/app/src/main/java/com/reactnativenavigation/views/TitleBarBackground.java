package com.reactnativenavigation.views;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;

public class TitleBarBackground extends TransitionDrawable {
    private static final int DURATION = 200;

    private enum DrawableType {Translucent, Solid}

    private DrawableType displayedDrawable = DrawableType.Translucent;

    public TitleBarBackground(Drawable... drawables) {
        super(drawables);
        setCrossFadeEnabled(true);
    }

    public void showTranslucentBackground() {
        if (displayedDrawable == DrawableType.Translucent) {
            return;
        }
        displayedDrawable = DrawableType.Translucent;
        reverseTransition(DURATION);
    }

    public void showSolidBackground() {
        if (displayedDrawable == DrawableType.Solid) {
            return;
        }
        displayedDrawable = DrawableType.Solid;
        startTransition(DURATION);
    }
}

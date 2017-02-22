package com.reactnativenavigation.views;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;

import com.reactnativenavigation.params.StyleParams;

public class TranslucentAndSolidTitleBarBackground extends TransitionDrawable {
    private static final int DURATION = 200;
    private enum DrawableType {Translucent, Solid}

    private DrawableType displayedDrawable = DrawableType.Translucent;

    public TranslucentAndSolidTitleBarBackground(StyleParams.Color color) {
        super(new Drawable[] {
                new TranslucentTitleBarBackground(),
                new ColorDrawable(color.getColor())
        });
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

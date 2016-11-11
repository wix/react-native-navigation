package com.reactnativenavigation.views.collapsingToolbar;

import android.view.View;

public class ViewCollapser {
    private static final int DURATION = 100;

    public static void collapse(CollapsingView view, CollapseAmount amount) {
        if (amount.collapseToTop()) {
            collapseView(view, true, view.getFinalCollapseValue());
        } else if (amount.collapseToBottom()) {
            collapseView(view, true, 0);
        } else {
            collapse(view.asView(), amount);
        }
    }

    public static void collapse(View view, CollapseAmount amount) {
        view.setTranslationY(amount.get());
    }

    private static void collapseView(CollapsingView view, boolean animate, float translation) {
        if (animate) {
            animate(view, translation);
        } else {
            view.asView().setTranslationY(translation);
        }
    }

    private static void animate(CollapsingView view, float translation) {
        view.asView().animate()
                .translationY(translation)
                .setDuration(DURATION)
                .start();
    }
}

package com.reactnativenavigation.presentation;

import android.util.Log;
import android.view.View;

import com.reactnativenavigation.anim.StackAnimator;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.parse.TopBarOptions;
import com.reactnativenavigation.parse.TopTabsOptions;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.utils.TypefaceLoader;
import com.reactnativenavigation.views.TopBar;

import static com.reactnativenavigation.parse.NavigationOptions.*;
import static com.reactnativenavigation.parse.NavigationOptions.BooleanOptions.*;

public class OptionsPresenter {

    private final StackAnimator animator;
    private View contentView;
    private TopBar topBar;

    public OptionsPresenter(TopBar topBar, View contentView) {
        this.topBar = topBar;
        this.contentView = contentView;
        animator = new StackAnimator(topBar.getContext());
    }

    public void applyOptions(NavigationOptions options) {
        applyTopBarOptions(options.topBarOptions);
        applyTopTabsOptions(options.topTabsOptions);
    }

    private void applyTopBarOptions(TopBarOptions options) {
        topBar.setTitle(options.title);
        topBar.setBackgroundColor(options.backgroundColor);
        topBar.setTitleTextColor(options.textColor);
        topBar.setTitleFontSize(options.textFontSize);

        TypefaceLoader typefaceLoader = new TypefaceLoader();
        topBar.setTitleTypeface(typefaceLoader.getTypeFace(topBar.getContext(), options.textFontFamily));
        if (options.hidden == True) {
            hideTopBar(options.animateHide);
        }
        if (options.hidden == False) {
            showTopBar(options.animateHide);
        }
        if (options.collapse == True) {
            ((ReactView) contentView).setScrollListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                int dif = scrollY - oldScrollY;
                Log.i("NIGA", "dif = " + dif);
                if (dif > 50) {
                    hideTopBar(True);
                    Log.i("NIGA", "HIDE");
                } else  if (dif < -50){
                    showTopBar(True);
                    Log.i("NIGA", "SHOW");
                }
            });
        } else {
            ((ReactView) contentView).setScrollListener(null);
        }
    }

    private void showTopBar(BooleanOptions animated) {
        if (topBar.getVisibility() == View.VISIBLE) {
            return;
        }
        if (animated == True) {
            animator.animateShowTopBar(topBar, contentView);
        } else {
            topBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideTopBar(BooleanOptions animated) {
        if (topBar.getVisibility() == View.GONE) {
            return;
        }
        if (animated == True) {
            animator.animateHideTopBar(topBar, contentView);
        } else {
            topBar.setVisibility(View.GONE);
        }
    }

    private void applyTopTabsOptions(TopTabsOptions topTabsOptions) {
        // TODO: -guyca
    }
}

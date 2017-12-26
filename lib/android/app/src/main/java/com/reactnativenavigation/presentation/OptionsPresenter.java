package com.reactnativenavigation.presentation;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

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

    private int previousScroll;

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
            ((ReactView) contentView).setScrollListener(scrollY -> {
                int diff = scrollY - previousScroll;
                previousScroll = scrollY;
                int nextHeight = topBar.getMeasuredHeight() - diff;
                if (diff < 0) {
                    int minHeight = topBar.getTitleBar().getMinimumHeight();
                    if (topBar.getVisibility() == View.GONE) {
                        topBar.setVisibility(View.VISIBLE);
                    } else if (nextHeight < minHeight) {
                        ViewGroup.LayoutParams topBarLayoutParams = topBar.getLayoutParams();
                        topBarLayoutParams.height = nextHeight;
                        topBar.setLayoutParams(topBarLayoutParams);
                    } else if (topBar.getMeasuredHeight() != minHeight) {
                        ViewGroup.LayoutParams topBarLayoutParams = topBar.getLayoutParams();
                        topBarLayoutParams.height = minHeight;
                        topBar.setLayoutParams(topBarLayoutParams);
                    }
                } else {
                    if (nextHeight < 0 && topBar.getVisibility() == View.VISIBLE) {
                        topBar.setVisibility(View.GONE);
                    } else if (nextHeight > 0) {
                        ViewGroup.LayoutParams topBarLayoutParams = topBar.getLayoutParams();
                        topBarLayoutParams.height = nextHeight;
                        topBar.setLayoutParams(topBarLayoutParams);
                    }
                }
            });
        } else {
            ((ReactView) contentView).setScrollListener(null);
            topBar.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams topBarLayoutParams = topBar.getLayoutParams();
            topBarLayoutParams.height = topBar.getTitleBar().getMinimumHeight();
            topBar.setLayoutParams(topBarLayoutParams);
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

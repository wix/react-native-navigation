package com.reactnativenavigation.presentation;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.anim.StackAnimator;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.parse.TopBarOptions;
import com.reactnativenavigation.parse.TopTabsOptions;
import com.reactnativenavigation.utils.TypefaceLoader;
import com.reactnativenavigation.views.TopBar;

import static android.widget.RelativeLayout.BELOW;
import static com.reactnativenavigation.parse.NavigationOptions.BooleanOptions;
import static com.reactnativenavigation.parse.NavigationOptions.BooleanOptions.False;
import static com.reactnativenavigation.parse.NavigationOptions.BooleanOptions.True;

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
        } else if (options.hidden == False) {
            showTopBar(options.animateHide);
        }

        if (options.collapse == True) {
            topBar.enableCollapse();
        } else if (options.collapse == False) {
            topBar.disableCollapse();
        }

        if (options.drawUnder == True) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
            layoutParams.removeRule(BELOW);
            contentView.setLayoutParams(layoutParams);
        } else if (options.drawUnder == False) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
            layoutParams.addRule(BELOW, topBar.getId());
            contentView.setLayoutParams(layoutParams);
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

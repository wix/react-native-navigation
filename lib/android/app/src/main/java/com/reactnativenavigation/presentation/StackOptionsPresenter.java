package com.reactnativenavigation.presentation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.reactnativenavigation.parse.AnimationsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.OrientationOptions;
import com.reactnativenavigation.parse.TopBarButtons;
import com.reactnativenavigation.parse.TopBarOptions;
import com.reactnativenavigation.parse.TopTabOptions;
import com.reactnativenavigation.parse.TopTabsOptions;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.views.Component;
import com.reactnativenavigation.views.topbar.TopBar;

public class StackOptionsPresenter {
    private static final int DEFAULT_TITLE_COLOR = Color.BLACK;
    private static final int DEFAULT_SUBTITLE_COLOR = Color.GRAY;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private final double defaultTitleFontSize;
    private final double defaultSubtitleFontSize;

    private TopBar topBar;
    private Options defaultOptions;

    public StackOptionsPresenter(Context context, Options defaultOptions) {
        defaultTitleFontSize = UiUtils.dpToSp(context, 18);
        defaultSubtitleFontSize = UiUtils.dpToSp(context, 14);
        this.defaultOptions = defaultOptions;
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public Options getDefaultOptions() {
        return defaultOptions;
    }

    public void bindView(TopBar topBar) {
        this.topBar = topBar;
    }

    public void applyLayoutParamsOptions(Options options, View view) {
        Options withDefault = options.copy().withDefaultOptions(defaultOptions);
        if (view instanceof Component) {
            if (withDefault.topBar.drawBehind.isTrue() && !withDefault.layout.topMargin.hasValue()) {
                ((Component) view).drawBehindTopBar();
            } else if (options.topBar.drawBehind.isFalseOrUndefined()) {
                ((Component) view).drawBelowTopBar(topBar);
            }
        }
        applyTopBarVisibility(withDefault.topBar, withDefault.animations, options);
    }

    public void applyChildOptions(Options options, Component child) {
        Options withDefaultOptions = options.copy().withDefaultOptions(defaultOptions);
        applyOrientation(withDefaultOptions.layout.orientation);
        applyButtons(withDefaultOptions.topBar.buttons);
        applyTopBarOptions(withDefaultOptions.topBar, withDefaultOptions.animations, child, options);
        applyTopTabsOptions(withDefaultOptions.topTabs);
        applyTopTabOptions(withDefaultOptions.topTabOptions);
    }

    public void applyOrientation(OrientationOptions options) {
        OrientationOptions withDefaultOptions = options.copy().mergeWithDefault(defaultOptions.layout.orientation);
        ((Activity) topBar.getContext()).setRequestedOrientation(withDefaultOptions.getValue());
    }

    private void applyTopBarOptions(TopBarOptions options, AnimationsOptions animationOptions, Component component, Options componentOptions) {
        topBar.setHeight(options.height.get(LayoutParams.WRAP_CONTENT));
        topBar.setElevation(options.elevation.get(4d));

        topBar.setTitleHeight(options.title.height.get(LayoutParams.WRAP_CONTENT));
        topBar.setTitle(options.title.text.get(""));
        if (options.title.component.hasValue()) topBar.setTitleComponent(options.title.component);
        topBar.setTitleFontSize(options.title.fontSize.get(defaultTitleFontSize));
        topBar.setTitleTextColor(options.title.color.get(DEFAULT_TITLE_COLOR));
        topBar.setTitleTypeface(options.title.fontFamily);
        topBar.setTitleAlignment(options.title.alignment);

        topBar.setSubtitle(options.subtitle.text.get(""));
        topBar.setSubtitleFontSize(options.subtitle.fontSize.get(defaultSubtitleFontSize));
        topBar.setSubtitleColor(options.subtitle.color.get(DEFAULT_SUBTITLE_COLOR));
        topBar.setSubtitleFontFamily(options.subtitle.fontFamily);
        topBar.setSubtitleAlignment(options.subtitle.alignment);

        topBar.setBorderHeight(options.borderHeight.get(0d));
        topBar.setBorderColor(options.borderColor.get(DEFAULT_BORDER_COLOR));

        topBar.setBackgroundColor(options.background.color.get(Color.WHITE));
        topBar.setBackgroundComponent(options.background.component);
        if (options.testId.hasValue()) topBar.setTestId(options.testId.get());
        applyTopBarVisibility(options, animationOptions, componentOptions);
        if (options.drawBehind.isTrue() && !componentOptions.layout.topMargin.hasValue()) {
            component.drawBehindTopBar();
        } else if (options.drawBehind.isFalseOrUndefined()) {
            component.drawBelowTopBar(topBar);
        }
        if (options.hideOnScroll.isTrue()) {
            if (component instanceof IReactView) {
                topBar.enableCollapse(((IReactView) component).getScrollEventListener());
            }
        } else if (options.hideOnScroll.isFalseOrUndefined()) {
            topBar.disableCollapse();
        }
    }

    private void applyTopBarVisibility(TopBarOptions options, AnimationsOptions animationOptions, Options componentOptions) {
        if (options.visible.isFalse()) {
            if (options.animate.isTrueOrUndefined() && componentOptions.animations.push.enable.isTrueOrUndefined()) {
                topBar.hideAnimate(animationOptions.pop.topBar);
            } else {
                topBar.hide();
            }
        }
        if (options.visible.isTrueOrUndefined()) {
            if (options.animate.isTrueOrUndefined() && componentOptions.animations.push.enable.isTrueOrUndefined()) {
                topBar.showAnimate(animationOptions.push.topBar);
            } else {
                topBar.show();
            }
        }
    }

    private void applyButtons(TopBarButtons buttons) {
        topBar.setLeftButtons(buttons.left);
        topBar.setRightButtons(buttons.right);
        if (buttons.back.visible.isTrue() && !buttons.hasLeftButtons()) topBar.setBackButton(buttons.back);
    }

    private void applyTopTabsOptions(TopTabsOptions options) {
        topBar.applyTopTabsColors(options.selectedTabColor, options.unselectedTabColor);
        topBar.applyTopTabsFontSize(options.fontSize);
        topBar.setTopTabsVisible(options.visible.isTrueOrUndefined());
        topBar.setTopTabsHeight(options.height.get(LayoutParams.WRAP_CONTENT));
    }

    private void applyTopTabOptions(TopTabOptions topTabOptions) {
        if (topTabOptions.fontFamily != null) topBar.setTopTabFontFamily(topTabOptions.tabIndex, topTabOptions.fontFamily);
    }

    public void onChildWillAppear(Options appearing, Options disappearing) {
        if (disappearing.topBar.visible.isTrueOrUndefined() && appearing.topBar.visible.isFalse()) {
            if (disappearing.topBar.animate.isTrueOrUndefined() && disappearing.animations.pop.enable.isTrueOrUndefined()) {
                topBar.hideAnimate(disappearing.animations.pop.topBar);
            } else {
                topBar.hide();
            }
        }
    }

    public void mergeChildOptions(Options options, Component child) {
        mergeOrientation(options.layout.orientation);
        mergeButtons(options.topBar.buttons);
        mergeTopBarOptions(options.topBar, options.animations, child);
        mergeTopTabsOptions(options.topTabs);
        mergeTopTabOptions(options.topTabOptions);
    }

    private void mergeOrientation(OrientationOptions orientationOptions) {
        if (orientationOptions.hasValue()) applyOrientation(orientationOptions);
    }

    private void mergeButtons(TopBarButtons buttons) {
        if (buttons.left != null) topBar.setLeftButtons(buttons.left);
        if (buttons.right != null) topBar.setRightButtons(buttons.right);
        if (buttons.back.hasValue()) topBar.setBackButton(buttons.back);
    }

    private void mergeTopBarOptions(TopBarOptions options, AnimationsOptions animationsOptions, Component component) {
        if (options.height.hasValue()) topBar.setHeight(options.height.get());
        if (options.elevation.hasValue()) topBar.setElevation(options.elevation.get());

        if (options.title.height.hasValue()) topBar.setTitleHeight(options.title.height.get());
        if (options.title.text.hasValue()) topBar.setTitle(options.title.text.get());
        if (options.title.component.hasValue()) topBar.setTitleComponent(options.title.component);
        if (options.title.color.hasValue()) topBar.setTitleTextColor(options.title.color.get());
        if (options.title.fontSize.hasValue()) topBar.setTitleFontSize(options.title.fontSize.get());
        if (options.title.fontFamily != null) topBar.setTitleTypeface(options.title.fontFamily);

        if (options.subtitle.text.hasValue()) topBar.setSubtitle(options.subtitle.text.get());
        if (options.subtitle.color.hasValue()) topBar.setSubtitleColor(options.subtitle.color.get());
        if (options.subtitle.fontSize.hasValue()) topBar.setSubtitleFontSize(options.subtitle.fontSize.get());
        if (options.subtitle.fontFamily != null) topBar.setSubtitleFontFamily(options.subtitle.fontFamily);

        if (options.background.color.hasValue()) topBar.setBackgroundColor(options.background.color.get());

        if (options.testId.hasValue()) topBar.setTestId(options.testId.get());

        if (options.visible.isFalse()) {
            if (options.animate.isTrueOrUndefined()) {
                topBar.hideAnimate(animationsOptions.pop.topBar);
            } else {
                topBar.hide();
            }
        }
        if (options.visible.isTrue()) {
            if (options.animate.isTrueOrUndefined()) {
                topBar.showAnimate(animationsOptions.push.topBar);
            } else {
                topBar.show();
            }
        }
        if (options.drawBehind.isTrue()) {
            component.drawBehindTopBar();
        }
        if (options.drawBehind.isFalse()) {
            component.drawBelowTopBar(topBar);
        }
        if (options.hideOnScroll.isTrue() && component instanceof IReactView) {
            topBar.enableCollapse(((IReactView) component).getScrollEventListener());
        }
        if (options.hideOnScroll.isFalse()) {
            topBar.disableCollapse();
        }
    }

    private void mergeTopTabsOptions(TopTabsOptions options) {
        if (options.selectedTabColor.hasValue() && options.unselectedTabColor.hasValue()) topBar.applyTopTabsColors(options.selectedTabColor, options.unselectedTabColor);
        if (options.fontSize.hasValue()) topBar.applyTopTabsFontSize(options.fontSize);
        if (options.visible.hasValue()) topBar.setTopTabsVisible(options.visible.isTrue());
        if (options.height.hasValue()) topBar.setTopTabsHeight(options.height.get(LayoutParams.WRAP_CONTENT));
    }

    private void mergeTopTabOptions(TopTabOptions topTabOptions) {
        if (topTabOptions.fontFamily != null) topBar.setTopTabFontFamily(topTabOptions.tabIndex, topTabOptions.fontFamily);
    }
}

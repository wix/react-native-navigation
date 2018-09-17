package com.reactnativenavigation.presentation;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

import com.reactnativenavigation.parse.Alignment;
import com.reactnativenavigation.parse.AnimationsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.OrientationOptions;
import com.reactnativenavigation.parse.TopBarButtons;
import com.reactnativenavigation.parse.TopBarOptions;
import com.reactnativenavigation.parse.TopTabOptions;
import com.reactnativenavigation.parse.TopTabsOptions;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.utils.ButtonOptionsPresenter;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.TitleBarButtonController;
import com.reactnativenavigation.viewcontrollers.TitleBarReactViewController;
import com.reactnativenavigation.viewcontrollers.button.NavigationIconResolver;
import com.reactnativenavigation.views.Component;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;
import com.reactnativenavigation.views.topbar.TopBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.reactnativenavigation.utils.CollectionUtils.forEach;
import static com.reactnativenavigation.utils.CollectionUtils.keyBy;
import static com.reactnativenavigation.utils.CollectionUtils.merge;

public class StackOptionsPresenter {
    private static final int DEFAULT_TITLE_COLOR = Color.BLACK;
    private static final int DEFAULT_SUBTITLE_COLOR = Color.GRAY;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final double DEFAULT_ELEVATION = 4d;
    private final double defaultTitleFontSize;
    private final double defaultSubtitleFontSize;
    private final Activity activity;

    private TopBar topBar;
    private final TitleBarReactViewCreator titleViewCreator;
    private TitleBarButtonController.OnClickListener onClickListener;
    private final ImageLoader imageLoader;
    private final ReactViewCreator buttonCreator;
    private Options defaultOptions;
    private Map<Component, TitleBarReactViewController> titleComponentViewControllers = new HashMap<>();
    private Map<Component, Map<String, TitleBarButtonController>> componentRightButtons = new HashMap<>();
    private Map<Component, Map<String, TitleBarButtonController>> componentLeftButtons = new HashMap<>();

    public StackOptionsPresenter(Activity activity, TitleBarReactViewCreator titleViewCreator, ReactViewCreator buttonCreator, ImageLoader imageLoader, Options defaultOptions) {
        this.activity = activity;
        this.titleViewCreator = titleViewCreator;
        this.buttonCreator = buttonCreator;
        this.imageLoader = imageLoader;
        this.defaultOptions = defaultOptions;
        defaultTitleFontSize = UiUtils.dpToSp(activity, 18);
        defaultSubtitleFontSize = UiUtils.dpToSp(activity, 14);
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void setButtonOnClickListener(TitleBarButtonController.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Options getDefaultOptions() {
        return defaultOptions;
    }

    public List<TitleBarButtonController> getComponentButtons(Component child) {
        return merge(getRightButtons(child), getLeftButtons(child), Collections.EMPTY_LIST);
    }

    public List<TitleBarButtonController> getComponentButtons(Component child, List<TitleBarButtonController> defaultValue) {
        return merge(getRightButtons(child), getLeftButtons(child), defaultValue);
    }

    private List<TitleBarButtonController> getRightButtons(Component child) {
        return componentRightButtons.containsKey(child) ? new ArrayList<>(componentRightButtons.get(child).values()) : null;
    }

    private List<TitleBarButtonController> getLeftButtons(Component child) {
        return componentLeftButtons.containsKey(child) ? new ArrayList<>(componentLeftButtons.get(child).values()) : null;
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
    }

    public void applyInitialChildLayoutOptions(Options options) {
        Options withDefault = options.copy().withDefaultOptions(defaultOptions);
        setInitialTopBarVisibility(withDefault.topBar);
    }

    public void applyChildOptions(Options options, Component child) {
        Options withDefault = options.copy().withDefaultOptions(defaultOptions);
        applyOrientation(withDefault.layout.orientation);
        applyButtons(withDefault.topBar, child);
        applyTopBarOptions(withDefault.topBar, withDefault.animations, child, options);
        applyTopTabsOptions(withDefault.topTabs);
        applyTopTabOptions(withDefault.topTabOptions);
    }

    public void applyOrientation(OrientationOptions options) {
        OrientationOptions withDefaultOptions = options.copy().mergeWithDefault(defaultOptions.layout.orientation);
        ((Activity) topBar.getContext()).setRequestedOrientation(withDefaultOptions.getValue());
    }

    public void onChildDestroyed(Component child) {
        TitleBarReactViewController removed = titleComponentViewControllers.remove(child);
        if (removed != null) {
            removed.destroy();
        }
        destroyButtons(componentRightButtons.get(child));
        destroyButtons(componentLeftButtons.get(child));
        componentRightButtons.remove(child);
        componentLeftButtons.remove(child);
    }

    private void destroyButtons(Map<String, TitleBarButtonController> buttons) {
        if (buttons != null) {
            for (TitleBarButtonController button : buttons.values()) {
                button.destroy();
            }
        }
    }

    private void applyTopBarOptions(TopBarOptions options, AnimationsOptions animationOptions, Component component, Options componentOptions) {
        topBar.setHeight(options.height.get(LayoutParams.WRAP_CONTENT));
        topBar.setElevation(options.elevation.get(DEFAULT_ELEVATION));
        if (topBar.getLayoutParams() instanceof MarginLayoutParams) {
            ((MarginLayoutParams) topBar.getLayoutParams()).topMargin = UiUtils.dpToPx(activity, options.topMargin.get(0));
        }

        topBar.setTitleHeight(options.title.height.get(LayoutParams.WRAP_CONTENT));
        topBar.setTitle(options.title.text.get(""));

        if (options.title.component.hasValue()) {
            if (titleComponentViewControllers.containsKey(component)) {
                topBar.setTitleComponent(titleComponentViewControllers.get(component).getView());
            } else {
                TitleBarReactViewController controller = new TitleBarReactViewController(activity, titleViewCreator);
                titleComponentViewControllers.put(component, controller);
                controller.setComponent(options.title.component);
                controller.getView().setLayoutParams(getComponentLayoutParams(options.title.component));
                topBar.setTitleComponent(controller.getView());
            }
        }

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

    private void setInitialTopBarVisibility(TopBarOptions options) {
        if (options.visible.isFalse()) {
            topBar.hide();
        }
        if (options.visible.isTrueOrUndefined()) {
            topBar.show();
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

    private void applyButtons(TopBarOptions options, Component child) {
        List<Button> rightButtons = mergeButtonsWithColor(options.buttons.right, options.rightButtonColor, options.rightButtonDisabledColor);
        List<Button> leftButtons = mergeButtonsWithColor(options.buttons.left, options.leftButtonColor, options.leftButtonDisabledColor);

        if (rightButtons != null) {
            List<TitleBarButtonController> rightButtonControllers = getOrCreateButtonControllers(componentRightButtons.get(child), rightButtons);
            componentRightButtons.put(child, keyBy(rightButtonControllers, TitleBarButtonController::getButtonInstanceId));
            topBar.setRightButtons(rightButtonControllers);
        } else {
            topBar.setRightButtons(null);
        }

        if (leftButtons != null) {
            List<TitleBarButtonController> leftButtonControllers = getOrCreateButtonControllers(componentLeftButtons.get(child), leftButtons);
            componentLeftButtons.put(child, keyBy(leftButtonControllers, TitleBarButtonController::getButtonInstanceId));
            topBar.setLeftButtons(leftButtonControllers);
        } else {
            topBar.setLeftButtons(null);
        }

        if (options.buttons.back.visible.isTrue() && !options.buttons.hasLeftButtons()) {
            topBar.setBackButton(createButtonController(options.buttons.back));
        }
    }

    private List<TitleBarButtonController> getOrCreateButtonControllers(@Nullable Map<String, TitleBarButtonController> currentButtons, @Nullable List<Button> buttons) {
        if (buttons == null) return null;
        Map<String, TitleBarButtonController> result = new LinkedHashMap<>();
        for (Button b : buttons) {
            result.put(b.instanceId, currentButtons != null && currentButtons.containsKey(b.instanceId) ? currentButtons.get(b.instanceId) : createButtonController(b));
        }
        return new ArrayList<>(result.values());
    }

    private TitleBarButtonController createButtonController(Button button) {
        return new TitleBarButtonController(activity,
                new NavigationIconResolver(activity, imageLoader),
                imageLoader,
                new ButtonOptionsPresenter(topBar.getTitleBar(), button),
                button,
                buttonCreator,
                onClickListener
        );
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

    public void mergeChildOptions(Options toMerge, Options resolvedOptions, Component child) {
        TopBarOptions topBar = toMerge.copy().mergeWith(resolvedOptions).withDefaultOptions(defaultOptions).topBar;
        mergeOrientation(toMerge.layout.orientation);
        mergeButtons(topBar, toMerge.topBar.buttons, child);
        mergeTopBarOptions(toMerge.topBar, toMerge.animations, child);
        mergeTopTabsOptions(toMerge.topTabs);
        mergeTopTabOptions(toMerge.topTabOptions);
    }

    private void mergeOrientation(OrientationOptions orientationOptions) {
        if (orientationOptions.hasValue()) applyOrientation(orientationOptions);
    }

    private void mergeButtons(TopBarOptions options, TopBarButtons buttons, Component child) {
        List<Button> rightButtons = mergeButtonsWithColor(buttons.right, options.rightButtonColor, options.rightButtonDisabledColor);
        List<Button> leftButtons = mergeButtonsWithColor(buttons.left, options.leftButtonColor, options.leftButtonDisabledColor);

        List<TitleBarButtonController> rightButtonControllers = getOrCreateButtonControllers(componentRightButtons.get(child), rightButtons);
        List<TitleBarButtonController> leftButtonControllers = getOrCreateButtonControllers(componentLeftButtons.get(child), leftButtons);

        if (rightButtonControllers != null) {
            Map previousRightButtons = componentRightButtons.put(child, keyBy(rightButtonControllers, TitleBarButtonController::getButtonInstanceId));
            if (previousRightButtons != null) forEach(previousRightButtons.values(), TitleBarButtonController::destroy);
        }
        if (leftButtonControllers != null) {
            Map previousLeftButtons = componentLeftButtons.put(child, keyBy(leftButtonControllers, TitleBarButtonController::getButtonInstanceId));
            if (previousLeftButtons != null) forEach(previousLeftButtons.values(), TitleBarButtonController::destroy);
        }

        if (buttons.right != null) topBar.setRightButtons(rightButtonControllers);
        if (buttons.left != null) topBar.setLeftButtons(leftButtonControllers);
        if (buttons.back.hasValue()) topBar.setBackButton(createButtonController(buttons.back));
    }

    @Nullable
    private List<Button> mergeButtonsWithColor(List<Button> buttons, Colour buttonColor, Colour disabledColor) {
        List<Button> result = null;
        if (buttons != null) {
            result = new ArrayList<>();
            for (Button button : buttons) {
                Button copy = button.copy();
                if (!button.color.hasValue()) copy.color = buttonColor;
                if (!button.disabledColor.hasValue()) copy.disabledColor = disabledColor;
                result.add(copy);
            }
        }
        return result;
    }

    private void mergeTopBarOptions(TopBarOptions options, AnimationsOptions animationsOptions, Component component) {
        if (options.height.hasValue()) topBar.setHeight(options.height.get());
        if (options.elevation.hasValue()) topBar.setElevation(options.elevation.get());
        if (options.topMargin.hasValue() && topBar.getLayoutParams() instanceof MarginLayoutParams) {
            ((MarginLayoutParams) topBar.getLayoutParams()).topMargin = UiUtils.dpToPx(activity, options.topMargin.get());
        }

        if (options.title.height.hasValue()) topBar.setTitleHeight(options.title.height.get());
        if (options.title.text.hasValue()) topBar.setTitle(options.title.text.get());

        if (options.title.component.hasValue()) {
            if (titleComponentViewControllers.containsKey(component)) {
                topBar.setTitleComponent(titleComponentViewControllers.get(component).getView());
            } else {
                TitleBarReactViewController controller = new TitleBarReactViewController(activity, titleViewCreator);
                titleComponentViewControllers.put(component, controller);
                controller.setComponent(options.title.component);
                controller.getView().setLayoutParams(getComponentLayoutParams(options.title.component));
                topBar.setTitleComponent(controller.getView());
            }
        }

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

    private LayoutParams getComponentLayoutParams(com.reactnativenavigation.parse.Component component) {
        return new Toolbar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, component.alignment == Alignment.Center ? Gravity.CENTER : Gravity.START);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public Map<Component, TitleBarReactViewController> getTitleComponents() {
        return titleComponentViewControllers;
    }
}

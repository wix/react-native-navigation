package com.reactnativenavigation.presentation;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

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
import com.reactnativenavigation.utils.ButtonPresenter;
import com.reactnativenavigation.utils.CollectionUtils;
import com.reactnativenavigation.utils.ObjectUtils;
import com.reactnativenavigation.utils.StatusBarUtils;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.viewcontrollers.TitleBarButtonController;
import com.reactnativenavigation.viewcontrollers.TitleBarReactViewController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.button.IconResolver;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.titlebar.TitleBarButtonCreator;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;
import com.reactnativenavigation.views.topbar.TopBar;
import com.reactnativenavigation.views.topbar.TopBarBackgroundViewCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.appcompat.widget.Toolbar;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static com.reactnativenavigation.utils.ObjectUtils.perform;
import static com.reactnativenavigation.utils.ObjectUtils.take;

public class StackPresenter {
    private static final int DEFAULT_TITLE_COLOR = Color.BLACK;
    private static final int DEFAULT_SUBTITLE_COLOR = Color.GRAY;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final double DEFAULT_ELEVATION = 4d;
    private final double defaultTitleFontSize;
    private final double defaultSubtitleFontSize;
    private final Activity activity;

    private TopBar topBar;
    private TopBarController topBarController;
    private final TitleBarReactViewCreator titleViewCreator;
    private TitleBarButtonController.OnClickListener onClickListener;
    private final RenderChecker renderChecker;
    private final TopBarBackgroundViewCreator topBarBackgroundViewCreator;
    private final TitleBarButtonCreator buttonCreator;
    private Options defaultOptions;

    private List<TitleBarButtonController> currentRightButtons = new ArrayList<>();
    private Map<View, TitleBarReactViewController> titleControllers = new HashMap();
    private Map<View, TopBarBackgroundViewController> backgroundControllers = new HashMap();
    private Map<View, Map<String, TitleBarButtonController>> componentRightButtons = new HashMap();
    private Map<View, Map<String, TitleBarButtonController>> componentLeftButtons = new HashMap();
    private IconResolver iconResolver;

    public StackPresenter(Activity activity,
                          TitleBarReactViewCreator titleViewCreator,
                          TopBarBackgroundViewCreator topBarBackgroundViewCreator,
                          TitleBarButtonCreator buttonCreator,
                          IconResolver iconResolver,
                          RenderChecker renderChecker,
                          Options defaultOptions) {
        this.activity = activity;
        this.titleViewCreator = titleViewCreator;
        this.topBarBackgroundViewCreator = topBarBackgroundViewCreator;
        this.buttonCreator = buttonCreator;
        this.iconResolver = iconResolver;
        this.renderChecker = renderChecker;
        this.defaultOptions = defaultOptions;
        defaultTitleFontSize = 18;
        defaultSubtitleFontSize = 14;
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

    public void bindView(TopBarController topBarController) {
        this.topBarController = topBarController;
        topBar = topBarController.getView();
    }

    public boolean isRendered(View component) {
        ArrayList<ViewController> controllers = new ArrayList<>(perform(componentRightButtons.get(component), new ArrayList<>(), Map::values));
        controllers.add(backgroundControllers.get(component));
        controllers.add(titleControllers.get(component));
        return renderChecker.areRendered(filter(controllers, ObjectUtils::notNull));
    }

    public void mergeOptions(Options options, StackController stack, ViewController currentChild) {
        mergeOrientation(options.layout.orientation);
//        mergeButtons(topBar, withDefault.topBar.buttons, child);
        mergeTopBarOptions(options, stack, currentChild);
        mergeTopTabsOptions(options.topTabs);
        mergeTopTabOptions(options.topTabOptions);
    }

    public void applyInitialChildLayoutOptions(Options options) {
        Options withDefault = options.copy().withDefaultOptions(defaultOptions);
        setInitialTopBarVisibility(withDefault.topBar);
    }

    public void applyChildOptions(Options options, StackController stack, ViewController child) {
        Options withDefault = options.copy().withDefaultOptions(defaultOptions);
        applyOrientation(withDefault.layout.orientation);
        applyButtons(withDefault.topBar, child);
        applyTopBarOptions(withDefault, stack, child, options);
        applyTopTabsOptions(withDefault.topTabs);
        applyTopTabOptions(withDefault.topTabOptions);
    }

    public void applyOrientation(OrientationOptions options) {
        OrientationOptions withDefaultOptions = options.copy().mergeWithDefault(defaultOptions.layout.orientation);
        ((Activity) topBar.getContext()).setRequestedOrientation(withDefaultOptions.getValue());
    }

    public void onChildDestroyed(ViewController child) {
        perform(titleControllers.remove(child.getView()), TitleBarReactViewController::destroy);
        perform(backgroundControllers.remove(child.getView()), TopBarBackgroundViewController::destroy);
        destroyButtons(componentRightButtons.get(child.getView()));
        destroyButtons(componentLeftButtons.get(child.getView()));
        componentRightButtons.remove(child.getView());
        componentLeftButtons.remove(child.getView());
    }

    private void destroyButtons(@Nullable Map<String, TitleBarButtonController> buttons) {
        if (buttons != null) forEach(buttons.values(), ViewController::destroy);
    }

    private void applyTopBarOptions(Options options, StackController stack, ViewController child, Options componentOptions) {
        final View component = child.getView();
        TopBarOptions topBarOptions = options.topBar;
        AnimationsOptions animationOptions = options.animations;

        topBar.setTestId(topBarOptions.testId.get(""));
        topBar.setLayoutDirection(options.layout.direction);
        topBar.setHeight(topBarOptions.height.get(UiUtils.getTopBarHeightDp(activity)));
        topBar.setElevation(topBarOptions.elevation.get(DEFAULT_ELEVATION));
        if (topBar.getLayoutParams() instanceof MarginLayoutParams) {
            ((MarginLayoutParams) topBar.getLayoutParams()).topMargin = UiUtils.dpToPx(activity, topBarOptions.topMargin.get(0));
        }

        topBar.setTitleHeight(topBarOptions.title.height.get(UiUtils.getTopBarHeightDp(activity)));
        topBar.setTitle(topBarOptions.title.text.get(""));
        topBar.setTitleTopMargin(topBarOptions.title.topMargin.get(0));

        if (topBarOptions.title.component.hasValue()) {
            if (titleControllers.containsKey(component)) {
                topBarController.setTitleComponent(titleControllers.get(component));
            } else {
                TitleBarReactViewController controller = new TitleBarReactViewController(activity, titleViewCreator, topBarOptions.title.component);
                controller.setWaitForRender(topBarOptions.title.component.waitForRender);
                titleControllers.put(component, controller);
                controller.getView().setLayoutParams(getComponentLayoutParams(topBarOptions.title.component));
                topBarController.setTitleComponent(controller);
            }
        }

        topBar.setTitleFontSize(topBarOptions.title.fontSize.get(defaultTitleFontSize));
        topBar.setTitleTextColor(topBarOptions.title.color.get(DEFAULT_TITLE_COLOR));
        topBar.setTitleTypeface(topBarOptions.title.fontFamily);
        topBar.setTitleAlignment(topBarOptions.title.alignment);

        topBar.setSubtitle(topBarOptions.subtitle.text.get(""));
        topBar.setSubtitleFontSize(topBarOptions.subtitle.fontSize.get(defaultSubtitleFontSize));
        topBar.setSubtitleColor(topBarOptions.subtitle.color.get(DEFAULT_SUBTITLE_COLOR));
        topBar.setSubtitleFontFamily(topBarOptions.subtitle.fontFamily);
        topBar.setSubtitleAlignment(topBarOptions.subtitle.alignment);

        topBar.setBorderHeight(topBarOptions.borderHeight.get(0d));
        topBar.setBorderColor(topBarOptions.borderColor.get(DEFAULT_BORDER_COLOR));

        topBar.setBackgroundColor(topBarOptions.background.color.get(Color.WHITE));

        if (topBarOptions.background.component.hasValue()) {
            View createdComponent = findBackgroundComponent(topBarOptions.background.component);
            if (createdComponent != null) {
                topBar.setBackgroundComponent(createdComponent);
            } else {
                TopBarBackgroundViewController controller = new TopBarBackgroundViewController(activity, topBarBackgroundViewCreator);
                controller.setWaitForRender(topBarOptions.background.waitForRender);
                backgroundControllers.put(component, controller);
                controller.setComponent(topBarOptions.background.component);
                controller.getView().setLayoutParams(new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                topBar.setBackgroundComponent(controller.getView());
            }
        } else {
            topBar.clearBackgroundComponent();
        }

        applyTopBarVisibility(topBarOptions, animationOptions, componentOptions, stack, child);
        if (topBarOptions.hideOnScroll.isTrue()) {
            if (component instanceof IReactView) {
                topBar.enableCollapse(((IReactView) component).getScrollEventListener());
            }
        } else if (topBarOptions.hideOnScroll.isFalseOrUndefined()) {
            topBar.disableCollapse();
        }
    }

    @Nullable
    private View findBackgroundComponent(com.reactnativenavigation.parse.Component component) {
        for (TopBarBackgroundViewController controller : backgroundControllers.values()) {
            if (ObjectUtils.equalsNotNull(controller.getComponent().name.get(null), component.name.get(null)) &&
                ObjectUtils.equalsNotNull(controller.getComponent().componentId.get(null), component.componentId.get(null))) {
                return controller.getView();
            }
        }
        return null;
    }

    private void setInitialTopBarVisibility(TopBarOptions options) {
        if (options.visible.isFalse()) {
            topBarController.hide();
        }
        if (options.visible.isTrueOrUndefined()) {
            topBarController.show();
        }
    }

    private void applyTopBarVisibility(TopBarOptions options, AnimationsOptions animationOptions, Options componentOptions, StackController stack, ViewController child) {
        if (options.visible.isFalse()) {
            topBarController.resetViewProperties();
            if (options.animate.isTrueOrUndefined() && componentOptions.animations.push.enabled.isTrueOrUndefined()) {
                topBarController.hideAnimate(animationOptions.pop.topBar, 0, getTopBarTranslationAnimationDelta(stack, child));
            } else {
                topBarController.hide();
            }
        } else if (options.visible.isTrueOrUndefined()) {
            topBarController.resetViewProperties();
            if (options.animate.isTrueOrUndefined() && componentOptions.animations.push.enabled.isTrueOrUndefined()) {
                topBarController.showAnimate(animationOptions.push.topBar, getTopBarTranslationAnimationDelta(stack, child));
            } else {
                topBarController.show();
            }
        }
    }

    private void applyButtons(TopBarOptions options, ViewController child) {
        if (options.buttons.right != null) {
            List<Button> rightButtons = mergeButtonsWithColor(options.buttons.right, options.rightButtonColor, options.rightButtonDisabledColor);
            List<TitleBarButtonController> rightButtonControllers = getOrCreateButtonControllersByInstanceId(componentRightButtons.get(child.getView()), rightButtons);
            componentRightButtons.put(child.getView(), keyBy(rightButtonControllers, TitleBarButtonController::getButtonInstanceId));
            if (!CollectionUtils.equals(currentRightButtons, rightButtonControllers)) {
                currentRightButtons = rightButtonControllers;
                topBarController.applyRightButtons(currentRightButtons);
            }
        } else {
            currentRightButtons = null;
            topBar.clearRightButtons();
        }

        if (options.buttons.left != null) {
            List<Button> leftButtons = mergeButtonsWithColor(options.buttons.left, options.leftButtonColor, options.leftButtonDisabledColor);
            List<TitleBarButtonController> leftButtonControllers = getOrCreateButtonControllersByInstanceId(componentLeftButtons.get(child.getView()), leftButtons);
            componentLeftButtons.put(child.getView(), keyBy(leftButtonControllers, TitleBarButtonController::getButtonInstanceId));
            topBarController.setLeftButtons(leftButtonControllers);
        } else {
            topBar.clearLeftButtons();
        }

        if (options.buttons.back.visible.isTrue() && !options.buttons.hasLeftButtons()) {
            topBar.setBackButton(createButtonController(options.buttons.back));
        }

        topBar.setOverflowButtonColor(options.rightButtonColor.get(Color.BLACK));
    }

    private List<TitleBarButtonController> getOrCreateButtonControllersByInstanceId(@Nullable Map<String, TitleBarButtonController> currentButtons, @Nullable List<Button> buttons) {
        if (buttons == null) return null;
        Map<String, TitleBarButtonController> result = new LinkedHashMap<>();
        forEach(buttons, b -> result.put(b.instanceId, getOrDefault(currentButtons, b.instanceId, () -> createButtonController(b))));
        return new ArrayList<>(result.values());
    }

    private List<TitleBarButtonController> getOrCreateButtonControllers(@Nullable Map<String, TitleBarButtonController> currentButtons, @NonNull List<Button> buttons) {
        ArrayList result = new ArrayList<TitleBarButtonController>();
        for (Button b : buttons) {
            result.add(take(first(perform(currentButtons, null, Map::values), button -> button.getButton().equals(b)), createButtonController(b)));
        }
        return result;
    }

    private TitleBarButtonController createButtonController(Button button) {
        TitleBarButtonController controller = new TitleBarButtonController(activity,
                new ButtonPresenter(button, iconResolver),
                button,
                buttonCreator,
                onClickListener
        );
        controller.setWaitForRender(button.component.waitForRender);
        return controller;
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

    public void onChildWillAppear(StackController parent, ViewController appearing, ViewController disappearing) {
        if (disappearing.options.topBar.visible.isTrueOrUndefined() && appearing.options.topBar.visible.isFalse()) {
            if (disappearing.options.topBar.animate.isTrueOrUndefined() && disappearing.options.animations.pop.enabled.isTrueOrUndefined()) {
                topBarController.hideAnimate(disappearing.options.animations.pop.topBar, 0, getTopBarTranslationAnimationDelta(parent, appearing));
            } else {
                topBarController.hide();
            }
        }
    }

    public void mergeChildOptions(Options toMerge, Options resolvedOptions, StackController stack, ViewController child) {
        TopBarOptions topBar = toMerge.copy().mergeWith(resolvedOptions).withDefaultOptions(defaultOptions).topBar;
        mergeOrientation(toMerge.layout.orientation);
        mergeButtons(topBar, toMerge.topBar.buttons, child.getView());
        mergeTopBarOptions(toMerge, stack, child);
        mergeTopTabsOptions(toMerge.topTabs);
        mergeTopTabOptions(toMerge.topTabOptions);
    }

    private void mergeOrientation(OrientationOptions orientationOptions) {
        if (orientationOptions.hasValue()) applyOrientation(orientationOptions);
    }

    private void mergeButtons(TopBarOptions options, TopBarButtons buttons, View child) {
        mergeRightButtons(options, buttons, child);
        mergeLeftButton(options, buttons, child);
        mergeBackButton(buttons);
    }

    private void mergeRightButtons(TopBarOptions options, TopBarButtons buttons, View child) {
        if (buttons.right == null) return;
        List<Button> rightButtons = mergeButtonsWithColor(buttons.right, options.rightButtonColor, options.rightButtonDisabledColor);
        List<TitleBarButtonController> toMerge = getOrCreateButtonControllers(componentRightButtons.get(child), rightButtons);
        List<TitleBarButtonController> toRemove = difference(currentRightButtons, toMerge, TitleBarButtonController::areButtonsEqual);
        forEach(toRemove, TitleBarButtonController::destroy);

        if (!CollectionUtils.equals(currentRightButtons, toMerge)) {
            topBarController.mergeRightButtons(toMerge, toRemove);
            currentRightButtons = toMerge;
        }
        if (options.rightButtonColor.hasValue()) topBar.setOverflowButtonColor(options.rightButtonColor.get());
    }

    private void mergeLeftButton(TopBarOptions options, TopBarButtons buttons, View child) {
        if (buttons.left == null) return;
        List<Button> leftButtons = mergeButtonsWithColor(buttons.left, options.leftButtonColor, options.leftButtonDisabledColor);
        List<TitleBarButtonController> toMerge = getOrCreateButtonControllers(componentLeftButtons.get(child), leftButtons);
        componentLeftButtons.put(child, keyBy(toMerge, TitleBarButtonController::getButtonInstanceId));
        topBarController.setLeftButtons(toMerge);
    }

    private void mergeBackButton(TopBarButtons buttons) {
        if (buttons.back.hasValue()) {
            if (buttons.back.visible.isFalse()) {
                topBar.clearLeftButtons();
            } else {
                topBar.setBackButton(createButtonController(buttons.back));
            }
        }
    }

    private List<Button> mergeButtonsWithColor(@NonNull List<Button> buttons, Colour buttonColor, Colour disabledColor) {
        List<Button> result = new ArrayList<>();
        for (Button button : buttons) {
            Button copy = button.copy();
            if (!button.color.hasValue()) copy.color = buttonColor;
            if (!button.disabledColor.hasValue()) copy.disabledColor = disabledColor;
            result.add(copy);
        }
        return result;
    }

    private void mergeTopBarOptions(Options options, StackController stack, ViewController child) {
        AnimationsOptions animationsOptions = options.copy().withDefaultOptions(defaultOptions).animations;
        TopBarOptions topBarOptions = options.topBar;
        final View component = child.getView();
        if (options.layout.direction.hasValue()) topBar.setLayoutDirection(options.layout.direction);
        if (topBarOptions.height.hasValue()) topBar.setHeight(topBarOptions.height.get());
        if (topBarOptions.elevation.hasValue()) topBar.setElevation(topBarOptions.elevation.get());
        if (topBarOptions.topMargin.hasValue() && topBar.getLayoutParams() instanceof MarginLayoutParams) {
            ((MarginLayoutParams) topBar.getLayoutParams()).topMargin = UiUtils.dpToPx(activity, topBarOptions.topMargin.get());
        }

        if (topBarOptions.title.height.hasValue()) topBar.setTitleHeight(topBarOptions.title.height.get());
        if (topBarOptions.title.topMargin.hasValue()) topBar.setTitleTopMargin(topBarOptions.title.topMargin.get());

        if (topBarOptions.title.component.hasValue()) {
            TitleBarReactViewController controller = findTitleComponent(topBarOptions.title.component);
            if (controller == null) {
                controller = new TitleBarReactViewController(activity, titleViewCreator, topBarOptions.title.component);
                perform(titleControllers.put(component, controller), ViewController::destroy);
                controller.getView().setLayoutParams(getComponentLayoutParams(topBarOptions.title.component));
            }
            topBarController.setTitleComponent(controller);
        } else if (topBarOptions.title.text.hasValue()) {
            perform(titleControllers.remove(component), ViewController::destroy);
            topBar.setTitle(topBarOptions.title.text.get());
        }

        if (topBarOptions.title.color.hasValue()) topBar.setTitleTextColor(topBarOptions.title.color.get());
        if (topBarOptions.title.fontSize.hasValue()) topBar.setTitleFontSize(topBarOptions.title.fontSize.get());
        if (topBarOptions.title.fontFamily != null) topBar.setTitleTypeface(topBarOptions.title.fontFamily);

        if (topBarOptions.subtitle.text.hasValue()) topBar.setSubtitle(topBarOptions.subtitle.text.get());
        if (topBarOptions.subtitle.color.hasValue()) topBar.setSubtitleColor(topBarOptions.subtitle.color.get());
        if (topBarOptions.subtitle.fontSize.hasValue()) topBar.setSubtitleFontSize(topBarOptions.subtitle.fontSize.get());
        if (topBarOptions.subtitle.fontFamily != null) topBar.setSubtitleFontFamily(topBarOptions.subtitle.fontFamily);

        if (topBarOptions.background.color.hasValue()) topBar.setBackgroundColor(topBarOptions.background.color.get());

        if (topBarOptions.background.component.hasValue()) {
            if (backgroundControllers.containsKey(component)) {
                topBar.setBackgroundComponent(backgroundControllers.get(component).getView());
            } else {
                TopBarBackgroundViewController controller = new TopBarBackgroundViewController(activity, topBarBackgroundViewCreator);
                backgroundControllers.put(component, controller);
                controller.setComponent(topBarOptions.background.component);
                controller.getView().setLayoutParams(new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                topBar.setBackgroundComponent(controller.getView());
            }
        }

        if (topBarOptions.testId.hasValue()) topBar.setTestId(topBarOptions.testId.get());

        topBarController.resetViewProperties();
        if (topBarOptions.visible.isFalse()) {
            if (topBarOptions.animate.isTrueOrUndefined()) {
                topBarController.hideAnimate(animationsOptions.pop.topBar, 0, getTopBarTranslationAnimationDelta(stack, child));
            } else {
                topBarController.hide();
            }
        }
        if (topBarOptions.visible.isTrue()) {
            if (topBarOptions.animate.isTrueOrUndefined()) {
                topBarController.showAnimate(animationsOptions.push.topBar, getTopBarTranslationAnimationDelta(stack, child));
            } else {
                topBarController.show();
            }
        }
        if (topBarOptions.hideOnScroll.isTrue() && component instanceof IReactView) {
            topBar.enableCollapse(((IReactView) component).getScrollEventListener());
        }
        if (topBarOptions.hideOnScroll.isFalse()) {
            topBar.disableCollapse();
        }
    }

    private TitleBarReactViewController findTitleComponent(com.reactnativenavigation.parse.Component component) {
        for (TitleBarReactViewController controller : titleControllers.values()) {
            if (ObjectUtils.equalsNotNull(controller.getComponent().name.get(null), component.name.get(null)) &&
                ObjectUtils.equalsNotNull(controller.getComponent().componentId.get(null), component.componentId.get(null))) {
                return controller;
            }
        }
        return null;
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
    public Map<View, TitleBarReactViewController> getTitleComponents() {
        return titleControllers;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public Map<View, TopBarBackgroundViewController> getBackgroundComponents() {
        return backgroundControllers;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public List<TitleBarButtonController> getComponentButtons(View child) {
        return merge(getRightButtons(child), getLeftButtons(child), Collections.EMPTY_LIST);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public List<TitleBarButtonController> getComponentButtons(View child, List<TitleBarButtonController> defaultValue) {
        return merge(getRightButtons(child), getLeftButtons(child), defaultValue);
    }

    public void applyTopInsets(StackController stack, ViewController child) {
        if (stack.isCurrentChild(child)) applyStatusBarInsets(stack, child);
        child.applyTopInset();
    }

    private List<TitleBarButtonController> getRightButtons(View child) {
        return componentRightButtons.containsKey(child) ? new ArrayList<>(componentRightButtons.get(child).values()) : null;
    }

    private List<TitleBarButtonController> getLeftButtons(View child) {
        return componentLeftButtons.containsKey(child) ? new ArrayList<>(componentLeftButtons.get(child).values()) : null;
    }

    private void applyStatusBarInsets(StackController stack, ViewController child) {
        MarginLayoutParams lp = (MarginLayoutParams) topBar.getLayoutParams();
        lp.topMargin = getTopBarTopMargin(stack, child);
        topBar.requestLayout();
    }

    private int getTopBarTranslationAnimationDelta(StackController stack, ViewController child) {
        Options options = stack.resolveChildOptions(child).withDefaultOptions(defaultOptions);
        return options.statusBar.hasTransparency() ? getTopBarTopMargin(stack, child) : 0;
    }

    private int getTopBarTopMargin(StackController stack, ViewController child) {
        Options withDefault = stack.resolveChildOptions(child).withDefaultOptions(defaultOptions);
        int topMargin = UiUtils.dpToPx(activity, withDefault.topBar.topMargin.get(0));
        int statusBarInset = withDefault.statusBar.visible.isTrueOrUndefined() ? StatusBarUtils.getStatusBarHeight(child.getActivity()) : 0;
        return topMargin + statusBarInset;
    }

    public int getTopInset(Options resolvedOptions) {
        return resolvedOptions.withDefaultOptions(defaultOptions).topBar.isHiddenOrDrawBehind() ? 0 : topBarController.getHeight();
    }
}

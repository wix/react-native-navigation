package com.reactnativenavigation.viewcontrollers.stack;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.reactnativenavigation.options.Alignment;
import com.reactnativenavigation.options.AnimationsOptions;
import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.options.ComponentOptions;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.OrientationOptions;
import com.reactnativenavigation.options.TopBarButtons;
import com.reactnativenavigation.options.TopBarOptions;
import com.reactnativenavigation.options.TopTabOptions;
import com.reactnativenavigation.options.TopTabsOptions;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.parsers.TypefaceLoader;
import com.reactnativenavigation.utils.CollectionUtils;
import com.reactnativenavigation.utils.ObjectUtils;
import com.reactnativenavigation.utils.RenderChecker;
import com.reactnativenavigation.utils.StatusBarUtils;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarController;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonPresenter;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.IconResolver;
import com.reactnativenavigation.viewcontrollers.stack.topbar.title.TitleBarReactViewController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.IReactView;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.stack.topbar.TopBar;
import com.reactnativenavigation.views.stack.topbar.TopBarBackgroundViewCreator;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarButtonCreator;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarReactViewCreator;

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
    private ButtonController.OnClickListener onClickListener;
    private final RenderChecker renderChecker;
    private final TopBarBackgroundViewCreator topBarBackgroundViewCreator;
    private final TitleBarButtonCreator buttonCreator;
    private Options defaultOptions;

    private List<ButtonController> currentRightButtons = new ArrayList<>();
    private Map<View, TitleBarReactViewController> titleControllers = new HashMap();
    private Map<View, TopBarBackgroundViewController> backgroundControllers = new HashMap();
    private Map<View, Map<String, ButtonController>> componentRightButtons = new HashMap();
    private Map<View, Map<String, ButtonController>> componentLeftButtons = new HashMap();
    private IconResolver iconResolver;
    private TypefaceLoader typefaceLoader;

    public StackPresenter(Activity activity,
                          TitleBarReactViewCreator titleViewCreator,
                          TopBarBackgroundViewCreator topBarBackgroundViewCreator,
                          TitleBarButtonCreator buttonCreator,
                          IconResolver iconResolver,
                          TypefaceLoader typefaceLoader,
                          RenderChecker renderChecker,
                          Options defaultOptions) {
        this.activity = activity;
        this.titleViewCreator = titleViewCreator;
        this.topBarBackgroundViewCreator = topBarBackgroundViewCreator;
        this.buttonCreator = buttonCreator;
        this.iconResolver = iconResolver;
        this.typefaceLoader = typefaceLoader;
        this.renderChecker = renderChecker;
        this.defaultOptions = defaultOptions;
        defaultTitleFontSize = 18;
        defaultSubtitleFontSize = 14;
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void setButtonOnClickListener(ButtonController.OnClickListener onClickListener) {
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
        TopBarOptions resolvedTopBarOptions = options.topBar.copy().mergeWithDefault(stack.resolveChildOptions(currentChild).topBar).mergeWithDefault(defaultOptions.topBar);
        mergeOrientation(options.layout.orientation);
        //        mergeButtons(topBar, withDefault.topBar.buttons, child);
        mergeTopBarOptions(resolvedTopBarOptions, options, stack, currentChild);
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

    private void destroyButtons(@Nullable Map<String, ButtonController> buttons) {
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
        topBar.setTitleTypeface(typefaceLoader, topBarOptions.title.font);
        topBar.setTitleAlignment(topBarOptions.title.alignment);

        topBar.setSubtitle(topBarOptions.subtitle.text.get(""));
        topBar.setSubtitleFontSize(topBarOptions.subtitle.fontSize.get(defaultSubtitleFontSize));
        topBar.setSubtitleColor(topBarOptions.subtitle.color.get(DEFAULT_SUBTITLE_COLOR));
        topBar.setSubtitleTypeface(typefaceLoader, topBarOptions.subtitle.font);
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
    private View findBackgroundComponent(ComponentOptions component) {
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
            List<ButtonOptions> rightButtons = mergeButtonsWithColor(options.buttons.right, options.rightButtonColor, options.rightButtonDisabledColor);
            List<ButtonController> rightButtonControllers = getOrCreateButtonControllersByInstanceId(componentRightButtons.get(child.getView()), rightButtons);
            componentRightButtons.put(child.getView(), keyBy(rightButtonControllers, ButtonController::getButtonInstanceId));
            if (!CollectionUtils.equals(currentRightButtons, rightButtonControllers)) {
                currentRightButtons = rightButtonControllers;
                topBarController.applyRightButtons(currentRightButtons);
            }
        } else {
            currentRightButtons = null;
            topBar.clearRightButtons();
        }

        if (options.buttons.left != null) {
            List<ButtonOptions> leftButtons = mergeButtonsWithColor(options.buttons.left, options.leftButtonColor, options.leftButtonDisabledColor);
            List<ButtonController> leftButtonControllers = getOrCreateButtonControllersByInstanceId(componentLeftButtons.get(child.getView()), leftButtons);
            componentLeftButtons.put(child.getView(), keyBy(leftButtonControllers, ButtonController::getButtonInstanceId));
            topBarController.setLeftButtons(leftButtonControllers);
        } else {
            topBar.clearLeftButtons();
        }

        if (options.buttons.back.visible.isTrue() && !options.buttons.hasLeftButtons()) {
            topBar.setBackButton(createButtonController(options.buttons.back));
        }

        topBar.setOverflowButtonColor(options.rightButtonColor.get(Color.BLACK));
    }

    private List<ButtonController> getOrCreateButtonControllersByInstanceId(@Nullable Map<String, ButtonController> currentButtons, @Nullable List<ButtonOptions> buttons) {
        if (buttons == null) return null;
        Map<String, ButtonController> result = new LinkedHashMap<>();
        forEach(buttons, b -> result.put(b.instanceId, getOrDefault(currentButtons, b.instanceId, () -> createButtonController(b))));
        return new ArrayList<>(result.values());
    }

    private List<ButtonController> getOrCreateButtonControllers(@Nullable Map<String, ButtonController> currentButtons, @NonNull List<ButtonOptions> buttons) {
        ArrayList result = new ArrayList<ButtonController>();
        for (ButtonOptions b : buttons) {
            result.add(take(first(perform(currentButtons, null, Map::values), button -> button.getButton().equals(b)), createButtonController(b)));
        }
        return result;
    }

    private ButtonController createButtonController(ButtonOptions button) {
        ButtonController controller = new ButtonController(activity,
                new ButtonPresenter(activity, button, iconResolver),
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
        TopBarOptions topBar = toMerge.copy().topBar.mergeWithDefault(resolvedOptions.topBar).mergeWithDefault(defaultOptions.topBar);
        mergeOrientation(toMerge.layout.orientation);
        mergeButtons(topBar, toMerge.topBar.buttons, child.getView());
        mergeTopBarOptions(topBar, toMerge, stack, child);
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
        List<ButtonOptions> rightButtons = mergeButtonsWithColor(buttons.right, options.rightButtonColor, options.rightButtonDisabledColor);
        List<ButtonController> toMerge = getOrCreateButtonControllers(componentRightButtons.get(child), rightButtons);
        List<ButtonController> toRemove = difference(currentRightButtons, toMerge, ButtonController::areButtonsEqual);
        forEach(toRemove, ButtonController::destroy);

        if (!CollectionUtils.equals(currentRightButtons, toMerge)) {
            componentRightButtons.put(child, keyBy(toMerge, ButtonController::getButtonInstanceId));
            topBarController.mergeRightButtons(toMerge, toRemove);
            currentRightButtons = toMerge;
        }
        if (options.rightButtonColor.hasValue()) topBar.setOverflowButtonColor(options.rightButtonColor.get());
    }

    private void mergeLeftButton(TopBarOptions options, TopBarButtons buttons, View child) {
        if (buttons.left == null) return;
        List<ButtonOptions> leftButtons = mergeButtonsWithColor(buttons.left, options.leftButtonColor, options.leftButtonDisabledColor);
        List<ButtonController> toMerge = getOrCreateButtonControllers(componentLeftButtons.get(child), leftButtons);
        componentLeftButtons.put(child, keyBy(toMerge, ButtonController::getButtonInstanceId));
        topBarController.setLeftButtons(toMerge);
    }

    private void mergeBackButton(TopBarButtons buttons) {
        if (buttons.back.hasValue() && isNullOrEmpty(buttons.left)) {
            if (buttons.back.visible.isFalse()) {
                topBar.clearLeftButtons();
            } else {
                topBar.setBackButton(createButtonController(buttons.back));
            }
        }
    }

    private List<ButtonOptions> mergeButtonsWithColor(@NonNull List<ButtonOptions> buttons, Colour buttonColor, Colour disabledColor) {
        List<ButtonOptions> result = new ArrayList<>();
        for (ButtonOptions button : buttons) {
            ButtonOptions copy = button.copy();
            if (!button.color.hasValue()) copy.color = buttonColor;
            if (!button.disabledColor.hasValue()) copy.disabledColor = disabledColor;
            result.add(copy);
        }
        return result;
    }

    private void mergeTopBarOptions(TopBarOptions resolveOptions, Options options, StackController stack, ViewController child) {
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

        if (resolveOptions.title.color.hasValue()) topBar.setTitleTextColor(resolveOptions.title.color.get());
        if (resolveOptions.title.fontSize.hasValue()) topBar.setTitleFontSize(resolveOptions.title.fontSize.get());
        if (resolveOptions.title.font.hasValue()) topBar.setTitleTypeface(typefaceLoader, resolveOptions.title.font);

        if (topBarOptions.subtitle.text.hasValue()) topBar.setSubtitle(topBarOptions.subtitle.text.get());
        if (resolveOptions.subtitle.color.hasValue()) topBar.setSubtitleColor(resolveOptions.subtitle.color.get());
        if (resolveOptions.subtitle.fontSize.hasValue()) topBar.setSubtitleFontSize(resolveOptions.subtitle.fontSize.get());
        if (resolveOptions.subtitle.font.hasValue()) topBar.setSubtitleTypeface(typefaceLoader, resolveOptions.subtitle.font);

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

    private TitleBarReactViewController findTitleComponent(ComponentOptions component) {
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

    private LayoutParams getComponentLayoutParams(ComponentOptions component) {
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
    public List<ButtonController> getComponentButtons(View child) {
        return merge(getRightButtons(child), getLeftButtons(child), Collections.EMPTY_LIST);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public List<ButtonController> getComponentButtons(View child, List<ButtonController> defaultValue) {
        return merge(getRightButtons(child), getLeftButtons(child), defaultValue);
    }

    public void applyTopInsets(StackController stack, ViewController child) {
        if (stack.isCurrentChild(child)) applyStatusBarInsets(stack, child);
        child.applyTopInset();
    }

    private List<ButtonController> getRightButtons(View child) {
        return componentRightButtons.containsKey(child) ? new ArrayList<>(componentRightButtons.get(child).values()) : null;
    }

    private List<ButtonController> getLeftButtons(View child) {
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

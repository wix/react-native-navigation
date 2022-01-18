package com.reactnativenavigation.viewcontrollers.stack;

import static com.reactnativenavigation.utils.CollectionUtils.filter;
import static com.reactnativenavigation.utils.CollectionUtils.forEach;
import static com.reactnativenavigation.utils.CollectionUtils.isNullOrEmpty;
import static com.reactnativenavigation.utils.CollectionUtils.merge;
import static com.reactnativenavigation.utils.ObjectUtils.perform;
import static com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarControllerKt.DEFAULT_BORDER_COLOR;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.reactnativenavigation.options.Alignment;
import com.reactnativenavigation.options.AnimationOptions;
import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.options.ComponentOptions;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.OrientationOptions;
import com.reactnativenavigation.options.TopBarButtons;
import com.reactnativenavigation.options.TopBarOptions;
import com.reactnativenavigation.options.TopTabOptions;
import com.reactnativenavigation.options.TopTabsOptions;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.options.parsers.TypefaceLoader;
import com.reactnativenavigation.utils.CollectionUtils;
import com.reactnativenavigation.utils.ObjectUtils;
import com.reactnativenavigation.utils.RenderChecker;
import com.reactnativenavigation.utils.SystemUiUtils;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsController;
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
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonBar;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarButtonCreator;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarReactViewCreator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StackPresenter {
    private static final double DEFAULT_ELEVATION = 4d;
    private final Activity activity;

    private TopBar topBar;
    private TopBarController topBarController;
    private @Nullable
    BottomTabsController bottomTabsController;
    private final TitleBarReactViewCreator titleViewCreator;
    private ButtonController.OnClickListener onClickListener;
    private final RenderChecker renderChecker;
    private final TopBarBackgroundViewCreator topBarBackgroundViewCreator;
    private final TitleBarButtonCreator buttonCreator;
    private Options defaultOptions;

    private final Map<View, TitleBarReactViewController> titleControllers = new HashMap();
    private final Map<View, TopBarBackgroundViewController> backgroundControllers = new HashMap();
    private final Map<View, Map<String, ButtonController>> rightButtonControllers = new HashMap();
    private final Map<View, Map<String, ButtonController>> leftButtonControllers = new HashMap();
    private final IconResolver iconResolver;
    private final TypefaceLoader typefaceLoader;

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

    public void bindView(TopBarController topBarController, @Nullable BottomTabsController bottomTabsController) {
        this.topBarController = topBarController;
        this.bottomTabsController = bottomTabsController;
        topBar = topBarController.getView();
    }

    public boolean isRendered(View component) {
        ArrayList<ViewController<?>> controllers = new ArrayList<>();
        controllers.addAll(perform(rightButtonControllers.get(component), new ArrayList<>(), Map::values));
        controllers.addAll(perform(leftButtonControllers.get(component), new ArrayList<>(), Map::values));
        controllers.add(backgroundControllers.get(component));
        controllers.add(titleControllers.get(component));
        return renderChecker.areRendered(filter(controllers, ObjectUtils::notNull));
    }

    public void mergeOptions(Options options, StackController stack, ViewController<?> currentChild) {
        TopBarOptions resolvedTopBarOptions = options.topBar.copy().mergeWithDefault(stack.resolveChildOptions(currentChild).topBar).mergeWithDefault(defaultOptions.topBar);
        mergeOrientation(options.layout.orientation);
        mergeTopBarOptions(resolvedTopBarOptions, options, stack, currentChild);
        mergeTopTabsOptions(options.topTabs);
        mergeTopTabOptions(options.topTabOptions);
    }

    public void onConfigurationChanged(Options options, ViewController<?> currentChild) {
        if (topBar == null) return;
        Options withDefault = options.copy().withDefaultOptions(defaultOptions);
        if (withDefault.topBar.buttons.back.visible.isTrue()) {
            topBarController.setBackButton(createButtonController(withDefault.topBar.buttons.back));
        }
        topBarController.onConfigurationChanged(withDefault,
                leftButtonControllers.get(currentChild.getView()),
                rightButtonControllers.get(currentChild.getView()));

    }

    public void applyInitialChildLayoutOptions(Options options) {
        Options withDefault = options.copy().withDefaultOptions(defaultOptions);
        applyTopBarVisibility(withDefault.topBar);
    }

    public void applyChildOptions(Options currentChildOptions, StackController stack, ViewController<?> child) {
        Options finalChildOptions = currentChildOptions.copy().withDefaultOptions(defaultOptions);
        applyOrientation(finalChildOptions.layout.orientation);
        applyButtons(finalChildOptions.topBar, child);
        applyTopBarOptions(finalChildOptions, stack, child);
        applyTopTabsOptions(finalChildOptions.topTabs);
        applyTopTabOptions(finalChildOptions.topTabOptions);
    }

    public void applyOrientation(OrientationOptions options) {
        OrientationOptions withDefaultOptions = options.copy().mergeWithDefault(defaultOptions.layout.orientation);
        ((Activity) topBar.getContext()).setRequestedOrientation(withDefaultOptions.getValue());
    }

    public void onChildDestroyed(ViewController<?> child) {
        perform(titleControllers.remove(child.getView()), TitleBarReactViewController::destroy);
        perform(backgroundControllers.remove(child.getView()), TopBarBackgroundViewController::destroy);
        destroyButtons(rightButtonControllers.get(child.getView()));
        destroyButtons(leftButtonControllers.get(child.getView()));
        rightButtonControllers.remove(child.getView());
        leftButtonControllers.remove(child.getView());
    }

    private void destroyButtons(@Nullable Map<String, ButtonController> buttons) {
        if (buttons != null) forEach(buttons.values(), ViewController::destroy);
    }

    private void applyTopBarOptions(Options options, StackController stack, ViewController<?> child) {
        final View component = child.getView();
        TopBarOptions topBarOptions = options.topBar;

        Options withDefault = stack.resolveChildOptions(child).withDefaultOptions(defaultOptions);

        topBar.setTestId(topBarOptions.testId.get(""));
        topBar.setLayoutDirection(options.layout.direction);
        applyStatusBarDrawBehindOptions(topBarOptions, withDefault);
        topBar.setElevation(topBarOptions.elevation.get(DEFAULT_ELEVATION));
        if (topBarOptions.topMargin.hasValue() && topBar.getLayoutParams() instanceof MarginLayoutParams) {
            ((MarginLayoutParams) topBar.getLayoutParams()).topMargin = UiUtils.dpToPx(activity, topBarOptions.topMargin.get(0));
        }

        topBar.setTitleHeight(topBarOptions.title.height.get(UiUtils.getTopBarHeightDp(activity)));
        topBar.setTitleTopMargin(topBarOptions.title.topMargin.get(0));

        if (topBarOptions.title.component.hasValue()) {
            if (titleControllers.containsKey(component)) {
                topBarController.setTitleComponent(Objects.requireNonNull(titleControllers.get(component)));
            } else {
                TitleBarReactViewController controller = new TitleBarReactViewController(activity, titleViewCreator, topBarOptions.title.component);
                controller.setWaitForRender(topBarOptions.title.component.waitForRender);
                titleControllers.put(component, controller);
                topBarController.setTitleComponent(controller);
            }
            topBarController.alignTitleComponent(topBarOptions.title.component.alignment);
        } else {
            topBar.applyTitleOptions(topBarOptions.title, typefaceLoader);
            topBar.applySubtitleOptions(topBarOptions.subtitle, typefaceLoader);
            topBarController.alignTitleComponent(topBarOptions.title.alignment);
        }


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

        applyTopBarVisibilityIfChildIsNotBeingAnimated(topBarOptions, stack, child);
        if (topBarOptions.hideOnScroll.isTrue()) {
            if (component instanceof IReactView) {
                topBar.enableCollapse(((IReactView) component).getScrollEventListener());
            }
        } else if (topBarOptions.hideOnScroll.isFalseOrUndefined()) {
            topBar.disableCollapse();
        }
    }

    private void applyStatusBarDrawBehindOptions(TopBarOptions topBarOptions, Options withDefault) {
        if (withDefault.statusBar.visible.isTrueOrUndefined() && withDefault.statusBar.drawBehind.isTrue()) {
            topBar.setTopPadding(SystemUiUtils.getStatusBarHeight(activity));
            topBar.setHeight(topBarOptions.height.get(UiUtils.getTopBarHeightDp(activity)) + SystemUiUtils.getStatusBarHeightDp(activity));

        } else {
            topBar.setTopPadding(0);
            topBar.setHeight(topBarOptions.height.get(UiUtils.getTopBarHeightDp(activity)));
        }
    }
    
    private void mergeStatusBarDrawBehindOptions(TopBarOptions topBarOptions, Options childOptions) {
        if(childOptions.statusBar.visible.isTrueOrUndefined()){
            if (childOptions.statusBar.drawBehind.hasValue()) {
                if (childOptions.statusBar.drawBehind.isTrue()) {
                    topBar.setTopPadding(SystemUiUtils.getStatusBarHeight(activity));
                    topBar.setHeight(topBarOptions.height.get(UiUtils.getTopBarHeightDp(activity)) + SystemUiUtils.getStatusBarHeightDp(activity));
                } else {
                    topBar.setTopPadding(0);
                    topBar.setHeight(topBarOptions.height.get(UiUtils.getTopBarHeightDp(activity)));
                }
            }
        }else{
            if (childOptions.statusBar.drawBehind.hasValue()) {
                if (childOptions.statusBar.drawBehind.isFalseOrUndefined()) {
                    topBar.setTopPadding(SystemUiUtils.getStatusBarHeight(activity));
                    topBar.setHeight(topBarOptions.height.get(UiUtils.getTopBarHeightDp(activity)) + SystemUiUtils.getStatusBarHeightDp(activity));
                } else {
                    topBar.setTopPadding(0);
                    topBar.setHeight(topBarOptions.height.get(UiUtils.getTopBarHeightDp(activity)));
                }
            }
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

    private void applyTopBarVisibilityIfChildIsNotBeingAnimated(TopBarOptions options, StackController stack, ViewController<?> child) {
        if (!stack.isChildInTransition(child) || options.animate.isFalse()) applyTopBarVisibility(options);
    }

    private void applyTopBarVisibility(TopBarOptions options) {
        if (options.visible.isFalse()) {
            topBarController.hide();
        }
        if (options.visible.isTrueOrUndefined()) {
            topBarController.show();
        }
    }

    private void applyButtons(TopBarOptions options, ViewController<?> child) {
        //should be at first in order for next actions to be animated
        if (options.animateRightButtons.hasValue())
            topBarController.animateRightButtons(options.animateRightButtons.isTrue());
        if (options.animateLeftButtons.hasValue())
            topBarController.animateLeftButtons(options.animateLeftButtons.isTrue());

        applyRightButtonsOptions(options, child);

        applyLeftButtonsOptions(options, child);

        if (options.buttons.back.visible.isTrue() && !options.buttons.hasLeftButtons()) {
            topBarController.setBackButton(createButtonController(options.buttons.back));
        }

        topBar.setOverflowButtonColor(options.rightButtonColor.get(Color.BLACK));
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
        topBar.applyTopTabsColors(options.selectedTabColor,
                options.unselectedTabColor);
        topBar.applyTopTabsFontSize(options.fontSize);
        topBar.setTopTabsVisible(options.visible.isTrueOrUndefined());
        topBar.setTopTabsHeight(options.height.get(LayoutParams.WRAP_CONTENT));
    }

    private void applyTopTabOptions(TopTabOptions topTabOptions) {
        if (topTabOptions.fontFamily != null) {
            topBar.setTopTabFontFamily(topTabOptions.tabIndex, topTabOptions.fontFamily);
        }
    }

    public List<Animator> getAdditionalPushAnimations(StackController stack, ViewController<?> appearing, Options appearingOptions) {
        return CollectionUtils.asList(
                topBarController.getPushAnimation(appearingOptions, getTopBarTranslationAnimationDelta(stack, appearing)),
                perform(bottomTabsController, null, btc -> btc.getPushAnimation(appearingOptions))
        );
    }

    public List<Animator> getAdditionalPopAnimations(Options appearingOptions, Options disappearingOptions) {
        return CollectionUtils.asList(
                topBarController.getPopAnimation(appearingOptions, disappearingOptions),
                perform(bottomTabsController, null, btc -> btc.getPopAnimation(appearingOptions, disappearingOptions))
        );
    }

    public List<Animator> getAdditionalSetRootAnimations(StackController stack, ViewController<?> appearing, Options appearingOptions) {
        return CollectionUtils.asList(
                topBarController.getSetStackRootAnimation(appearingOptions, getTopBarTranslationAnimationDelta(stack, appearing)),
                perform(bottomTabsController, null, btc -> btc.getSetStackRootAnimation(appearingOptions))
        );
    }
    public void mergeChildOptions(Options toMerge, Options resolvedOptions, StackController stack, ViewController<?> child) {
        TopBarOptions topBar = toMerge.copy().topBar.mergeWithDefault(resolvedOptions.topBar).mergeWithDefault(defaultOptions.topBar);
        mergeOrientation(toMerge.layout.orientation);
        mergeButtons(topBar, toMerge.topBar, child.getView(), stack);
        mergeTopBarOptions(topBar, toMerge, stack, child);
        mergeTopTabsOptions(toMerge.topTabs);
        mergeTopTabOptions(toMerge.topTabOptions);
    }

    private void mergeOrientation(OrientationOptions orientationOptions) {
        if (orientationOptions.hasValue()) applyOrientation(orientationOptions);
    }

    private void mergeButtons(TopBarOptions options, TopBarOptions optionsToMerge, View child, StackController stack) {
        if (optionsToMerge.animateRightButtons.hasValue())
            topBarController.animateRightButtons(optionsToMerge.animateRightButtons.isTrue());
        if (optionsToMerge.animateLeftButtons.hasValue())
            topBarController.animateLeftButtons(optionsToMerge.animateLeftButtons.isTrue());

        mergeRightButtonsOptions(options, optionsToMerge.buttons, child);
        mergeLeftButtonsOptions(options, optionsToMerge.buttons, child);
        mergeLeftButtonsColor(child, optionsToMerge.leftButtonColor, optionsToMerge.leftButtonDisabledColor);
        mergeRightButtonsColor(child, optionsToMerge.rightButtonColor, optionsToMerge.rightButtonDisabledColor);
        mergeBackButton(optionsToMerge.buttons, stack);
    }

    private void mergeLeftButtonsColor(View child, ThemeColour color, ThemeColour disabledColor) {
        if (color.hasValue() || disabledColor.hasValue()) {
            Map<String, ButtonController> stringButtonControllerMap = leftButtonControllers.get(child);
            if (stringButtonControllerMap != null) {
                forEach(stringButtonControllerMap.values(), (btnController) -> {
                    if (color.hasValue()) {
                        btnController.applyColor(topBarController.getView().getLeftButtonBar(), color);
                    }
                    if (disabledColor.hasValue()) {
                        btnController.applyDisabledColor(topBarController.getView().getLeftButtonBar(), disabledColor);
                    }
                });
            }
        }
    }

    private void mergeRightButtonsColor(View child, ThemeColour color, ThemeColour disabledColor) {
        if (color.hasValue() || disabledColor.hasValue()) {
            Map<String, ButtonController> stringButtonControllerMap = rightButtonControllers.get(child);
            if (stringButtonControllerMap != null) {
                forEach(stringButtonControllerMap.values(), (btnController) -> {
                    if (color.hasValue()) {
                        btnController.applyColor(topBarController.getView().getRightButtonBar(), color);
                    }
                    if (disabledColor.hasValue()) {
                        btnController.applyDisabledColor(topBarController.getView().getRightButtonBar(), disabledColor);
                    }
                });
            }
        }
    }

    private void applyLeftButtonsOptions(TopBarOptions options, ViewController<?> child) {
        if (options.buttons.left != null) {
            List<ButtonOptions> leftButtons = mergeButtonsWithColor(options.buttons.left,
                    options.leftButtonColor
                    , options.leftButtonDisabledColor);
            final ViewGroup childView = child.getView();
            final Map<String, ButtonController> btnControllers = getOrCreateButtonControllerMap(childView, leftButtonControllers);
            topBarController.applyLeftButtonsOptions(btnControllers, leftButtons, this::createButtonController);
        } else {
            topBarController.clearLeftButtons();
        }
    }

    private void applyRightButtonsOptions(TopBarOptions options, ViewController<?> child) {
        if (options.buttons.right != null) {
            List<ButtonOptions> rightButtons = mergeButtonsWithColor(options.buttons.right,
                    options.rightButtonColor
                    , options.rightButtonDisabledColor);
            final ViewGroup childView = child.getView();
            final Map<String, ButtonController> btnControllers = getOrCreateButtonControllerMap(childView, rightButtonControllers);
            topBarController.applyRightButtonsOptions(btnControllers, rightButtons, this::createButtonController);
        } else {
            topBarController.clearRightButtons();
        }
    }

    private void mergeRightButtonsOptions(TopBarOptions options, TopBarButtons buttons, View child) {
        if (buttons.right == null) return;
        List<ButtonOptions> rightButtons = mergeButtonsWithColor(buttons.right, options.rightButtonColor,
                options.rightButtonDisabledColor);
        final Map<String, ButtonController> btnControllers = getOrCreateButtonControllerMap(child, rightButtonControllers);
        topBarController.mergeRightButtonsOptions(btnControllers, rightButtons, this::createButtonController);
        if (options.rightButtonColor.hasValue()) topBar.setOverflowButtonColor(options.rightButtonColor.get());
    }

    private void mergeLeftButtonsOptions(TopBarOptions options, TopBarButtons buttons, View child) {
        if (buttons.left == null) return;
        List<ButtonOptions> leftButtons = mergeButtonsWithColor(buttons.left, options.leftButtonColor,
                options.leftButtonDisabledColor);
        final Map<String, ButtonController> btnControllers = getOrCreateButtonControllerMap(child, leftButtonControllers);
        topBarController.mergeLeftButtonsOptions(btnControllers, leftButtons, this::createButtonController);
        if (options.leftButtonColor.hasValue()) topBar.setOverflowButtonColor(options.leftButtonColor.get());
    }

    @NonNull
    private Map<String, ButtonController> getOrCreateButtonControllerMap(View child, Map<View, Map<String, ButtonController>> buttonControllers) {
        final Map<String, ButtonController> controllerMap = buttonControllers.get(child);
        final Map<String, ButtonController> btnControllers = controllerMap != null ? controllerMap : new HashMap<>();
        if (controllerMap == null)
            buttonControllers.put(child, btnControllers);
        return btnControllers;
    }

    private void mergeBackButton(TopBarButtons buttons, StackController stack) {
        if (buttons.back.hasValue() && isNullOrEmpty(buttons.left)) {
            if (buttons.back.visible.isFalse()) {
                topBarController.clearBackButton();
            } else if (stack.size() > 1) {
                topBarController.setBackButton(createButtonController(buttons.back));
            }
        }
    }

    private List<ButtonOptions> mergeButtonsWithColor(@NonNull List<ButtonOptions> buttons, ThemeColour buttonColor,
                                                      ThemeColour disabledColor) {
        List<ButtonOptions> result = new ArrayList<>();
        for (ButtonOptions button : buttons) {
            ButtonOptions copy = button.copy();
            if (!button.color.hasValue()) copy.color = buttonColor;
            if (!button.disabledColor.hasValue()) copy.disabledColor = disabledColor;
            result.add(copy);
        }
        return result;
    }

    private void mergeTopBarOptions(TopBarOptions resolveOptions, Options toMerge, StackController stack,
                                    ViewController<?> child) {
        TopBarOptions topBarOptions = toMerge.topBar;
        final View component = child.getView();
        if (toMerge.layout.direction.hasValue()) topBar.setLayoutDirection(toMerge.layout.direction);
        if (topBarOptions.height.hasValue()) topBar.setHeight(topBarOptions.height.get());
        if (topBarOptions.elevation.hasValue()) topBar.setElevation(topBarOptions.elevation.get());
        if (topBarOptions.topMargin.hasValue() && topBar.getLayoutParams() instanceof MarginLayoutParams) {
            ((MarginLayoutParams) topBar.getLayoutParams()).topMargin = UiUtils.dpToPx(activity, topBarOptions.topMargin.get());
        }
        Options childOptions = stack.resolveChildOptions(child).mergeWith(toMerge).withDefaultOptions(defaultOptions);
        mergeStatusBarDrawBehindOptions(resolveOptions, childOptions);
        if (topBarOptions.title.height.hasValue()) topBar.setTitleHeight(topBarOptions.title.height.get());
        if (topBarOptions.title.topMargin.hasValue()) topBar.setTitleTopMargin(topBarOptions.title.topMargin.get());

        if (topBarOptions.title.component.hasValue()) {
            TitleBarReactViewController controller = findTitleComponent(topBarOptions.title.component);
            if (controller == null) {
                controller = new TitleBarReactViewController(activity, titleViewCreator, topBarOptions.title.component);
                perform(titleControllers.put(component, controller), ViewController::destroy);
            }
            topBarController.setTitleComponent(controller);
            topBarController.alignTitleComponent(topBarOptions.title.component.alignment);
        } else if (topBarOptions.title.text.hasValue()) {
            perform(titleControllers.remove(component), ViewController::destroy);
            topBar.setTitle(topBarOptions.title.text.get());
            topBarController.alignTitleComponent(topBarOptions.title.alignment);
        }
        if (resolveOptions.title.alignment != Alignment.Default) {
            topBarController.alignTitleComponent(resolveOptions.title.alignment);
        }

        if (resolveOptions.title.color.hasValue()) topBar.setTitleTextColor(resolveOptions.title.color.get());
        if (resolveOptions.title.fontSize.hasValue()) topBar.setTitleFontSize(resolveOptions.title.fontSize.get());
        if (resolveOptions.title.font.hasValue()) topBar.setTitleTypeface(typefaceLoader, resolveOptions.title.font);

        if (resolveOptions.subtitle.text.hasValue()) {
            topBar.setSubtitle(resolveOptions.subtitle.text.get());
            topBar.setSubtitleAlignment(resolveOptions.subtitle.alignment);
        }
        if (resolveOptions.subtitle.color.hasValue()) topBar.setSubtitleColor(resolveOptions.subtitle.color.get());
        if (resolveOptions.subtitle.fontSize.hasValue()) {
            topBar.setSubtitleFontSize(resolveOptions.subtitle.fontSize.get());
        }
        if (resolveOptions.subtitle.font.hasValue()) {
            topBar.setSubtitleTypeface(typefaceLoader, resolveOptions.subtitle.font);
        }

        if (topBarOptions.background.color.hasValue()) topBar.setBackgroundColor(topBarOptions.background.color.get());

        if (topBarOptions.background.component.hasValue()) {
            if (backgroundControllers.containsKey(component)) {
                topBar.setBackgroundComponent(Objects.requireNonNull(backgroundControllers.get(component)).getView());
            } else {
                TopBarBackgroundViewController controller = new TopBarBackgroundViewController(activity, topBarBackgroundViewCreator);
                backgroundControllers.put(component, controller);
                controller.setComponent(topBarOptions.background.component);
                controller.getView().setLayoutParams(new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                topBar.setBackgroundComponent(controller.getView());
            }
        }

        if (topBarOptions.testId.hasValue()) topBar.setTestId(topBarOptions.testId.get());

        if (topBarOptions.visible.isFalse()) {
            if (topBarOptions.animate.isTrueOrUndefined()) {
                topBarController.hideAnimate(new AnimationOptions(), getTopBarTranslationAnimationDelta(stack, child));
            } else {
                topBarController.hide();
            }
        }
        if (topBarOptions.visible.isTrue()) {
            if (topBarOptions.animate.isTrueOrUndefined()) {
                topBarController.showAnimate(new AnimationOptions(), getTopBarTranslationAnimationDelta(stack, child));
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
        if (options.selectedTabColor.hasValue() && options.unselectedTabColor.hasValue()) {
            topBar.applyTopTabsColors(options.selectedTabColor, options.unselectedTabColor);
        }
        if (options.fontSize.hasValue()) topBar.applyTopTabsFontSize(options.fontSize);
        if (options.visible.hasValue()) topBar.setTopTabsVisible(options.visible.isTrue());
        if (options.height.hasValue()) topBar.setTopTabsHeight(options.height.get(LayoutParams.WRAP_CONTENT));
    }

    private void mergeTopTabOptions(TopTabOptions topTabOptions) {
        if (topTabOptions.fontFamily != null) {
            topBar.setTopTabFontFamily(topTabOptions.tabIndex, topTabOptions.fontFamily);
        }
    }

    public boolean shouldPopOnHardwareButtonPress(ViewController<?> viewController) {
        return viewController.resolveCurrentOptions().hardwareBack.popStackOnPress.get(true);
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

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setComponentsButtonController(View child, ButtonController rightController, ButtonController leftController) {
        forEach(leftButtonControllers.get(child).keySet(), (key) -> leftButtonControllers.get(child).put(key, leftController));
        forEach(rightButtonControllers.get(child).keySet(), (key) -> rightButtonControllers.get(child).put(key, rightController));
    }


    public void applyTopInsets(StackController stack, ViewController<?> child) {
        if (stack.isCurrentChild(child)) applyStatusBarInsets(stack, child);
        child.applyTopInset();
    }

    private List<ButtonController> getRightButtons(View child) {
        return rightButtonControllers.containsKey(child) ? new ArrayList<>(rightButtonControllers.get(child).values()) : null;
    }

    private List<ButtonController> getLeftButtons(View child) {
        return leftButtonControllers.containsKey(child) ? new ArrayList<>(leftButtonControllers.get(child).values()) : null;
    }

    private void applyStatusBarInsets(StackController stack, ViewController<?> child) {
        MarginLayoutParams lp = (MarginLayoutParams) topBar.getLayoutParams();
        lp.topMargin = getTopBarTopMargin(stack, child);
        topBar.requestLayout();
    }

    private int getTopBarTranslationAnimationDelta(StackController stack, ViewController<?> child) {
        Options options = stack.resolveChildOptions(child).withDefaultOptions(defaultOptions);
        return options.statusBar.hasTransparency() ? getTopBarTopMargin(stack, child) : 0;
    }

    private int getTopBarTopMargin(StackController stack, ViewController<?> child) {
        Options withDefault = stack.resolveChildOptions(child).withDefaultOptions(defaultOptions);
        int topMargin = UiUtils.dpToPx(activity, withDefault.topBar.topMargin.get(0));
        int statusBarInset = withDefault.statusBar.visible.isTrueOrUndefined() && !withDefault.statusBar.drawBehind.isTrue() ? SystemUiUtils.getStatusBarHeight(child.getActivity()) : 0;
        return topMargin + statusBarInset;
    }

    public int getTopInset(Options resolvedOptions) {
        return resolvedOptions.withDefaultOptions(defaultOptions).topBar.isHiddenOrDrawBehind() ? 0 : topBarController.getHeight();
    }


    private TitleBarReactViewController findTitleById(String id){
        final Collection<TitleBarReactViewController> values = titleControllers.values();
        for(TitleBarReactViewController child: values){
            if(child.getId().equals(id)){
                return child;
            }
        }
        return null;
    }
    private Pair<ButtonController, ButtonBar> findButtonById(String id){
        final Collection<Map<String, ButtonController>> leftControllers = leftButtonControllers.values();
        final Collection<Map<String, ButtonController>> rightControllers = rightButtonControllers.values();
        for(Map<String, ButtonController> map : leftControllers){
            if(map.containsKey(id)){
                return Pair.create(map.get(id),topBar.getLeftButtonBar());
            }
        }
        for(Map<String, ButtonController> map : rightControllers){
            if(map.containsKey(id)){
                return Pair.create(map.get(id),topBar.getRightButtonBar());
            }
        }
        return null;
    }
    @Nullable
    public View findTopBarViewById(String id) {
        final Pair<ButtonController, ButtonBar> buttonById = findButtonById(id);
        if(buttonById!=null){
            final View view = buttonById.first.getNullableView();
            if(view==null){
                final MenuItem menuItem = buttonById.first.getMenuItem();
                if(menuItem!=null){
                    final int order = menuItem.getOrder();
                    return buttonById.second.getChildAt(order/10);
                }
            }
            return view;
        }else {
            final TitleBarReactViewController titleById = findTitleById(id);
            if(titleById!=null){
                return titleById.getView();
            }
        }
        return null;
    }
}

package com.reactnativenavigation.screens;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.RelativeLayout;

import com.facebook.react.bridge.Callback;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.animation.VisibilityAnimator;
import com.reactnativenavigation.events.ContextualMenuHiddenEvent;
import com.reactnativenavigation.events.Event;
import com.reactnativenavigation.events.EventBus;
import com.reactnativenavigation.events.FabSetEvent;
import com.reactnativenavigation.events.Subscriber;
import com.reactnativenavigation.events.ViewPagerScreenChangedEvent;
import com.reactnativenavigation.params.BaseScreenParams;
import com.reactnativenavigation.params.ContextualMenuParams;
import com.reactnativenavigation.params.FabParams;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.LeftButtonOnClickListener;
import com.reactnativenavigation.views.TopBar;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class Screen extends RelativeLayout implements Subscriber {

    public interface OnDisplayListener {
        void onDisplay();
    }

    protected final AppCompatActivity activity;
    protected final ScreenParams screenParams;
    protected TopBar topBar;
    private final LeftButtonOnClickListener leftButtonOnClickListener;
    private VisibilityAnimator topBarVisibilityAnimator;
    private ScreenAnimator screenAnimator;
    protected final StyleParams styleParams;

    public Screen(AppCompatActivity activity, ScreenParams screenParams, LeftButtonOnClickListener leftButtonOnClickListener) {
        super(activity);
        this.activity = activity;
        this.screenParams = screenParams;
        styleParams = screenParams.styleParams;
        this.leftButtonOnClickListener = leftButtonOnClickListener;
        screenAnimator = new ScreenAnimator(this);
        createViews();
        EventBus.instance.register(this);
    }

    @Override
    public void onEvent(Event event) {
        if (ContextualMenuHiddenEvent.TYPE.equals(event.getType()) && isShown()) {
            setStyle();
            topBar.onContextualMenuHidden();
        }
        if (ViewPagerScreenChangedEvent.TYPE.equals(event.getType()) && isShown() ) {
            setStyle();
            topBar.dismissContextualMenu();
        }
    }

    public void setStyle() {
        setStatusBarColor(styleParams.statusBarColor);
        setNavigationBarColor(styleParams.navigationBarColor);
        topBar.setStyle(styleParams);
        if (styleParams.screenBackgroundColor.hasColor()) {
            setBackgroundColor(styleParams.screenBackgroundColor.getColor());
        }
    }

    private void createViews() {
        createAndAddTopBar();
        createTitleBar();
        createContent();
    }

    protected abstract void createContent();

    private void createTitleBar() {
        addTitleBarButtons();
        topBar.setTitle(screenParams.title);
        topBar.setSubtitle(screenParams.subtitle);
    }

    private void addTitleBarButtons() {
        setButtonColorFromScreen(screenParams.rightButtons);
        if (screenParams.leftButton != null) {
            screenParams.leftButton.setColorFromScreenStyle(screenParams.styleParams.titleBarButtonColor);
        }
        topBar.addTitleBarAndSetButtons(screenParams.rightButtons,
                screenParams.leftButton,
                leftButtonOnClickListener,
                screenParams.getNavigatorEventId(),
                screenParams.overrideBackPressInJs);
    }

    private void createAndAddTopBar() {
        topBar = createTopBar();
        addTopBar();
    }

    protected TopBar createTopBar() {
        return new TopBar(getContext());
    }

    private void addTopBar() {
        createTopBarVisibilityAnimator();
        addView(topBar, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    }

    private void createTopBarVisibilityAnimator() {
        ViewUtils.runOnPreDraw(topBar, new Runnable() {
            @Override
            public void run() {
                if (topBarVisibilityAnimator == null) {
                    topBarVisibilityAnimator = new VisibilityAnimator(topBar,
                            VisibilityAnimator.HideDirection.Up,
                            topBar.getHeight());
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(StyleParams.Color statusBarColor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;

        final Activity context = (Activity) getContext();
        final Window window = context.getWindow();
        if (statusBarColor.hasColor()) {
            window.setStatusBarColor(statusBarColor.getColor());
        } else {
            window.setStatusBarColor(Color.BLACK);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setNavigationBarColor(StyleParams.Color navigationBarColor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;

        final Activity context = (Activity) getContext();
        final Window window = context.getWindow();
        if (navigationBarColor.hasColor()) {
            window.setNavigationBarColor(navigationBarColor.getColor());
        } else {
            window.setNavigationBarColor(Color.BLACK);
        }
    }

    public abstract void unmountReactView();

    public String getScreenInstanceId() {
        return screenParams.getScreenInstanceId();
    }

    public abstract String getNavigatorEventId();

    public BaseScreenParams getScreenParams() {
        return screenParams;
    }

    public void setTopBarVisible(boolean visible, boolean animate) {
        topBarVisibilityAnimator.setVisible(visible, animate);
    }

    public void setTitleBarTitle(String title) {
        topBar.setTitle(title);
    }

    public void setTitleBarSubtitle(String subtitle) {
        topBar.setSubtitle(subtitle);
    }

    public void setTitleBarRightButtons(String navigatorEventId, List<TitleBarButtonParams> titleBarButtons) {
        setButtonColorFromScreen(titleBarButtons);
        topBar.setTitleBarRightButtons(navigatorEventId, titleBarButtons);
    }

    public void setTitleBarLeftButton(String navigatorEventId, LeftButtonOnClickListener backButtonListener,
                                      TitleBarLeftButtonParams titleBarLeftButtonParams) {
        titleBarLeftButtonParams.setColorFromScreenStyle(styleParams.titleBarButtonColor);
        topBar.setTitleBarLeftButton(navigatorEventId,
                backButtonListener,
                titleBarLeftButtonParams,
                screenParams.overrideBackPressInJs);
    }

    public void setFab(FabParams fabParams) {
        screenParams.fabParams = fabParams;
        if (isShown()) {
            EventBus.instance.post(new FabSetEvent(fabParams));
        }
    }

    public StyleParams getStyleParams() {
        return screenParams.styleParams;
    }

    private void setButtonColorFromScreen(List<TitleBarButtonParams> titleBarButtonParams) {
        if (titleBarButtonParams == null) {
            return;
        }

        for (TitleBarButtonParams titleBarButtonParam : titleBarButtonParams) {
            titleBarButtonParam.setColorFromScreenStyle(screenParams.styleParams.titleBarButtonColor);
        }
    }

    public abstract void setOnDisplayListener(OnDisplayListener onContentViewDisplayedListener);

    public void show() {
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("willAppear", screenParams.getNavigatorEventId());
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("didAppear", screenParams.getNavigatorEventId());
        screenAnimator.show(screenParams.animateScreenTransitions);
    }

    public void show(boolean animated) {
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("willAppear", screenParams.getNavigatorEventId());
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("didAppear", screenParams.getNavigatorEventId());
        screenAnimator.show(animated);
    }

    public void show(boolean animated, Runnable onAnimationEnd) {
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("willAppear", screenParams.getNavigatorEventId());
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("didAppear", screenParams.getNavigatorEventId());
        setStyle();
        screenAnimator.show(animated, onAnimationEnd);
    }

    public void hide(boolean animated, Runnable onAnimatedEnd) {
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("willDisappear", screenParams.getNavigatorEventId());
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("didDisappear", screenParams.getNavigatorEventId());
        screenAnimator.hide(animated, onAnimatedEnd);
    }

    public void showContextualMenu(ContextualMenuParams params, Callback onButtonClicked) {
        topBar.showContextualMenu(params, styleParams, onButtonClicked);
        setStatusBarColor(styleParams.contextualMenuStatusBarColor);
    }

    public void dismissContextualMenu() {
        topBar.dismissContextualMenu();
    }

    public void destroy() {
        unmountReactView();
        EventBus.instance.unregister(this);
    }
}

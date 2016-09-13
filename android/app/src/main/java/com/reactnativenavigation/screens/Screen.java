package com.reactnativenavigation.screens;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.RelativeLayout;

import com.reactnativenavigation.animation.VisibilityAnimator;
import com.reactnativenavigation.params.BaseScreenParams;
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

public abstract class Screen extends RelativeLayout {

    public interface OnDisplayListener {
        void onDisplay();
    }

    protected final AppCompatActivity activity;
    protected final ScreenParams screenParams;
    protected TopBar topBar;
    private final LeftButtonOnClickListener leftButtonOnClickListener;
    private VisibilityAnimator topBarVisibilityAnimator;
    private ScreenAnimator screenAnimator;
    private final StyleParams styleParams;

    public Screen(AppCompatActivity activity, ScreenParams screenParams, LeftButtonOnClickListener leftButtonOnClickListener) {
        super(activity);
        this.activity = activity;
        this.screenParams = screenParams;
        styleParams = screenParams.styleParams;
        this.leftButtonOnClickListener = leftButtonOnClickListener;
        screenAnimator = new ScreenAnimator(this);
        createViews();
    }

    public void setStyle() {
        setStatusBarColor(styleParams.statusBarColor);
        setNavigationBarColor(styleParams.navigationBarColor);
        topBar.setStyle(styleParams);
    }

    private void createViews() {
        createTopBar();
        createTitleBar();
        createContent();
    }

    protected abstract void createContent();

    private void createTitleBar() {
        addTitleBarButtons();
        topBar.setTitle(screenParams.title);
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

    private void createTopBar() {
        topBar = new TopBar(getContext());
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

    public String getNavigatorEventId() {
        return screenParams.getNavigatorEventId();
    }

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
        topBar.setTitleBarLeftButton(navigatorEventId,
                backButtonListener,
                titleBarLeftButtonParams,
                screenParams.overrideBackPressInJs);
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
        screenAnimator.show(screenParams.animateScreenTransitions);
    }

    public void show(boolean animated) {
        screenAnimator.show(animated);
    }

    public void show(boolean animated, Runnable onAnimationEnd) {
        setStyle();
        screenAnimator.show(animated, onAnimationEnd);
    }

    public void hide(boolean animated, Runnable onAnimatedEnd) {
        screenAnimator.hide(animated, onAnimatedEnd);
    }
}

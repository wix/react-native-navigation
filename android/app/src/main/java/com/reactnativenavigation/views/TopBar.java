package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.bridge.Callback;
import com.reactnativenavigation.animation.VisibilityAnimator;
import com.reactnativenavigation.params.BaseScreenParams;
import com.reactnativenavigation.params.ContextualMenuParams;
import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.ViewUtils;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class TopBar extends AppBarLayout {
    protected TitleBar titleBar;
    private ContextualMenu contextualMenu;
    protected FrameLayout titleBarAndContextualMenuContainer;
    protected TopTabs topTabs;
    private VisibilityAnimator visibilityAnimator;
    @Nullable
    private Pair<String, ContentView> reactView;

    public TopBar(Context context) {
        super(context);
        setId(ViewUtils.generateViewId());
        createTopBarVisibilityAnimator();
        createLayout();
    }

    private void createTopBarVisibilityAnimator() {
        ViewUtils.runOnPreDraw(this, new Runnable() {
            @Override
            public void run() {
                visibilityAnimator = new VisibilityAnimator(TopBar.this,
                        VisibilityAnimator.HideDirection.Up,
                        getHeight());
            }
        });
    }

    protected void createLayout() {
        titleBarAndContextualMenuContainer = new FrameLayout(getContext());
        addView(titleBarAndContextualMenuContainer);
    }

    public void addTitleBarAndSetButtons(List<TitleBarButtonParams> rightButtons,
                                         TitleBarLeftButtonParams leftButton,
                                         LeftButtonOnClickListener leftButtonOnClickListener,
                                         String navigatorEventId, boolean overrideBackPressInJs,
                                         StyleParams styleParams) {
        titleBar = createTitleBar();
        addTitleBar(styleParams);
        addButtons(rightButtons, leftButton, leftButtonOnClickListener, navigatorEventId, overrideBackPressInJs);
    }

    protected TitleBar createTitleBar() {
        return new TitleBar(getContext());
    }

    protected void addTitleBar(StyleParams styleParams) {
        final int titleBarHeight = styleParams.titleBarHeight > 0
            ? (int) ViewUtils.convertDpToPixel(styleParams.titleBarHeight)
            : MATCH_PARENT;

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(MATCH_PARENT, titleBarHeight);

        titleBarAndContextualMenuContainer.addView(titleBar, lp);
    }

    private void addButtons(List<TitleBarButtonParams> rightButtons, TitleBarLeftButtonParams leftButton, LeftButtonOnClickListener leftButtonOnClickListener, String navigatorEventId, boolean overrideBackPressInJs) {
        titleBar.setRightButtons(rightButtons, navigatorEventId);
        titleBar.setLeftButton(leftButton, leftButtonOnClickListener, navigatorEventId, overrideBackPressInJs);
    }

    public void setTitle(String title, StyleParams styleParams) {
        titleBar.setTitle(title, styleParams);
    }

    public void setSubtitle(String subtitle, StyleParams styleParams) {
        titleBar.setSubtitle(subtitle, styleParams);
    }

    public void setReactView(@NonNull StyleParams styleParams) {
        if (styleParams.hasTopBarCustomComponent()) {
            if (isReactViewAlreadySetAndUnchanged(styleParams)) {
                return;
            }
            unmountReactView();
            reactView = new Pair<>(styleParams.topBarReactView, createReactView(styleParams));
            int height = styleParams.hasCustomTitleBarHeight() ? (int) ViewUtils.convertDpToPixel(styleParams.titleBarHeight) : ViewUtils.getToolBarHeight();
            if ("fill".equals(styleParams.topBarReactViewAlignment)) {
                addReactViewFill(reactView.second, height);
            } else {
                addCenteredReactView(reactView.second, height);
            }
        } else {
            unmountReactView();
        }
    }

    private void unmountReactView() {
        if (reactView == null) return;
        titleBar.removeView(reactView.second);
        reactView.second.unmountReactView();
        reactView = null;
    }

    private boolean isReactViewAlreadySetAndUnchanged(@NonNull StyleParams styleParams) {
        return reactView != null && styleParams.topBarReactView.equals(reactView.first);
    }

    private ContentView createReactView(StyleParams styleParams) {
        return new ContentView(getContext(),
                styleParams.topBarReactView,
                NavigationParams.EMPTY,
                styleParams.topBarReactViewInitialProps
        );
    }

    private void addReactViewFill(ContentView view, int height) {
        view.setLayoutParams(new LayoutParams(MATCH_PARENT, height));
        titleBar.addView(view);
    }

    private void addCenteredReactView(final ContentView view, int height) {
        titleBar.addView(view, new LayoutParams(WRAP_CONTENT, height));
        view.setOnDisplayListener(new Screen.OnDisplayListener() {
            @Override
            public void onDisplay() {
                view.getLayoutParams().width = (int) (float) view.getChildAt(0).getMeasuredWidth();
                ((ActionBar.LayoutParams) view.getLayoutParams()).gravity = Gravity.CENTER;
                view.requestLayout();
            }
        });
    }

    public void setButtonColor(StyleParams styleParams) {
        titleBar.setButtonColor(styleParams.titleBarButtonColor);
    }

    public void setStyle(StyleParams styleParams) {
        if (styleParams.topBarBorderColor.hasColor()) {
            setBackground(new TopBarBorder(styleParams));
        } else if (styleParams.topBarColor.hasColor()) {
            setBackgroundColor(styleParams.topBarColor.getColor());
        }
        if (styleParams.topBarTransparent) {
            setTransparent();
        }
        titleBar.setStyle(styleParams);
        setReactView(styleParams);
        setTopTabsStyle(styleParams);
        if (!styleParams.topBarElevationShadowEnabled) {
            disableElevationShadow();
        }
    }

    private void setTransparent() {
        setBackgroundColor(Color.TRANSPARENT);
        disableElevationShadow();
    }

    private void disableElevationShadow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(null);
        }
    }

    public void setTitleBarRightButtons(String navigatorEventId, List<TitleBarButtonParams> titleBarButtons) {
        titleBar.setRightButtons(titleBarButtons, navigatorEventId);
    }

    public TopTabs initTabs(StyleParams styleParams) {
        topTabs = new TopTabs(getContext());

        final int topTabsHeight = styleParams.topTabsHeight > 0
            ? (int) ViewUtils.convertDpToPixel(styleParams.topTabsHeight)
            : MATCH_PARENT;

        addView(topTabs, new ViewGroup.LayoutParams(MATCH_PARENT, topTabsHeight));
        return topTabs;
    }

    public void setTitleBarLeftButton(String navigatorEventId,
                                      LeftButtonOnClickListener leftButtonOnClickListener,
                                      TitleBarLeftButtonParams titleBarLeftButtonParams,
                                      boolean overrideBackPressInJs) {
        titleBar.setLeftButton(titleBarLeftButtonParams, leftButtonOnClickListener, navigatorEventId,
                overrideBackPressInJs);
    }

    private void setTopTabsStyle(StyleParams style) {
        if (topTabs == null) {
            return;
        }
        topTabs.setTopTabsTextColor(style);
        topTabs.setSelectedTabIndicatorStyle(style);
        topTabs.setScrollable(style);
        topTabs.setTopTabsTextFontFamily(style);
    }

    public void showContextualMenu(final ContextualMenuParams params, StyleParams styleParams, Callback onButtonClicked) {
        final ContextualMenu menuToRemove = contextualMenu != null ? contextualMenu : null;
        contextualMenu = new ContextualMenu(getContext(), params, styleParams, onButtonClicked);
        titleBarAndContextualMenuContainer.addView(contextualMenu, new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        ViewUtils.runOnPreDraw(contextualMenu, new Runnable() {
            @Override
            public void run() {
                titleBar.hide();
                contextualMenu.show(new Runnable() {
                    @Override
                    public void run() {
                        if (menuToRemove != null) {
                            titleBarAndContextualMenuContainer.removeView(menuToRemove);
                        }
                    }
                });
            }
        });
    }

    public void onContextualMenuHidden() {
        contextualMenu = null;
        titleBar.show();
    }

    public void dismissContextualMenu() {
        if (contextualMenu != null) {
            contextualMenu.dismiss();
            contextualMenu = null;
            titleBar.show();
        }
    }

    public void destroy() {
        if (reactView != null) {
            reactView.second.unmountReactView();
            reactView = null;
        }
        titleBar.destroy();
    }

    public void onViewPagerScreenChanged(BaseScreenParams screenParams) {
        titleBar.onViewPagerScreenChanged(screenParams);
    }

    public void setVisible(boolean visible, boolean animate) {
        if (visible) {
            titleBar.setVisibility(false);
            visibilityAnimator.setVisible(true, animate, null);
        } else {
            visibilityAnimator.setVisible(false, animate, new Runnable() {
                @Override
                public void run() {
                    titleBar.setVisibility(true);
                }
            });
        }
    }
}

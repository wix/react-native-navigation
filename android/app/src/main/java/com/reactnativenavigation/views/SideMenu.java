package com.reactnativenavigation.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.BaseScreenParams;
import com.reactnativenavigation.params.SideMenuParams;
import com.reactnativenavigation.screens.NavigationType;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.ViewUtils;

public class SideMenu extends DrawerLayout {
    private SideMenuParams leftMenuParams;
    private SideMenuParams rightMenuParams;

    public enum Side {
        Left(Gravity.LEFT), Right(Gravity.RIGHT);

        int gravity;

        Side(int gravity) {
            this.gravity = gravity;
        }

        public static Side fromString(String side) {
            return "left".equals(side.toLowerCase()) ? Left : Right;
        }
    }

    private ContentView leftSideMenuView;
    private ContentView rightSideMenuView;
    private RelativeLayout contentContainer;
    private SimpleDrawerListener sideMenuListener;

    public RelativeLayout getContentContainer() {
        return contentContainer;
    }

    public void destroy() {
        removeDrawerListener(sideMenuListener);
        destroySideMenu(leftSideMenuView);
        destroySideMenu(rightSideMenuView);
    }

    private void destroySideMenu(ContentView sideMenuView) {
        if (sideMenuView == null) {
            return;
        }
        removeDrawerListener(sideMenuListener);
        sideMenuView.unmountReactView();
        removeView(sideMenuView);
    }

    public void setVisible(boolean visible, boolean animated, Side side) {
        if (visible) {
            openDrawer(animated, side);
        }

        if (!visible) {
            closeDrawer(animated, side);
        }
    }

    public void setEnabled(boolean enabled, Side side) {
        if (enabled) {
            setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, side.gravity);
        } else {
            setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, side.gravity);
        }
    }

    public void openDrawer(Side side) {
        openDrawer(side.gravity);
    }

    public void openDrawer(boolean animated, Side side) {
        openDrawer(side.gravity, animated);
    }

    public void toggleVisible(boolean animated, Side side) {
        if (isDrawerOpen(side.gravity)) {
            closeDrawer(animated, side);
        } else {
            openDrawer(animated, side);
        }
    }

    public void closeDrawer(boolean animated, Side side) {
        closeDrawer(side.gravity, animated);
    }

    public SideMenu(Context context, SideMenuParams leftMenuParams, SideMenuParams rightMenuParams) {
        super(context);
        this.leftMenuParams = leftMenuParams;
        this.rightMenuParams = rightMenuParams;
        createContentContainer();
        leftSideMenuView = createSideMenu(leftMenuParams);
        rightSideMenuView = createSideMenu(rightMenuParams);
        setStyle(leftMenuParams);
        setStyle(rightMenuParams);
        setScreenEventListener();
    }

    private void createContentContainer() {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        contentContainer = new RelativeLayout(getContext());
        contentContainer.setId(ViewUtils.generateViewId());
        addView(contentContainer, lp);
    }

    private ContentView createSideMenu(@Nullable SideMenuParams params) {
        if (params == null) {
            return null;
        }
        ContentView sideMenuView = new ContentView(getContext(), params.screenId, params.navigationParams);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp.gravity = params.side.gravity;
        setSideMenuWidth(sideMenuView);
        addView(sideMenuView, lp);
        return sideMenuView;
    }

    private void setSideMenuWidth(final ContentView sideMenuView) {
        sideMenuView.setOnDisplayListener(new Screen.OnDisplayListener() {
            @Override
            public void onDisplay() {
                ViewGroup.LayoutParams lp = sideMenuView.getLayoutParams();
                lp.width = sideMenuView.getChildAt(0).getWidth();
                sideMenuView.setLayoutParams(lp);
            }
        });
    }

    public void setScreenEventListener() {
        sideMenuListener = new SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                NavigationApplication.instance.getEventEmitter().sendWillAppearEvent(getVisibleDrawerScreenParams(), NavigationType.OpenSideMenu);
                NavigationApplication.instance.getEventEmitter().sendDidAppearEvent(getVisibleDrawerScreenParams(), NavigationType.OpenSideMenu);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                NavigationApplication.instance.getEventEmitter().sendWillDisappearEvent(getVisibleDrawerScreenParams((ContentView) drawerView), NavigationType.CloseSideMenu);
                NavigationApplication.instance.getEventEmitter().sendDidDisappearEvent(getVisibleDrawerScreenParams((ContentView) drawerView), NavigationType.CloseSideMenu);
            }

            private BaseScreenParams getVisibleDrawerScreenParams() {
                return isDrawerOpen(Side.Left.gravity) ? leftMenuParams : rightMenuParams;
            }

            private BaseScreenParams getVisibleDrawerScreenParams(ContentView drawerView) {
                return drawerView == leftSideMenuView ? leftMenuParams : rightMenuParams;
            }
        };
        addDrawerListener(sideMenuListener);
    }

    private void setStyle(SideMenuParams params) {
        if (params == null) {
            return;
        }
        if (params.disableOpenGesture) {
            setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, params.side.gravity);
        }
    }
}

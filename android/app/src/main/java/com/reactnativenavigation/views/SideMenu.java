package com.reactnativenavigation.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.params.SideMenuParams;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.ViewUtils;

public class SideMenu extends DrawerLayout {
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

    public RelativeLayout getContentContainer() {
        return contentContainer;
    }

    public void destroy() {
        destroySideMenu(leftSideMenuView);
        destroySideMenu(rightSideMenuView);
    }

    private void destroySideMenu(ContentView sideMenuView) {
        if (sideMenuView == null) {
            return;
        }
        sideMenuView.unmountReactView();
        removeView(sideMenuView);
    }

    public void setVisible(boolean visible, boolean animated, Side side) {
        if (!isShown() && visible) {
            openDrawer(animated, side);
        }

        if (isShown() && !visible) {
            closeDrawer(animated, side);
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
        createContentContainer();
        leftSideMenuView = createSideMenu(leftMenuParams);
        rightSideMenuView = createSideMenu(rightMenuParams);
        setStyle(leftMenuParams);
        setStyle(rightMenuParams);
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

    private void setStyle(SideMenuParams params) {
        if (params == null) {
            return;
        }
        if (params.disableOpenGesture) {
            setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, params.side.gravity);
        }
    }
}

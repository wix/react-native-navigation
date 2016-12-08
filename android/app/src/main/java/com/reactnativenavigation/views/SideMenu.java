package com.reactnativenavigation.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.params.SideMenuParams;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.ViewUtils;

public class SideMenu extends DrawerLayout {
    public enum Side {Left, Right}

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

    public void setVisible(boolean visible, boolean animated) {
        if (!isShown() && visible) {
            openDrawer(animated);
        }

        if (isShown() && !visible) {
            closeDrawer(animated);
        }
    }

    public void openDrawer() {
        openDrawer(Gravity.LEFT);
    }

    public void openDrawer(boolean animated) {
        openDrawer(Gravity.LEFT, animated);
    }

    public void closeDrawer(boolean animated) {
        closeDrawer(Gravity.LEFT, animated);
    }

    public void toggleVisible(boolean animated) {
        if (isDrawerOpen(Gravity.LEFT)) {
            closeDrawer(animated);
        } else {
            openDrawer(animated);
        }
    }

    public SideMenu(Context context, SideMenuParams leftMenuParams, SideMenuParams rightMenuParams) {
        super(context);
        createContentContainer();
        leftSideMenuView = createSideMenu(leftMenuParams);
        rightSideMenuView = createSideMenu(rightMenuParams);
        setStyle(leftMenuParams);
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
        lp.gravity = params.side == Side.Left ? Gravity.LEFT : Gravity.RIGHT;
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
        if (params.disableOpenGesture) {
            setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }
}

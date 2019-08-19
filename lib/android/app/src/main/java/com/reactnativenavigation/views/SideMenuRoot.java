package com.reactnativenavigation.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.SideMenuOptions;
import com.reactnativenavigation.viewcontrollers.ViewController;

import androidx.annotation.RestrictTo;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentWithBehaviour;

public class SideMenuRoot extends CoordinatorLayout {
    private SideMenu sideMenu;

    public SideMenuRoot(Context context) {
        super(context);
    }

    public void addSideMenu(SideMenu sideMenu, BehaviourAdapter behaviourAdapter) {
        this.sideMenu = sideMenu;
        enableDrawingBehindStatusBar();
        addView(sideMenu, matchParentWithBehaviour(new BehaviourDelegate(behaviourAdapter)));
    }

    public boolean isDrawerOpen(int gravity) {
        return sideMenu.isDrawerOpen(gravity);
    }

    public void setCenter(ViewController center) {
        sideMenu.addView(center.getView());
    }

    public void setLeft(ViewController left, Options options) {
        sideMenu.addView(left.getView(), createLayoutParams(options.sideMenuRootOptions.left, Gravity.LEFT));
    }

    public void setRight(ViewController right, Options options) {
        sideMenu.addView(right.getView(), createLayoutParams(options.sideMenuRootOptions.right, Gravity.RIGHT));
    }

    public boolean isSideMenu(View view) {
        return sideMenu == view;
    }

    private DrawerLayout.LayoutParams createLayoutParams(SideMenuOptions options, int gravity) {
        return new DrawerLayout.LayoutParams(getWidth(options), getHeight(options), gravity);
    }

    private int getWidth(SideMenuOptions sideMenuOptions) {
        int width = MATCH_PARENT;
        if (sideMenuOptions.width.hasValue()) {
            width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sideMenuOptions.width.get(), Resources
                    .getSystem().getDisplayMetrics());
        }
        return width;
    }

    private int getHeight(SideMenuOptions sideMenuOptions) {
        int height = MATCH_PARENT;
        if (sideMenuOptions.height.hasValue()) {
            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sideMenuOptions.height.get(), Resources.getSystem().getDisplayMetrics());
        }
        return height;
    }

    private void enableDrawingBehindStatusBar() {
        sideMenu.setFitsSystemWindows(true);
        ViewCompat.setOnApplyWindowInsetsListener(sideMenu, (view, insets) -> insets);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public SideMenu getSideMenu() {
        return sideMenu;
    }
}

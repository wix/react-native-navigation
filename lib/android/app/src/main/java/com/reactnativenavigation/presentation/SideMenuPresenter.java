package com.reactnativenavigation.presentation;

import android.view.Gravity;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.SideMenuRootOptions;
import com.reactnativenavigation.views.SideMenu;

import androidx.annotation.RestrictTo;
import androidx.drawerlayout.widget.DrawerLayout;

public class SideMenuPresenter {

    private SideMenu sideMenu;

    public void bindView(SideMenu sideMenu) {
        this.sideMenu = sideMenu;
    }

    public boolean handleBack() {
        if (sideMenu.isDrawerOpen(Gravity.LEFT)) {
            sideMenu.closeDrawer(Gravity.LEFT);
            return true;
        }
        if (sideMenu.isDrawerOpen(Gravity.RIGHT)) {
            sideMenu.closeDrawer(Gravity.RIGHT);
            return true;
        }
        return false;
    }

    public void applyOptions(Options options) {
        applyLockMode(options.sideMenuRootOptions);
    }

    public void mergeOptions(SideMenuRootOptions options) {
        mergeLockMode(options);
        mergeVisibility(options);
    }

    public void applyChildOptions(Options options) {
        applyLockMode(options.sideMenuRootOptions);
        mergeVisibility(options.sideMenuRootOptions);
    }

    private void applyLockMode(SideMenuRootOptions options) {
        int leftLockMode = options.left.enabled.isTrueOrUndefined() ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        sideMenu.setDrawerLockMode(leftLockMode, Gravity.LEFT);

        int rightLockMode = options.right.enabled.isTrueOrUndefined() ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        sideMenu.setDrawerLockMode(rightLockMode, Gravity.RIGHT);
    }

    private void mergeVisibility(SideMenuRootOptions options) {
        if (options.left.visible.isTrue()) {
            sideMenu.openDrawer(Gravity.LEFT, options.left.animate.get(true));
        } else if (options.left.visible.isFalse()) {
            sideMenu.closeDrawer(Gravity.LEFT, options.left.animate.get(true));
        }

        if (options.right.visible.isTrue()) {
            sideMenu.openDrawer(Gravity.RIGHT, options.right.animate.get(true));
        } else if (options.right.visible.isFalse()) {
            sideMenu.closeDrawer(Gravity.RIGHT, options.right.animate.get(true));
        }

        options.left.visible.consume();
        options.right.visible.consume();
    }

    private void mergeLockMode(SideMenuRootOptions options) {
        if (options.left.enabled.isFalse()) {
            sideMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
        } else if (options.left.enabled.isTrue()) {
            sideMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
        }

        if (options.right.enabled.isFalse()) {
            sideMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        } else if (options.right.enabled.isTrue()) {
            sideMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public SideMenu getSideMenu() {
        return sideMenu;
    }
}

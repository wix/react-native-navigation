package com.reactnativenavigation.viewcontrollers.sidemenu;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;

import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.SideMenuRootOptions;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.sidemenu.SideMenu;

import androidx.annotation.RestrictTo;
import androidx.drawerlayout.widget.DrawerLayout;

public class SideMenuPresenter {

    private SideMenu sideMenu;
    private ViewController<?> left;
    private ViewController<?> right;

    public void bindView(SideMenu sideMenu) {
        this.sideMenu = sideMenu;
    }

    public void bindLeft(ViewController<?> left) {
        this.left = left;
    }

    public void bindRight(ViewController<?> right) {
        this.right = right;
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
        applyLeftWidth(options.sideMenuRootOptions);
        applyRightWidth(options.sideMenuRootOptions);
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

    private void applyLeftWidth(SideMenuRootOptions sideMenuRootOptions) {
        if (left != null && sideMenuRootOptions.left.width.hasValue()) {
            left.getView().getLayoutParams().width = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    sideMenuRootOptions.left.width.get(),
                    Resources.getSystem().getDisplayMetrics());

            left.getView().requestLayout();
        }
    }

    private void applyRightWidth(SideMenuRootOptions sideMenuRootOptions) {
        if (right != null && sideMenuRootOptions.right.width.hasValue()) {
            right.getView().getLayoutParams().width = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    sideMenuRootOptions.right.width.get(),
                    Resources.getSystem().getDisplayMetrics());

            right.getView().requestLayout();
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public SideMenu getSideMenu() {
        return sideMenu;
    }
}

package com.reactnativenavigation.presentation;

import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import com.reactnativenavigation.parse.SideMenuRootOptions;

public class SideMenuPresenter {

    private DrawerLayout sideMenu;

    public void bindView(DrawerLayout sideMenu) {
        this.sideMenu = sideMenu;
    }

    /**
     * Called when initializing the sideMenu DrawerLayout.
     *
     * @param options Side menu options
     */
    public void applyInitialOptions(SideMenuRootOptions options) {
        if (options.left.enabled.isFalse()) {
            sideMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
        }
        else  if (options.left.enabled.isTrue()) {
            sideMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
        }

        if (options.right.enabled.isFalse()) {
            sideMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        }
        else  if (options.right.enabled.isTrue()) {
            sideMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
        }
    }

    public void present(SideMenuRootOptions options) {
        // TODO: Not sure why we call these options when we show the DrawerLayout rather than when initializing it.
        // TODO: (i.e. `setDrawerLockMode()` is supposed to be called when the DrawerLayout is initialized.
        applyInitialOptions(options);

        if (options.left.visible.isTrue()) {
            sideMenu.openDrawer(Gravity.LEFT);

        } else if (options.left.visible.isFalse() && sideMenu.isDrawerOpen(Gravity.LEFT)) {
            sideMenu.closeDrawer(Gravity.LEFT);
        }

        if (options.right.visible.isTrue()) {
            sideMenu.openDrawer(Gravity.RIGHT);

        } else if (options.right.visible.isFalse() && sideMenu.isDrawerOpen(Gravity.RIGHT)){
            sideMenu.closeDrawer(Gravity.RIGHT);
        }
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
}

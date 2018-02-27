package com.reactnativenavigation.presentation;

import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import com.reactnativenavigation.parse.SideMenuRootOptions;

public class SideMenuOptionsPresenter {

    private DrawerLayout sideMenu;

    public SideMenuOptionsPresenter(DrawerLayout sideMenu) {
        this.sideMenu = sideMenu;
    }

    public void present(SideMenuRootOptions options) {
        if (options.left.visible.isTrue()) {
            sideMenu.openDrawer(Gravity.LEFT);
        }
        if (options.right.visible.isTrue()) {
            sideMenu.openDrawer(Gravity.RIGHT);
        }
    }
}

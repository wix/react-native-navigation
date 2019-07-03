package com.reactnativenavigation.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

public class SideMenu extends DrawerLayout {
    public SideMenu(@NonNull Context context) {
        super(context);
    }

    @Override
    public void openDrawer(int gravity, boolean animate) {
        try {
            super.openDrawer(gravity, animate);
        } catch (IllegalArgumentException e) {
            Log.w("RNN", "Tried to open sideMenu, but it's not defined");
        }
    }
}

package com.reactnativenavigation.views;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

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

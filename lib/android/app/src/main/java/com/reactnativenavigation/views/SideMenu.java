package com.reactnativenavigation.views;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
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

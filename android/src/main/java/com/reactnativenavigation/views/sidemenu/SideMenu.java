package com.reactnativenavigation.views.sidemenu;

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

    @Override
    public void setDrawerLockMode(int lockMode, int edgeGravity) {
        int currentLockMode = getDrawerLockMode(edgeGravity);
        if (currentLockMode != lockMode) super.setDrawerLockMode(lockMode, edgeGravity);
    }
}

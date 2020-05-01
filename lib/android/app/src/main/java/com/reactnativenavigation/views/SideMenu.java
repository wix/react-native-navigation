package com.reactnativenavigation.views;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Gravity;

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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isDrawerVisible(Gravity.START)) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isDrawerVisible(Gravity.START)) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}

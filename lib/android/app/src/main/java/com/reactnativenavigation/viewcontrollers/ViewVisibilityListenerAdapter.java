package com.reactnativenavigation.viewcontrollers;

import android.view.View;

public class ViewVisibilityListenerAdapter implements ViewController.ViewVisibilityListener {
    @Override
    public boolean onViewAppeared(View view) {
        return false;
    }

    @Override
    public boolean onViewDisappear(View view) {
        return false;
    }
}

package com.reactnativenavigation.mocks;

import android.content.*;
import android.view.*;

import com.reactnativenavigation.viewcontrollers.*;

public class TopTabLayoutMock extends View implements ComponentViewController.IReactView {

    public TopTabLayoutMock(Context context) {
        super(context);
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public View asView() {
        return this;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void sendComponentStart() {

    }

    @Override
    public void sendComponentStop() {

    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {

    }
}

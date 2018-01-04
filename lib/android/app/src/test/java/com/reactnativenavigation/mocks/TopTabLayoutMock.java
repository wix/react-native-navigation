package com.reactnativenavigation.mocks;

import android.content.Context;
import android.view.View;

import com.reactnativenavigation.viewcontrollers.ContainerViewController;

public class TopTabLayoutMock extends View implements ContainerViewController.IReactView {

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
    public void sendContainerStart() {

    }

    @Override
    public void sendContainerStop() {

    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {

    }
}

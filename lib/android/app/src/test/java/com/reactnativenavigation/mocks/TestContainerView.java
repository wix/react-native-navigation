package com.reactnativenavigation.mocks;

import android.content.Context;
import android.view.View;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.views.ReactContainer;
import com.reactnativenavigation.views.TopBar;

public class TestContainerView extends View implements ReactContainer {

    private TopBar topBar;

    public TestContainerView(final Context context) {
        super(context);
        topBar = new TopBar(context, this);

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
    public void applyOptions(NavigationOptions options) {

    }

    @Override
    public void sendOnNavigationButtonPressed(String id) {

    }

    @Override
    public TopBar getTopBar() {
        return topBar;
    }

    @Override
    public View getContentView() {
        return null;
    }

    @Override
    public void drawBehindTopBar() {

    }

    @Override
    public void drawBelowTopBar() {

    }
}

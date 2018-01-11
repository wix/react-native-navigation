package com.reactnativenavigation.mocks;

import android.content.*;
import android.view.*;

import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.views.*;

public class TestComponentView extends View implements ReactComponent, TitleBarButton.OnClickListener {

    private TopBar topBar;

    public TestComponentView(final Context context) {
        super(context);
        topBar = new TopBar(context, this, this, null);

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
    public void applyOptions(Options options) {

    }

    @Override
    public void sendOnNavigationButtonPressed(String id) {

    }

    @Override
    public TopBar getTopBar() {
        return topBar;
    }

    @Override
    public void drawBehindTopBar() {

    }

    @Override
    public void drawBelowTopBar() {

    }

    @Override
    public void onPress(String buttonId) {

    }
}

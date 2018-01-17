package com.reactnativenavigation.mocks;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.TitleBarButton;
import com.reactnativenavigation.views.TopBar;

public class TestComponentLayout extends RelativeLayout implements ReactComponent, TitleBarButton.OnClickListener {

    private final View contentView;

    public TestComponentLayout(final Context context) {
        super(context);
        contentView = new View(context);
        addView(contentView);
    }

    @Override
    public void drawBehindTopBar() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.removeRule(BELOW);
        contentView.setLayoutParams(layoutParams);
    }

    @Override
    public void drawBelowTopBar(TopBar topBar) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(BELOW, topBar.getId());
        contentView.setLayoutParams(layoutParams);
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
    public ScrollEventListener getScrollEventListener() {
        return null;
    }

    @Override
    public void onPress(String buttonId) {

    }
}

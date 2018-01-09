package com.reactnativenavigation.mocks;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.views.ReactContainer;
import com.reactnativenavigation.views.TopBar;

public class TestContainerLayout extends RelativeLayout implements ReactContainer {

    private final TopBar topBar;
    private final View contentView;
    private final OptionsPresenter optionsPresenter;

    public TestContainerLayout(final Context context) {
        super(context);
        topBar = new TopBar(context, this);
        contentView = new View(context);
        addView(topBar);
        addView(contentView);
        optionsPresenter = new OptionsPresenter(this);
    }

    public TopBar getTopBar() {
        return topBar;
    }

    @Override
    public View getContentView() {
        return contentView;
    }

    @Override
    public void drawBehindTopBar() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.removeRule(BELOW);
        contentView.setLayoutParams(layoutParams);
    }

    @Override
    public void drawBelowTopBar() {
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
    public void sendContainerStart() {
    }

    @Override
    public void sendContainerStop() {
    }

    @Override
    public void applyOptions(NavigationOptions options) {
        optionsPresenter.applyOptions(options);
    }

    @Override
    public void sendOnNavigationButtonPressed(String id) {

    }
}

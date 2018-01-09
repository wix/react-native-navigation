package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.react.uimanager.events.EventDispatcher;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.viewcontrollers.ContainerViewController.IReactView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("ViewConstructor")
public class ContainerLayout extends RelativeLayout implements ReactContainer {

    private TopBar topBar;
    private IReactView reactView;
    private final OptionsPresenter optionsPresenter;

    public ContainerLayout(Context context, IReactView reactView, EventDispatcher eventDispatcher) {
        super(context);
        this.reactView = reactView;
        this.topBar = new TopBar(context, this, eventDispatcher);
        topBar.setId(View.generateViewId());

        optionsPresenter = new OptionsPresenter(this);
        initViews();
    }

    private void initViews() {
        LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        layoutParams.addRule(BELOW, topBar.getId());
        addView(reactView.asView(), layoutParams);
        addView(topBar, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    }

    @Override
    public boolean isReady() {
        return reactView.isReady();
    }

    @Override
    public View asView() {
        return this;
    }

    @Override
    public void destroy() {
        reactView.destroy();
    }

    @Override
    public void sendContainerStart() {
        reactView.sendContainerStart();
    }

    @Override
    public void sendContainerStop() {
        reactView.sendContainerStop();
    }

    @Override
    public void applyOptions(NavigationOptions options) {
        optionsPresenter.applyOptions(options);
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        reactView.sendOnNavigationButtonPressed(buttonId);
    }

    @Override
    public TopBar getTopBar() {
        return topBar;
    }

    @Override
    public View getContentView() {
        return reactView.asView();
    }

    @Override
    public void drawBehindTopBar() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) reactView.asView().getLayoutParams();
        layoutParams.removeRule(BELOW);
        reactView.asView().setLayoutParams(layoutParams);
    }

    @Override
    public void drawBelowTopBar() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) reactView.asView().getLayoutParams();
        layoutParams.addRule(BELOW, topBar.getId());
        reactView.asView().setLayoutParams(layoutParams);
    }
}
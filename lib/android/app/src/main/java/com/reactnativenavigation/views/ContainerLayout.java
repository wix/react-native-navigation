package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.react.uimanager.events.EventDispatcher;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.viewcontrollers.ContainerViewController.IReactView;

@SuppressLint("ViewConstructor")
public class ContainerLayout extends RelativeLayout implements ReactContainer {

    private TopBar topBar;
    private IReactView reactView;
    private final OptionsPresenter optionsPresenter;

    public ContainerLayout(Context context, IReactView reactView, EventDispatcher eventDispatcher) {
        super(context);
        this.topBar = new TopBar(context);
        topBar.setId(View.generateViewId());
        topBar.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.reactView = reactView;
        optionsPresenter = new OptionsPresenter(topBar, reactView.asView(), eventDispatcher);
        initViews();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initViews() {
//        setOrientation(VERTICAL);
        addView(reactView.asView());
        addView(topBar);
        LayoutParams layoutParams = (LayoutParams) reactView.asView().getLayoutParams();
//        layoutParams.addRule(BELOW, topBar.getId());
        reactView.asView().setLayoutParams(layoutParams);
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
    @RestrictTo(RestrictTo.Scope.TESTS)
    public TopBar getTopBar() {
        return topBar;
    }
}

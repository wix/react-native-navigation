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
import com.reactnativenavigation.viewcontrollers.ContainerViewController.IReactView;

@SuppressLint("ViewConstructor")
public class ContainerLayout extends RelativeLayout implements ReactContainer {

	private TopBar topBar;
	private IReactView reactView;
	private final OptionsPresenter optionsPresenter;

    public ContainerLayout(Context context, IReactView reactView, EventDispatcher eventDispatcher) {
        super(context);

        this.topBar = new TopBar(context, this, eventDispatcher);
        topBar.setId(View.generateViewId());

        this.reactView = reactView;
        optionsPresenter = new OptionsPresenter(topBar, reactView.asView());
        initViews();
	}

    private void initViews() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(BELOW, topBar.getId());
        addView(reactView.asView(), layoutParams);
        addView(topBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
    @RestrictTo(RestrictTo.Scope.TESTS)
    public TopBar getTopBar() {
        return topBar;
    }
}

package com.reactnativenavigation.views;

import android.annotation.*;
import android.content.*;
import android.view.*;
import android.widget.*;

import com.facebook.react.uimanager.events.*;
import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.presentation.*;
import com.reactnativenavigation.viewcontrollers.ComponentViewController.*;

import static android.view.ViewGroup.LayoutParams.*;

@SuppressLint("ViewConstructor")
public class ComponentLayout extends RelativeLayout implements ReactComponent, TitleBarButton.OnClickListener {

    private TopBar topBar;
    private IReactView reactView;
    private final OptionsPresenter optionsPresenter;

	public ComponentLayout(Context context, IReactView reactView, EventDispatcher eventDispatcher) {
		super(context);
		this.reactView = reactView;
		this.topBar = new TopBar(context, reactView.asView(), this, eventDispatcher);
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
	public void sendComponentStart() {
		reactView.sendComponentStart();
	}

	@Override
	public void sendComponentStop() {
		reactView.sendComponentStop();
	}

    @Override
    public void applyOptions(Options options) {
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

    @Override
    public void onPress(String buttonId) {
        reactView.sendOnNavigationButtonPressed(buttonId);
    }
}

package com.reactnativenavigation.mocks;

import android.content.Context;
import android.view.View;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.views.ReactContainer;
import com.reactnativenavigation.views.TopBar;

public class TestContainerLayout extends View implements ReactContainer {

    private final TopBar topBar;
    private final OptionsPresenter optionsPresenter;

    public TestContainerLayout(final Context context) {
		super(context);
        topBar = new TopBar(context);
        optionsPresenter = new OptionsPresenter(topBar, this);
    }

    public TopBar getTopBar() {
        return topBar;
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
}

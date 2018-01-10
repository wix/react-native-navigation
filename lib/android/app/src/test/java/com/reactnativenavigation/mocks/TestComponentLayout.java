package com.reactnativenavigation.mocks;

import android.content.*;
import android.view.*;
import android.widget.*;

import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.presentation.*;
import com.reactnativenavigation.views.*;

public class TestComponentLayout extends RelativeLayout implements ReactComponent, TitleBarButton.OnClickListener {

    private final TopBar topBar;
    private final View contentView;
    private final OptionsPresenter optionsPresenter;

    public TestComponentLayout(final Context context) {
        super(context);
        contentView = new View(context);
        topBar = new TopBar(context, contentView, this, null);
        addView(topBar);
        addView(contentView);
        optionsPresenter = new OptionsPresenter(this);
    }

    public TopBar getTopBar() {
        return topBar;
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
    public void sendComponentStart() {
    }

    @Override
    public void sendComponentStop() {
    }

    @Override
    public void applyOptions(Options options) {
        optionsPresenter.applyOptions(options);
    }

    @Override
    public void sendOnNavigationButtonPressed(String id) {

    }

    @Override
    public void onPress(String buttonId) {

    }
}

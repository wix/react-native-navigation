package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.utils.CompatUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("ViewConstructor")
public class StackLayout extends RelativeLayout {
    private TopBar topBar;

    public StackLayout(Context context, TitleBarButton.OnClickListener topBarButtonClickListener) {
        super(context);
        topBar = new TopBar(context, topBarButtonClickListener, this);
        topBar.setId(CompatUtils.generateViewId());
        createLayout();
        setContentDescription("StackLayout");
    }

    void createLayout() {
        addView(topBar, MATCH_PARENT, WRAP_CONTENT);
    }

    public void applyOptions(Options options) {
        new OptionsPresenter(topBar).applyOrientation(options.orientationOptions);
    }

    public void applyOptions(Options options, Component component) {
        new OptionsPresenter(topBar, component).applyOptions(options);
    }

    public void onChildWillDisappear(Options disappearing, Options appearing) {
        new OptionsPresenter(topBar).onChildWillDisappear(disappearing, appearing);
    }

    public void clearOptions() {
        topBar.clear();
    }

    public void initTopTabs(ViewPager viewPager) {
        topBar.initTopTabs(viewPager);
    }

    public void clearTopTabs() {
        topBar.clearTopTabs();
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public TopBar getTopBar() {
        return topBar;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setTopBar(TopBar topBar) {
        this.topBar = topBar;
    }
}

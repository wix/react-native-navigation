package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.FabOptionsPresenter;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.utils.CompatUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("ViewConstructor")
public class StackLayout extends RelativeLayout {

    private final TopBar topBar;

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

    public void applyOptions(Options options, ReactComponent component) {
        new OptionsPresenter(topBar, component).applyOptions(options);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public TopBar getTopBar() {
        return topBar;
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
}

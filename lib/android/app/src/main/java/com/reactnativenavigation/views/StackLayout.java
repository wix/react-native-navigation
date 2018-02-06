package com.reactnativenavigation.views;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.utils.CompatUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class StackLayout extends RelativeLayout implements TitleBarButton.OnClickListener {

    private final TopBar topBar;
    private Fab fab;

    public StackLayout(Context context) {
        super(context);
        topBar = new TopBar(context, this);
        topBar.setId(CompatUtils.generateViewId());
        initFab(context);
        createLayout();
    }

    private void initFab(Context context) {
        fab = new Fab(context);
    }

    void createLayout() {
        addView(topBar, MATCH_PARENT, WRAP_CONTENT);
        addView(fab);
    }

    @Override
    public void onPress(String buttonId) {

    }

    public void applyOptions(Options options, ReactComponent component) {
        new OptionsPresenter(topBar, component, fab).applyOptions(options);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public TopBar getTopBar() {
        return topBar;
    }

    public void clearOptions() {
        topBar.clear();
    }

    public void setupTopTabsWithViewPager(ViewPager viewPager) {
        topBar.setupTopTabsWithViewPager(viewPager);
    }
}

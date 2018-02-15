package com.reactnativenavigation.views;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.utils.CompatUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class StackLayout extends RelativeLayout implements TitleBarButton.OnClickListener {

    private final TopBar topBar;

    public StackLayout(Context context) {
        super(context);
        topBar = new TopBar(context, this, this);
        topBar.setId(CompatUtils.generateViewId());
        createLayout();
        setContentDescription("StackLayout");
    }

    void createLayout() {
        addView(topBar, MATCH_PARENT, WRAP_CONTENT);
    }

    @Override
    public void onPress(String buttonId) {

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

    public void setupTopTabsWithViewPager(ViewPager viewPager) {
        topBar.setupTopTabsWithViewPager(viewPager);
    }
}

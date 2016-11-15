package com.reactnativenavigation.screens;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;

import com.reactnativenavigation.params.PageParams;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.views.CollapsingContentView;
import com.reactnativenavigation.views.ContentView;
import com.reactnativenavigation.views.LeftButtonOnClickListener;
import com.reactnativenavigation.views.collapsingToolbar.CollapseAmount;
import com.reactnativenavigation.views.collapsingToolbar.CollapseCalculator;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingViewMeasurer;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingTopBar;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingView;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingViewPager;
import com.reactnativenavigation.views.collapsingToolbar.OnScrollListener;
import com.reactnativenavigation.views.collapsingToolbar.OnScrollViewAddedListener;
import com.reactnativenavigation.views.collapsingToolbar.ScrollListener;
import com.reactnativenavigation.views.collapsingToolbar.behaviours.CollapseBehaviour;

public class CollapsingViewPagerScreen extends ViewPagerScreen {

    public CollapsingViewPagerScreen(AppCompatActivity activity, ScreenParams screenParams, LeftButtonOnClickListener backButtonListener) {
        super(activity, screenParams, backButtonListener);
    }

    @Override
    protected void createTopBar() {
        final CollapsingTopBar topBar = new CollapsingTopBar(getContext(), styleParams.collapsingTopBarParams);
        topBar.setScrollListener(getScrollListener());
        this.topBar = topBar;
    }

    @Override
    protected ViewPager createViewPager(Context context) {
        CollapsingViewPager viewPager = new CollapsingViewPager(context);
        if (screenParams.styleParams.drawScreenBelowTopBar) {
            viewPager.setViewMeasurer(new CollapsingViewMeasurer((CollapsingTopBar) topBar, this));
        }
        return viewPager;
    }

    protected ContentView createContentView(PageParams tab) {
        CollapsingContentView contentView = new CollapsingContentView(getContext(), tab.screenId, tab.navigationParams);
        contentView.setViewMeasurer(new CollapsingViewMeasurer((CollapsingTopBar) topBar, this));
        setupCollapseDetection(contentView);
        return contentView;
    }

    private void setupCollapseDetection(CollapsingContentView contentView) {
        contentView.setupCollapseDetection(getScrollListener(), new OnScrollViewAddedListener() {
            @Override
            public void onScrollViewAdded(ScrollView scrollView) {
                ((CollapsingTopBar) topBar).onScrollViewAdded(scrollView);
            }
        });
    }

    private ScrollListener getScrollListener() {
        return new ScrollListener(new CollapseCalculator((CollapsingView) topBar, getCollapseBehaviour()),
                new OnScrollListener() {
                    @Override
                    public void onScroll(CollapseAmount amount) {
                        ((CollapsingView) topBar).collapse(amount);
                        ((CollapsingView) viewPager).collapse(amount);
                    }

                    @Override
                    public void onFling(CollapseAmount amount) {
                        ((CollapsingView) topBar).collapse(amount);
                    }
                },
                getCollapseBehaviour()
        );
    }

    private CollapseBehaviour getCollapseBehaviour() {
        return screenParams.styleParams.collapsingTopBarParams.collapseBehaviour;
    }

    @Override
    public void destroy() {
        super.destroy();
        for (ContentView contentView : contentViews) {
            ((CollapsingContentView) contentView).destroy();
        }
    }
}

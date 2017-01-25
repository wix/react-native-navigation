package com.reactnativenavigation.screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;

import com.reactnativenavigation.events.Event;
import com.reactnativenavigation.events.ViewPagerScreenChangedEvent;
import com.reactnativenavigation.events.ViewPagerScreenScrollStartEvent;
import com.reactnativenavigation.params.PageParams;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.views.CollapsingContentView;
import com.reactnativenavigation.views.ContentView;
import com.reactnativenavigation.views.LeftButtonOnClickListener;
import com.reactnativenavigation.views.collapsingToolbar.CollapseAmount;
import com.reactnativenavigation.views.collapsingToolbar.CollapseCalculator;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingTopBar;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingView;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingViewMeasurer;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingViewPager;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingViewPagerContentViewMeasurer;
import com.reactnativenavigation.views.collapsingToolbar.OnScrollListener;
import com.reactnativenavigation.views.collapsingToolbar.OnScrollViewAddedListener;
import com.reactnativenavigation.views.collapsingToolbar.ScrollListener;
import com.reactnativenavigation.views.collapsingToolbar.behaviours.CollapseBehaviour;

@SuppressLint("ViewConstructor")
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
        if (tab.hasCollapsingTopBar()) {
            CollapsingContentView contentView = new CollapsingContentView(getContext(), tab.screenId, tab.navigationParams);
            contentView.setViewMeasurer(new CollapsingViewPagerContentViewMeasurer((CollapsingTopBar) topBar, this, screenParams.styleParams));
            setupCollapseDetection(contentView);
            return contentView;
        } else {
            return new ContentView(getContext(), tab.screenId, tab.navigationParams);
        }
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
                        ((CollapsingView) viewPager).collapse(amount);
                    }
                },
                getCollapseBehaviour()
        );
    }

    private CollapseBehaviour getCollapseBehaviour() {
        return screenParams.styleParams.collapsingTopBarParams.collapseBehaviour;
    }

    @Override
    public void onEvent(Event event) {
        super.onEvent(event);
        if (ViewPagerScreenScrollStartEvent.TYPE.equals(event.getType()) || ViewPagerScreenChangedEvent.TYPE.equals(event.getType())) {
            ((CollapsingView) topBar).collapse(new CollapseAmount(CollapseCalculator.Direction.Down));
            ((CollapsingView) viewPager).collapse(new CollapseAmount(CollapseCalculator.Direction.Down));
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        for (ContentView contentView : contentViews) {
            if (contentView instanceof CollapsingContentView) {
                ((CollapsingContentView) contentView).destroy();
            }
        }
    }
}

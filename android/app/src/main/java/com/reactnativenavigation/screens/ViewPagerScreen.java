package com.reactnativenavigation.screens;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.reactnativenavigation.params.BaseScreenParams;
import com.reactnativenavigation.params.FabParams;
import com.reactnativenavigation.params.PageParams;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.views.ContentView;
import com.reactnativenavigation.views.LeftButtonOnClickListener;
import com.reactnativenavigation.views.TopTabs;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ViewPagerScreen extends Screen {

    private static final int OFFSCREEN_PAGE_LIMIT = 99;
    protected List<ContentView> contentViews;
    protected ViewPager viewPager;

    public ViewPagerScreen(AppCompatActivity activity, ScreenParams screenParams, LeftButtonOnClickListener backButtonListener) {
        super(activity, screenParams, backButtonListener);
    }

    @Override
    public BaseScreenParams getScreenParams() {
        return screenParams.topTabParams.get(viewPager.getCurrentItem());
    }

    @Override
    public void setFab(FabParams fabParams) {
        super.setFab(fabParams);
        getScreenParams().fabParams = fabParams;
    }

    @Override
    public ContentView getContentView() {
        return contentViews.get(viewPager.getCurrentItem());
    }

    @Override
    protected void createContent() {
        TopTabs topTabs = topBar.initTabs();
        createViewPager();
        addPages();
        setupViewPager(topTabs);
        setTopTabIcons(topTabs);
    }

    private void createViewPager() {
        viewPager = createViewPager(getContext());
        viewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        LayoutParams lp = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        if (screenParams.styleParams.drawScreenBelowTopBar) {
            lp.addRule(BELOW, topBar.getId());
        }
        addView(viewPager, lp);
    }

    protected ViewPager createViewPager(Context context) {
        return new ViewPager(context);
    }

    private void addPages() {
        contentViews = new ArrayList<>();
        for (PageParams tab : screenParams.topTabParams) {
            addPage(tab);
        }
    }

    private void addPage(PageParams tab) {
        ContentView contentView = createContentView(tab);
        addContent(contentView);
        contentViews.add(contentView);
    }

    protected ContentView createContentView(PageParams tab) {
        return new ContentView(getContext(), tab.screenId, tab.navigationParams);
    }

    private void setupViewPager(TabLayout tabLayout) {
        ContentViewPagerAdapter adapter = new ContentViewPagerAdapter(contentViews, screenParams.topTabParams);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setTopTabIcons(TopTabs topTabs) {
        for (int i = 0; i < topTabs.getTabCount(); i++) {
            PageParams pageParams = screenParams.topTabParams.get(i);
            if (pageParams.tabIcon != null) {
                topTabs.getTabAt(i).setIcon(pageParams.tabIcon);
            }
        }
        topTabs.setTopTabsIconColor(screenParams.styleParams);
    }

    private void addContent(ContentView contentView) {
        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        viewPager.addView(contentView, params);
    }

    @Override
    public void unmountReactView() {
        for (ContentView contentView : contentViews) {
            contentView.unmountReactView();
        }
    }

    @Override
    public void setOnDisplayListener(OnDisplayListener onContentViewDisplayedListener) {
        contentViews.get(0).setOnDisplayListener(onContentViewDisplayedListener);
    }

    @Override
    public String getScreenInstanceId() {
        return screenParams.topTabParams.get(viewPager.getCurrentItem()).navigationParams.screenInstanceId;
    }

    @Override
    public String getNavigatorEventId() {
        return screenParams.topTabParams.get(viewPager.getCurrentItem()).navigationParams.navigatorEventId;
    }
}

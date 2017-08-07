package com.reactnativenavigation.screens;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.events.EventBus;
import com.reactnativenavigation.events.ScreenChangedEvent;
import com.reactnativenavigation.events.ViewPagerScreenChangedEvent;
import com.reactnativenavigation.events.ViewPagerScreenScrollStartEvent;
import com.reactnativenavigation.params.PageParams;
import com.reactnativenavigation.views.ContentView;

import java.util.List;

class ContentViewPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private List<ContentView> contentViews;
    private List<PageParams> pageParams;
    private int currentPosition = 0;

    ContentViewPagerAdapter(List<ContentView> contentViews, List<PageParams> pageParams) {
        this.contentViews = contentViews;
        this.pageParams = pageParams;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return contentViews.get(position);
    }

    @Override
    public int getCount() {
        return contentViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageParams.get(position).title;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        EventBus.instance.post(new ViewPagerScreenChangedEvent());
        sendDisappearEvents(currentPosition);
        currentPosition = position;
        EventBus.instance.post(new ScreenChangedEvent(pageParams.get(currentPosition)));
        sendTabSelectedEventToJs();
        sendAppearEvents(position);
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            EventBus.instance.post(new ViewPagerScreenScrollStartEvent());
        }
    }

    private void sendAppearEvents(int position) {
        PageParams pageParams = this.pageParams.get(position);
        pageParams.timestamp = System.currentTimeMillis();
        NavigationApplication.instance.getEventEmitter().sendWillAppearEvent(pageParams, NavigationType.TopTabSelected);
        NavigationApplication.instance.getEventEmitter().sendDidAppearEvent(pageParams, NavigationType.TopTabSelected);
    }

    private void sendDisappearEvents(int position) {
        PageParams pageParams = this.pageParams.get(position);
        pageParams.timestamp = System.currentTimeMillis();
        NavigationApplication.instance.getEventEmitter().sendWillDisappearEvent(pageParams, NavigationType.TopTabSelected);
        NavigationApplication.instance.getEventEmitter().sendDidDisappearEvent(pageParams, NavigationType.TopTabSelected);
    }

    private void sendTabSelectedEventToJs() {
        WritableMap data = Arguments.createMap();
        String navigatorEventId = contentViews.get(currentPosition).getNavigatorEventId();
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("tabSelected", navigatorEventId, data);
    }
}

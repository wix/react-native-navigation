package com.reactnativenavigation.viewcontrollers.toptabs;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class TopTabsAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private List<TopTabController> tabs;
    private int currentPage = 0;

    TopTabsAdapter(List<TopTabController> tabs) {
        this.tabs = tabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTabTitle();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return tabs.get(position).getView();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabs.get(currentPage).onViewDisappear();
        tabs.get(position).onViewAppeared();
        currentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    int getCurrentItem() {
        return currentPage;
    }
}

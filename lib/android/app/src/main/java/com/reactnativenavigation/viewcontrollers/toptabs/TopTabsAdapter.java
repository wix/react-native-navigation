package com.reactnativenavigation.viewcontrollers.toptabs;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.List;

public class TopTabsAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private List<ViewController> tabs;
    private int currentPage = 0;

    public TopTabsAdapter(List<ViewController> tabs) {
        this.tabs = tabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTabOptions(position).topTabOptions.title.get("");
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
    public Object instantiateItem(ViewGroup component, int position) {
        return tabs.get(position).getView();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabs.get(currentPage).onViewDisappear();
        tabs.get(position).onViewWillAppear();
        currentPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private Options getTabOptions(int position) {
        return tabs.get(position).options;
    }
}

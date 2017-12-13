package com.reactnativenavigation.viewcontrollers.toptabs;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.List;

public class TopTabsAdapter extends PagerAdapter {
    private List<ViewController> tabs;

    TopTabsAdapter(List<ViewController> tabs) {
        this.tabs = tabs;
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
}

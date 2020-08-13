package com.reactnativenavigation.viewcontrollers.toptabs;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.viewcontrollers.viewcontroller.IReactView;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

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
        View tabView = tabs.get(position).getView();
        component.addView(tabView, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        return tabView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if(object instanceof IReactView)((IReactView)object).destroy();
        container.removeView((View) object);
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

    @VisibleForTesting
    public ViewGroup getItem(int position){
        return tabs.get(position).getView();
    }

}

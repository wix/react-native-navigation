package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.reactnativenavigation.utils.CollectionUtils.filter;
import static com.reactnativenavigation.utils.CollectionUtils.forEach;

public class BottomTabsAttacher {
    private List<ViewController> tabs;
    private BottomTabsPresenter presenter;
    private Runnable onAppeared;

    public BottomTabsAttacher(List<ViewController> tabs, BottomTabsPresenter presenter) {
        this.tabs = tabs;
        this.presenter = presenter;
    }

    void attach(ViewGroup parent, Options current) {
        int currentTab = current.bottomTabsOptions.currentTabIndex.get(0);
        List<ViewController> otherTabs = filter(tabs, (t) -> tabs.indexOf(t) != currentTab);
        onAppeared = () -> forEach(otherTabs, (t) -> attachInternal(parent, current, t));
        tabs.get(currentTab).addOnAppearedListener(onAppeared);
        attachInternal(parent, current, tabs.get(currentTab));
    }

    private void attachInternal(ViewGroup parent, Options current, ViewController tab) {
        ViewGroup view = tab.getView();
        view.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        int index = tabs.indexOf(tab);
        presenter.applyLayoutParamsOptions(current, index);
        view.setVisibility(index == 0 ? View.VISIBLE : View.INVISIBLE);
        parent.addView(view);
    }

    public void destroy() {
        tabs.get(0).removeOnAppearedListener(onAppeared);
        onAppeared = null;
    }
}

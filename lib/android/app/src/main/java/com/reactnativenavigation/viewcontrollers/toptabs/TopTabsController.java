package com.reactnativenavigation.viewcontrollers.toptabs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.TopTabsLayout;

import java.util.Collection;
import java.util.List;

public class TopTabsController extends ParentController implements NavigationOptionsListener {

    private List<TopTabController> tabs;
    private TopTabsLayout topTabsLayout;

    public TopTabsController(Activity activity, String id, List<TopTabController> tabs) {
        super(activity, id);
        this.tabs = tabs;
        for (ViewController tab : tabs) {
            tab.setParentController(this);
        }
    }

    @NonNull
    @Override
    protected ViewGroup createView() {
        topTabsLayout = new TopTabsLayout(getActivity(), tabs);
        return topTabsLayout;
    }

    @NonNull
    @Override
    public Collection<? extends ViewController> getChildControllers() {
        return tabs;
    }

    @Override
    public void onViewAppeared() {
        topTabsLayout.performOnCurrentTab(TopTabController::onViewAppeared);
    }

    @Override
    public void onViewDisappear() {
        topTabsLayout.performOnCurrentTab(TopTabController::onViewDisappear);
    }

    @Override
    public void applyOptions(NavigationOptions options) {
        topTabsLayout.applyOptions(options);
    }

    @Override
    public void mergeNavigationOptions(NavigationOptions options) {

    }

    public void switchToTab(int index) {
        topTabsLayout.switchToTab(index);
    }
}

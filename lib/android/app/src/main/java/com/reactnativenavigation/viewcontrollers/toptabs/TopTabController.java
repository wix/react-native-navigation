package com.reactnativenavigation.viewcontrollers.toptabs;

import android.app.Activity;
import android.view.View;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.viewcontrollers.ComponentViewController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.TopTab;

public class TopTabController extends ViewController implements NavigationOptionsListener {

    private final String componentName;
    private ComponentViewController.ReactViewCreator viewCreator;
    private final Options options;
    private TopTab topTab;
    private boolean isSelectedTab;

    public TopTabController(Activity activity, String id, String name, ComponentViewController.ReactViewCreator viewCreator, Options initialOptions) {
        super(activity, id);
        this.componentName = name;
        this.viewCreator = viewCreator;
        this.options = initialOptions;
    }

    @Override
    public void onViewAppeared() {
        super.onViewAppeared();
        isSelectedTab = true;
        applyOptions(options);
        topTab.sendComponentStart();
    }

    @Override
    public void applyOptions(Options options) {
        getParentController().applyOptions(options);
    }

    @Override
    public void onViewDisappear() {
        super.onViewDisappear();
        isSelectedTab = false;
        topTab.sendComponentStop();
    }

    @Override
    protected boolean isViewShown() {
        return super.isViewShown() && isSelectedTab;
    }

    @Override
    public View createView() {
        topTab = new TopTab(
                getActivity(),
                viewCreator.create(getActivity(), getId(), componentName)
        );
        return topTab;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (topTab != null) topTab.destroy();
        topTab = null;
    }

    @Override
    public void mergeOptions(Options options) {
        this.options.mergeWith(options);
        applyOptions(this.options);
    }

    String getTabTitle() {
        return options.topTabOptions.title;
    }

    public void setTabIndex(int i) {
        options.topTabOptions.tabIndex = i;
    }
}

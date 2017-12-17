package com.reactnativenavigation.viewcontrollers.toptabs;

import android.app.Activity;
import android.view.View;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.react.ReactContainerViewCreator;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.TopTab;

public class TopTabController extends ViewController implements NavigationOptionsListener {

    private final String containerName;
    private final ReactInstanceManager instanceManager;
    private final NavigationOptions options;
    private TopTab topTab;
    private boolean isSelectedTab;

    public TopTabController(Activity activity, String id, String name, ReactInstanceManager instanceManager, NavigationOptions initialOptions) {
        super(activity, id);
        this.containerName = name;
        this.instanceManager = instanceManager;
        this.options = initialOptions;
    }

    @Override
    public void onViewAppeared() {
        super.onViewAppeared();
        isSelectedTab = true;
        getParentController().applyOptions(options);
    }

    @Override
    public void onViewDisappear() {
        super.onViewDisappear();
        isSelectedTab = false;
    }

    @Override
    protected boolean isViewShown() {
        return super.isViewShown() && isSelectedTab;
    }

    @Override
    protected View createView() {
        topTab = new TopTab(
                getActivity(),
                new ReactContainerViewCreator(instanceManager).create(getActivity(), getId(), containerName)
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
    public void mergeNavigationOptions(NavigationOptions options) {
        this.options.mergeWith(options);
    }
}

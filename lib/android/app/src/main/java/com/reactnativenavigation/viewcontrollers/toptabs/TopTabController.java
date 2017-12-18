package com.reactnativenavigation.viewcontrollers.toptabs;

import android.app.Activity;
import android.view.View;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.TopTab;

public class TopTabController extends ViewController implements NavigationOptionsListener {

    private final String containerName;
    private ContainerViewController.ReactViewCreator viewCreator;
    private final NavigationOptions options;
    private TopTab topTab;
    private boolean isSelectedTab;

    public TopTabController(Activity activity, String id, String name, ContainerViewController.ReactViewCreator viewCreator, NavigationOptions initialOptions) {
        super(activity, id);
        this.containerName = name;
        this.viewCreator = viewCreator;
        this.options = initialOptions;
    }

    @Override
    public void onViewAppeared() {
        super.onViewAppeared();
        isSelectedTab = true;
        applyOptions(options);
        topTab.sendContainerStart();
    }

    @Override
    public void applyOptions(NavigationOptions options) {
        getParentController().applyOptions(options);
    }

    @Override
    public void onViewDisappear() {
        super.onViewDisappear();
        isSelectedTab = false;
        topTab.sendContainerStop();
    }

    @Override
    protected boolean isViewShown() {
        return super.isViewShown() && isSelectedTab;
    }

    @Override
    public View createView() {
        topTab = new TopTab(
                getActivity(),
                viewCreator.create(getActivity(), getId(), containerName)
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

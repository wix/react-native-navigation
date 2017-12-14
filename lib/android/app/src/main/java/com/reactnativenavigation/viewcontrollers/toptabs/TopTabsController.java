package com.reactnativenavigation.viewcontrollers.toptabs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.TopTabsLayout;

import java.util.Collection;
import java.util.List;

public class TopTabsController extends ParentController {

    private List<ViewController> tabs;

    public TopTabsController(Activity activity, String id, List<ViewController> tabs) {
        super(activity, id);
        this.tabs = tabs;
    }

    @NonNull
    @Override
    protected ViewGroup createView() {
        return new TopTabsLayout(getActivity(), tabs);
    }

    @NonNull
    @Override
    public Collection<ViewController> getChildControllers() {
        return tabs;
    }

    @Override
    protected boolean isViewShown() {
        return super.isViewShown();
    }
}

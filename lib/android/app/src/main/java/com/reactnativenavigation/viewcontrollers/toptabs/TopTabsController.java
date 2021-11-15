package com.reactnativenavigation.viewcontrollers.toptabs;

import android.app.Activity;
import android.view.View;

import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.utils.Functions.Func1;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewVisibilityListenerAdapter;
import com.reactnativenavigation.views.toptabs.TopTabsLayoutCreator;
import com.reactnativenavigation.views.toptabs.TopTabsViewPager;

import java.util.Collection;
import java.util.List;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

public class TopTabsController extends ParentController<TopTabsViewPager> {

    private List<ViewController<?>> tabs;
    private TopTabsLayoutCreator viewCreator;

    public TopTabsController(Activity activity, ChildControllersRegistry childRegistry, String id, List<ViewController<?>> tabs, TopTabsLayoutCreator viewCreator, Options options, Presenter presenter) {
        super(activity, childRegistry, id, presenter, options);
        this.viewCreator = viewCreator;
        this.tabs = tabs;
        for (ViewController<?> tab : tabs) {
            tab.setParentController(this);
            tab.setViewVisibilityListener(new ViewVisibilityListenerAdapter() {
                @Override
                public boolean onViewAppeared(View view) {
                    return getView().isCurrentView(view);
                }
            });
        }
    }

    @Override
    public ViewController<?> getCurrentChild() {
        return tabs.get(getView().getCurrentItem());
    }

    @NonNull
    @Override
    public TopTabsViewPager createView() {
        view = viewCreator.create();
        return view;
    }

    @NonNull
    @Override
    public Collection<? extends ViewController<?>> getChildControllers() {
        return tabs;
    }

    @Override
    public void onViewWillAppear() {
        super.onViewWillAppear();
        performOnParentController(parentController -> parentController.setupTopTabsWithViewPager(getView()));
        performOnCurrentTab(ViewController::onViewWillAppear);
    }

    @Override
    public void onViewWillDisappear() {
        super.onViewWillDisappear();
    }

    @Override
    public void onViewDisappear() {
        super.onViewDisappear();
        performOnCurrentTab(ViewController::onViewDisappear);
        performOnParentController(ParentController::clearTopTabs);
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        performOnCurrentTab(tab -> tab.sendOnNavigationButtonPressed(buttonId));
    }

    @Override
    public void applyOptions(Options options) {
        super.applyOptions(options);
        getView().applyOptions(options);
    }

    @Override
    public void applyChildOptions(Options options, ViewController<?> child) {
        super.applyChildOptions(options, child);
        performOnParentController(parentController -> parentController.applyChildOptions(this.options.copy(), child));
    }

    @CallSuper
    public void mergeChildOptions(Options options, ViewController<?> child) {
        super.mergeChildOptions(options, child);
        performOnParentController(parentController -> parentController.applyChildOptions(options.copy(), child));
    }

    public void switchToTab(int index) {
        getView().switchToTab(index);
        getCurrentChild().onViewDidAppear();
    }

    private void performOnCurrentTab(Func1<ViewController<?>> task) {
        task.run(tabs.get(getView().getCurrentItem()));
    }
}

package com.reactnativenavigation.viewcontrollers.toptabs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.TopTabCreator;

public class TopTabController extends ViewController {
    private String name;
    private TopTabCreator topTabCreator;

    public TopTabController(Activity activity, String id, String name, TopTabCreator topTabCreator) {
        super(activity, id);
        this.name = name;
        this.topTabCreator = topTabCreator;
    }

    @NonNull
    @Override
    protected ViewGroup createView() {
        return (ViewGroup) topTabCreator.create(getActivity(), getId(), name);
    }
}

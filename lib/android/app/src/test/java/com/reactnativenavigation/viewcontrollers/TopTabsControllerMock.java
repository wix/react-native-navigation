package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import java.util.Collection;

public class TopTabsControllerMock extends ParentController {
    public TopTabsControllerMock(Activity activity, String id) {
        super(activity, id);
    }

    @NonNull
    @Override
    protected ViewGroup createView() {
        return null;
    }

    @NonNull
    @Override
    public Collection<? extends ViewController> getChildControllers() {
        return null;
    }
}

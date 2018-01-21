package com.reactnativenavigation.mocks;

import android.app.Activity;
import android.widget.FrameLayout;

import com.reactnativenavigation.viewcontrollers.ViewController;

public class SimpleViewController extends ViewController<FrameLayout> {

    public SimpleViewController(final Activity activity, String id) {
        super(activity, id);
    }

    @Override
    protected FrameLayout createView() {
        return new FrameLayout(getActivity());
    }

    @Override
    public String toString() {
        return "SimpleViewController " + getId();
    }
}

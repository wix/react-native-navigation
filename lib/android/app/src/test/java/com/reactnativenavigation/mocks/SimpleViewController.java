package com.reactnativenavigation.mocks;

import android.app.*;
import android.view.*;

import com.reactnativenavigation.viewcontrollers.*;

public class SimpleViewController extends ViewController {

    public SimpleViewController(final Activity activity, String id) {
        super(activity, id);
    }

    @Override
    protected View createView() {
        return new View(getActivity());
    }

    @Override
    public String toString() {
        return "SimpleViewController " + getId();
    }
}

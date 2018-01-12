package com.reactnativenavigation.viewcontrollers;

import android.app.*;
import android.support.annotation.*;
import android.view.*;

import java.util.*;

public class TopTabsControllerMock extends ParentController {
    TopTabsControllerMock(Activity activity, String id) {
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

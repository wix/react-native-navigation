package com.reactnativenavigation.mocks;

import android.app.*;

import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.viewcontrollers.*;

public class SimpleComponentViewController extends ComponentViewController {
    public SimpleComponentViewController(final Activity activity, final String id) {
        super(activity, id, "theComponentName", new TestComponentViewCreator(), new Options());
    }
}

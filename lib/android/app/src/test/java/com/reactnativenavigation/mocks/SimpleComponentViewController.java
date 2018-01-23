package com.reactnativenavigation.mocks;

import android.app.*;

import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.viewcontrollers.*;

public class SimpleComponentViewController extends ComponentViewController {
    public SimpleComponentViewController(final Activity activity, final String id, Options initialOptions) {
        super(activity, id, "theComponentName", new TestComponentViewCreator(), initialOptions);
    }
}

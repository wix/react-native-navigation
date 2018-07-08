package com.reactnativenavigation.mocks;

import android.app.*;

import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.viewcontrollers.*;

public class SimpleComponentViewController extends ComponentViewController {
    public SimpleComponentViewController(Activity activity, ChildControllersRegistry childRegistry, String id, Options initialOptions) {
        super(activity, childRegistry,id, "theComponentName", new TestComponentViewCreator(), initialOptions, new OptionsPresenter(activity, new Options()));
    }
}

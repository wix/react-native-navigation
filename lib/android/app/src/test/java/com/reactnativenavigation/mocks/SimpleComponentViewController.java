package com.reactnativenavigation.mocks;

import android.app.*;

import com.reactnativenavigation.options.*;
import com.reactnativenavigation.viewcontrollers.component.ComponentPresenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.component.ComponentViewController;

public class SimpleComponentViewController extends ComponentViewController {
    public SimpleComponentViewController(Activity activity, ChildControllersRegistry childRegistry, String id, Options initialOptions) {
        super(activity, childRegistry,id, "theComponentName", new TestComponentViewCreator(), initialOptions, new Presenter(activity, new Options()), new ComponentPresenter(Options.EMPTY));
    }
}

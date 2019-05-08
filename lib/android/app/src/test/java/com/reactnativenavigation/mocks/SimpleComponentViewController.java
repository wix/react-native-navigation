package com.reactnativenavigation.mocks;

import android.app.*;

import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.presentation.ComponentPresenter;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.viewcontrollers.*;

public class SimpleComponentViewController extends ComponentViewController {
    public SimpleComponentViewController(Activity activity, ChildControllersRegistry childRegistry, String id, Options initialOptions) {
        super(activity, childRegistry,id, "theComponentName", new TestComponentViewCreator(), initialOptions, new Presenter(activity, new Options()), new ComponentPresenter(Options.EMPTY));
    }
}

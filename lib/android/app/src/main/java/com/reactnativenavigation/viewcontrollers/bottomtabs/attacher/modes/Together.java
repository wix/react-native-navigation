package com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.modes;

import android.view.*;

import com.reactnativenavigation.options.*;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import java.util.*;

import static com.reactnativenavigation.utils.CollectionUtils.*;

public class Together extends AttachMode {
    public Together(ViewGroup parent, List<ViewController<?>> tabs, BottomTabsPresenter presenter, Options resolved) {
        super(parent, tabs, presenter, resolved);
    }

    @Override
    public void attach() {
        forEach(tabs, this::attach);
    }
}
package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.view.*;

import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.presentation.*;
import com.reactnativenavigation.viewcontrollers.*;

import java.util.*;

import static com.reactnativenavigation.utils.CollectionUtils.*;

public class Together extends AttachMode {
    public Together(ViewGroup parent, List<ViewController> tabs, BottomTabsPresenter presenter, Options resolved) {
        super(parent, tabs, presenter, resolved);
    }

    @Override
    public void attach() {
        forEach(tabs, this::attach);
    }
}
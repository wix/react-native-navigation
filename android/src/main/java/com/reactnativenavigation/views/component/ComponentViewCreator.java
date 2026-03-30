package com.reactnativenavigation.views.component;

import android.content.Context;

import com.reactnativenavigation.react.ReactComponentViewCreator;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.viewcontrollers.viewcontroller.IReactView;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ReactViewCreator;

public class ComponentViewCreator implements ReactViewCreator {
	@Override
	public IReactView create(Context context, String componentId, String componentName) {
        ReactView reactView = new ReactComponentViewCreator().create(context, componentId, componentName);
        return new ComponentLayout(context, reactView);
	}
}

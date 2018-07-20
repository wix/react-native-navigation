package com.reactnativenavigation.react;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.reactnativenavigation.views.element.Element;

public class ElementViewManager extends ViewGroupManager<Element> {
    @Override
    public String getName() {
        return "RNNElement";
    }

    @Override
    protected Element createViewInstance(ThemedReactContext reactContext) {
        return new Element(reactContext);
    }

    @ReactProp(name = "elementId")
    public void setElementId(Element element, String key) {

    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }
}

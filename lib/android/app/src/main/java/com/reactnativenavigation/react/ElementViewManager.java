package com.reactnativenavigation.react;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.reactnativenavigation.views.element.Element;

import static com.reactnativenavigation.utils.UiUtils.runOnPreDrawOnce;
import static com.reactnativenavigation.utils.ViewUtils.performOnParentReactView;

public class ElementViewManager extends ViewGroupManager<Element> {

    @Override
    public String getName() {
        return "RNNElement";
    }

    @Override
    protected Element createViewInstance(ThemedReactContext reactContext) {
        Element element = createView(reactContext);
        register(element);
        return element;
    }

    public Element createView(ThemedReactContext reactContext) {
        return new Element(reactContext);
    }

    @Override
    public void onDropViewInstance(Element element) {
        super.onDropViewInstance(element);
        unregister(element);
    }

    @ReactProp(name = "elementId")
    public void setElementId(Element element, String id) {
        element.setElementId(id);
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

    private void register(Element element) {
        runOnPreDrawOnce(element, () -> performOnParentReactView(element, (parent) -> parent.registerElement(element)));
    }

    private void unregister(Element element) {
        performOnParentReactView(element, (parent) -> parent.unregisterElement(element));
    }
}

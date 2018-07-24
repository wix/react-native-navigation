package com.reactnativenavigation.react;

import android.util.Log;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.element.Element;

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
    public void onDropViewInstance(Element view) {
        super.onDropViewInstance(view);
        Log.d("ElementViewManager", "onDropViewInstance:" + (view.getParent() != null));
        unregister(view);
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
        UiUtils.runOnPreDrawOnce(element, () -> {
            Log.i("ElementViewManager", "createViewInstance:" + (element.getParent() != null));
            ViewUtils.performOnParentReactView(element, (parent) -> {
                parent.registerElement(element);
            });
        });
    }

    private void unregister(Element element) {
        ViewUtils.performOnParentReactView(element, (parent) -> {
            parent.unregisterElement(element);
        });
    }
}

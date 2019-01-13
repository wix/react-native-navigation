package com.reactnativenavigation.presentation;

import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.Collection;

import static com.reactnativenavigation.utils.CollectionUtils.reduce;

public class RenderChecker {
    public boolean areRendered(Collection<ViewController> components) {
        return reduce(components, true, ViewController::isRendered);
    }
}

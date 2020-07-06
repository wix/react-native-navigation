package com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.modes;

import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import org.junit.*;

public class TogetherTest extends AttachModeTest {

    @Override
    public void beforeEach() {
        super.beforeEach();
        uut = new Together(parent, tabs, presenter, options);
    }

    @Test
    public void attach_allTabsAreAttached() {
        uut.attach();
        assertIsChild(parent, tabs.toArray(new ViewController[0]));
    }
}

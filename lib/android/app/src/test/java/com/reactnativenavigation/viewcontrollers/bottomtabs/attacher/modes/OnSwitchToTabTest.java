package com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.modes;

import org.junit.*;

public class OnSwitchToTabTest extends AttachModeTest {

    @Override
    public void beforeEach() {
        super.beforeEach();
        uut = new OnSwitchToTab(parent, tabs, presenter, options);
    }

    @Test
    public void attach_onlyInitialTabIsAttached() {
        uut.attach();
        assertIsChild(parent, initialTab());
        assertNotChildOf(parent, otherTabs());
    }

    @Test
    public void onTabSelected_initialTabIsNotHandled() {
        uut.onTabSelected(initialTab());
        assertNotChildOf(parent, initialTab());
    }

    @Test
    public void onTabSelected_otherTabIsAttached() {
        uut.onTabSelected(tab1);
        assertIsChild(parent, tab1);
    }
}

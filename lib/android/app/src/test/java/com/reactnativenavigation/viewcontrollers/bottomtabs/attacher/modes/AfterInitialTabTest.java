package com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.modes;


import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class AfterInitialTabTest extends AttachModeTest {

    @Override
    public void beforeEach() {
        super.beforeEach();
        uut = new AfterInitialTab(parent, tabs, presenter, options);
    }

    @Test
    public void attach_initialTabIsAttached() {
        uut.attach();
        assertIsChild(parent, tab2);
    }

    @Test
    public void attach_otherTabsAreAttachedAfterInitialTab() {
        uut.attach();
        assertNotChildOf(parent, otherTabs());

        initialTab().onViewWillAppear();
        idleMainLooper();
        assertIsChild(parent, otherTabs());
    }

    @Test
    public void destroy() {
        uut.destroy();
        verify(initialTab()).removeOnAppearedListener(any());
    }
}

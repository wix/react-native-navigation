package com.reactnativenavigation.viewcontrollers.bottomtabs.attachmode;


import com.reactnativenavigation.viewcontrollers.bottomtabs.*;

import org.junit.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        assertIsChild(parent, otherTabs());
    }

    @Test
    public void destroy() {
        uut.destroy();
        verify(initialTab()).removeOnAppearedListener(any());
    }
}

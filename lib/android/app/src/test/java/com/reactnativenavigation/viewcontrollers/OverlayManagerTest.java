package com.reactnativenavigation.viewcontrollers;

import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleComponentViewController;
import com.reactnativenavigation.presentation.OverlayManager;

import org.junit.Test;
import org.mockito.Mockito;

public class OverlayManagerTest extends BaseTest {
    private ViewGroup mockRoot;
    private OverlayManager uut;
    private ViewController overlayController;

    @Override
    public void beforeEach() {
        super.beforeEach();
        uut = new OverlayManager();
        mockRoot = Mockito.mock(ViewGroup.class);
        overlayController = new SimpleComponentViewController(newActivity(), "overlayId");

    }

    @Test
    public void overlayIsAddedToRoot() throws Exception {
        uut.show(mockRoot, overlayController);
        Mockito.verify(mockRoot, Mockito.times(1)).addView(overlayController.getView());
    }
}

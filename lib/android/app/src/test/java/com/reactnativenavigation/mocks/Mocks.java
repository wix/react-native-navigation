package com.reactnativenavigation.mocks;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ViewController;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Mocks {
    public static ViewController viewController() {
        ViewController mock = mock(ViewController.class);
        when(mock.resolveCurrentOptions()).thenReturn(Options.EMPTY);
        return mock;
    }
}

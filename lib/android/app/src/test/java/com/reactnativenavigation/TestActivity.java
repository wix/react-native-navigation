package com.reactnativenavigation;

import com.reactnativenavigation.react.ReactGateway;
import com.reactnativenavigation.viewcontrollers.Navigator;

import org.mockito.Mockito;

public class TestActivity extends NavigationActivity {

    @Override
    public ReactGateway getReactGateway() {
        return Mockito.mock(ReactGateway.class);
    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }
}

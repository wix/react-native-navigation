package com.reactnativenavigation.views;

import com.reactnativenavigation.viewcontrollers.ContainerViewController;

public interface Container extends ContainerViewController.IReactView {
    TopBar getTopBar();
}

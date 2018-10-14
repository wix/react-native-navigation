package com.reactnativenavigation.viewcontrollers.sidemenu;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;

public class SideMenuTestHelper {
    static final Options LEFT_OPEN = new Options();
    static final Options RIGHT_OPEN = new Options();

    static {
        LEFT_OPEN.sideMenuRootOptions.left.visible = new Bool(true);
        RIGHT_OPEN.sideMenuRootOptions.right.visible = new Bool(true);
    }
}

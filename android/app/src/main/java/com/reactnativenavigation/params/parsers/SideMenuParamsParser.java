package com.reactnativenavigation.params.parsers;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.params.SideMenuParams;
import com.reactnativenavigation.views.SideMenu.Side;

class SideMenuParamsParser extends Parser {
    public static SideMenuParams[] parse(Bundle sideMenues) {
        SideMenuParams[] result = new SideMenuParams[2];
        result[Side.Left.ordinal()] = parseSideMenu(sideMenues.getBundle("left"), Side.Left);
        result[Side.Right.ordinal()] = parseSideMenu(sideMenues.getBundle("right"), Side.Right);
        return result;
    }

    private static SideMenuParams parseSideMenu(@Nullable Bundle sideMenu, Side side) {
        if (sideMenu == null || sideMenu.isEmpty()) {
            return null;
        }
        SideMenuParams result = new SideMenuParams();
        result.screenId = sideMenu.getString("screenId");
        result.navigationParams = new NavigationParams(sideMenu.getBundle("navigationParams"));
        result.disableOpenGesture = sideMenu.getBoolean("disableOpenGesture", false);
        result.side = side;
        return result;
    }
}

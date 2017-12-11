package com.reactnativenavigation.params.parsers;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.params.SideMenuParams;
import com.reactnativenavigation.params.SideMenuStyle;
import com.reactnativenavigation.views.SideMenu;
import com.reactnativenavigation.views.SideMenu.Side;

class SideMenuParamsParser extends Parser {
    public static SideMenuParams[] parse(Bundle sideMenues, @Nullable Bundle style) {
        SideMenuParams[] result = new SideMenuParams[2];
        result[Side.Left.ordinal()] = parseSideMenu(sideMenues.getBundle("left"), Side.Left, style);
        result[Side.Right.ordinal()] = parseSideMenu(sideMenues.getBundle("right"), Side.Right, style);
        return result;
    }

    private static SideMenuParams parseSideMenu(@Nullable Bundle sideMenu, Side side, Bundle style) {
        if (sideMenu == null || sideMenu.isEmpty()) {
            return null;
        }
        SideMenuParams result = new SideMenuParams();
        result.screenId = sideMenu.getString("screenId");
        result.navigationParams = new NavigationParams(sideMenu.getBundle("navigationParams"));
        result.disableOpenGesture = sideMenu.getBoolean("disableOpenGesture", false);
        result.side = side;
        result.style = parseStyle(style, side);
        return result;
    }

    private static SideMenuStyle parseStyle(@Nullable Bundle style, Side side) {
        if (style == null || style.isEmpty()) {
            return null;
        }

        SideMenuStyle result = new SideMenuStyle();

        if (side.equals(Side.Left) && hasKey(style, "leftDrawerWidth")) {
            result.weight = style.getInt("leftDrawerWidth");
        }

        if (side.equals(Side.Right) && hasKey(style, "rightDrawerWidth")) {
            result.weight = style.getInt("rightDrawerWidth");
        }

        return result;
    }
}

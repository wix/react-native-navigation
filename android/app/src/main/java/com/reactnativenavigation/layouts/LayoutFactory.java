package com.reactnativenavigation.layouts;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.reactnativenavigation.params.ActivityParams;

public class LayoutFactory {
    public static Layout create(AppCompatActivity activity, ActivityParams params) {
        switch (params.type) {
            case TabBased:
                return createBottomTabsScreenLayout(activity, params);
            case SingleScreen:
            default:
                return createSingleScreenLayout(activity, params);
        }
    }

    private static Layout createSingleScreenLayout(AppCompatActivity activity, ActivityParams params) {
        return new SingleScreenLayout(activity, params.leftSideMenuParams, params.rightSideMenuParams, params.screenParams);
    }

    private static Layout createBottomTabsScreenLayout(AppCompatActivity activity, ActivityParams params) {
        if (params.tabParams.size() > 5) {
            removeAllButTheFirst5Tabs(params);
        }
        return new BottomTabsLayout(activity, params);
    }

    private static void removeAllButTheFirst5Tabs(ActivityParams params) {
        Log.e("Navigation", "LayoutFactory:createBottomTabsScreenLayout() does not support more than 5 tabs, currently");
        while (params.tabParams.size() > 5) {
            params.tabParams.remove(params.tabParams.size() - 1);
        }
    }
}

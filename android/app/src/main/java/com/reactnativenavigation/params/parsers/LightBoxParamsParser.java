package com.reactnativenavigation.params.parsers;

import android.os.Bundle;
import com.reactnativenavigation.params.LightBoxParams;
import com.reactnativenavigation.params.NavigationParams;

public class LightBoxParamsParser extends Parser {
    private Bundle params;

    public LightBoxParamsParser(Bundle params) {
        this.params = params;
    }

    public LightBoxParams parse() {
        LightBoxParams result = new LightBoxParams();
        if (params.isEmpty()) {
            return result;
        }
        result.screenId = params.getString("screenId");
        result.navigationParams = new NavigationParams(params.getBundle("navigationParams"));
        result.backgroundColor = getColor(params, "backgroundColor");
        result.tapBackgroundToDismiss = params.getBoolean("tapBackgroundToDismiss");
        result.overrideBackPress = params.getBoolean("overrideBackPress");
        return result;
    }
}

package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

import com.reactnativenavigation.params.FabActionParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.react.ImageLoader;
import com.reactnativenavigation.utils.ViewUtils;

public class FabActionParamsParser extends Parser {
    public FabActionParams parse(Bundle params, String navigatorEventId) {
        FabActionParams fabActionParams = new FabActionParams();
        fabActionParams.id = params.getString("id");
        fabActionParams.navigatorEventId = navigatorEventId;
        fabActionParams.icon = ImageLoader.loadImage(params.getString("icon"));
        fabActionParams.backgroundColor = StyleParams.Color.parse(params, "backgroundColor");
        fabActionParams.iconColor = StyleParams.Color.parse(params, "iconColor");
        if (fabActionParams.iconColor.hasColor()) {
            ViewUtils.tintDrawable(fabActionParams.icon, fabActionParams.iconColor.getColor(), true);
        }
        return fabActionParams;
    }
}

package com.reactnativenavigation.params.parsers;

import android.graphics.Color;
import android.os.Bundle;

import com.reactnativenavigation.params.FabActionParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.react.ImageLoader;

public class FabActionParamsParser extends Parser {
    public FabActionParams parse(Bundle params, String navigatorEventId) {
        FabActionParams fabActionParams = new FabActionParams();
        fabActionParams.id = params.getString("id");
        fabActionParams.navigatorEventId = navigatorEventId;
        fabActionParams.icon = ImageLoader.loadImage(params.getString("icon"));
        fabActionParams.backgroundColor = getColor(params, "backgroundColor", new StyleParams.Color());
        fabActionParams.title = params.getString("title");
        fabActionParams.titleBackgroundColor = getColor(params, "titleBackgroundColor",
                new StyleParams.Color(Color.parseColor("#CC000000")));
        fabActionParams.titleColor = getColor(params, "titleColor", new StyleParams.Color(Color.WHITE));

        return fabActionParams;
    }
}

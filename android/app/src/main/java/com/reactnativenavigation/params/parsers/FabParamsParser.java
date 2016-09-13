package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

import com.reactnativenavigation.params.FabActionParams;
import com.reactnativenavigation.params.FabParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.react.ImageLoader;

public class FabParamsParser extends Parser {
    public FabParams parse(Bundle params, final String navigatorEventId) {
        FabParams fabParams = new FabParams();
        fabParams.collapsedId = params.getString("collapsedId");
        fabParams.expendedId = params.getString("expendedId");
        fabParams.navigatorEventId = navigatorEventId;
        fabParams.backgroundColor = getColor(params, "backgroundColor", new StyleParams.Color());

        if (hasKey(params, "collapsedIcon")) {
            fabParams.collapsedIcon = ImageLoader.loadImage(params.getString("collapsedIcon"));
        }
        if (hasKey(params, "expendedIcon")) {
            fabParams.expendedIcon = ImageLoader.loadImage(params.getString("expendedIcon"));
        }
        if (hasKey(params, "actions")) {
            fabParams.actions = parseBundle(params.getBundle("actions"), new ParseStrategy<FabActionParams>() {
                @Override
                public FabActionParams parse(Bundle params) {
                    return new FabActionParamsParser().parse(params, navigatorEventId);
                }
            });
        }
        return fabParams;
    }
}

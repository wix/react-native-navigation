package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

import com.reactnativenavigation.params.ContextualMenuButtonParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.react.ImageLoader;

import java.util.List;

public class ContextualMenuButtonParamsParser extends TitleBarButtonParamsParser {
    public List<ContextualMenuButtonParams> parseContextualMenuButtons(Bundle params) {
        return parseBundle(params, new ParseStrategy<ContextualMenuButtonParams>() {
            @Override
            public ContextualMenuButtonParams parse(Bundle button) {
                return parseSingleContextualMenuButton(button);
            }
        });
    }

    private ContextualMenuButtonParams parseSingleContextualMenuButton(Bundle button) {
        ContextualMenuButtonParams result = new ContextualMenuButtonParams();
        if (button.get("icon") != null) {
            result.icon = ImageLoader.loadImage(button.getString("icon"));
        }
        result.showAsAction = parseShowAsAction(button.getString("showAsAction"));
        result.color = StyleParams.Color.parse(button, "color");
        result.label = button.getString("label");
        result.index = button.getInt("index");
        return result;
    }
}

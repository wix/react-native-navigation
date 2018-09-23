package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.ColorParser;
import com.reactnativenavigation.parse.parsers.TextParser;
import com.reactnativenavigation.react.Constants;

import org.json.JSONObject;
// TODO implement test
public class OverflowButton extends Button {
    public static OverflowButton parse(JSONObject json) {
        OverflowButton result = new OverflowButton();
        if (json == null || json.toString().equals("{}")) return result;

        result.hasValue = true;
        if (json.has("icon")) result.icon = TextParser.parse(json.optJSONObject("icon"), "uri");
        result.id = json.optString("id", Constants.OVERFLOW_BUTTON_ID);
        result.color = ColorParser.parse(json, "color");
        result.testId = TextParser.parse(json, "testID");

        return result;
    }

    OverflowButton() {
        id = Constants.OVERFLOW_BUTTON_ID;
    }

    private boolean hasValue;

    public boolean hasValue() {
        return hasValue;
    }

    public void mergeWith(OverflowButton other) {
        if (other.icon.hasValue()) icon = other.icon;
        if (other.color.hasValue()) color = other.color;
        if (other.testId.hasValue()) testId = other.testId;
    }

    void mergeWithDefault(final OverflowButton defaultOptions) {
        if (!icon.hasValue()) icon = defaultOptions.icon;
        if (!color.hasValue()) color = defaultOptions.color;
        if (!testId.hasValue()) testId = defaultOptions.testId;
    }
}

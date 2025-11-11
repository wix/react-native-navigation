package com.reactnativenavigation.options;

import android.content.Context;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.options.parsers.BoolParser;
import com.reactnativenavigation.options.parsers.TextParser;
import com.reactnativenavigation.react.Constants;

import org.json.JSONObject;

public class BackButton extends ButtonOptions {
    public static final String DEFAULT_ACCESSIBILITY_LABEL = "Navigate Up";
    public static BackButton parse(Context context, JSONObject json) {
        BackButton result = new BackButton();
        if (json == null || json.toString().equals("{}")) return result;

        result.hasValue = true;
        result.visible = BoolParser.parse(json, "visible");
        result.accessibilityLabel = TextParser.parse(json, "accessibilityLabel", BackButton.DEFAULT_ACCESSIBILITY_LABEL);
        if (json.has("icon")) result.icon = TextParser.parse(json.optJSONObject("icon"), "uri");
        result.id = json.optString("id", Constants.BACK_BUTTON_ID);
        result.enabled = BoolParser.parse(json, "enabled");
        result.disableIconTint = BoolParser.parse(json, "disableIconTint");
        result.color = ThemeColour.parse(context, json.optJSONObject( "color"));
        result.disabledColor = ThemeColour.parse(context, json.optJSONObject( "disabledColor"));
        result.testId = TextParser.parse(json, "testID");
        result.popStackOnPress = BoolParser.parse(json, "popStackOnPress");

        return result;
    }

    public BackButton() {
        id = Constants.BACK_BUTTON_ID;
        accessibilityLabel = new Text(BackButton.DEFAULT_ACCESSIBILITY_LABEL);
    }

    public Bool visible = new NullBool();
    private boolean hasValue;

    public boolean hasValue() {
        return hasValue;
    }

    public void mergeWith(BackButton other) {
        if (!Constants.BACK_BUTTON_ID.equals(other.id)) id = other.id;
        if (other.accessibilityLabel.hasValue() && !other.accessibilityLabel.equals(new Text(BackButton.DEFAULT_ACCESSIBILITY_LABEL))) accessibilityLabel = other.accessibilityLabel;
        if (other.icon.hasValue()) icon = other.icon;
        if (other.visible.hasValue()) visible = other.visible;
        if (other.color.hasValue()) color = other.color;
        if (other.disabledColor.hasValue()) disabledColor = other.disabledColor;
        if (other.disableIconTint.hasValue()) disableIconTint = other.disableIconTint;
        if (other.enabled.hasValue()) enabled = other.enabled;
        if (other.testId.hasValue()) testId = other.testId;
        if (other.popStackOnPress.hasValue()) popStackOnPress = other.popStackOnPress;
    }

    void mergeWithDefault(final BackButton defaultOptions) {
        if (Constants.BACK_BUTTON_ID.equals(id)) id = defaultOptions.id;
        if (!(accessibilityLabel.hasValue() && !accessibilityLabel.equals(new Text(BackButton.DEFAULT_ACCESSIBILITY_LABEL)))) accessibilityLabel = defaultOptions.accessibilityLabel;
        if (!icon.hasValue()) icon = defaultOptions.icon;
        if (!visible.hasValue()) visible = defaultOptions.visible;
        if (!color.hasValue()) color = defaultOptions.color;
        if (!disabledColor.hasValue()) disabledColor = defaultOptions.disabledColor;
        if (!disableIconTint.hasValue()) disableIconTint = defaultOptions.disableIconTint;
        if (!enabled.hasValue()) enabled = defaultOptions.enabled;
        if (!testId.hasValue()) testId = defaultOptions.testId;
        if (!popStackOnPress.hasValue()) popStackOnPress = defaultOptions.popStackOnPress;
    }

    public void setVisible() {
        visible = new Bool(true);
        hasValue = true;
    }

    public void setHidden() {
        visible = new Bool(false);
        hasValue = true;
    }

    @Override
    public boolean isBackButton() {
        return true;
    }
}

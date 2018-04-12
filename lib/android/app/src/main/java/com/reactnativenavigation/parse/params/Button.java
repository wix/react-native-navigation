package com.reactnativenavigation.parse.params;

import android.view.MenuItem;

import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.ColorParser;
import com.reactnativenavigation.parse.parsers.NumberParser;
import com.reactnativenavigation.parse.parsers.TextParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Button {
    public String id;
    public Text title = new NullText();
    public Bool enabled = new NullBool();
    public Bool disableIconTint = new NullBool();
    public int showAsAction;
    public Color buttonColor = new NullColor();
    public Number buttonFontSize = new NullNumber();
    private Text buttonFontWeight = new NullText();
    public Text icon = new NullText();
    public Text testId = new NullText();
    public Component component = new Component();

    private static Button parseJson(JSONObject json) {
        Button button = new Button();
        button.id = json.optString("id");
        button.title = TextParser.parse(json, "title");
        button.enabled = BoolParser.parse(json, "enabled");
        button.disableIconTint = BoolParser.parse(json, "disableIconTint");
        button.showAsAction = parseShowAsAction(json);
        button.buttonColor = ColorParser.parse(json, "buttonColor");
        button.buttonFontSize = NumberParser.parse(json, "buttonFontSize");
        button.buttonFontWeight = TextParser.parse(json, "buttonFontWeight");
        button.testId = TextParser.parse(json, "testID");
        button.component = Component.parse(json.optJSONObject("component"));

        if (json.has("icon")) {
            button.icon = TextParser.parse(json.optJSONObject("icon"), "uri");
        }

        return button;
    }

    public static ArrayList<Button> parseJsonArray(JSONArray jsonArray) {
        ArrayList<Button> buttons = new ArrayList<>();

        if (jsonArray == null) {
            return null;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.optJSONObject(i);
            Button button = Button.parseJson(json);
            buttons.add(button);
        }

        return buttons;
    }

    public boolean hasComponent() {
        return component.hasValue();
    }

    public boolean hasIcon() {
        return icon.hasValue();
    }

    private static int parseShowAsAction(JSONObject json) {
        final Text showAsAction = TextParser.parse(json, "showAsAction");
        if (!showAsAction.hasValue()) {
            return MenuItem.SHOW_AS_ACTION_IF_ROOM;
        }

        switch (showAsAction.get()) {
            case "always":
                return MenuItem.SHOW_AS_ACTION_ALWAYS;
            case "never":
                return MenuItem.SHOW_AS_ACTION_NEVER;
            case "withText":
                return MenuItem.SHOW_AS_ACTION_WITH_TEXT;
            case "ifRoom":
            default:
                return MenuItem.SHOW_AS_ACTION_IF_ROOM;
        }
    }
}

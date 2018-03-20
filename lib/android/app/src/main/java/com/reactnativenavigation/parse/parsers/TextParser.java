package com.reactnativenavigation.parse.parsers;

import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Text;

import org.json.JSONObject;

public class TextParser {
    public static Text parse(JSONObject json, String text) {
        return json.has(text) ? new Text(json.optString(text)) : new NullText();
    }
}

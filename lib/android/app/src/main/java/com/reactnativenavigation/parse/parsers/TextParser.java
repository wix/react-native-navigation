package com.reactnativenavigation.parse.parsers;

import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Text;

import org.json.JSONObject;

import javax.annotation.*;

public class TextParser {
    public static Text parse(@Nullable JSONObject json, String text) {
        return json != null && json.has(text) ? new Text(json.optString(text)) : new NullText();
    }
}

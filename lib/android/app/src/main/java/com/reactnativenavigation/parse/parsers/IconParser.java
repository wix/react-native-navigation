package com.reactnativenavigation.parse.parsers;

import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Text;

import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;

public class IconParser {
    public static Text parse(@Nullable JSONObject json, String key) {
        if (json == null || !json.has(key)) return new NullText();
        try {
            return json.get(key) instanceof String ? TextParser.parse(json, key) : TextParser.parse(json.optJSONObject(key), "uri");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new NullText();
    }
}

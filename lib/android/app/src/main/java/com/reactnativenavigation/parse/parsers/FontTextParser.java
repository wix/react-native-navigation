package com.reactnativenavigation.parse.parsers;

import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Text;

import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;

public class FontTextParser {
    public static Text parse(@Nullable JSONObject json, String text) {
        if (json.has(text)) {
            try {
                Object val = json.get(text);
                if (val instanceof Number) {
                    String unicode = "" + new Character((char)((Number) val).longValue());
                    return new Text(unicode);
                } else {
                    return TextParser.parse(json, text);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new NullText();
    }
}

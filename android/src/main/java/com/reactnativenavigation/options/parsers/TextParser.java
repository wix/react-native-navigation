package com.reactnativenavigation.options.parsers;

import com.reactnativenavigation.options.params.NullText;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.utils.ObjectUtils;

import org.json.JSONObject;

import javax.annotation.*;

public class TextParser {
    public static Text parse(@Nullable JSONObject json, String text, String defaultValue) {
        return ObjectUtils.take(parse(json, text), new Text(defaultValue));
    }

    public static Text parse(@Nullable JSONObject json, String text) {
        return json != null && json.has(text) ? new Text(json.optString(text)) : new NullText();
    }
}

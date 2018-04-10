package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.parsers.TextParser;

import org.json.JSONObject;

public class Component {
    public static Component parse(JSONObject json) {
        Component result = new Component();
        if (json == null) return result;

        result.name = TextParser.parse(json, "name");
        result.componentId = TextParser.parse(json, "componentId");
        result.alignment = Alignment.fromString(TextParser.parse(json, "alignment").get(""));

        return result;
    }

    public Text name = new NullText();
    public Text componentId = new NullText();
    public Alignment alignment = Alignment.Default;

    void mergeWith(Component other) {
        if (other.componentId.hasValue()) componentId = other.componentId;
        if (other.name.hasValue()) name = other.name;
        if (other.alignment != Alignment.Default) alignment = other.alignment;
    }

    public void mergeWithDefault(Component defaultOptions) {
        if (!componentId.hasValue()) componentId = defaultOptions.componentId;
        if (!name.hasValue()) name = defaultOptions.name;
        if (alignment == Alignment.Default) alignment = defaultOptions.alignment;
    }

    public boolean hasValue() {
        return name.hasValue();
    }
}

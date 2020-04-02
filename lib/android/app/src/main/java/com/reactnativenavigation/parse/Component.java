package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.NumberParser;
import com.reactnativenavigation.parse.parsers.TextParser;

import org.json.JSONObject;

public class Component {
    public static Component parse(JSONObject json) {
        Component result = new Component();
        if (json == null) return result;

        result.name = TextParser.parse(json, "name");
        result.componentId = TextParser.parse(json, "componentId");
        result.alignment = Alignment.fromString(TextParser.parse(json, "alignment").get(""));
        result.waitForRender = BoolParser.parse(json, "waitForRender");
        result.width = NumberParser.parse(json, "width");
        result.height = NumberParser.parse(json, "height");

        return result;
    }

    public Text name = new NullText();
    public Text componentId = new NullText();
    public Alignment alignment = Alignment.Default;
    public Bool waitForRender = new NullBool();
    public Number width = new NullNumber();
    public Number height = new NullNumber();

    void mergeWith(Component other) {
        if (other.componentId.hasValue()) componentId = other.componentId;
        if (other.name.hasValue()) name = other.name;
        if (other.waitForRender.hasValue()) waitForRender = other.waitForRender;
        if (other.alignment != Alignment.Default) alignment = other.alignment;
        if (other.width.hasValue()) width = other.width;
        if (other.height.hasValue()) height = other.height;
    }

    public void mergeWithDefault(Component defaultOptions) {
        if (!componentId.hasValue()) componentId = defaultOptions.componentId;
        if (!name.hasValue()) name = defaultOptions.name;
        if (!waitForRender.hasValue()) waitForRender = defaultOptions.waitForRender;
        if (alignment == Alignment.Default) alignment = defaultOptions.alignment;
        if (!width.hasValue()) width = defaultOptions.width;
        if (!height.hasValue()) height = defaultOptions.height;
    }

    public boolean hasValue() {
        return name.hasValue();
    }

    public boolean equals(Component other) {
        return name.equals(other.name) &&
               componentId.equals(other.componentId) &&
               alignment.equals(other.alignment) &&
               waitForRender.equals(other.waitForRender) &&
               width.equals(other.width) &&
               height.equals(other.height);
    }
}

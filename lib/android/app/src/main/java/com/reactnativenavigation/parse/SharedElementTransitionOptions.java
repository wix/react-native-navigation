package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.parsers.NumberParser;
import com.reactnativenavigation.parse.parsers.TextParser;

import org.json.JSONObject;

import androidx.annotation.Nullable;

public class SharedElementTransitionOptions {
    public Text fromId = new NullText();
    public Text toId = new NullText();
    public Number duration = new NullNumber();

    public static SharedElementTransitionOptions parse(@Nullable JSONObject json) {
        SharedElementTransitionOptions transition = new SharedElementTransitionOptions();
        if (json == null) return transition;

        transition.fromId = TextParser.parse(json, "fromId");
        transition.toId = TextParser.parse(json, "toId");
        transition.duration = NumberParser.parse(json, "duration");

        return transition;
    }

    void mergeWith(SharedElementTransitionOptions other) {
        if (other.fromId.hasValue()) fromId = other.fromId;
        if (other.toId.hasValue()) toId = other.toId;
        if (other.duration.hasValue()) duration = other.duration;
    }

    void mergeWithDefault(SharedElementTransitionOptions defaultOptions) {
        if (!fromId.hasValue()) fromId = defaultOptions.fromId;
        if (!toId.hasValue()) toId = defaultOptions.toId;
        if (!duration.hasValue()) duration = defaultOptions.duration;
    }

    public long getDuration() {
        return duration.get(0).longValue();
    }
}

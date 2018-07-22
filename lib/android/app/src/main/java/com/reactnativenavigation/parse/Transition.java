package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.parsers.FractionParser;
import com.reactnativenavigation.parse.parsers.TextParser;

import org.json.JSONObject;

public class Transition {
    public Text fromId = new NullText();
    public Text toId = new NullText();
    public Number startDelay = new NullNumber();
    public Number duration = new NullNumber();

    public static Transition parse(JSONObject json) {
        Transition transition = new Transition();
        if (json == null) return transition;

        transition.fromId = TextParser.parse(json, "fromId");
        transition.toId = TextParser.parse(json, "toId");
        Fraction startDelay = FractionParser.parse(json, "startDelay");
        if (startDelay.hasValue()) {
            transition.startDelay = new Number((int) (startDelay.get() * 1000));
        }
        Fraction duration = FractionParser.parse(json, "duration");
        if (duration.hasValue()) {
            transition.duration = new Number((int) (duration.get() * 1000));
        }

        return transition;
    }

    void mergeWith(Transition other) {
        if (other.fromId.hasValue()) fromId = other.fromId;
        if (other.toId.hasValue()) toId = other.toId;
        if (other.startDelay.hasValue()) startDelay = other.startDelay;
        if (other.duration.hasValue()) duration = other.duration;
    }

    void mergeWithDefault(Transition defaultOptions) {
        if (!fromId.hasValue()) fromId = defaultOptions.fromId;
        if (!toId.hasValue()) toId = defaultOptions.toId;
        if (!startDelay.hasValue()) startDelay = defaultOptions.startDelay;
        if (!duration.hasValue()) duration = defaultOptions.duration;
    }
}

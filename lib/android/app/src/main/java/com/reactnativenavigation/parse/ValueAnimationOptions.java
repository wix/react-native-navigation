package com.reactnativenavigation.parse;


import com.reactnativenavigation.parse.params.FloatParam;
import com.reactnativenavigation.parse.params.Interpolation;
import com.reactnativenavigation.parse.params.NullFloatParam;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.parsers.FloatParser;
import com.reactnativenavigation.parse.parsers.InterpolationParser;
import com.reactnativenavigation.parse.parsers.NumberParser;

import org.json.JSONObject;

public class ValueAnimationOptions implements DEFAULT_VALUES {

    public static ValueAnimationOptions parse(JSONObject json) {
        ValueAnimationOptions options = new ValueAnimationOptions();
        if (json == null) return options;

        options.from = FloatParser.parse(json, "from");
        options.to = FloatParser.parse(json, "to");
        options.duration = NumberParser.parse(json, "duration");
        options.delayStart = NumberParser.parse(json, "startDelay");
        options.interpolation = InterpolationParser.parse(json, "interpolation");

        return options;
    }

    public FloatParam from = new NullFloatParam();
    public FloatParam to = new NullFloatParam();
    public Number duration = new NullNumber();
    public Number delayStart = new NullNumber();
    public Interpolation interpolation = Interpolation.NO_VALUE;

    void mergeWith(final ValueAnimationOptions other) {
        if (other.from.hasValue())
            from = other.from;
        if (other.to.hasValue())
            to= other.to;
        if (other.duration.hasValue())
            duration = other.duration;
        if (other.delayStart.hasValue())
            delayStart = other.delayStart;
        if (other.interpolation != Interpolation.NO_VALUE)
            interpolation = other.interpolation;
    }

    void mergeWithDefault(ValueAnimationOptions defaultOptions) {
        if (from == null)
            from = defaultOptions.from;
        if (!to.hasValue())
            to = defaultOptions.to;
        if (!duration.hasValue())
            duration = defaultOptions.duration;
        if (!delayStart.hasValue())
            delayStart = defaultOptions.delayStart;
        if (interpolation == Interpolation.NO_VALUE)
            interpolation = defaultOptions.interpolation;
    }
}

package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

import com.reactnativenavigation.params.Interpolation;

public class InterpolationParser extends Parser {
    private Bundle bundle;

    public InterpolationParser(Bundle bundle) {
        this.bundle = bundle;
    }

    public Interpolation parse() {
        Interpolation result = new Interpolation();
        result.type = bundle.getString("type");
        return result;
    }
}

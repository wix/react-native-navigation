package com.reactnativenavigation.options.params;


public class NullFloatParam extends FloatParam {

    public NullFloatParam() {
        super(0f);
    }

    @Override
    public boolean hasValue() {
        return false;
    }
}

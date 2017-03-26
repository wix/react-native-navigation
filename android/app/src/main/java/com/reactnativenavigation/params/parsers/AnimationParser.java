package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

class AnimationParser extends Parser {
    private Bundle params;

    AnimationParser(Bundle params) {
        this.params = params;
    }

    public boolean parse() {
        if (params.isEmpty()) {
            return true;
        }
        final String animationType = params.getString("animationType", "slide-up");
        return !animationType.equals("none") && params.getBoolean("animated", true);
    }
}

package com.reactnativenavigation.params;

import android.view.animation.Interpolator;

public abstract class InterpolationParams {
    public enum Type {
        Path, Linear;

        public static Type fromString(String type) {
            switch (type) {
                case "path":
                    return Path;
                case "linear":
                default:
                    return Linear;
            }
        }
    }
    public Type type;

    public abstract Interpolator get();
}

package com.reactnativenavigation.params;

import android.animation.TimeInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public abstract class InterpolationParams {
    public enum Type {
        Path("path"), Linear("linear");
        private String name;

        Type(String name) {
            this.name = name;
        }

        public static Type fromString(String name) {
            for (Type type : values()) {
                if (type.name.equals(name)) {
                    return type;
                }
            }
            return Linear;
        }
    }

    public enum Easing {
        AccelerateDecelerate("accelerateDecelerate", new AccelerateDecelerateInterpolator()),
        Accelerate("accelerate", new AccelerateInterpolator()),
        Decelerate("decelerate", new DecelerateInterpolator()),
        FastOutSlowIn("FastOutSlowIn", new FastOutSlowInInterpolator()),
        Linear("linear", new LinearInterpolator());

        private String name;
        private TimeInterpolator interpolator;

        Easing(String name, TimeInterpolator interpolator) {
            this.name = name;
            this.interpolator = interpolator;
        }

        public static Easing fromString(String name) {
            for (Easing easing : values()) {
                if (easing.name.equals(name)) {
                    return easing;
                }
            }
            return Linear;
        }

        public TimeInterpolator getInterpolator() {
            return interpolator;
        }
    }

    public Type type;
    public Easing easing;

    public abstract Interpolator get();
}

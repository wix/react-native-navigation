package com.reactnativenavigation.params;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PathInterpolationParams extends InterpolationParams {
    public float controlX1;
    public float controlY1;
    public float controlX2;
    public float controlY2;

    @Override
    public Interpolator get() {
        return  new PathInterpolator(
                controlX1,
                controlY1,
                controlX2,
                controlY2
        );
    }
}

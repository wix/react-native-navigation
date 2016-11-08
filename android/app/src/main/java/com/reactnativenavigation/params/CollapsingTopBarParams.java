package com.reactnativenavigation.params;

import android.support.annotation.Nullable;

public class CollapsingTopBarParams {
    public enum CollapseBehaviour {CollapseTogether, CollapseTopBarFirst}

    public @Nullable String imageUri;
    public StyleParams.Color scrimColor;
    public CollapseBehaviour collapseBehaviour;

    public boolean hasBackgroundImage() {
        return imageUri != null;
    }
}

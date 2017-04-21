package com.reactnativenavigation.screens;

import android.animation.Animator;
import android.os.Bundle;

public interface CustomAnimator {
    int DEFAULT_ANIMATION_DURATION_MS = 300;

    Animator createAnimator(Bundle animation, Screen screen, Runnable onAnimationEnd);
}
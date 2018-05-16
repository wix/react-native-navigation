package com.reactnativenavigation.params.parsers;

import com.reactnativenavigation.R;
import com.reactnativenavigation.params.ScreenParams;

public class ModalAnimationFactory {
    public static int create(ScreenParams params) {
        if (!params.animateScreenTransitions) return R.style.ModalNoAnimation;
        switch (params.animationType) {
            case "fade":
                return R.style.ModalFadeAnimation;
            case "slide-horizontal":
                return R.style.ModalSlideHorizontal;
            case "screen":
                return R.style.ModalScreenAnimations;
            default:
                return R.style.ModalDefaultAnimations;
        }
    }
}

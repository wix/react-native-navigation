package com.reactnativenavigation.react;

import com.facebook.react.views.image.ReactImageView;
import com.reactnativenavigation.utils.ReflectionUtils;

public class ReactViewHacks {
    // Hack to prevent Image flicker until https://github.com/facebook/react-native/issues/10194 is fixed
    public static void disableReactImageViewRemoteImageFadeInAnimation(ReactImageView reactImageView) {
        reactImageView.setFadeDuration(0);
        ReflectionUtils.setField(reactImageView, "mIsDirty", true);
        reactImageView.maybeUpdateView();
    }
}

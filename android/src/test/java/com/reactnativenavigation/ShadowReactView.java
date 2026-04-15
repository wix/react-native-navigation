package com.reactnativenavigation;

import android.content.Context;
import android.widget.FrameLayout;

import com.reactnativenavigation.react.ReactView;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.util.ReflectionHelpers;
import org.robolectric.util.ReflectionHelpers.ClassParameter;

@Implements(ReactView.class)
public class ShadowReactView extends org.robolectric.shadows.ShadowViewGroup {

    @RealObject
    private ReactView realObject;

    @Implementation
    protected void __constructor__(Context context, String componentId, String componentName) {
        Shadow.invokeConstructor(FrameLayout.class, realObject,
                ClassParameter.from(Context.class, context));
        ReflectionHelpers.setField(realObject, "componentId", componentId);
        ReflectionHelpers.setField(realObject, "componentName", componentName);
    }

    @Implementation
    protected void onAttachedToWindow() {
    }

    @Implementation
    public void start() {
    }

    @Implementation
    public void destroy() {
    }

    @Implementation
    public boolean isRendered() {
        return realObject.getChildCount() >= 1;
    }
}

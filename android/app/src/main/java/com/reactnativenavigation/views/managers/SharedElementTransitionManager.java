package com.reactnativenavigation.views.managers;

import android.util.Log;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.sharedElementTransition.SharedElementTransition;

public class SharedElementTransitionManager extends ViewGroupManager<SharedElementTransition> {

    private SharedElementShadow shadowNode;

    @Override
    public String getName() {
        return "SharedElementTransition";
    }

    @Override
    protected SharedElementTransition createViewInstance(ThemedReactContext reactContext) {
        SharedElementTransition sharedElementTransition = new SharedElementTransition(reactContext);
        shadowNode.setSharedElement(sharedElementTransition);
        return sharedElementTransition;
    }

    @ReactProp(name = "sharedElementId")
    public void setSharedElementId(SharedElementTransition elementTransition, String key) {
        elementTransition.registerSharedElementTransition(key);
    }

    @ReactProp(name = "duration")
    public void setDuration(SharedElementTransition view, int duration) {
        view.paramsParser.setDuration(duration);
    }

    @ReactProp(name = "hideDuration")
    public void setHideDuration(SharedElementTransition view, int duration) {
        view.paramsParser.setHideDuration(duration);
    }

    @ReactProp(name = "showDuration")
    public void setShowDuration(SharedElementTransition view, int duration) {
        view.paramsParser.setShowDuration(duration);
    }

    @ReactProp(name = "showInterpolation")
    public void setShowInterpolation(SharedElementTransition view, ReadableMap interpolation) {
        view.paramsParser.setShowInterpolation(interpolation);
    }

    @ReactProp(name = "hideInterpolation")
    public void setHideInterpolation(SharedElementTransition view, ReadableMap interpolation) {
        view.paramsParser.setHideInterpolation(interpolation);
    }

    @ReactProp(name = "animateClipBounds")
    public void setAnimateClipBounds(SharedElementTransition view, boolean animateClipBounds) {
        view.paramsParser.animateClipBounds = animateClipBounds;
    }

    @Override
    protected void onAfterUpdateTransaction(SharedElementTransition view) {
        view.showTransitionParams = view.paramsParser.parseShowTransitionParams();
        view.hideTransitionParams = view.paramsParser.parseHideTransitionParams();
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        shadowNode = new SharedElementShadow();
        return shadowNode;
    }

    private static class SharedElementShadow extends LayoutShadowNode {
        private SharedElementTransition sharedElement;
        private static int count = 0;

        public void setSharedElement(final SharedElementTransition sharedElement) {
            this.sharedElement = sharedElement;
            ViewUtils.runOnPreDraw(sharedElement, new Runnable() {
                @Override
                public void run() {
                    Log.d("GUYCA", "width: " + sharedElement.getWidth() + " height: " + sharedElement.getHeight());
//                    setStyleWidth(1376);
//                    setStyleHeight(600);
                }
            });
        }



        public SharedElementShadow() {
            if (count == 0) {
//                setStyleWidth(1440);
//                setStyleHeight(760);
                count+=1;
            }
        }
    }
}

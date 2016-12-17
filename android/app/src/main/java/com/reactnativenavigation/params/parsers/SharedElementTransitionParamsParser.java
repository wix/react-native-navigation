package com.reactnativenavigation.params.parsers;

import android.os.Bundle;

public class SharedElementTransitionParamsParser extends Parser {
    private Bundle bundle;

    public SharedElementTransitionParamsParser(Bundle bundle) {
        this.bundle = bundle;
    }

    public SharedElementTransitionParams parse() {
        final int fromViewTag = bundle.getInt("ref");
        final String key = bundle.getString("sharedElementId");
        SharedElementTransitionParams result = new SharedElementTransitionParams(fromViewTag, key);
        return result;
//        result.interpolation = new InterpolationParser(bundle).parse();
    }

//    private void ResolveViewByTag() {
//        ReactContext context = NavigationApplication.instance.getReactGateway().getReactContext();
//        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);
//        uiManager.addUIBlock(new UIBlock() {
//            @Override
//            public void execute(NativeViewHierarchyManager nativeViewHierarchyManager) {
//                try {
//                    View view = nativeViewHierarchyManager.resolveView(tag);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }
}

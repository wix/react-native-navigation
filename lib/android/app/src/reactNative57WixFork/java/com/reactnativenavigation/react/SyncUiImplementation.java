package com.reactnativenavigation.react;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIImplementation;
import com.facebook.react.uimanager.UIImplementationProvider;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.uimanager.common.MeasureSpecProvider;
import com.facebook.react.uimanager.common.SizeMonitoringFrameLayout;
import com.facebook.react.uimanager.events.EventDispatcher;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class SyncUiImplementation extends UIImplementation {
    private static final Object lock = new Object();

    public static class Provider extends UIImplementationProvider {
        @Override
        public UIImplementation createUIImplementation(ReactApplicationContext reactContext, List<ViewManager> viewManagerList, EventDispatcher eventDispatcher, int minTimeLeftInFrameForNonBatchedOperationMs) {
            return new SyncUiImplementation(reactContext, viewManagerList, eventDispatcher, minTimeLeftInFrameForNonBatchedOperationMs);
        }

        @Override
        public UIImplementation createUIImplementation(ReactApplicationContext reactContext, UIManagerModule.ViewManagerResolver viewManagerResolver, EventDispatcher eventDispatcher, int minTimeLeftInFrameForNonBatchedOperationMs) {
            return new SyncUiImplementation(reactContext, viewManagerResolver, eventDispatcher, minTimeLeftInFrameForNonBatchedOperationMs);
        }
    }

    public SyncUiImplementation(ReactApplicationContext reactContext, List<ViewManager> viewManagerList, EventDispatcher eventDispatcher, int minTimeLeftInFrameForNonBatchedOperationMs) {
        super(reactContext, viewManagerList, eventDispatcher, minTimeLeftInFrameForNonBatchedOperationMs);
    }

    public SyncUiImplementation(ReactApplicationContext reactContext, UIManagerModule.ViewManagerResolver viewManagerResolver, EventDispatcher eventDispatcher, int minTimeLeftInFrameForNonBatchedOperationMs) {
        super(reactContext, viewManagerResolver, eventDispatcher, minTimeLeftInFrameForNonBatchedOperationMs);
    }

    @Override
    public void manageChildren(
            int viewTag,
            @Nullable ReadableArray moveFrom,
            @Nullable ReadableArray moveTo,
            @Nullable ReadableArray addChildTags,
            @Nullable ReadableArray addAtIndices,
            @Nullable ReadableArray removeFrom) {
        synchronized (lock) {
            super.manageChildren(viewTag, moveFrom, moveTo, addChildTags, addAtIndices, removeFrom);
        }
    }

    @Override
    public void setChildren(int viewTag, ReadableArray childrenTags) {
        synchronized (lock) {
            super.setChildren(viewTag, childrenTags);
        }
    }

    @Override
    public void createView(int tag, String className, int rootViewTag, ReadableMap props) {
        synchronized (lock) {
            super.createView(tag, className, rootViewTag, props);
        }
    }

    @Override
    public void removeRootShadowNode(int rootViewTag) {
        synchronized (lock) {
            super.removeRootShadowNode(rootViewTag);
        }
    }

    @Override
    public <T extends SizeMonitoringFrameLayout & MeasureSpecProvider> void registerRootView(T rootView, int tag, ThemedReactContext context) {
        synchronized (lock) {
            super.registerRootView(rootView, tag, context);
        }
    }
}

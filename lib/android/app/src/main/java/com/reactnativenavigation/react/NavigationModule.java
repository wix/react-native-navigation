package com.reactnativenavigation.react;

import android.support.annotation.NonNull;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.reactnativenavigation.NavigationActivity;
import com.reactnativenavigation.parse.JSONParser;
import com.reactnativenavigation.parse.LayoutFactory;
import com.reactnativenavigation.parse.LayoutNode;
import com.reactnativenavigation.parse.LayoutNodeParser;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.OverlayOptions;
import com.reactnativenavigation.utils.TypefaceLoader;
import com.reactnativenavigation.utils.UiThread;
import com.reactnativenavigation.viewcontrollers.Navigator;
import com.reactnativenavigation.viewcontrollers.ViewController;

public class NavigationModule extends ReactContextBaseJavaModule {
	private static final String NAME = "RNNBridgeModule";
	private final ReactInstanceManager reactInstanceManager;

	@SuppressWarnings("WeakerAccess")
    public NavigationModule(final ReactApplicationContext reactContext, final ReactInstanceManager reactInstanceManager) {
		super(reactContext);
		this.reactInstanceManager = reactInstanceManager;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@ReactMethod
	public void setRoot(final ReadableMap rawLayoutTree, final Promise promise) {
		final LayoutNode layoutTree = LayoutNodeParser.parse(JSONParser.parse(rawLayoutTree));
		handle(() -> {
            final ViewController viewController = newLayoutFactory().create(layoutTree);
            navigator().setRoot(viewController, promise);
        });
	}

	@ReactMethod
	public void setDefaultOptions(final ReadableMap options) {
        final Options defaultOptions = Options.parse(new TypefaceLoader(activity()), JSONParser.parse(options));
        handle(() -> navigator().setDefaultOptions(defaultOptions));
    }

	@ReactMethod
	public void setOptions(final String onComponentId, final ReadableMap options) {
		final Options navOptions = Options.parse(new TypefaceLoader(activity()), JSONParser.parse(options));
		handle(() -> navigator().setOptions(onComponentId, navOptions));
	}

	@ReactMethod
	public void push(final String onComponentId, final ReadableMap rawLayoutTree, final Promise promise) {
		final LayoutNode layoutTree = LayoutNodeParser.parse(JSONParser.parse(rawLayoutTree));
		handle(() -> {
            final ViewController viewController = newLayoutFactory().create(layoutTree);
            navigator().push(onComponentId, viewController, promise);
        });
	}

	@ReactMethod
	public void pop(final String onComponentId, final ReadableMap options, final Promise promise) {
		handle(() -> navigator().popSpecific(onComponentId, promise));
	}

	@ReactMethod
	public void popTo(final String componentId, final Promise promise) {
		handle(() -> navigator().popTo(componentId, promise));
	}

	@ReactMethod
	public void popToRoot(final String componentId, final Promise promise) {
		handle(() -> navigator().popToRoot(componentId, promise));
	}

	@ReactMethod
	public void showModal(final ReadableMap rawLayoutTree, final Promise promise) {
		final LayoutNode layoutTree = LayoutNodeParser.parse(JSONParser.parse(rawLayoutTree));
		handle(() -> {
            final ViewController viewController = newLayoutFactory().create(layoutTree);
            navigator().showModal(viewController, promise);
        });
	}

	@ReactMethod
	public void dismissModal(final String componentId, final Promise promise) {
		handle(() -> navigator().dismissModal(componentId, promise));
	}

	@ReactMethod
	public void dismissAllModals(final Promise promise) {
		handle(() -> navigator().dismissAllModals(promise));
	}

	@ReactMethod
	public void showOverlay(final String type, final ReadableMap data, final Promise promise) {
        final OverlayOptions overlayOptions = OverlayOptions.parse(JSONParser.parse(data));
        handle(() -> navigator().showOverlay(type, overlayOptions, promise));
	}

	@ReactMethod
	public void dismissOverlay() {
		handle(() -> navigator().dismissOverlay());
	}

	private NavigationActivity activity() {
		return (NavigationActivity) getCurrentActivity();
	}

	private Navigator navigator() {
		return activity().getNavigator();
	}

	@NonNull
	private LayoutFactory newLayoutFactory() {
		return new LayoutFactory(activity(), reactInstanceManager, navigator().getDefaultOptions());
	}

	private void handle(Runnable task) {
		if (activity() == null || activity().isFinishing()) return;
		UiThread.post(task);
	}
}

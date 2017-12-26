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
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.parse.OverlayOptions;
import com.reactnativenavigation.utils.TypefaceLoader;
import com.reactnativenavigation.utils.UiThread;
import com.reactnativenavigation.viewcontrollers.Navigator;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.overlay.OverlayFactory;

public class NavigationModule extends ReactContextBaseJavaModule {
	private static final String NAME = "RNNBridgeModule";
	private final ReactInstanceManager reactInstanceManager;

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
		handle(new Runnable() {
			@Override
			public void run() {
				final ViewController viewController = newLayoutFactory().create(layoutTree);
				navigator().setRoot(viewController, promise);
			}
		});
	}

	@ReactMethod
	public void setDefaultOptions(final ReadableMap options) {
        final NavigationOptions defaultOptions = NavigationOptions.parse(new TypefaceLoader(activity()), JSONParser.parse(options));
        handle(new Runnable() {
            @Override
            public void run() {
                navigator().setDefaultOptions(defaultOptions);
            }
        });
    }

	@ReactMethod
	public void setOptions(final String onContainerId, final ReadableMap options) {
		final NavigationOptions navOptions = NavigationOptions.parse(new TypefaceLoader(activity()), JSONParser.parse(options));
		handle(new Runnable() {
			@Override
			public void run() {
				navigator().setOptions(onContainerId, navOptions);
			}
		});
	}

	@ReactMethod
	public void push(final String onContainerId, final ReadableMap rawLayoutTree, final Promise promise) {
		final LayoutNode layoutTree = LayoutNodeParser.parse(JSONParser.parse(rawLayoutTree));
		handle(new Runnable() {
			@Override
			public void run() {
				final ViewController viewController = newLayoutFactory().create(layoutTree);
				navigator().push(onContainerId, viewController, promise);
			}
		});
	}

	@ReactMethod
	public void pop(final String onContainerId, final ReadableMap options, final Promise promise) {
		handle(new Runnable() {
			@Override
			public void run() {
				navigator().popSpecific(onContainerId, promise);
			}
		});
	}

	@ReactMethod
	public void popTo(final String containerId, final Promise promise) {
		handle(new Runnable() {
			@Override
			public void run() {
				navigator().popTo(containerId, promise);
			}
		});
	}

	@ReactMethod
	public void popToRoot(final String containerId, final Promise promise) {
		handle(new Runnable() {
			@Override
			public void run() {
				navigator().popToRoot(containerId, promise);
			}
		});
	}

	@ReactMethod
	public void showModal(final ReadableMap rawLayoutTree, final Promise promise) {
		final LayoutNode layoutTree = LayoutNodeParser.parse(JSONParser.parse(rawLayoutTree));
		handle(new Runnable() {
			@Override
			public void run() {
				final ViewController viewController = newLayoutFactory().create(layoutTree);
				navigator().showModal(viewController, promise);
			}
		});
	}

	@ReactMethod
	public void dismissModal(final String containerId, final Promise promise) {
		handle(new Runnable() {
			@Override
			public void run() {
				navigator().dismissModal(containerId, promise);
			}
		});
	}

	@ReactMethod
	public void dismissAllModals(final Promise promise) {
		handle(new Runnable() {
			@Override
			public void run() {
				navigator().dismissAllModals(promise);
			}
		});
	}

	@ReactMethod
	public void showOverlay(final String type, final ReadableMap data, final Promise promise) {
		if (OverlayFactory.Overlay.create(type) == OverlayFactory.Overlay.CustomDialog) {
			final LayoutNode layoutTree = LayoutNodeParser.parse(JSONParser.parse(data));
			handle(new Runnable() {
				@Override
				public void run() {
					ViewController viewController = newLayoutFactory().create(layoutTree);
					navigator().showOverlay(type, OverlayOptions.create(viewController), promise);
				}
			});
		} else {
			final OverlayOptions overlayOptions = OverlayOptions.parse(JSONParser.parse(data));
			handle(new Runnable() {
				@Override
				public void run() {
					navigator().showOverlay(type, overlayOptions, promise);
				}
			});
		}


	}

	@ReactMethod
	public void dismissOverlay() {
		handle(new Runnable() {
			@Override
			public void run() {
				navigator().dismissOverlay();
			}
		});
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

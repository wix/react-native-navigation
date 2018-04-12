package com.reactnativenavigation.react;

import android.support.annotation.NonNull;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.reactnativenavigation.NavigationActivity;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.parse.LayoutFactory;
import com.reactnativenavigation.parse.LayoutNode;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.parsers.LayoutNodeParser;
import com.reactnativenavigation.utils.TypefaceLoader;
import com.reactnativenavigation.utils.UiThread;
import com.reactnativenavigation.viewcontrollers.Navigator;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponentCreator;

import org.json.JSONObject;

import java.util.Map;

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
		final LayoutNode layoutTree = LayoutNodeParser.parse(new JSONObject(rawLayoutTree.toHashMap()));
		handle(() -> {
            final ViewController viewController = newLayoutFactory().create(layoutTree);
            navigator().setRoot(viewController, promise);
        });
	}

	@ReactMethod
	public void setDefaultOptions(final ReadableMap options) {
        final Options defaultOptions = Options.parse(new TypefaceLoader(activity()), new JSONObject(options.toHashMap()));
        handle(() -> navigator().setDefaultOptions(defaultOptions));
    }

	@ReactMethod
	public void setOptions(final String onComponentId, final ReadableMap options) {
		final Options navOptions = Options.parse(new TypefaceLoader(activity()), new JSONObject(options.toHashMap()));
		handle(() -> navigator().setOptions(onComponentId, navOptions));
	}

	@ReactMethod
	public void push(final String onComponentId, final ReadableMap rawLayoutTree, final Promise promise) {
		final LayoutNode layoutTree = LayoutNodeParser.parse(new JSONObject(rawLayoutTree.toHashMap()));
		handle(() -> {
            final ViewController viewController = newLayoutFactory().create(layoutTree);
            navigator().push(onComponentId, viewController, new CommandListenerAdapter(promise));
        });
	}

    @ReactMethod
    public void setStackRoot(final String onComponentId, final ReadableMap rawLayoutTree, final Promise promise) {
        final LayoutNode layoutTree = LayoutNodeParser.parse(new JSONObject(rawLayoutTree.toHashMap()));
        handle(() -> {
            final ViewController viewController = newLayoutFactory().create(layoutTree);
            navigator().setStackRoot(onComponentId, viewController, new CommandListenerAdapter(promise));
        });
    }

	@ReactMethod
	public void pop(final String onComponentId, final ReadableMap options, final Promise promise) {
		handle(() -> navigator().popSpecific(onComponentId, new CommandListenerAdapter(promise)));
	}

	@ReactMethod
	public void popTo(final String componentId, final Promise promise) {
		handle(() -> navigator().popTo(componentId, new CommandListenerAdapter(promise)));
	}

	@ReactMethod
	public void popToRoot(final String componentId, final Promise promise) {
		handle(() -> navigator().popToRoot(componentId, new CommandListenerAdapter(promise)));
	}

	@ReactMethod
	public void showModal(final ReadableMap rawLayoutTree, final Promise promise) {
		final LayoutNode layoutTree = LayoutNodeParser.parse(new JSONObject(rawLayoutTree.toHashMap()));
		handle(() -> {
            final ViewController viewController = newLayoutFactory().create(layoutTree);
            navigator().showModal(viewController, promise);
        });
	}

	@ReactMethod
	public void dismissModal(final String componentId, final Promise promise) {
		handle(() -> navigator().dismissModal(componentId, new CommandListenerAdapter(promise)));
	}

	@ReactMethod
	public void dismissAllModals(final Promise promise) {
		handle(() -> navigator().dismissAllModals(new CommandListenerAdapter(promise)));
	}

	@ReactMethod
	public void showOverlay(final ReadableMap rawLayoutTree) {
        final LayoutNode layoutTree = LayoutNodeParser.parse(new JSONObject(rawLayoutTree.toHashMap()));
        handle(() -> {
            final ViewController viewController = newLayoutFactory().create(layoutTree);
            navigator().showOverlay(viewController);
        });
	}

	@ReactMethod
	public void dismissOverlay(final String componentId) {
		handle(() -> navigator().dismissOverlay(componentId));
	}

	private Navigator navigator() {
		return activity().getNavigator();
	}

	@NonNull
	private LayoutFactory newLayoutFactory() {
		return new LayoutFactory(activity(),
                reactInstanceManager,
                externalComponentCreator(),
                navigator().getDefaultOptions()
        );
	}

	private Map<String, ExternalComponentCreator> externalComponentCreator() {
        return ((NavigationApplication) activity().getApplication()).getExternalComponents();
    }

	private void handle(Runnable task) {
		if (activity() == null || activity().isFinishing()) return;
		UiThread.post(task);
	}

    private NavigationActivity activity() {
        return (NavigationActivity) getCurrentActivity();
    }

    private class CommandListenerAdapter implements Navigator.CommandListener {
        private Promise promise;

        CommandListenerAdapter(Promise promise) {
            this.promise = promise;
        }

        @Override
        public void onSuccess(String childId) {
            promise.resolve(childId);
        }

        @Override
        public void onError(String message) {
            promise.reject(new Throwable(message));
        }
    }
}

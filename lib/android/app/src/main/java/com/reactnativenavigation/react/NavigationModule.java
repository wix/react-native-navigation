package com.reactnativenavigation.react;

import android.support.annotation.NonNull;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.reactnativenavigation.NavigationActivity;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.parse.LayoutFactory;
import com.reactnativenavigation.parse.LayoutNode;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.parsers.JSONParser;
import com.reactnativenavigation.parse.parsers.LayoutNodeParser;
import com.reactnativenavigation.utils.NativeCommandListener;
import com.reactnativenavigation.utils.Now;
import com.reactnativenavigation.utils.TypefaceLoader;
import com.reactnativenavigation.utils.UiThread;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.Navigator;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponentCreator;

import java.util.Map;

public class NavigationModule extends ReactContextBaseJavaModule {
	private static final String NAME = "RNNBridgeModule";

    private final Now now = new Now();
	private final ReactInstanceManager reactInstanceManager;
    private EventEmitter eventEmitter;

    @SuppressWarnings("WeakerAccess")
    public NavigationModule(ReactApplicationContext reactContext, ReactInstanceManager reactInstanceManager) {
		super(reactContext);
		this.reactInstanceManager = reactInstanceManager;
		reactInstanceManager.addReactInstanceEventListener(context -> eventEmitter = new EventEmitter(context));
    }

	@Override
	public String getName() {
		return NAME;
	}

	@ReactMethod
    public void getConstants(Promise promise) {
        WritableMap constants = Arguments.createMap();
        constants.putString(Constants.BACK_BUTTON_JS_KEY, Constants.BACK_BUTTON_ID);
        constants.putDouble(Constants.STATUS_BAR_HEIGHT_KEY, UiUtils.getStatusBarHeight(getReactApplicationContext()));
        constants.putDouble(Constants.TOP_BAR_HEIGHT_KEY, UiUtils.getToolBarHeight(getReactApplicationContext()));
        constants.putDouble(Constants.TOP_BAR_HEIGHT_KEY, UiUtils.dpToPx(getReactApplicationContext(), Constants.BOTTOM_TABS_HEIGHT));
        promise.resolve(constants);
    }

	@ReactMethod
	public void setRoot(String commandId, ReadableMap rawLayoutTree, Promise promise) {
		final LayoutNode layoutTree = LayoutNodeParser.parse(JSONParser.parse(rawLayoutTree).optJSONObject("root"));
		handle(() -> {
            navigator().setEventEmitter(eventEmitter);
            final ViewController viewController = newLayoutFactory().create(layoutTree);
            navigator().setRoot(viewController, new NativeCommandListener(commandId, promise, eventEmitter, now));
        });
	}

	@ReactMethod
	public void setDefaultOptions(ReadableMap options) {
        final Options defaultOptions = Options.parse(new TypefaceLoader(activity()), JSONParser.parse(options));
        handle(() -> navigator().setDefaultOptions(defaultOptions));
    }

	@ReactMethod
	public void mergeOptions(String onComponentId, ReadableMap options) {
		final Options navOptions = Options.parse(new TypefaceLoader(activity()), JSONParser.parse(options));
		handle(() -> navigator().mergeOptions(onComponentId, navOptions));
	}

	@ReactMethod
	public void push(String commandId, String onComponentId, ReadableMap rawLayoutTree, Promise promise) {
		final LayoutNode layoutTree = LayoutNodeParser.parse(JSONParser.parse(rawLayoutTree));
		handle(() -> {
            final ViewController viewController = newLayoutFactory().create(layoutTree);
            navigator().push(onComponentId, viewController, new NativeCommandListener(commandId, promise, eventEmitter, now));
        });
	}

    @ReactMethod
    public void setStackRoot(String commandId, String onComponentId, ReadableMap rawLayoutTree, Promise promise) {
        final LayoutNode layoutTree = LayoutNodeParser.parse(JSONParser.parse(rawLayoutTree));
        handle(() -> {
            final ViewController viewController = newLayoutFactory().create(layoutTree);
            navigator().setStackRoot(onComponentId, viewController, new NativeCommandListener(commandId, promise, eventEmitter, now));
        });
    }

	@ReactMethod
	public void pop(String commandId, String onComponentId, ReadableMap options, Promise promise) {
		handle(() -> navigator().popSpecific(onComponentId, new NativeCommandListener(commandId, promise, eventEmitter, now)));
	}

	@ReactMethod
	public void popTo(String commandId, String componentId, Promise promise) {
		handle(() -> navigator().popTo(componentId, new NativeCommandListener(commandId, promise, eventEmitter, now)));
	}

	@ReactMethod
	public void popToRoot(String commandId, String componentId, Promise promise) {
		handle(() -> navigator().popToRoot(componentId, new NativeCommandListener(commandId, promise, eventEmitter, now)));
	}

	@ReactMethod
	public void showModal(String commandId, ReadableMap rawLayoutTree, Promise promise) {
		final LayoutNode layoutTree = LayoutNodeParser.parse(JSONParser.parse(rawLayoutTree));
		handle(() -> {
            final ViewController viewController = newLayoutFactory().create(layoutTree);
            navigator().showModal(viewController, new NativeCommandListener(commandId, promise, eventEmitter, now));
        });
	}

	@ReactMethod
	public void dismissModal(String commandId, String componentId, Promise promise) {
		handle(() -> navigator().dismissModal(componentId, new NativeCommandListener(commandId, promise, eventEmitter, now)));
	}

	@ReactMethod
	public void dismissAllModals(String commandId, Promise promise) {
		handle(() -> navigator().dismissAllModals(new NativeCommandListener(commandId, promise, eventEmitter, now)));
	}

	@ReactMethod
	public void showOverlay(String commandId, ReadableMap rawLayoutTree, Promise promise) {
        final LayoutNode layoutTree = LayoutNodeParser.parse(JSONParser.parse(rawLayoutTree));
        handle(() -> {
            final ViewController viewController = newLayoutFactory().create(layoutTree);
            navigator().showOverlay(viewController, new NativeCommandListener(commandId, promise, eventEmitter, now));
        });
	}

	@ReactMethod
	public void dismissOverlay(String commandId, String componentId, Promise promise) {
		handle(() -> navigator().dismissOverlay(componentId, new NativeCommandListener(commandId, promise, eventEmitter, now)));
	}

	private Navigator navigator() {
		return activity().getNavigator();
	}

	@NonNull
	private LayoutFactory newLayoutFactory() {
		return new LayoutFactory(activity(),
                navigator().getChildRegistry(),
                reactInstanceManager,
                eventEmitter,
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
}

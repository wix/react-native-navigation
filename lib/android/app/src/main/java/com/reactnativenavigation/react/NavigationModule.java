package com.reactnativenavigation.react;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.reactnativenavigation.NavigationActivity;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.options.LayoutFactory;
import com.reactnativenavigation.options.LayoutNode;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.parsers.JSONParser;
import com.reactnativenavigation.options.parsers.LayoutNodeParser;
import com.reactnativenavigation.options.parsers.TypefaceLoader;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.LaunchArgsParser;
import com.reactnativenavigation.utils.Now;
import com.reactnativenavigation.utils.SystemUiUtils;
import com.reactnativenavigation.utils.UiThread;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.navigator.Navigator;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import java.util.ArrayList;
import java.util.Objects;

import static com.reactnativenavigation.utils.UiUtils.pxToDp;

import android.app.Activity;

public class NavigationModule extends ReactContextBaseJavaModule {
    private static final String NAME = "RNNBridgeModule";

    private final Now now = new Now();
    private final ReactInstanceManager reactInstanceManager;
    private final JSONParser jsonParser;
    private final LayoutFactory layoutFactory;
    private EventEmitter eventEmitter;

    @SuppressWarnings("WeakerAccess")
    public NavigationModule(ReactApplicationContext reactContext, ReactInstanceManager reactInstanceManager, LayoutFactory layoutFactory) {
        this(reactContext, reactInstanceManager, new JSONParser(), layoutFactory);
    }

    public NavigationModule(ReactApplicationContext reactContext, ReactInstanceManager reactInstanceManager, JSONParser jsonParser, LayoutFactory layoutFactory) {
        super(reactContext);
        this.reactInstanceManager = reactInstanceManager;
        this.jsonParser = jsonParser;
        this.layoutFactory = layoutFactory;
        reactContext.addLifecycleEventListener(new LifecycleEventListenerAdapter() {
            @Override
            public void onHostPause() {
                super.onHostPause();
                UiUtils.runOnMainThread(() -> {
                    if (activity() != null) navigator().onHostPause();
                });
            }

            @Override
            public void onHostResume() {
                eventEmitter = new EventEmitter(reactContext);
                final Navigator currentNavigator=navigator();
                final NavigationActivity navigationActivity=activity();
                if(currentNavigator!=null && navigationActivity!=null){
                    currentNavigator.setEventEmitter(eventEmitter);
                    layoutFactory.init(
                            navigationActivity,
                            eventEmitter,
                            currentNavigator.getChildRegistry(),
                            ((NavigationApplication) navigationActivity.getApplication()).getExternalComponents()
                    );
                    UiUtils.runOnMainThread(currentNavigator::onHostResume);
                }
            }
        });
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void getLaunchArgs(String commandId, Promise promise) {
        final NavigationActivity navigationActivity=activity();
        if(navigationActivity!=null)
        {
            promise.resolve(LaunchArgsParser.parse(navigationActivity));
        }
        else{
            promise.reject(new NullPointerException());
        }
    }

    private WritableMap createNavigationConstantsMap() {
        ReactApplicationContext ctx = getReactApplicationContext();
        final Activity currentActivity = ctx.getCurrentActivity();
        WritableMap constants = Arguments.createMap();
        constants.putString(Constants.BACK_BUTTON_JS_KEY, Constants.BACK_BUTTON_ID);
        constants.putDouble(Constants.BOTTOM_TABS_HEIGHT_KEY, pxToDp(ctx, UiUtils.getBottomTabsHeight(ctx)));
        constants.putDouble(Constants.STATUS_BAR_HEIGHT_KEY, pxToDp(ctx, SystemUiUtils.getStatusBarHeight(currentActivity)));
        constants.putDouble(Constants.TOP_BAR_HEIGHT_KEY, pxToDp(ctx, UiUtils.getTopBarHeight(ctx)));
        return constants;
    }

    @ReactMethod
    public void getNavigationConstants(Promise promise) {
        promise.resolve(createNavigationConstantsMap());
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    public WritableMap getNavigationConstantsSync() {
        return createNavigationConstantsMap();
    }

    @ReactMethod
    public void setRoot(String commandId, ReadableMap rawLayoutTree, Promise promise) {
        final LayoutNode layoutTree = LayoutNodeParser.parse(Objects.requireNonNull(jsonParser.parse(rawLayoutTree).optJSONObject("root")));
        handle(() -> {
            final ViewController<?> viewController = layoutFactory.create(layoutTree);
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.setRoot(viewController, new NativeCommandListener("setRoot", commandId, promise, eventEmitter, now), reactInstanceManager);
            }
        });
    }

    @ReactMethod
    public void setDefaultOptions(ReadableMap options) {
        handle(() -> {
            Options defaultOptions = parse(options);
            layoutFactory.setDefaultOptions(defaultOptions);
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.setDefaultOptions(defaultOptions);
            }
        });
    }

    @ReactMethod
    public void mergeOptions(String onComponentId, @Nullable ReadableMap options) {
        handle(() -> {
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.mergeOptions(onComponentId, parse(options));
            }
        });
    }

    @ReactMethod
    public void push(String commandId, String onComponentId, ReadableMap rawLayoutTree, Promise promise) {
        final LayoutNode layoutTree = LayoutNodeParser.parse(jsonParser.parse(rawLayoutTree));
        handle(() -> {
            final ViewController<?> viewController = layoutFactory.create(layoutTree);
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.push(onComponentId, viewController, new NativeCommandListener("push", commandId, promise, eventEmitter, now));
            }
        });
    }

    @ReactMethod
    public void setStackRoot(String commandId, String onComponentId, ReadableArray children, Promise promise) {
        handle(() -> {
            ArrayList<ViewController<?>> _children = new ArrayList<>();
            for (int i = 0; i < children.size(); i++) {
                final LayoutNode layoutTree = LayoutNodeParser.parse(jsonParser.parse(children.getMap(i)));
                _children.add(layoutFactory.create(layoutTree));
            }
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.setStackRoot(onComponentId, _children, new NativeCommandListener("setStackRoot", commandId, promise, eventEmitter, now));
            }
        });
    }

    @ReactMethod
    public void pop(String commandId, String componentId, @Nullable ReadableMap mergeOptions, Promise promise) {
        handle(() -> {
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.pop(componentId, parse(mergeOptions), new NativeCommandListener("pop", commandId, promise, eventEmitter, now));
            }
        });
    }

    @ReactMethod
    public void popTo(String commandId, String componentId, @Nullable ReadableMap mergeOptions, Promise promise) {
        handle(() -> {
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.popTo(componentId, parse(mergeOptions), new NativeCommandListener("popTo", commandId, promise, eventEmitter, now));
            }
        });
    }

    @ReactMethod
    public void popToRoot(String commandId, String componentId, @Nullable ReadableMap mergeOptions, Promise promise) {
        handle(() -> {
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.popToRoot(componentId, parse(mergeOptions), new NativeCommandListener("popToRoot", commandId, promise, eventEmitter, now));
            }
        });
    }

    @ReactMethod
    public void showModal(String commandId, ReadableMap rawLayoutTree, Promise promise) {
        final LayoutNode layoutTree = LayoutNodeParser.parse(jsonParser.parse(rawLayoutTree));
        handle(() -> {
            final ViewController<?> viewController = layoutFactory.create(layoutTree);
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.showModal(viewController, new NativeCommandListener("showModal", commandId, promise, eventEmitter, now));
            }
        });
    }

    @ReactMethod
    public void dismissModal(String commandId, String componentId, @Nullable ReadableMap mergeOptions, Promise promise) {
        handle(() -> {
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.mergeOptions(componentId, parse(mergeOptions));
                currentNavigator.dismissModal(componentId, new NativeCommandListener("dismissModal", commandId, promise, eventEmitter, now));
            }
        });
    }

    @ReactMethod
    public void dismissAllModals(String commandId, @Nullable ReadableMap mergeOptions, Promise promise) {
        handle(() -> {
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.dismissAllModals(parse(mergeOptions), new NativeCommandListener("dismissAllModals", commandId, promise, eventEmitter, now));
            }
        });
    }

    @ReactMethod
    public void showOverlay(String commandId, ReadableMap rawLayoutTree, Promise promise) {
        final LayoutNode layoutTree = LayoutNodeParser.parse(jsonParser.parse(rawLayoutTree));
        handle(() -> {
            final ViewController<?> viewController = layoutFactory.create(layoutTree);
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.showOverlay(viewController, new NativeCommandListener("showOverlay", commandId, promise, eventEmitter, now));
            }
        });
    }

    @ReactMethod
    public void dismissOverlay(String commandId, String componentId, Promise promise) {
        handle(() -> {
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.dismissOverlay(componentId, new NativeCommandListener("dismissOverlay", commandId, promise, eventEmitter, now));
            }
        });
    }

    @ReactMethod
    public void dismissAllOverlays(String commandId, Promise promise) {
        handle(() -> {
            Navigator currentNavigator=navigator();
            if(currentNavigator!=null){
                currentNavigator.dismissAllOverlays(new NativeCommandListener("dismissAllOverlays", commandId, promise, eventEmitter, now));
            }
        });
    }

    private Navigator navigator() {
        NavigationActivity navigationActivity=activity();
        if(navigationActivity!=null){
            return navigationActivity.getNavigator();
        }
        return null;
    }

    private Options parse(@Nullable ReadableMap mergeOptions) {
        ReactApplicationContext ctx = getReactApplicationContext();
        return mergeOptions ==
                null ? Options.EMPTY : Options.parse(ctx, new TypefaceLoader(activity()), jsonParser.parse(mergeOptions));
    }

    protected void handle(Runnable task) {
        UiThread.post(() -> {
            NavigationActivity navigationActivity=activity();
            if (getCurrentActivity() != null && navigationActivity!=null && !navigationActivity.isFinishing()) {
                task.run();
            }
        });
    }

    protected NavigationActivity activity() {
        return (NavigationActivity) getCurrentActivity();
    }

    @Override
    public void onCatalystInstanceDestroy() {
        final NavigationActivity navigationActivity = activity();
        if (navigationActivity != null) {
            navigationActivity.onCatalystInstanceDestroy();
        }
        super.onCatalystInstanceDestroy();
    }
}

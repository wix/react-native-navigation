package com.reactnativenavigation.core;

import android.app.Application;
import android.util.Log;

import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaJSExecutor;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.devsupport.DevSupportManager;
import com.facebook.react.devsupport.ReactInstanceDevCommandsHandler;
import com.reactnativenavigation.activities.BaseReactActivity;
import com.reactnativenavigation.core.objects.Screen;
import com.reactnativenavigation.utils.ContextProvider;
import com.reactnativenavigation.utils.ReflectionUtils;

import static com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

/**
 * Created by guyc on 22/02/16.
 */
public class RctManager {
    private static final String TAG = "RctManager";
    private static final String KEY_EVENT_ID = "id";
    private static final String KEY_EVENT_TYPE = "type";
    private static final String EVENT_TYPE = "NavBarButtonPress";
    private static RctManager sInstance;

    private ReactInstanceManager mReactManager;

    private RctManager() {
        // Singleton
    }

    public static synchronized RctManager getInstance() {
        if (sInstance == null) {
            sInstance = new RctManager();
        }
        return sInstance;
    }

    public ReactInstanceManager getReactInstanceManager() {
        return mReactManager;
    }

    public boolean isInitialized() {
        return mReactManager != null;
    }

    public void init(BaseReactActivity context) {
        createReactInstanceManager(context);
    }

    /**
     * Creates a React Instance Manager associated with this component name
     */
    public ReactInstanceManager createReactInstanceManager(BaseReactActivity reactActivity) {
        ReactInstanceManager.Builder builder = ReactInstanceManager.builder()
                .setUseOldBridge(true)
                .setApplication((Application) reactActivity.getApplicationContext())
                .setJSMainModuleName(reactActivity.getJSMainModuleName())
                .setUseDeveloperSupport(reactActivity.getUseDeveloperSupport())
                .setInitialLifecycleState(LifecycleState.BEFORE_RESUME);

        for (ReactPackage reactPackage : reactActivity.getPackages()) {
            builder.addPackage(reactPackage);
        }

        String jsBundleFile = reactActivity.getJSBundleFile();

        if (jsBundleFile != null) {
            builder.setJSBundleFile(jsBundleFile);
        } else {
            builder.setBundleAssetName(reactActivity.getBundleAssetName());
        }

        mReactManager = builder.build();
        if (reactActivity.getUseDeveloperSupport()) {
            setupDevSupportHandler(mReactManager);
        }
        return mReactManager;
    }

    /**
     * Inject our CustomDevCommandsHandler to {@code reactInstanceManager} so we can detect when the bundle was
     * parsed and is about to load.
     * @param reactInstanceManager
     */
    private void setupDevSupportHandler(ReactInstanceManager reactInstanceManager) {
        final ReactInstanceDevCommandsHandler devInterface = (ReactInstanceDevCommandsHandler)
                ReflectionUtils.getDeclaredField(reactInstanceManager, "mDevInterface");
        if (devInterface == null) {
            Log.e(TAG, "Could not get field mDevInterface");
            return;
        }

        // Create customDevCommandsHandler
        CustomDevCommandsHandler customDevCommandsHandler = new CustomDevCommandsHandler(devInterface);
        boolean success = ReflectionUtils.setField(reactInstanceManager, "mDevInterface", customDevCommandsHandler);
        if (!success) {
            Log.e(TAG, "Could not set field mDevInterface");
            return;
        }

        // Set customDevCommandsHandler in devSupportManager. Fun =).
        DevSupportManager devSupportManager = (DevSupportManager)
                ReflectionUtils.getDeclaredField(reactInstanceManager, "mDevSupportManager");
        if (devSupportManager == null) {
            Log.e(TAG, "Could not get field mDevSupportManager");
            return;
        }

        success = ReflectionUtils.setField(devSupportManager, "mReactInstanceCommandsHandler", customDevCommandsHandler);
        if (!success) {
            Log.e(TAG, "Could not set field mReactInstanceCommandsHandler");
        }
    }

    public <T extends ReactContextBaseJavaModule> T getNativeModule(Class<T> nativeModuleClass) {
        if (mReactManager == null || mReactManager.getCurrentReactContext() == null) {
            return null;
        }

        return mReactManager.getCurrentReactContext().getNativeModule(nativeModuleClass);
    }

    /**
     * Sends an event to JavaScript using <a href="https://facebook.github.io/react-native/docs/native-modules-android.html#sending-events-to-javascript">RCTDeviceEventEmitter</a>
     * @param eventName Name of the event
     * @param params Event params
     * @param screen screen which should receive the event
     */
    public void sendEvent(String eventName, Screen screen, WritableMap params) {
        RCTDeviceEventEmitter eventEmitter = getEventEmitter();
        if (eventEmitter == null) {
            return;
        }

        params.putString(KEY_EVENT_TYPE, EVENT_TYPE);
        params.putString(KEY_EVENT_ID, eventName);
        params.putString(Screen.KEY_NAVIGATOR_EVENT_ID, screen.navigatorEventId);
        eventEmitter.emit(screen.navigatorEventId, params);
    }

    private RCTDeviceEventEmitter getEventEmitter() {
        if (mReactManager == null) {
            return null;
        }

        ReactContext currentReactContext = mReactManager.getCurrentReactContext();
        if (currentReactContext == null) {
            return null;
        }

        return currentReactContext.getJSModule(RCTDeviceEventEmitter.class);
    }

    public void onDestroy() {
        mReactManager = null;
        sInstance = null;
    }

    /**
     * Proxy for {@link ReactInstanceDevCommandsHandler} used by {@link DevSupportManager} for requesting React
     * instance recreation. Used to notify {@link BaseReactActivity} that the bundle has been reloaded.
     */
    private static class CustomDevCommandsHandler implements ReactInstanceDevCommandsHandler {
        private ReactInstanceDevCommandsHandler mCommandsHandler;

        public CustomDevCommandsHandler(ReactInstanceDevCommandsHandler commandsHandler) {
            mCommandsHandler = commandsHandler;
        }

        @Override
        public void onReloadWithJSDebugger(JavaJSExecutor.Factory proxyExecutorFactory) {
            onJSBundleReloaded();
            mCommandsHandler.onReloadWithJSDebugger(proxyExecutorFactory);
        }

        @Override
        public void onJSBundleLoadedFromServer() {
            onJSBundleReloaded();
            mCommandsHandler.onJSBundleLoadedFromServer();
        }

        /**
         * Detach previously added ReactRootViews before handling bundle.
         */
        private void onJSBundleReloaded() {
            BaseReactActivity context = ContextProvider.getActivityContext();
            if (context != null) {
                context.onJSBundleReloaded();
            }
        }

        @Override
        public void toggleElementInspector() {
            mCommandsHandler.toggleElementInspector();
        }
    }
}


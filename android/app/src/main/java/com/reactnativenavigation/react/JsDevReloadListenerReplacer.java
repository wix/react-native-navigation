package com.reactnativenavigation.react;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.JavaJSExecutor;
import com.facebook.react.devsupport.ReactInstanceManagerDevHelper;
import com.reactnativenavigation.utils.ReflectionUtils;

class JsDevReloadListenerReplacer {
    private final ReactInstanceManager reactInstanceManager;
    private final Listener listener;

    interface Listener {
        void onJsDevReload();
    }

    JsDevReloadListenerReplacer(ReactInstanceManager reactInstanceManager, Listener listener) {
        this.reactInstanceManager = reactInstanceManager;
        this.listener = listener;
    }

    void replace() {
        ReactInstanceManagerDevHelper originalHandler = getOriginalHandler();
        DevCommandsHandlerProxy proxy = new DevCommandsHandlerProxy(originalHandler, listener);
        replaceInReactInstanceManager(proxy);
        replaceInDevSupportManager(proxy);
    }

    private void replaceInDevSupportManager(DevCommandsHandlerProxy proxy) {
        Object devSupportManager = ReflectionUtils.getDeclaredField(reactInstanceManager, "mDevSupportManager");
        ReflectionUtils.setField(devSupportManager, "mReactInstanceCommandsHandler", proxy);
    }

    private ReactInstanceManagerDevHelper getOriginalHandler() {
        return (ReactInstanceManagerDevHelper) ReflectionUtils.getDeclaredField(reactInstanceManager, "mDevInterface");
    }

    private void replaceInReactInstanceManager(DevCommandsHandlerProxy proxy) {
        ReflectionUtils.setField(reactInstanceManager, "mDevInterface", proxy);
    }

    private static class DevCommandsHandlerProxy implements ReactInstanceManagerDevHelper {
        private ReactInstanceManagerDevHelper originalReactHandler;
        private final Listener listener;

        DevCommandsHandlerProxy(ReactInstanceManagerDevHelper originalReactHandler, Listener listener) {
            this.originalReactHandler = originalReactHandler;
            this.listener = listener;
        }

        @Override
        public void onReloadWithJSDebugger(JavaJSExecutor.Factory proxyExecutorFactory) {
            listener.onJsDevReload();
            originalReactHandler.onReloadWithJSDebugger(proxyExecutorFactory);
        }

        @Override
        public void onJSBundleLoadedFromServer() {
            listener.onJsDevReload();
            originalReactHandler.onJSBundleLoadedFromServer();
        }

        @Override
        public void toggleElementInspector() {
            originalReactHandler.toggleElementInspector();
        }

        @Override
        public Activity getCurrentActivity() {
            return null;
        }

    }
}

package com.reactnativenavigation.react;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.utils.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
        Object originalHelper = getOriginalHelper();

        Object devSupportManager = ReflectionUtils.getDeclaredField(reactInstanceManager, "mDevSupportManager");

        Object proxy = Proxy.newProxyInstance(
                originalHelper.getClass().getClassLoader(),
                originalHelper.getClass().getInterfaces(),
                new DevHelperProxy(originalHelper, listener));

        if (ReflectionUtils.getDeclaredField(reactInstanceManager, "mDevInterface") == null) { // RN >= 0.52
            ReflectionUtils.setField(devSupportManager, "mReactInstanceManagerHelper", proxy);
        } else {                                                                                         // RN <= 0.51
            ReflectionUtils.setField(reactInstanceManager, "mDevInterface", proxy);
            ReflectionUtils.setField(devSupportManager, "mReactInstanceCommandsHandler", proxy);
        }
    }


    private Object getOriginalHelper() {
        Object devInterface = ReflectionUtils.getDeclaredField(reactInstanceManager, "mDevInterface");

        if (devInterface == null) { // RN >= 0.52
            Object devSupportManager = ReflectionUtils.getDeclaredField(reactInstanceManager, "mDevSupportManager");
            return ReflectionUtils.getDeclaredField(devSupportManager, "mReactInstanceManagerHelper");
        }

        return devInterface;        // RN <= 0.51
    }


    private static class DevHelperProxy implements InvocationHandler {
        private Object originalReactHelper;
        private final Listener listener;

        DevHelperProxy(Object originalReactHelper, Listener listener) {
            this.originalReactHelper = originalReactHelper;
            this.listener = listener;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();

            if (methodName.equals("onJSBundleLoadedFromServer") || methodName.equals("onReloadWithJSDebugger")) {
                listener.onJsDevReload();
            }

            return method.invoke(originalReactHelper, args);
        }
    }
}

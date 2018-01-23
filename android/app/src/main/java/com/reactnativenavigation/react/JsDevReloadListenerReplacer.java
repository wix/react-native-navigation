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
        Object originalHandler = getOriginalHandler();

        Object devSupportManager = ReflectionUtils.getDeclaredField(reactInstanceManager, "mDevSupportManager");

        Object proxy = Proxy.newProxyInstance(
                originalHandler.getClass().getClassLoader(),
                originalHandler.getClass().getInterfaces(),
                new DevCommandsHandlerProxy(originalHandler, listener));

        if (ReflectionUtils.getDeclaredField(reactInstanceManager, "mDevInterface") == null) {
            ReflectionUtils.setField(devSupportManager, "mReactInstanceManagerHelper", proxy);
        } else {
            ReflectionUtils.setField(reactInstanceManager, "mDevInterface", proxy);
            ReflectionUtils.setField(devSupportManager, "mReactInstanceCommandsHandler", proxy);
        }
    }


    private Object getOriginalHandler() {
        Object devInterface = ReflectionUtils.getDeclaredField(reactInstanceManager, "mDevInterface");
        if (devInterface == null) {
            Object devSupportManager = ReflectionUtils.getDeclaredField(reactInstanceManager, "mDevSupportManager");
            devInterface = ReflectionUtils.getDeclaredField(devSupportManager, "mReactInstanceManagerHelper");
        }
        return devInterface;
    }


    private static class DevCommandsHandlerProxy implements InvocationHandler {
        private Object originalReactHandler;
        private final Listener listener;

        DevCommandsHandlerProxy(Object originalReactHandler, Listener listener) {
            this.originalReactHandler = originalReactHandler;
            this.listener = listener;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            if (method.getName().equals("onJSBundleLoadedFromServer") || method.getName().equals("onReloadWithJSDebugger")) {
                listener.onJsDevReload();
            }
            return method.invoke(originalReactHandler, args);
        }
    }
}

package com.reactnativenavigation.react;

public class ReloadHandler extends ReloadHandlerFacade implements JsDevReloadHandler.ReloadListener {

    private static final Runnable NO_OP_RELOAD_LISTENER = () -> {};
    private Runnable onReloadListener = NO_OP_RELOAD_LISTENER;

    public void setOnReloadListener(Runnable onReload) {
        this.onReloadListener = onReload;
    }

    @Override
    public void onReload() {
        onReloadListener.run();
    }

    @Override
    public void onSuccess() {
        onReloadListener.run();
    }

    public void destroy() {
        onReloadListener = NO_OP_RELOAD_LISTENER;
    }
}

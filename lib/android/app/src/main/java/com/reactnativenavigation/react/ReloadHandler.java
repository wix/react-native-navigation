package com.reactnativenavigation.react;

public class ReloadHandler extends ReloadHandlerFacade implements JsDevReloadHandler.ReloadListener {

    private Runnable onReloadListener = () -> {};

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
        onReloadListener = null;
    }
}

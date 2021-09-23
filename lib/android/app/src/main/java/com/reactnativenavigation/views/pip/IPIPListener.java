package com.reactnativenavigation.views.pip;

public interface IPIPListener {
    void onPIPStateChanged(PIPStates oldPIPState, PIPStates pipState);

    void onCloseClick();

    void onFullScreenClick();
}

package com.reactnativenavigation.views.pip;

import com.reactnativenavigation.options.PIPActionButton;

public interface IPIPListener {
    void onPIPStateChanged(PIPStates oldPIPState, PIPStates pipState);

    void onCloseClick();

    void onFullScreenClick();

    void onPIPButtonClick(PIPActionButton button);
}

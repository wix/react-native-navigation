package com.reactnativenavigation.react;

import com.facebook.react.devsupport.interfaces.DevSupportManager;
import com.reactnativenavigation.BaseTest;

import org.junit.Test;
import org.mockito.Mockito;

public class JsDevReloadHandlerTest extends BaseTest {
    @Test
    public void onSuccess_afterListenerRemovalIsIgnored() {
        JsDevReloadHandler.ReloadListener listener =
                Mockito.mock(JsDevReloadHandler.ReloadListener.class);
        JsDevReloadHandler handler =
                new JsDevReloadHandler(Mockito.mock(DevSupportManager.class));
        handler.setReloadListener(listener);
        handler.removeReloadListener(listener);

        handler.onSuccess();

        Mockito.verifyNoInteractions(listener);
    }
}

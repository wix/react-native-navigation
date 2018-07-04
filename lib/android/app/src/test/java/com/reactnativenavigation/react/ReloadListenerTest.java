package com.reactnativenavigation.react;

import com.reactnativenavigation.BaseTest;

import org.junit.Test;
import org.mockito.Mockito;

public class ReloadListenerTest extends BaseTest {
    private ReloadHandler uut;
    private Runnable handler;

    @Override
    public void beforeEach() {
        handler = Mockito.mock(Runnable.class);
        uut = new ReloadHandler();
    }

    @Test
    public void onSuccess_viewsAreDestroyed() {
        uut.setOnReloadListener(handler);
        uut.onSuccess();
        Mockito.verify(handler).run();
    }
}

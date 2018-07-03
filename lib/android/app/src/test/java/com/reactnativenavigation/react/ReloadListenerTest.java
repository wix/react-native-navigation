package com.reactnativenavigation.react;

import com.facebook.react.devsupport.interfaces.DevBundleDownloadListener;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.viewcontrollers.Navigator;

import org.junit.Test;
import org.mockito.Mockito;

public class ReloadListenerTest extends BaseTest {
    private DevBundleDownloadListener uut;
    private Navigator navigator;

    @Override
    public void beforeEach() {
        navigator = Mockito.mock(Navigator.class);
        uut = new ReloadListener(navigator);
    }

    @Test
    public void onSuccess_viewsAreDestroyed() {
        uut.onSuccess();
        Mockito.verify(navigator).destroyViews();
    }
}

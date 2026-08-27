package com.reactnativenavigation.react;

import android.app.Activity;
import android.content.Intent;
import com.facebook.react.devsupport.interfaces.DevSupportManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {28, 33, 34}, manifest = Config.NONE)
public class JsDevReloadHandlerTest {
    private Activity activity;
    private DevSupportManager devSupportManager;
    private JsDevReloadHandler handler;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(Activity.class).setup().get();
        devSupportManager = mock(DevSupportManager.class);
        handler = new JsDevReloadHandler(devSupportManager);
    }

    @Test
    public void resumedActivityReceivesDevelopmentReloadBroadcast() {
        handler.onActivityResumed(activity);
        activity.sendBroadcast(new Intent("com.reactnativenavigation.broadcast.RELOAD"));
        ShadowLooper.idleMainLooper();

        verify(devSupportManager).handleReloadJS();
        handler.onActivityPaused(activity);
    }

    @Test
    public void pausedActivityNoLongerReceivesReloadBroadcast() {
        handler.onActivityResumed(activity);
        handler.onActivityPaused(activity);
        activity.sendBroadcast(new Intent("com.reactnativenavigation.broadcast.RELOAD"));
        ShadowLooper.idleMainLooper();

        verify(devSupportManager, never()).handleReloadJS();
    }

    @Test
    public void removingListenerDoesNotCrashPendingReloadCallbacks() {
        JsDevReloadHandler.ReloadListener listener = mock(JsDevReloadHandler.ReloadListener.class);
        handler.setReloadListener(listener);
        handler.removeReloadListener(listener);
        handler.onSuccess();
        handler.onActivityResumed(activity);
        activity.sendBroadcast(new Intent("com.reactnativenavigation.broadcast.RELOAD"));
        ShadowLooper.idleMainLooper();

        verify(listener, never()).onReload();
        verify(devSupportManager).handleReloadJS();
        handler.onActivityPaused(activity);
    }
}

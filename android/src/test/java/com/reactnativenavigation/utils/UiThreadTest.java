package com.reactnativenavigation.utils;

import com.reactnativenavigation.*;

import org.junit.*;
import org.robolectric.shadows.*;

import static org.mockito.Mockito.*;


public class UiThreadTest extends BaseRobolectricTest {
    @Test
    public void postOnUiThread() {
        Runnable task = mock(Runnable.class);
        ShadowLooper.pauseMainLooper();
        UiThread.post(task);
        verifyNoInteractions(task);
        ShadowLooper.runUiThreadTasks();
        verify(task, times(1)).run();
    }

    @Test
    public void postDelayedOnUiThread() {
        Runnable task = mock(Runnable.class);
        UiThread.postDelayed(task, 1000);
        verifyNoInteractions(task);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        verify(task, times(1)).run();
    }
}

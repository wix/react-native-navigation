package com.reactnativenavigation.utils;


import com.reactnativenavigation.*;

import org.junit.*;

import static org.mockito.Mockito.*;

import android.view.View;

public class UiUtilsTest extends BaseRobolectricTest {
    @Test
    public void runOnPreDrawOnce() {
        View view = new View(getContext());
        Runnable task = mock(Runnable.class);
        verifyNoInteractions(task);

        UiUtils.runOnPreDrawOnce(view, task);
        view.getViewTreeObserver().dispatchOnPreDraw();
        verify(task, times(1)).run();
    }
}

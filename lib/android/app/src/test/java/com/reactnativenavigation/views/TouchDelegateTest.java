package com.reactnativenavigation.views;

import android.view.MotionEvent;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleOverlay;
import com.reactnativenavigation.views.touch.OverlayTouchDelegate;

import org.junit.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TouchDelegateTest extends BaseTest {
    private OverlayTouchDelegate uut;
    private final int x = 10;
    private final int y = 10;
    private final MotionEvent downEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, x, y, 0);
    private final MotionEvent upEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, x, y, 0);

    @Override
    public void beforeEach() {
        super.beforeEach();
        uut = spy(new OverlayTouchDelegate(new SimpleOverlay(newActivity())));
    }

    @Test
    public void downEventIsHandled() throws Exception {
        uut.setInterceptTouchOutside(true);
        uut.onInterceptTouchEvent(downEvent);
        verify(uut, times(1)).handleDown(downEvent);
    }

    @Test
    public void onlyDownEventIsHandled() throws Exception {
        uut.setInterceptTouchOutside(true);
        uut.onInterceptTouchEvent(upEvent);
        verify(uut, times(0)).handleDown(upEvent);
    }

    @Test
    public void downIsHandledOnlyIfInterceptTouchOutsideIsTrue() throws Exception {
        uut.onInterceptTouchEvent(downEvent);
        verify(uut, times(0)).handleDown(downEvent);
    }
}

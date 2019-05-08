package com.reactnativenavigation.views;

import android.view.MotionEvent;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleOverlay;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.views.touch.OverlayTouchDelegate;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TouchDelegateTest extends BaseTest {
    private OverlayTouchDelegate uut;
    private final int x = 10;
    private final int y = 10;
    private final MotionEvent downEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, x, y, 0);
    private final MotionEvent upEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, x, y, 0);
    private SimpleOverlay reactView;

    @Override
    public void beforeEach() {
        super.beforeEach();
        reactView = spy(new SimpleOverlay(newActivity()));
        uut = spy(new OverlayTouchDelegate(reactView));
    }

    @Test
    public void downEventIsHandled() {
        uut.setInterceptTouchOutside(new Bool(true));
        uut.onInterceptTouchEvent(downEvent);
        verify(uut, times(1)).handleDown(downEvent);
    }

    @Test
    public void onlyDownEventIsHandled() {
        uut.setInterceptTouchOutside(new Bool(true));
        uut.onInterceptTouchEvent(upEvent);
        verify(uut, times(0)).handleDown(upEvent);
    }

    @Test
    public void nonDownEventsDontIntercept() {
        uut.setInterceptTouchOutside(new Bool(true));
        assertThat(uut.onInterceptTouchEvent(upEvent)).isFalse();
    }

    @Test
    public void nonDownEventsDispatchTouchEventsToJs() {
        uut.setInterceptTouchOutside(new Bool(true));
        uut.onInterceptTouchEvent(upEvent);
        verify(reactView, times(1)).dispatchTouchEventToJs(upEvent);
    }
}

package com.reactnativenavigation.views;

import android.view.MotionEvent;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.views.component.ComponentLayout;
import com.reactnativenavigation.views.touch.OverlayTouchDelegate;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OverlayTouchDelegateTest extends BaseTest {
    private OverlayTouchDelegate uut;
    private final int x = 10;
    private final int y = 10;
    private final MotionEvent downEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, x, y, 0);
    private final MotionEvent upEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, x, y, 0);
    private ComponentLayout component;

    @Override
    public void beforeEach() {
        ReactView reactView = mock(ReactView.class);
        component = mock(ComponentLayout.class);
        uut = spy(new OverlayTouchDelegate(component, reactView));
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
    public void nonDownEventsInvokeSuperImplementation() {
        uut.setInterceptTouchOutside(new Bool(true));
        uut.onInterceptTouchEvent(upEvent);
        verify(component).superOnInterceptTouchEvent(upEvent);
    }
}

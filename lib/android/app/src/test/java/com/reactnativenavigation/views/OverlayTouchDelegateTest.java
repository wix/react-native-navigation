package com.reactnativenavigation.views;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.views.debuggingoverlay.DebuggingOverlay;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.views.component.ComponentLayout;
import com.reactnativenavigation.views.touch.OverlayTouchDelegate;

import org.junit.Test;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OverlayTouchDelegateTest extends BaseTest {
    private OverlayTouchDelegate uut;
    private final int x = 10;
    private final int y = 10;
    private final MotionEvent downEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, x, y, 0);
    private final MotionEvent upEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, x, y, 0);
    private ComponentLayout component;
    private ReactView reactView;

    @Override
    public void beforeEach() {
        reactView = mock(ReactView.class);
        component = mock(ComponentLayout.class);
        uut = spy(new OverlayTouchDelegate(component, reactView));
    }

    private void mockHierarchyWithDebuggingOverlay() {
        // Mock the hierarchy: ReactView -> ReactSurfaceView -> ReactViewGroup(s)
        ViewGroup reactSurfaceView = mock(ViewGroup.class);
        ViewGroup debuggingOverlayContainer = mock(ViewGroup.class);
        ViewGroup contentViewGroup = mock(ViewGroup.class);
        DebuggingOverlay debuggingOverlay = mock(DebuggingOverlay.class);

        // Set up ReactView -> ReactSurfaceView
        when(reactView.getChildAt(0)).thenReturn(reactSurfaceView);
        when(reactView.getChildCount()).thenReturn(1);

        // Set up ReactSurfaceView -> ReactViewGroup(s)
        // First child: ViewGroup with DebuggingOverlay (should be skipped)
        when(reactSurfaceView.getChildAt(0)).thenReturn(debuggingOverlayContainer);
        when(reactSurfaceView.getChildAt(1)).thenReturn(contentViewGroup);
        when(reactSurfaceView.getChildCount()).thenReturn(2);

        // Set up debuggingOverlayContainer: has DebuggingOverlay as first child
        when(debuggingOverlayContainer.getChildAt(0)).thenReturn(debuggingOverlay);

        // Set up contentViewGroup: not a DebuggingOverlay, visible, and coordinates
        // inside
        when(contentViewGroup.getChildAt(0)).thenReturn(null); // Not a DebuggingOverlay
        when(contentViewGroup.getVisibility()).thenReturn(View.VISIBLE); // For isVisible extension

        // Set up getHitRect for coordinatesInsideView to work
        Rect hitRect = new Rect(0, 0, 100, 100);
        doAnswer((Answer<Void>) invocation -> {
            Rect rect = invocation.getArgument(0);
            rect.set(hitRect);
            return null;
        }).when(contentViewGroup).getHitRect(any(Rect.class));

        // Also mock getHitRect for debuggingOverlayContainer (though it should be
        // skipped)
        doAnswer((Answer<Void>) invocation -> {
            Rect rect = invocation.getArgument(0);
            rect.set(new Rect(0, 0, 100, 100));
            return null;
        }).when(debuggingOverlayContainer).getHitRect(any(Rect.class));
    }

    @Test
    public void downEventIsHandled() {
        mockHierarchyWithDebuggingOverlay();
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

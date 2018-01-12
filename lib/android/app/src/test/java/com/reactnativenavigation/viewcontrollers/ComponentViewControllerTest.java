package com.reactnativenavigation.viewcontrollers;

import android.app.*;

import com.reactnativenavigation.*;
import com.reactnativenavigation.mocks.*;
import com.reactnativenavigation.parse.*;

import org.junit.*;

import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.Mockito.*;

public class ComponentViewControllerTest extends BaseTest {
    private ComponentViewController uut;
    private ComponentViewController.IReactView view;

    @Override
    public void beforeEach() {
        super.beforeEach();
        Activity activity = newActivity();
        view = spy(new TestComponentLayout(activity));
        uut = new ComponentViewController(activity, "componentId1", "componentName", new ComponentViewController.ReactViewCreator() {
            @Override
            public ComponentViewController.IReactView create(final Activity activity1, final String componentId, final String componentName) {
                return view;
            }
        }, new Options());
    }

    @Test
    public void createsViewFromComponentViewCreator() throws Exception {
        assertThat(uut.getView()).isSameAs(view);
    }

    @Test
    public void componentViewDestroyedOnDestroy() throws Exception {
        uut.ensureViewIsCreated();
        verify(view, times(0)).destroy();
        uut.destroy();
        verify(view, times(1)).destroy();
    }

    @Test
    public void lifecycleMethodsSentToComponentView() throws Exception {
        uut.ensureViewIsCreated();
        verify(view, times(0)).sendComponentStart();
        verify(view, times(0)).sendComponentStop();
        uut.onViewAppeared();
        verify(view, times(1)).sendComponentStart();
        verify(view, times(0)).sendComponentStop();
        uut.onViewDisappear();
        verify(view, times(1)).sendComponentStart();
        verify(view, times(1)).sendComponentStop();
    }

    @Test
    public void isViewShownOnlyIfComponentViewIsReady() throws Exception {
        assertThat(uut.isViewShown()).isFalse();
        uut.ensureViewIsCreated();
        when(view.asView().isShown()).thenReturn(true);
        assertThat(uut.isViewShown()).isFalse();
        when(view.isReady()).thenReturn(true);
        assertThat(uut.isViewShown()).isTrue();
    }
}

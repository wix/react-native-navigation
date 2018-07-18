package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.TestComponentLayout;
import com.reactnativenavigation.mocks.TestReactView;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.views.StackLayout;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ComponentViewControllerTest extends BaseTest {
    private ComponentViewController uut;
    private IReactView view;

    @Override
    public void beforeEach() {
        super.beforeEach();
        Activity activity = newActivity();
        view = spy(new TestComponentLayout(activity, new TestReactView(activity)));
        ParentController<StackLayout> parentController = TestUtils.newStackController(activity).build();
        OptionsPresenter presenter = new OptionsPresenter(activity, new Options());
        uut = new ComponentViewController(activity, new ChildControllersRegistry(), "componentId1", "componentName", (activity1, componentId, componentName) -> view, new Options(), presenter);
        uut.setParentController(parentController);
        parentController.ensureViewIsCreated();
    }

    @Test
    public void createsViewFromComponentViewCreator() {
        assertThat(uut.getView()).isSameAs(view);
    }

    @Test
    public void componentViewDestroyedOnDestroy() {
        uut.ensureViewIsCreated();
        verify(view, times(0)).destroy();
        uut.onViewAppeared();
        uut.destroy();
        verify(view, times(1)).destroy();
    }

    @Test
    public void lifecycleMethodsSentToComponentView() {
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
    public void isViewShownOnlyIfComponentViewIsReady() {
        assertThat(uut.isViewShown()).isFalse();
        uut.ensureViewIsCreated();
        when(view.asView().isShown()).thenReturn(true);
        assertThat(uut.isViewShown()).isFalse();
        when(view.isReady()).thenReturn(true);
        assertThat(uut.isViewShown()).isTrue();
    }

    @Test
    public void onNavigationButtonPressInvokedOnReactComponent() {
        uut.ensureViewIsCreated();
        uut.sendOnNavigationButtonPressed("btn1");
        verify(view, times(1)).sendOnNavigationButtonPressed("btn1");
    }
}

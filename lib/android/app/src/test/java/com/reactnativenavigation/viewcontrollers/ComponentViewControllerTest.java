package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.TestComponentLayout;
import com.reactnativenavigation.mocks.TestReactView;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.presentation.ComponentPresenter;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.utils.StatusBarUtils;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.views.ComponentLayout;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ComponentViewControllerTest extends BaseTest {
    private ComponentViewController uut;
    private ComponentLayout view;
    private ComponentPresenter presenter;
    private Options resolvedOptions = new Options();
    private StackController parent;
    private Activity activity;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        StatusBarUtils.saveStatusBarHeight(63);
        view = spy(new TestComponentLayout(activity, new TestReactView(activity)));
        parent = TestUtils.newStackController(activity).build();
        Presenter presenter = new Presenter(activity, new Options());
        this.presenter = spy(new ComponentPresenter(Options.EMPTY));
        uut = spy(new ComponentViewController(activity, new ChildControllersRegistry(), "componentId1", "componentName", (activity1, componentId, componentName) -> view, new Options(), presenter, this.presenter) {
            @Override
            public Options resolveCurrentOptions(Options defaultOptions) {
                return resolvedOptions;
            }
        });
        uut.setParentController(parent);
        parent.ensureViewIsCreated();
    }

    @Test
    public void setDefaultOptions() {
        Options defaultOptions = new Options();
        uut.setDefaultOptions(defaultOptions);
        verify(presenter).setDefaultOptions(defaultOptions);
    }

    @Test
    public void applyOptions() {
        Options options = new Options();
        uut.applyOptions(options);
        verify(view).applyOptions(options);
        verify(presenter).applyOptions(view, resolvedOptions);
    }

    @Test
    public void createsViewFromComponentViewCreator() {
        assertThat(uut.getView()).isSameAs(view);
    }

    @Test
    public void componentViewDestroyedOnDestroy() {
        uut.ensureViewIsCreated();
        verify(view, times(0)).destroy();
        uut.onViewWillAppear();
        uut.destroy();
        verify(view, times(1)).destroy();
    }

    @Test
    public void lifecycleMethodsSentToComponentView() {
        uut.ensureViewIsCreated();
        verify(view, times(0)).sendComponentStart();
        verify(view, times(0)).sendComponentStop();
        uut.onViewWillAppear();
        verify(view, times(0)).sendComponentStart();
        verify(view, times(0)).sendComponentStop();
        uut.onViewDidAppear();
        verify(view, times(1)).sendComponentStart();
        verify(view, times(0)).sendComponentStop();
        uut.onViewDisappear();
        verify(view, times(1)).sendComponentStart();
        verify(view, times(1)).sendComponentStop();
    }

    @Test
    public void onViewDidAppear_componentStartIsEmittedOnlyIfComponentIsNotAppeared() {
        uut.ensureViewIsCreated();

        uut.onViewDidAppear();
        verify(view).sendComponentStart();

        uut.onViewDidAppear();
        verify(view).sendComponentStart();

        uut.onViewDisappear();
        uut.onViewDidAppear();
        verify(view, times(2)).sendComponentStart();
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

    @Test
    public void mergeOptions_emptyOptionsAreIgnored() {
        ComponentViewController spy = spy(uut);
        spy.mergeOptions(Options.EMPTY);
        verify(spy, times(0)).performOnParentController(any());
    }

    @Test
    public void mergeOptions_delegatesToPresenterIfViewIsNotShown() {
        Options options = new Options();
        assertThat(uut.isViewShown()).isFalse();
        uut.mergeOptions(options);
        verifyZeroInteractions(presenter);

        when(uut.isViewShown()).thenReturn(true);
        uut.mergeOptions(options);
        verify(presenter).mergeOptions(uut.getView(), options);
    }

    @Test
    public void applyTopInset_delegatesToPresenter() {
        addToParent(activity, uut);
        uut.applyTopInset();
        verify(presenter).applyTopInsets(uut.getView(), uut.getTopInset());
    }

    @Test
    public void getTopInset_returnsStatusBarHeight() {
        //noinspection ConstantConditions
        uut.setParentController(null);
        assertThat(uut.getTopInset()).isEqualTo(StatusBarUtils.getStatusBarHeight(activity));
    }

    @Test
    public void getTopInset_resolveWithParent() {
        assertThat(uut.getTopInset()).isEqualTo(StatusBarUtils.getStatusBarHeight(activity) + parent.getTopInset(uut));
    }

    @Test
    public void getTopInset_drawBehind() {
        uut.options.statusBar.drawBehind = new Bool(true);
        uut.options.topBar.drawBehind = new Bool(true);
        assertThat(uut.getTopInset()).isEqualTo(0);
    }

    @Test
    public void applyBottomInset_delegatesToPresenter() {
        addToParent(activity, uut);
        uut.applyBottomInset();
        verify(presenter).applyBottomInset(uut.getView(), uut.getBottomInset());
    }
}

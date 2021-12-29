package com.reactnativenavigation.viewcontrollers.component;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.mocks.TestComponentLayout;
import com.reactnativenavigation.mocks.TestReactView;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.utils.SystemUiUtils;
import com.reactnativenavigation.views.component.ComponentLayout;

import org.assertj.core.api.Java6Assertions;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

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
        SystemUiUtils.saveStatusBarHeight(63);
        view = Mockito.spy(new TestComponentLayout(activity, new TestReactView(activity)));
        parent = TestUtils.newStackController(activity).build();
        Presenter presenter = new Presenter(activity, new Options());
        this.presenter = Mockito.spy(new ComponentPresenter(Options.EMPTY));
        uut = Mockito.spy(new ComponentViewController(activity, new ChildControllersRegistry(), "componentId1", "componentName", (activity1, componentId, componentName) -> view, new Options(), presenter, this.presenter) {
            @Override
            public Options resolveCurrentOptions(Options defaultOptions) {
                // Hacky way to return the same instance of resolvedOptions without copying it.
                return resolvedOptions
                        .withDefaultOptions(uut.options)
                        .withDefaultOptions(defaultOptions);
            }
        });
        uut.setParentController(parent);
        parent.ensureViewIsCreated();
    }

    @Test
    public void setDefaultOptions() {
        Options defaultOptions = new Options();
        uut.setDefaultOptions(defaultOptions);
        Mockito.verify(presenter).setDefaultOptions(defaultOptions);
    }

    @Test
    public void applyOptions() {
        Options options = new Options();
        uut.applyOptions(options);
        Mockito.verify(view).applyOptions(options);
        Mockito.verify(presenter).applyOptions(view, resolvedOptions);
    }

    @Test
    public void createsViewFromComponentViewCreator() {
        Java6Assertions.assertThat(uut.getView()).isSameAs(view);
    }

    @Test
    public void componentViewDestroyedOnDestroy() {
        uut.ensureViewIsCreated();
        Mockito.verify(view, Mockito.times(0)).destroy();
        uut.onViewWillAppear();
        uut.destroy();
        Mockito.verify(view, Mockito.times(1)).destroy();
    }

    @Test
    public void lifecycleMethodsSentToComponentView() {
        uut.ensureViewIsCreated();
        Mockito.verify(view, Mockito.times(0)).sendComponentStart();
        Mockito.verify(view, Mockito.times(0)).sendComponentStop();
        uut.onViewWillAppear();
        Mockito.verify(view, Mockito.times(0)).sendComponentStart();
        Mockito.verify(view, Mockito.times(0)).sendComponentStop();
        uut.onViewDidAppear();
        Mockito.verify(view, Mockito.times(1)).sendComponentStart();
        Mockito.verify(view, Mockito.times(0)).sendComponentStop();
        uut.onViewDisappear();
        Mockito.verify(view, Mockito.times(1)).sendComponentStart();
        Mockito.verify(view, Mockito.times(1)).sendComponentStop();
    }

    @Test
    public void shouldNotSendDidDisappearAboutDisappearedView() {
        uut.ensureViewIsCreated();
        uut.onViewDisappear();
        Mockito.verify(view, Mockito.times(0)).sendComponentStart();
        Mockito.verify(view, Mockito.times(0)).sendComponentStop();

        uut.onViewWillAppear();
        uut.onViewDidAppear();
        uut.onViewDisappear();
        Mockito.verify(view, Mockito.times(1)).sendComponentStart();
        Mockito.verify(view, Mockito.times(1)).sendComponentStop();

        uut.onViewDisappear();
        Mockito.verify(view, Mockito.times(1)).sendComponentStart();
        Mockito.verify(view, Mockito.times(1)).sendComponentStop();
    }

    @Test
    public void onViewDidAppear_componentStartIsEmittedOnlyIfComponentIsNotAppeared() {
        uut.ensureViewIsCreated();

        uut.onViewDidAppear();
        Mockito.verify(view).sendComponentStart();

        uut.onViewDidAppear();
        Mockito.verify(view).sendComponentStart();

        uut.onViewDisappear();
        uut.onViewDidAppear();
        Mockito.verify(view, Mockito.times(2)).sendComponentStart();
    }

    @Test
    public void shouldCallApplyWindowInsetsWhenViewFullyAppeared(){
        uut.ensureViewIsCreated();
        uut.onViewDidAppear();
        Mockito.verify(view).requestApplyInsets();
    }

    @Test
    public void isViewShownOnlyIfComponentViewIsReady() {
        Java6Assertions.assertThat(uut.isViewShown()).isFalse();
        uut.ensureViewIsCreated();
        Mockito.when(view.asView().isShown()).thenReturn(true);
        Java6Assertions.assertThat(uut.isViewShown()).isFalse();
        Mockito.when(view.isReady()).thenReturn(true);
        Java6Assertions.assertThat(uut.isViewShown()).isTrue();
    }

    @Test
    public void onNavigationButtonPressInvokedOnReactComponent() {
        uut.ensureViewIsCreated();
        uut.sendOnNavigationButtonPressed("btn1");
        Mockito.verify(view, Mockito.times(1)).sendOnNavigationButtonPressed("btn1");
    }

    @Test
    public void mergeOptions_emptyOptionsAreIgnored() {
        ComponentViewController spy = Mockito.spy(uut);
        spy.mergeOptions(Options.EMPTY);
        Mockito.verify(spy, Mockito.times(0)).performOnParentController(ArgumentMatchers.any());
    }

    @Test
    public void mergeOptions_delegatesToPresenterIfViewIsNotShown() {
        Options options = new Options();
        Java6Assertions.assertThat(uut.isViewShown()).isFalse();
        uut.mergeOptions(options);
        Mockito.verifyNoInteractions(presenter);

        Mockito.when(uut.isViewShown()).thenReturn(true);
        uut.mergeOptions(options);
        Mockito.verify(presenter).mergeOptions(uut.getView(), options);
    }

    @Test
    public void applyTopInset_delegatesToPresenter() {
        addToParent(activity, uut);
        uut.applyTopInset();
        Mockito.verify(presenter).applyTopInsets(uut.getView(), uut.getTopInset());
    }

    @Test
    public void getTopInset_returnsStatusBarHeight() {
        //noinspection ConstantConditions
        uut.setParentController(null);
        Java6Assertions.assertThat(uut.getTopInset()).isEqualTo(SystemUiUtils.getStatusBarHeight(activity));
    }

    @Test
    public void getTopInset_resolveWithParent() {
        Java6Assertions
                .assertThat(uut.getTopInset()).isEqualTo(SystemUiUtils.getStatusBarHeight(activity) + parent.getTopInset(uut));
    }

    @Test
    public void getTopInset_drawBehind() {
        uut.options.statusBar.drawBehind = new Bool(true);
        uut.options.topBar.drawBehind = new Bool(true);
        Java6Assertions.assertThat(uut.getTopInset()).isEqualTo(0);
    }

    @Test
    public void getTopInset_drawBehind_defaultOptions() {
        Options defaultOptions = new Options();
        defaultOptions.statusBar.drawBehind = new Bool(true);
        uut.setDefaultOptions(defaultOptions);

        uut.options.topBar.drawBehind = new Bool(true);
        Java6Assertions.assertThat(uut.getTopInset()).isEqualTo(0);
    }

    @Test
    public void applyBottomInset_delegatesToPresenter() {
        addToParent(activity, uut);
        uut.applyBottomInset();
        Mockito.verify(presenter).applyBottomInset(uut.getView(), uut.getBottomInset());
    }
}

package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;

import org.assertj.android.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Shadows;

import java.lang.reflect.Field;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ViewControllerTest extends BaseTest {

    private ViewController uut;
    private Activity activity;
    private ChildControllersRegistry childRegistry;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        uut = new SimpleViewController(activity, childRegistry, "uut", new Options());
        uut.setParentController(Mockito.mock(ParentController.class));
    }

    @Test
    public void holdsAView() {
        assertThat(uut.getView()).isNotNull().isInstanceOf(View.class);
    }

    @Test
    public void holdsARefToActivity() {
        assertThat(uut.getActivity()).isNotNull().isEqualTo(activity);
    }

    @Test
    public void canOverrideViewCreation() {
        final FrameLayout otherView = new FrameLayout(activity);
        ViewController myController = new ViewController(activity, "vc", new Options()) {
            @Override
            protected FrameLayout createView() {
                return otherView;
            }

            @Override
            public void sendOnNavigationButtonPressed(String buttonId) {

            }
        };
        assertThat(myController.getView()).isEqualTo(otherView);
    }

    @Test
    public void holdsAReferenceToStackControllerOrNull() {
        //noinspection ConstantConditions
        uut.setParentController(null);

        assertThat(uut.getParentController()).isNull();
        StackController nav = new StackControllerBuilder(activity)
                .setTopBarButtonCreator(new TopBarButtonCreatorMock())
                .setTitleBarReactViewCreator(new TitleBarReactViewCreatorMock())
                .setTopBarBackgroundViewController(new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()))
                .setTopBarController(new TopBarController())
                .setId("stack")
                .setInitialOptions(new Options())
                .createStackController();
        nav.push(uut, new CommandListenerAdapter());
        assertThat(uut.getParentController()).isEqualTo(nav);
    }

    @Test
    public void handleBackDefaultFalse() {
        assertThat(uut.handleBack(new CommandListenerAdapter())).isFalse();
    }

    @Test
    public void holdsId() {
        assertThat(uut.getId()).isEqualTo("uut");
    }

    @Test
    public void isSameId() {
        assertThat(uut.isSameId("")).isFalse();
        assertThat(uut.isSameId(null)).isFalse();
        assertThat(uut.isSameId("uut")).isTrue();
    }

    @Test
    public void findControllerById_SelfOrNull() {
        assertThat(uut.findControllerById("456")).isNull();
        assertThat(uut.findControllerById("uut")).isEqualTo(uut);
    }

    @Test
    public void onAppear_WhenShown() {
        ViewController spy = spy(uut);
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        Assertions.assertThat(spy.getView()).isNotShown();
        verify(spy, times(0)).onViewAppeared();

        Shadows.shadowOf(spy.getView()).setMyParent(mock(ViewParent.class));
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        Assertions.assertThat(spy.getView()).isShown();

        verify(spy, times(1)).onViewAppeared();
    }

    @Test
    public void onAppear_CalledAtMostOnce() {
        ViewController spy = spy(uut);
        Shadows.shadowOf(spy.getView()).setMyParent(mock(ViewParent.class));
        Assertions.assertThat(spy.getView()).isShown();
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();

        verify(spy, times(1)).onViewAppeared();
    }

    @Test
    public void onDisappear_WhenNotShown_AfterOnAppearWasCalled() {
        ViewController spy = spy(uut);
        Shadows.shadowOf(spy.getView()).setMyParent(mock(ViewParent.class));
        Assertions.assertThat(spy.getView()).isShown();
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        verify(spy, times(1)).onViewAppeared();
        verify(spy, times(0)).onViewDisappear();

        spy.getView().setVisibility(View.GONE);
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        Assertions.assertThat(spy.getView()).isNotShown();
        verify(spy, times(1)).onViewDisappear();
    }

    @Test
    public void onDisappear_CalledAtMostOnce() {
        ViewController spy = spy(uut);
        Shadows.shadowOf(spy.getView()).setMyParent(mock(ViewParent.class));
        Assertions.assertThat(spy.getView()).isShown();
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        spy.getView().setVisibility(View.GONE);
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        verify(spy, times(1)).onViewDisappear();
    }

    @Test
    public void onDestroy_RemovesGlobalLayoutListener() throws Exception {
        new SimpleViewController(activity, childRegistry, "ensureNotNull", new Options()).destroy();

        ViewController spy = spy(uut);
        View view = spy.getView();
        Shadows.shadowOf(view).setMyParent(mock(ViewParent.class));

        spy.destroy();

        Assertions.assertThat(view).isShown();
        view.getViewTreeObserver().dispatchOnGlobalLayout();
        verify(spy, times(0)).onViewAppeared();
        verify(spy, times(0)).onViewDisappear();

        Field field = ViewController.class.getDeclaredField("view");
        field.setAccessible(true);
        assertThat(field.get(spy)).isNull();
    }

    @Test
    public void onDestroy_CallsOnDisappearIfNeeded() {
        ViewController spy = spy(uut);
        Shadows.shadowOf(spy.getView()).setMyParent(mock(ViewParent.class));
        Assertions.assertThat(spy.getView()).isShown();
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        verify(spy, times(1)).onViewAppeared();

        spy.destroy();

        verify(spy, times(1)).onViewDisappear();
    }

    @Test
    public void onDestroy_RemovesSelfFromParentIfExists() {
        LinearLayout parent = new LinearLayout(activity);
        parent.addView(uut.getView());

        uut.destroy();
        assertThat(parent.getChildCount()).withFailMessage("expected not to have children").isZero();
    }

    @Test
    public void ensureViewIsCreated() {
        ViewController spy = spy(uut);
        verify(spy, times(0)).getView();
        spy.ensureViewIsCreated();
        verify(spy, times(1)).getView();
    }
}


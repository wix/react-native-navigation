package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.MockPromise;
import com.reactnativenavigation.mocks.SimpleViewController;

import org.assertj.android.api.Assertions;
import org.junit.Test;
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

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        uut = new SimpleViewController(activity, "uut");
    }

    @Test
    public void holdsAView() throws Exception {
        assertThat(uut.getView()).isNotNull().isInstanceOf(View.class);
    }

    @Test
    public void holdsARefToActivity() throws Exception {
        assertThat(uut.getActivity()).isNotNull().isEqualTo(activity);
    }

    @Test
    public void canOverrideViewCreation() throws Exception {
        final FrameLayout otherView = new FrameLayout(activity);
        ViewController myController = new ViewController(activity, "vc") {
            @Override
            protected FrameLayout createView() {
                return otherView;
            }
        };
        assertThat(myController.getView()).isEqualTo(otherView);
    }

    @Test
    public void holdsAReferenceToStackControllerOrNull() throws Exception {
        assertThat(uut.getParentController()).isNull();
        StackController nav = new StackController(activity, "stack");
        nav.animatePush(uut, new MockPromise());
        assertThat(uut.getParentController()).isEqualTo(nav);
    }

    @Test
    public void handleBackDefaultFalse() throws Exception {
        assertThat(uut.handleBack()).isFalse();
    }

    @Test
    public void holdsId() throws Exception {
        assertThat(uut.getId()).isEqualTo("uut");
    }

    @Test
    public void isSameId() throws Exception {
        assertThat(uut.isSameId("")).isFalse();
        assertThat(uut.isSameId(null)).isFalse();
        assertThat(uut.isSameId("uut")).isTrue();
    }

    @Test
    public void findControllerById_SelfOrNull() throws Exception {
        assertThat(uut.findControllerById("456")).isNull();
        assertThat(uut.findControllerById("uut")).isEqualTo(uut);
    }

    @Test
    public void onAppear_WhenShown() throws Exception {
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
    public void onAppear_CalledAtMostOnce() throws Exception {
        ViewController spy = spy(uut);
        Shadows.shadowOf(spy.getView()).setMyParent(mock(ViewParent.class));
        Assertions.assertThat(spy.getView()).isShown();
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();

        verify(spy, times(1)).onViewAppeared();
    }

    @Test
    public void onDisappear_WhenNotShown_AfterOnAppearWasCalled() throws Exception {
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
    public void onDisappear_CalledAtMostOnce() throws Exception {
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
        new SimpleViewController(activity, "ensureNotNull").destroy();

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
    public void onDestroy_CallsOnDisappearIfNeeded() throws Exception {
        ViewController spy = spy(uut);
        Shadows.shadowOf(spy.getView()).setMyParent(mock(ViewParent.class));
        Assertions.assertThat(spy.getView()).isShown();
        spy.getView().getViewTreeObserver().dispatchOnGlobalLayout();
        verify(spy, times(1)).onViewAppeared();

        spy.destroy();

        verify(spy, times(1)).onViewDisappear();
    }

    @Test
    public void assignsIdToCreatedView() throws Exception {
        assertThat(uut.getView().getId()).isPositive();
    }

    @Test
    public void onDestroy_RemovesSelfFromParentIfExists() throws Exception {
        LinearLayout parent = new LinearLayout(activity);
        parent.addView(uut.getView());

        uut.destroy();
        assertThat(parent.getChildCount()).withFailMessage("expected not to have children").isZero();
    }

    @Test
    public void ensureViewIsCreated() throws Exception {
        ViewController spy = spy(uut);
        verify(spy, times(0)).getView();
        spy.ensureViewIsCreated();
        verify(spy, times(1)).getView();
    }
}


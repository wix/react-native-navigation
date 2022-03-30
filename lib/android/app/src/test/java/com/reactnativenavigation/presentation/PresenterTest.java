package com.reactnativenavigation.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.Mocks;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.layout.LayoutInsets;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.utils.SystemUiUtils;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import org.junit.Test;
import org.mockito.Mockito;

public class PresenterTest extends BaseTest {
    private Presenter uut;
    private Activity activity;
    private ViewController<ViewGroup> controller;
    private ViewController parentController;
    private ViewGroup parentView;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        controller = mock(ViewController.class);
        parentView = mock(ViewGroup.class);
        parentController = Mocks.INSTANCE.parentController(null, parentView);
        controller.setParentController((ParentController) parentController);
        Mockito.when(controller.getTopMostParent()).thenReturn(parentController);
        uut = new Presenter(activity, Options.EMPTY);
    }

    @Test
    public void mergeStatusBarVisible_callsShowHide() {
        mockSystemUiUtils(1,1,(mockedStatic)->{
            ViewGroup spy = spy(new FrameLayout(activity));
            Mockito.when(controller.getView()).thenReturn(spy);
            Mockito.when(controller.resolveCurrentOptions()).thenReturn(Options.EMPTY);
            Options options = new Options();
            options.statusBar.visible = new Bool(false);
            uut.mergeOptions(controller, options);
            mockedStatic.verify(
                    ()-> SystemUiUtils.hideStatusBar(any(),eq(spy)),times(1));

            options.statusBar.visible = new Bool(true);
            uut.mergeOptions(controller, options);
            mockedStatic.verify(
                    ()-> SystemUiUtils.showStatusBar(any(),eq(spy)),times(1));
        });

    }

    @Test
    public void shouldApplyInsetsOnTopMostParent(){
        final ViewGroup spy = Mockito.mock(ViewGroup.class);
        Mockito.when(spy.getLayoutParams()).thenReturn(new ViewGroup.LayoutParams(0,0));
        Mockito.when(controller.getView()).thenReturn(spy);
        Options options = new Options();
        options.layout.setInsets(new LayoutInsets(
                1,2,3,4
        ));

        uut.applyOptions(controller,options);

        verify(parentView).setPadding(2,1,4,3);
    }

    @Test
    public void shouldMergeInsetsOnTopMostParent(){
        final ViewGroup spy = Mockito.mock(ViewGroup.class);
        Mockito.when(spy.getLayoutParams()).thenReturn(new ViewGroup.LayoutParams(0,0));
        Mockito.when(controller.getView()).thenReturn(spy);
        Mockito.when(controller.resolveCurrentOptions()).thenReturn(Options.EMPTY);

        Options options = new Options();
        options.layout.setInsets(new LayoutInsets(
                1,2,3,4
        ));

        uut.mergeOptions(controller,options);

        verify(parentView).setPadding(2,1,4,3);
    }

}

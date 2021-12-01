package com.reactnativenavigation.presentation;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.utils.SystemUiUtils;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PresenterTest extends BaseTest {
    private Presenter uut;
    private Activity activity;
    private ViewController<ViewGroup> controller;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        controller = mock(ViewController.class);
        uut = new Presenter(activity, Options.EMPTY);
    }

    @Test
    public void mergeStatusBarVisible_callsShowHide() {
        mockSystemUiUtils(1,1,(mockedStatic)->{
            ViewGroup spy = Mockito.spy(new FrameLayout(activity));
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
}

package com.reactnativenavigation.presentation;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.utils.SystemUiUtils;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PresenterTest extends BaseTest {
    private Presenter uut;
    private Activity activity;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        uut = new Presenter(activity, Options.EMPTY);
    }

    @Test
    public void mergeStatusBarVisible_callsShowHide() {
        mockStatusBarUtils(1,1,(mockedStatic)->{
            ViewGroup spy = Mockito.spy(new FrameLayout(activity));
            Options options = new Options();
            options.statusBar.visible = new Bool(false);
            uut.mergeOptions(spy, options);
            mockedStatic.verify(times(1),
                    ()-> SystemUiUtils.hideStatusBar(any(),any()));

            options.statusBar.visible = new Bool(true);
            uut.mergeOptions(spy, options);
            mockedStatic.verify(times(1),
                    ()-> SystemUiUtils.showStatusBar(any(),any()));
        });

    }
}

package com.reactnativenavigation.presentation;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

public class PresenterTest extends BaseTest {
    private Presenter uut;
    private Activity activity;

    @Override
    public void beforeEach() {
        activity = newActivity();
        uut = new Presenter(activity, Options.EMPTY);
    }

    @Test
    public void mergeStatusBarVisible_requestLayout() {
        ViewGroup spy = Mockito.spy(new FrameLayout(activity));
        Options options = new Options();
        options.statusBar.visible = new Bool(false);

        uut.mergeOptions(spy, options);
        verify(spy).requestLayout();

        // requested only if needed
        uut.mergeOptions(spy, options);
        verify(spy).requestLayout();
    }
}

package com.reactnativenavigation.viewcontrollers.bottomtabs;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.presentation.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.*;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BottomTabsAttacherTest extends BaseTest {

    private BottomTabsAttacher uut;
    private AttachMode mode;

    @Override
    public void beforeEach() {
        mode = Mockito.mock(AttachMode.class);
        uut = new BottomTabsAttacher(Collections.EMPTY_LIST, Mockito.mock(BottomTabsPresenter.class));
        uut.attachStrategy = mode;
    }

    @Test
    public void attach_delegatesToStrategy() {
        uut.attach();
        verify(mode).attach();
    }

    @Test
    public void onTabSelected() {
        ViewController tab = mock(ViewController.class);
        uut.onTabSelected(tab);
        verify(mode).onTabSelected(tab);
    }

    @Test
    public void destroy_delegatesToStrategy() {
        uut.destroy();
        verify(mode).destroy();
    }
}

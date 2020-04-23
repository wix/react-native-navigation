package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.TabsAttachMode;
import com.reactnativenavigation.presentation.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.ViewController;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BottomTabsAttacherTest extends BaseTest {

    private BottomTabsAttacher uut;
    private AttachMode mode;
    private Options defaultOptions = new Options();
    private List<ViewController> tabs;

    @Override
    public void beforeEach() {
        mode = Mockito.mock(AttachMode.class);
        tabs = Arrays.asList(mock(ViewController.class), mock(ViewController.class));
        uut = new BottomTabsAttacher(tabs, Mockito.mock(BottomTabsPresenter.class), defaultOptions);
        uut.attachStrategy = mode;
    }

    @Test
    public void init_defaultOptionsAreTakenIntoAccount() {
        defaultOptions.bottomTabsOptions.tabsAttachMode = TabsAttachMode.ON_SWITCH_TO_TAB;
        uut.init(Mockito.mock(ViewGroup.class), Options.EMPTY);
        assertThat(uut.attachStrategy).isInstanceOfAny(OnSwitchToTab.class);
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

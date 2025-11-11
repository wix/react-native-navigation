package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.TabsAttachMode;
import com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.BottomTabsAttacher;
import com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.modes.AttachMode;
import com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.modes.OnSwitchToTab;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

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
    private final Options defaultOptions = new Options();

    @Override
    public void beforeEach() {
        mode = Mockito.mock(AttachMode.class);
        List<ViewController<?>> tabs = Arrays.asList(mock(ViewController.class), mock(ViewController.class));
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
        ViewController<?> tab = mock(ViewController.class);
        uut.onTabSelected(tab);
        verify(mode).onTabSelected(tab);
    }

    @Test
    public void destroy_delegatesToStrategy() {
        uut.destroy();
        verify(mode).destroy();
    }
}

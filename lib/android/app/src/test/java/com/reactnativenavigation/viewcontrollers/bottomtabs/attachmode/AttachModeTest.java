package com.reactnativenavigation.viewcontrollers.bottomtabs.attachmode;

import android.app.*;
import android.view.*;
import android.widget.*;

import com.reactnativenavigation.*;
import com.reactnativenavigation.mocks.*;
import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.presentation.*;
import com.reactnativenavigation.viewcontrollers.*;
import com.reactnativenavigation.viewcontrollers.bottomtabs.*;

import org.junit.*;
import org.mockito.*;

import java.util.*;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

public abstract class AttachModeTest extends BaseTest {
    private static final int INITIAL_TAB = 1;

    private Activity activity;
    private ChildControllersRegistry childRegistry;
    protected ViewGroup parent;
    ViewController tab1;
    ViewController tab2;
    List<ViewController> tabs;
    protected Options options;
    protected BottomTabsPresenter presenter;
    protected AttachMode uut;

    @Override
    public void beforeEach() {
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        parent = new FrameLayout(activity);
        tabs = createTabs();
        options = new Options();
        options.bottomTabsOptions.currentTabIndex = new Number(INITIAL_TAB);
        presenter = Mockito.mock(BottomTabsPresenter.class);
    }

    @Test
    public void attach_layoutOptionsAreApplied() {
        uut.attach(tab1);
        verify(presenter).applyLayoutParamsOptions(options, tabs.indexOf(tab1));
    }

    @Test
    public void attach_initialTabIsVisible() {
        uut.attach(initialTab());
        assertThat(initialTab().getView().getVisibility()).isEqualTo(View.VISIBLE);
    }

    @Test
    public void attach_otherTabsAreInvisibleWhenAttached() {
        forEach(otherTabs(), t -> uut.attach(t));
        forEach(otherTabs(), t -> assertThat(t.getView().getVisibility()).isEqualTo(View.INVISIBLE));
    }

    ViewController[] otherTabs() {
        return filter(tabs, t -> t != initialTab()).toArray(new ViewController[0]);
    }

    ViewController initialTab() {
        return tabs.get(INITIAL_TAB);
    }

    private List<ViewController> createTabs() {
        tab1 = new SimpleViewController(activity, childRegistry, "child1", new Options());
        tab2 = spy(new SimpleViewController(activity, childRegistry, "child2", new Options()));
        ViewController tab3 = new SimpleViewController(activity, childRegistry, "child3", new Options());
        return Arrays.asList(tab1, tab2, tab3);
    }
}

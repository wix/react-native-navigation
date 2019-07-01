package com.reactnativenavigation.viewcontrollers.bottomtabs.attachmode;

import android.app.Activity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.presentation.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.AttachMode;
import com.reactnativenavigation.views.bottomtabs.BottomTabsBehaviour;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;

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
        parent = new CoordinatorLayout(activity);
        tabs = createTabs();
        options = new Options();
        options.bottomTabsOptions.currentTabIndex = new Number(INITIAL_TAB);
        presenter = Mockito.mock(BottomTabsPresenter.class);
    }

    @Test
    public void attach_layoutOptionsAreApplied() {
        uut.attach(tab1);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) tab1.getView().getLayoutParams();
        assertThat(lp).isNotNull();
        assertThat(lp.getBehavior()).isInstanceOf(BottomTabsBehaviour.class);
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

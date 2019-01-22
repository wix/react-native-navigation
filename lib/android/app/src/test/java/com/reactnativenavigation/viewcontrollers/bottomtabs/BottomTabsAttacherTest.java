package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.presentation.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ViewController;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static com.reactnativenavigation.utils.CollectionUtils.forEach;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class BottomTabsAttacherTest extends BaseTest {
    private Activity activity;
    private ChildControllersRegistry childRegistry;

    private BottomTabsAttacher uut;
    private ViewController tab1;
    private ViewController tab2;
    private ViewController tab3;
    private List<ViewController> tabs;
    private ViewGroup parent;
    private BottomTabsPresenter presenter;

    @Override
    public void beforeEach() {
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        parent = new FrameLayout(activity);
        tabs = createTabs();
        presenter = Mockito.mock(BottomTabsPresenter.class);
        uut = new BottomTabsAttacher(tabs, presenter);
    }

    private List<ViewController> createTabs() {
        tab1 = new SimpleViewController(activity, childRegistry, "child1", new Options());
        tab2 = new SimpleViewController(activity, childRegistry, "child2", new Options());
        tab3 = new SimpleViewController(activity, childRegistry, "child3", new Options());
        return Arrays.asList(tab1, tab2, tab3);
    }

    @Test
    public void attach_firstTabIsAttachedToParent() {
        uut.attach(parent, Options.EMPTY);
        assertThat(tab1.getView().getParent()).isEqualTo(parent);
    }

    @Test
    public void attach_otherTabsAreAttachedAfterFirstTabIsAttached() {
        uut.attach(parent, Options.EMPTY);
        assertNotChildOf(parent, tab2, tab3);

        tab1.onViewAppeared();
        assertIsChild(parent, tab2, tab3);
    }

    @Test
    public void attach_otherTabsAreInvisibleWhenAttached() {
        uut.attach(parent, Options.EMPTY);
        tab1.onViewAppeared();
        forEach(tabs, 1, t -> assertThat(t.getView().getVisibility()).isEqualTo(View.INVISIBLE));
    }

    @Test
    public void attach_layoutOptionsAreApplied() {
        Options current = Options.EMPTY;
        uut.attach(parent, current);
        tab1.onViewAppeared();
        forEach(tabs, (t) -> verify(presenter).applyLayoutParamsOptions(current, tabs.indexOf(t)));
    }

    @Test
    public void attach_initialTabIsAttachedFirst() {
        Options current = new Options();
        current.bottomTabsOptions.currentTabIndex = new Number(1);

        uut.attach(parent, current);
        assertThat(tab1.getView().getParent()).isNull();
        assertThat(tab2.getView().getParent()).isEqualTo(parent);
        assertThat(tab3.getView().getParent()).isNull();
    }

    @Test
    public void destroy_removesOnAppearListener() {
        uut.attach(parent, Options.EMPTY);

        uut.destroy();
        tab1.onViewAppeared();
        assertNotChildOf(parent, tab2, tab3);
    }
}

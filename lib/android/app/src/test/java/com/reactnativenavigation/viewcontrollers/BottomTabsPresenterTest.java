package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.presentation.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.bottomtabs.TabSelector;
import com.reactnativenavigation.views.BottomTabs;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BottomTabsPresenterTest extends BaseTest {
    private List<ViewController> tabs;
    private BottomTabsPresenter uut;
    private BottomTabs bottomTabs;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        ChildControllersRegistry childRegistry = new ChildControllersRegistry();
        ViewController child1 = spy(new SimpleViewController(activity, childRegistry, "child1", new Options()));
        ViewController child2 = spy(new SimpleViewController(activity, childRegistry, "child2", new Options()));
        tabs = Arrays.asList(child1, child2);
        uut = new BottomTabsPresenter(tabs, new Options());
        bottomTabs = Mockito.mock(BottomTabs.class);
        uut.bindView(bottomTabs, Mockito.mock(TabSelector.class));
    }

    @Test
    public void mergeChildOptions_onlyDeclaredOptionsAreApplied() { // default options are not applied on merge
        Options defaultOptions = new Options();
        defaultOptions.bottomTabsOptions.visible = new Bool(false);
        uut.setDefaultOptions(defaultOptions);

        Options options = new Options();
        options.bottomTabsOptions.backgroundColor = new Colour(10);
        uut.mergeChildOptions(options, tabs.get(0));
        verify(bottomTabs).setBackgroundColor(options.bottomTabsOptions.backgroundColor.get());
        verifyNoMoreInteractions(bottomTabs);
    }
}

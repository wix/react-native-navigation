package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.presentation.BottomTabsPresenter;
import com.reactnativenavigation.utils.OptionHelper;
import com.reactnativenavigation.viewcontrollers.bottomtabs.TabSelector;
import com.reactnativenavigation.views.BottomTabs;
import com.reactnativenavigation.views.Component;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class BottomTabsPresenterTest extends BaseTest {
    private List<ViewController> tabs;
    private BottomTabsPresenter uut;
    private BottomTabs bottomTabs;
    private Options tabOptions = OptionHelper.createBottomTabOptions();

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        ChildControllersRegistry childRegistry = new ChildControllersRegistry();
        ViewController child1 = spy(new SimpleViewController(activity, childRegistry, "child1", tabOptions));
        ViewController child2 = spy(new SimpleViewController(activity, childRegistry, "child2", tabOptions));
        tabs = Arrays.asList(child1, child2);
        uut = new BottomTabsPresenter(tabs, new Options());
        bottomTabs = createBottomTabsMock();
        uut.bindView(bottomTabs, Mockito.mock(TabSelector.class));
    }

    private BottomTabs createBottomTabsMock() {
        BottomTabs mock = Mockito.mock(BottomTabs.class);
        doCallRealMethod().when(mock).setTitleState(any());
        when(mock.getTitleState()).thenCallRealMethod();
        return mock;
    }

    @Test
    public void applyBottomTabsOptions_TitleDisplayModeHiddenIsUsedIfTabsHaveNoTitles() {
        uut.applyOptions(Options.EMPTY);
        assertThat(bottomTabs.getTitleState()).isEqualTo(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);

        forEach(tabs, t -> t.options.bottomTabOptions.text = new NullText());
        uut.applyOptions(Options.EMPTY);
        assertThat(bottomTabs.getTitleState()).isEqualTo(AHBottomNavigation.TitleState.ALWAYS_HIDE);
    }

    @Test
    public void mergeChildOptions_onlyDeclaredOptionsAreApplied() { // default options are not applies on merge
        Options defaultOptions = new Options();
        defaultOptions.bottomTabsOptions.visible = new Bool(false);
        uut.setDefaultOptions(defaultOptions);

        Options options = new Options();
        options.bottomTabsOptions.backgroundColor = new Colour(10);
        uut.mergeChildOptions(options, (Component) tabs.get(0).getView());
        verify(bottomTabs).setBackgroundColor(options.bottomTabsOptions.backgroundColor.get());
        verifyNoMoreInteractions(bottomTabs);
    }
}

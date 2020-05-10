package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.anim.BottomTabsAnimator;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.bottomtabs.TabSelector;
import com.reactnativenavigation.views.BottomTabs;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BottomTabsPresenterTest extends BaseTest {
    private List<ViewController> tabs;
    private BottomTabsPresenter uut;
    private BottomTabs bottomTabs;
    private BottomTabsAnimator animator;
    private TabSelector tabSelector;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        ChildControllersRegistry childRegistry = new ChildControllersRegistry();
        ViewController child1 = spy(new SimpleViewController(activity, childRegistry, "child1", new Options()));
        ViewController child2 = spy(new SimpleViewController(activity, childRegistry, "child2", new Options()));
        tabs = Arrays.asList(child1, child2);
        uut = new BottomTabsPresenter(tabs, new Options());
        bottomTabs = Mockito.mock(BottomTabs.class);
        animator = spy(new BottomTabsAnimator(bottomTabs));
        tabSelector = mock(TabSelector.class);
        uut.bindView(bottomTabs, tabSelector, animator);
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

    @Test
    public void mergeChildOptions_visibilityIsAppliedOnlyIsChildIsShown() {
        assertThat(tabs.get(0).isViewShown()).isFalse();
        assertThat(bottomTabs.isHidden()).isFalse();

        Options options = new Options();
        options.bottomTabsOptions.visible = new Bool(false);
        uut.mergeChildOptions(options, tabs.get(0));
        verify(animator, times(0)).hide(any());

        Mockito.when(tabs.get(0).isViewShown()).thenAnswer(ignored -> true);
        uut.mergeChildOptions(options, tabs.get(0));
        verify(animator).hide(any());
    }

    @Test
    public void applyChildOptions_currentTabIndexIsConsumedAfterApply() {
        Options defaultOptions = new Options();
        defaultOptions.bottomTabsOptions.currentTabIndex = new Number(1);
        uut.setDefaultOptions(defaultOptions);

        uut.applyChildOptions(Options.EMPTY, tabs.get(0));
        verify(tabSelector).selectTab(1);

        uut.applyChildOptions(Options.EMPTY, tabs.get(0));
        verifyNoMoreInteractions(tabSelector);
    }

    @Test
    public void applyChildOptions_currentTabIdIsConsumedAfterApply() {
        Options defaultOptions = new Options();
        defaultOptions.bottomTabsOptions.currentTabId = new Text(tabs.get(1).getId());
        uut.setDefaultOptions(defaultOptions);

        uut.applyChildOptions(Options.EMPTY, tabs.get(0));
        verify(tabSelector).selectTab(1);

        uut.applyChildOptions(Options.EMPTY, tabs.get(0));
        verifyNoMoreInteractions(tabSelector);
    }
}

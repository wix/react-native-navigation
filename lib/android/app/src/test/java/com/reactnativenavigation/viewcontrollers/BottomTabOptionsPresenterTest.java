package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.graphics.Color;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.BottomTabOptionsPresenter;
import com.reactnativenavigation.views.BottomTabs;
import com.reactnativenavigation.views.Component;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BottomTabOptionsPresenterTest extends BaseTest {
    private Options tab1Options = createTab1Options();
    private Options tab2Options = createTab2Options();
    private BottomTabOptionsPresenter uut;
    private BottomTabs bottomTabs;
    private List<ViewController> tabs;
    private ViewController child3;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        ChildControllersRegistry childRegistry = new ChildControllersRegistry();
        bottomTabs = Mockito.mock(BottomTabs.class);
        ViewController child1 = spy(new SimpleViewController(activity, childRegistry, "child1", tab1Options));
        ViewController child2 = spy(new SimpleViewController(activity, childRegistry, "child2", tab2Options));
        child3 = spy(new SimpleViewController(activity, childRegistry, "child2", new Options()));
        tabs = Arrays.asList(child1, child2, child3);
        uut = new BottomTabOptionsPresenter(activity, tabs, new Options());
        uut.bindView(bottomTabs);
        uut.setDefaultOptions(new Options());
    }

    @Test
    public void present() {
        uut.present();
        for (int i = 0; i < tabs.size(); i++) {
            verify(bottomTabs, times(1)).setBadge(i, tabs.get(i).options.bottomTabOptions.badge.get(""));
            verify(bottomTabs, times(1)).setTitleInactiveColor(i, tabs.get(i).options.bottomTabOptions.textColor.get(null));
            verify(bottomTabs, times(1)).setTitleActiveColor(i, tabs.get(i).options.bottomTabOptions.selectedTextColor.get(null));
        }
    }

    @Test
    public void mergeChildOptions() {
        for (int i = 0; i < 2; i++) {
            Options options = tabs.get(i).options;
            uut.mergeChildOptions(options, (Component) tabs.get(i).getView());
            verify(bottomTabs, times(1)).setBadge(i, options.bottomTabOptions.badge.get());
            verify(bottomTabs, times(1)).setIconActiveColor(eq(i), anyInt());
            verify(bottomTabs, times(1)).setIconInactiveColor(eq(i), anyInt());
        }
        verifyNoMoreInteractions(bottomTabs);
    }

    @Test
    public void mergeChildOptions_onlySetsDefinedOptions() {
        uut.mergeChildOptions(child3.options, (Component) child3.getView());
        verify(bottomTabs, times(0)).setBadge(eq(2), anyString());
        verify(bottomTabs, times(0)).setIconInactiveColor(eq(2), anyInt());
        verify(bottomTabs, times(0)).setIconActiveColor(eq(2), anyInt());
        verifyNoMoreInteractions(bottomTabs);
    }

    private Options createTab1Options() {
        Options options = new Options();
        options.bottomTabOptions.badge = new Text("tab1badge");
        options.bottomTabOptions.iconColor = new Colour(Color.RED);
        options.bottomTabOptions.selectedIconColor = new Colour(Color.RED);
        return options;
    }

    private Options createTab2Options() {
        Options options = new Options();
        options.bottomTabOptions.badge = new Text("tab2badge");
        options.bottomTabOptions.iconColor = new Colour(Color.RED);
        options.bottomTabOptions.selectedIconColor = new Colour(Color.RED);
        return options;
    }
}

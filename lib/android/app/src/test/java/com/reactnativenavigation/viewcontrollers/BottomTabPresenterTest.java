package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.graphics.Color;

import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.DontApplyColour;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.BottomTabPresenter;
import com.reactnativenavigation.views.BottomTabs;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BottomTabPresenterTest extends BaseTest {
    private Options tab1Options = createTab1Options();
    private Options tab2Options = createTab2Options();
    private BottomTabPresenter uut;
    private BottomTabs bottomTabs;
    private List<ViewController> tabs;
    private ViewController child1;
    private ViewController child2;
    private ViewController child3;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        ChildControllersRegistry childRegistry = new ChildControllersRegistry();
        bottomTabs = Mockito.mock(BottomTabs.class);
        child1 = spy(new SimpleViewController(activity, childRegistry, "child1", tab1Options));
        child2 = spy(new SimpleViewController(activity, childRegistry, "child2", tab2Options));
        child3 = spy(new SimpleViewController(activity, childRegistry, "child2", new Options()));
        tabs = Arrays.asList(child1, child2, child3);
        uut = new BottomTabPresenter(activity, tabs, ImageLoaderMock.mock(), new Options());
        uut.bindView(bottomTabs);
        uut.setDefaultOptions(new Options());
    }

    @Test
    public void present() {
        uut.applyOptions();
        for (int i = 0; i < tabs.size(); i++) {
            verify(bottomTabs, times(1)).setNotification(any(AHNotification.class), eq(i));
            verify(bottomTabs, times(1)).setTitleInactiveColor(i, tabs.get(i).options.bottomTabOptions.textColor.get(null));
            verify(bottomTabs, times(1)).setTitleActiveColor(i, tabs.get(i).options.bottomTabOptions.selectedTextColor.get(null));
        }
    }

    @Test
    public void mergeOptions_createTabsOnce() {
        Options options = new Options();
        options.bottomTabOptions.iconColor = new Colour(1);
        options.bottomTabOptions.selectedIconColor = new Colour(1);
        BottomTabPresenter spy = spy(uut);

        spy.mergeOptions(options);

        InOrder inOrder = inOrder(spy, child1, child2, child3, bottomTabs);
        inOrder.verify(bottomTabs).disableItemsCreation();
        forEach(tabs, tab -> inOrder.verify(spy).mergeChildOptions(options, tab));
        inOrder.verify(bottomTabs).enableItemsCreation();
    }

    @Test
    public void mergeChildOptions() {
        for (int i = 0; i < 2; i++) {
            Options options = tabs.get(i).options;
            uut.mergeChildOptions(options, tabs.get(i));
            verify(bottomTabs, times(1)).setNotification(any(AHNotification.class), eq(i));
            verify(bottomTabs, times(1)).setIconActiveColor(eq(i), anyInt());
            verify(bottomTabs, times(1)).setIconInactiveColor(eq(i), anyInt());
        }
        verifyNoMoreInteractions(bottomTabs);
    }

    @Test
    public void mergeChildOptions_onlySetsDefinedOptions() {
        uut.mergeChildOptions(child3.options, child3);
        verify(bottomTabs, times(0)).setNotification(any(AHNotification.class), anyInt());
        verify(bottomTabs, times(0)).setIconInactiveColor(eq(2), anyInt());
        verify(bottomTabs, times(0)).setIconActiveColor(eq(2), anyInt());
        verifyNoMoreInteractions(bottomTabs);
    }

    @Test
    public void mergeChildOptions_nullColorsAreNotMerged() {
        Options options = new Options();
        options.bottomTabOptions.iconColor = new DontApplyColour();
        options.bottomTabOptions.selectedIconColor = new DontApplyColour();
        uut.mergeChildOptions(options, child3);
        verify(bottomTabs, times(0)).setIconActiveColor(anyInt(), anyInt());
        verify(bottomTabs, times(0)).setIconInactiveColor(anyInt(), anyInt());
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

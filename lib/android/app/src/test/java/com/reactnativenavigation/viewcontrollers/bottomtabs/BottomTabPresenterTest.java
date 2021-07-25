package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.app.Activity;
import android.graphics.Color;

import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TypefaceLoaderMock;
import com.reactnativenavigation.options.BadgeOptions;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.DontApplyColour;
import com.reactnativenavigation.options.params.NullColor;
import com.reactnativenavigation.options.params.NullText;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.bottomtabs.BottomTabs;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
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
    private List<ViewController<?>> tabs;
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
        uut = new BottomTabPresenter(activity, tabs, ImageLoaderMock.mock(), new TypefaceLoaderMock(), new Options());
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

    @Test
    public void mergeChildOptions_shouldMergeBadgeOptions() {
        Options options = new Options();
        options.bottomTabOptions.badge = new BadgeOptions(new Text("tab1badge"), new Colour(Color.RED), new Colour(Color.BLUE));
        uut.mergeChildOptions(options, child2);
        ArgumentCaptor<AHNotification> notificationArgumentCaptor = ArgumentCaptor.forClass(AHNotification.class);
        verify(bottomTabs, times(1)).setNotification(notificationArgumentCaptor.capture(), anyInt());
        AHNotification value = notificationArgumentCaptor.getValue();
        assertThat(value).isNotNull();
        assertThat(value.getReadableText()).isEqualTo("tab1badge");
        assertThat(value.getBackgroundColor()).isEqualTo(Color.BLUE);
        assertThat(value.getTextColor()).isEqualTo(Color.RED);
    }

    @Test
    public void mergeChildOptions_shouldMergeChangedBadgeOptionsOnly() {
        Options options = new Options();
        options.bottomTabOptions.badge = new BadgeOptions(new Text("tab1badge"), new Colour(Color.RED), new Colour(Color.BLUE));
        uut.mergeChildOptions(options, child2);

        Options options2 = new Options();
        options2.bottomTabOptions.badge = new BadgeOptions(new NullText(), new Colour(Color.YELLOW), new NullColor());
        uut.mergeChildOptions(options2, child2);

        //now second time
        ArgumentCaptor<AHNotification> notificationArgumentCaptor2 = ArgumentCaptor.forClass(AHNotification.class);
        verify(bottomTabs, times(2)).setNotification(notificationArgumentCaptor2.capture(), anyInt());
        AHNotification value2 = notificationArgumentCaptor2.getValue();
        assertThat(value2.getTextColor()).isEqualTo(Color.YELLOW);
    }

    private Options createTab1Options() {
        Options options = new Options();
        options.bottomTabOptions.badge = new BadgeOptions(new Text("tab1badge"), new NullColor(), new NullColor());
        options.bottomTabOptions.iconColor = new Colour(Color.RED);
        options.bottomTabOptions.selectedIconColor = new Colour(Color.RED);
        return options;
    }

    private Options createTab2Options() {
        Options options = new Options();
        options.bottomTabOptions.badge = new BadgeOptions(new Text("tab2badge"), new NullColor(), new NullColor());
        options.bottomTabOptions.iconColor = new Colour(Color.RED);
        options.bottomTabOptions.selectedIconColor = new Colour(Color.RED);
        return options;
    }
}

package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;

import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TypefaceLoaderMock;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.DontApplyColour;
import com.reactnativenavigation.options.params.NullText;
import com.reactnativenavigation.options.params.NullThemeColour;
import com.reactnativenavigation.options.params.ThemeColour;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
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
    private ViewController<?> child1;
    private ViewController<?> child2;
    private ViewController<?> child3;
    private Activity activity;
    private ChildControllersRegistry childRegistry;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        createBottomTabs(tab1Options,tab2Options,new Options());
    }

    private void createBottomTabs(Options tab1Options,
                                  Options tab2Options,Options tab3Options ) {
        childRegistry = new ChildControllersRegistry();
        bottomTabs = Mockito.mock(BottomTabs.class);
        child1 = spy(new SimpleViewController(activity, childRegistry, "child1", tab1Options));
        child2 = spy(new SimpleViewController(activity, childRegistry, "child2", tab2Options));
        child3 = spy(new SimpleViewController(activity, childRegistry, "child2", tab3Options));
        tabs = Arrays.asList(child1, child2, child3);
        uut = new BottomTabPresenter(activity, tabs, ImageLoaderMock.mock(), new TypefaceLoaderMock(), new Options());
        uut.bindView(bottomTabs);
        uut.setDefaultOptions(new Options());
    }

    @Test
    public void onConfigurationChange_shouldChangeColors(){
        Options options = Options.EMPTY;
        options.bottomTabOptions.textColor = ThemeColour.of(Color.BLACK,Color.WHITE);
        options.bottomTabOptions.selectedTextColor = ThemeColour.of(Color.BLUE,Color.RED);
        options.bottomTabOptions.iconColor = ThemeColour.of(Color.BLACK,Color.WHITE);
        options.bottomTabOptions.selectedIconColor = ThemeColour.of(Color.BLUE,Color.RED);
        options.bottomTabOptions.badgeColor = ThemeColour.of(Color.BLACK,Color.WHITE);
        createBottomTabs(options,options,options);

        mockConfiguration.uiMode = Configuration.UI_MODE_NIGHT_NO;
        uut.onConfigurationChanged(options);
        for(int i=0;i<tabs.size();++i){
            verify(bottomTabs).setIconActiveColor(i,Color.BLUE);
            verify(bottomTabs).setIconInactiveColor(i,Color.BLACK);

            verify(bottomTabs).setTitleActiveColor(i,Color.BLUE);
            verify(bottomTabs).setTitleInactiveColor(i,Color.BLACK);
        }

        mockConfiguration.uiMode = Configuration.UI_MODE_NIGHT_YES;
        uut.onConfigurationChanged(options);
        for(int i=0;i<tabs.size();++i){
            verify(bottomTabs).setIconActiveColor(i,Color.RED);
            verify(bottomTabs).setIconInactiveColor(i,Color.WHITE);

            verify(bottomTabs).setTitleActiveColor(i,Color.RED);
            verify(bottomTabs).setTitleInactiveColor(i,Color.WHITE);
        }
    }

    @Test
    public void present() {
        uut.applyOptions();
        verify(bottomTabs, times(1)).setNotification(any(AHNotification.class), eq(0));
        verify(bottomTabs, times(1)).setNotification(any(AHNotification.class), eq(1));
        verify(bottomTabs, never()).setNotification(any(AHNotification.class), eq(2));

        for (int i = 0; i < tabs.size(); i++) {
            verify(bottomTabs, times(1)).setTitleInactiveColor(i, tabs.get(i).options.bottomTabOptions.textColor.get(null));
            verify(bottomTabs, times(1)).setTitleActiveColor(i, tabs.get(i).options.bottomTabOptions.selectedTextColor.get(null));
        }
    }

    @Test
    public void applyOptions_shouldPresentBadgeIfAvailable() {
        final Options tab1 = tab1Options.copy();
        tab1.bottomTabOptions.badge = new NullText();
        tab1.bottomTabOptions.badgeColor = new NullThemeColour();

        final Options tab2 = tab2Options.copy();
        tab2.bottomTabOptions.badge = new Text("Badge");
        tab2.bottomTabOptions.badgeColor = ThemeColour.of(Color.RED);


        createBottomTabs(tab1, tab2, new Options());
        uut.applyOptions();

        ArgumentCaptor<AHNotification> notificationArgumentCaptor = ArgumentCaptor.forClass(AHNotification.class);
        verify(bottomTabs).setNotification(notificationArgumentCaptor.capture(), eq(1));
        final AHNotification value = notificationArgumentCaptor.getValue();
        assertThat(value.getReadableText()).isEqualTo("Badge");
        assertThat(value.getBackgroundColor()).isEqualTo(Color.RED);

        verify(bottomTabs,never()).setNotification(notificationArgumentCaptor.capture(), eq(0));
        verify(bottomTabs,never()).setNotification(notificationArgumentCaptor.capture(), eq(2));

    }

    @Test
    public void mergeOptions_createTabsOnce() {
        Options options = new Options();
        options.bottomTabOptions.iconColor = new ThemeColour(new Colour(1));
        options.bottomTabOptions.selectedIconColor = new ThemeColour(new Colour(1));
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
        options.bottomTabOptions.iconColor = new ThemeColour(new DontApplyColour());
        options.bottomTabOptions.selectedIconColor = new ThemeColour(new DontApplyColour());
        uut.mergeChildOptions(options, child3);
        verify(bottomTabs, times(0)).setIconActiveColor(anyInt(), anyInt());
        verify(bottomTabs, times(0)).setIconInactiveColor(anyInt(), anyInt());
    }

    private Options createTab1Options() {
        Options options = new Options();
        options.bottomTabOptions.badge = new Text("tab1badge");
        options.bottomTabOptions.iconColor = new ThemeColour(new Colour(Color.RED));
        options.bottomTabOptions.selectedIconColor = new ThemeColour(new Colour(Color.RED));
        return options;
    }

    private Options createTab2Options() {
        Options options = new Options();
        options.bottomTabOptions.badge = new Text("tab2badge");
        options.bottomTabOptions.iconColor = new ThemeColour(new Colour(Color.RED));
        options.bottomTabOptions.selectedIconColor = new ThemeColour(new Colour(Color.RED));
        return options;
    }
}

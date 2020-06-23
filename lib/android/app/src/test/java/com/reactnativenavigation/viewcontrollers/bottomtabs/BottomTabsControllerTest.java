package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.BottomTabPresenter;
import com.reactnativenavigation.presentation.BottomTabsPresenter;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.OptionHelper;
import com.reactnativenavigation.utils.StatusBarUtils;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.fakes.FakeParentController;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.views.BottomTabs;
import com.reactnativenavigation.views.bottomtabs.BottomTabsLayout;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import edu.emory.mathcs.backport.java.util.Collections;

import static com.reactnativenavigation.TestUtils.hideBackButton;
import static com.reactnativenavigation.utils.ObjectUtils.perform;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BottomTabsControllerTest extends BaseTest {

    private Activity activity;
    private BottomTabs bottomTabs;
    private BottomTabsController uut;
    private Options initialOptions = new Options();
    private ViewController child1;
    private ViewController child2;
    private ViewController child3;
    private ViewController stackChild;
    private StackController child4;
    private ViewController child5;
    private Options tabOptions = OptionHelper.createBottomTabOptions();
    private ImageLoader imageLoaderMock = ImageLoaderMock.mock();
    private EventEmitter eventEmitter;
    private ChildControllersRegistry childRegistry;
    private List<ViewController> tabs;
    private BottomTabsPresenter presenter;
    private BottomTabPresenter bottomTabPresenter;
    private BottomTabsAttacher tabsAttacher;

    @Override
    public void beforeEach() {
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        eventEmitter = Mockito.mock(EventEmitter.class);
        prepareViewsForTests();
        StatusBarUtils.saveStatusBarHeight(63);
    }

    @Test
    public void createView_checkProperStructure() {
        assertThat(uut.getView()).isInstanceOf(CoordinatorLayout.class);
        assertThat(uut.getView().getChildAt(0)).isInstanceOf(BottomTabs.class);
        assertThat(((CoordinatorLayout.LayoutParams) uut.getBottomTabs().getLayoutParams()).gravity).isEqualTo(Gravity.BOTTOM);
    }

    @Test
    public void createView_tabsWithoutIconsAreAccepted() {
        tabOptions.bottomTabOptions.icon = new NullText();
        prepareViewsForTests();
        assertThat(uut.getBottomTabs().getItemsCount()).isEqualTo(tabs.size());
    }

    @Test
    public void createView_showTitlesWhenAllTabsDontHaveIcons() {
        tabOptions.bottomTabOptions.icon = new NullText();
        assertThat(tabOptions.bottomTabsOptions.titleDisplayMode.hasValue()).isFalse();
        prepareViewsForTests();
        presenter.applyOptions(Options.EMPTY);
        assertThat(bottomTabs.getTitleState()).isEqualTo(AHBottomNavigation.TitleState.ALWAYS_SHOW);
    }

    @Test(expected = RuntimeException.class)
    public void setTabs_ThrowWhenMoreThan5() {
        tabs.add(new SimpleViewController(activity, childRegistry, "6", tabOptions));
        createBottomTabs();
    }

    @Test
    public void parentControllerIsSet() {
        uut = createBottomTabs();
        for (ViewController tab : tabs) {
            assertThat(tab.getParentController()).isEqualTo(uut);
        }
    }

    @Test
    public void setTabs_allChildViewsAreAttachedToHierarchy() {
        uut.onViewWillAppear();
        assertThat(uut.getView().getChildCount()).isEqualTo(6);
        for (ViewController child : uut.getChildControllers()) {
            assertThat(child.getView().getParent()).isNotNull();
        }
    }

    @Test
    public void setTabs_firstChildIsVisibleOtherAreGone() {
        uut.onViewWillAppear();
        for (int i = 0; i < uut.getChildControllers().size(); i++) {
            assertThat(uut.getView().getChildAt(i + 1)).isEqualTo(tabs.get(i).getView());
            assertThat(uut.getView().getChildAt(i + 1).getVisibility()).isEqualTo(i == 0 ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Test
    public void onTabSelected() {
        uut.ensureViewIsCreated();
        assertThat(uut.getSelectedIndex()).isZero();
        assertThat(((ViewController) ((List) uut.getChildControllers()).get(0)).getView().getVisibility()).isEqualTo(View.VISIBLE);

        uut.onTabSelected(3, false);

        assertThat(uut.getSelectedIndex()).isEqualTo(3);
        assertThat(((ViewController) ((List) uut.getChildControllers()).get(0)).getView().getVisibility()).isEqualTo(View.INVISIBLE);
        assertThat(((ViewController) ((List) uut.getChildControllers()).get(3)).getView().getVisibility()).isEqualTo(View.VISIBLE);
        verify(eventEmitter, times(1)).emitBottomTabSelected(0, 3);
    }

    @Test
    public void onTabReSelected() {
        uut.ensureViewIsCreated();
        assertThat(uut.getSelectedIndex()).isZero();

        uut.onTabSelected(0, true);

        assertThat(uut.getSelectedIndex()).isEqualTo(0);
        assertThat(((ViewController) ((List) uut.getChildControllers()).get(0)).getView().getParent()).isNotNull();
        verify(eventEmitter, times(1)).emitBottomTabSelected(0, 0);
    }

    @Test
    public void handleBack_DelegatesToSelectedChild() {
        uut.ensureViewIsCreated();
        assertThat(uut.handleBack(new CommandListenerAdapter())).isFalse();
        uut.selectTab(4);
        assertThat(uut.handleBack(new CommandListenerAdapter())).isTrue();
        verify(child5, times(1)).handleBack(any());
    }

    @Test
    public void applyChildOptions_bottomTabsOptionsAreClearedAfterApply() {
        ParentController parent = Mockito.mock(ParentController.class);
        uut.setParentController(parent);

        child1.options.bottomTabsOptions.backgroundColor = new Colour(Color.RED);
        child1.onViewWillAppear();

        ArgumentCaptor<Options> optionsCaptor = ArgumentCaptor.forClass(Options.class);
        verify(parent).applyChildOptions(optionsCaptor.capture(), any());
        assertThat(optionsCaptor.getValue().bottomTabsOptions.backgroundColor.hasValue()).isFalse();
    }

    @Test
    public void applyOptions_bottomTabsCreateViewOnlyOnce() {
        verify(presenter).applyOptions(any());
        verify(bottomTabs, times(2)).superCreateItems(); // first time when view is created, second time when options are applied
    }

    @Test
    public void onSizeChanged_recreateItemsIfSizeHasChanged() {
        int numberOfPreviousInvocations = 2;
        bottomTabs.onSizeChanged(0, 0, 0, 0);
        verify(bottomTabs, times(numberOfPreviousInvocations)).superCreateItems();

        bottomTabs.onSizeChanged(100, 0, 0, 0);
        verify(bottomTabs, times(numberOfPreviousInvocations)).superCreateItems();

        bottomTabs.onSizeChanged(1080, 147, 0, 0);
        verify(bottomTabs, times(numberOfPreviousInvocations + 1)).superCreateItems();

        bottomTabs.onSizeChanged(1920, 147, 0, 0);
        verify(bottomTabs, times(numberOfPreviousInvocations + 2)).superCreateItems();

        when(bottomTabs.getItemsCount()).thenReturn(0);
        bottomTabs.onSizeChanged(1080, 147, 0, 0);
        verify(bottomTabs, times(numberOfPreviousInvocations + 2)).superCreateItems();
    }

    @Test
    public void mergeOptions_currentTabIndex() {
        uut.ensureViewIsCreated();
        assertThat(uut.getSelectedIndex()).isZero();

        Options options = new Options();
        options.bottomTabsOptions.currentTabIndex = new Number(1);
        uut.mergeOptions(options);
        assertThat(uut.getSelectedIndex()).isOne();
        verify(eventEmitter, times(0)).emitBottomTabSelected(any(Integer.class), any(Integer.class));
    }

    @Test
    public void mergeOptions_drawBehind() {
        assertThat(uut.getBottomInset(child1)).isEqualTo(uut.getBottomTabs().getHeight());

        Options o1 = new Options();
        o1.bottomTabsOptions.drawBehind = new Bool(true);
        child1.mergeOptions(o1);
        assertThat(uut.getBottomInset(child1)).isEqualTo(0);

        Options o2 = new Options();
        o2.topBar.title.text = new Text("Some text");
        child1.mergeOptions(o1);
        assertThat(uut.getBottomInset(child1)).isEqualTo(0);
    }

    @Test
    public void mergeOptions_drawBehind_stack() {
        uut.ensureViewIsCreated();
        uut.selectTab(3);

        assertThat(((MarginLayoutParams) stackChild.getView().getLayoutParams()).bottomMargin).isEqualTo(bottomTabs.getHeight());

        Options o1 = new Options();
        o1.bottomTabsOptions.drawBehind = new Bool(true);
        stackChild.mergeOptions(o1);

        assertThat(((MarginLayoutParams) stackChild.getView().getLayoutParams()).bottomMargin).isEqualTo(0);
    }

    @Test
    public void mergeOptions_mergesBottomTabOptions() {
        Options options = new Options();
        uut.mergeOptions(options);
        verify(bottomTabPresenter).mergeOptions(options);
    }

    @Test
    public void applyChildOptions_resolvedOptionsAreUsed() {
        Options childOptions = new Options();
        SimpleViewController pushedScreen = new SimpleViewController(activity , childRegistry, "child4.1", childOptions);
        disablePushAnimation(pushedScreen);
        child4 = spyOnStack(pushedScreen);

        tabs = new ArrayList<>(Collections.singletonList(child4));
        tabsAttacher = new BottomTabsAttacher(tabs, presenter, Options.EMPTY);

        initialOptions.bottomTabsOptions.currentTabIndex = new Number(0);
        Options resolvedOptions = new Options();
        uut = new BottomTabsController(activity,
                tabs,
                childRegistry,
                eventEmitter,
                imageLoaderMock,
                "uut",
                initialOptions,
                new Presenter(activity , new Options()),
                tabsAttacher,
                presenter,
                new BottomTabPresenter(activity , tabs, ImageLoaderMock.mock(), new Options())) {
            @Override
            public Options resolveCurrentOptions() {
                return resolvedOptions;
            }

            @NonNull
            @Override
            protected BottomTabs createBottomTabs() {
                return new BottomTabs(activity ) {
                    @Override
                    protected void createItems() {

                    }
                };
            }
        };

        activity.setContentView(uut.getView());
        verify(presenter, times(2)).applyChildOptions(eq(resolvedOptions), any());
    }

    @Test
    public void child_mergeOptions_currentTabIndex() {
        uut.ensureViewIsCreated();

        assertThat(uut.getSelectedIndex()).isZero();

        Options options = new Options();
        options.bottomTabsOptions.currentTabIndex = new Number(1);
        child1.mergeOptions(options);

        assertThat(uut.getSelectedIndex()).isOne();
    }

    @Test
    public void resolveCurrentOptions_returnsFirstTabIfInvokedBeforeViewIsCreated() {
        uut = createBottomTabs();
        assertThat(uut.getCurrentChild()).isEqualTo(tabs.get(0));
    }

    @Test
    public void buttonPressInvokedOnCurrentTab() {
        uut.ensureViewIsCreated();
        uut.selectTab(4);

        uut.sendOnNavigationButtonPressed("btn1");
        verify(child5, times(1)).sendOnNavigationButtonPressed("btn1");
    }

    @Test
    public void push() {
        uut.selectTab(3);

        SimpleViewController stackChild2 = new SimpleViewController(activity, childRegistry, "stackChild2", new Options());
        disablePushAnimation(stackChild2);
        hideBackButton(stackChild2);

        assertThat(child4.size()).isEqualTo(1);
        child4.push(stackChild2, new CommandListenerAdapter());
        assertThat(child4.size()).isEqualTo(2);
    }

    @Test
    public void oneTimeOptionsAreAppliedOnce() {
        Options options = new Options();
        options.bottomTabsOptions.currentTabIndex = new Number(1);

        assertThat(uut.getSelectedIndex()).isZero();
        uut.mergeOptions(options);
        assertThat(uut.getSelectedIndex()).isOne();
        assertThat(uut.options.bottomTabsOptions.currentTabIndex.hasValue()).isFalse();
        assertThat(uut.initialOptions.bottomTabsOptions.currentTabIndex.hasValue()).isFalse();
    }

    @Test
    public void selectTab() {
        uut.selectTab(1);
        verify(tabsAttacher).onTabSelected(tabs.get(1));
    }

    @Test
    public void selectTab_onViewDidAppearIsInvokedAfterSelection() {
        uut.selectTab(1);
        verify(child2).onViewDidAppear();
    }

    @Test
    public void getTopInset() {
        assertThat(child1.getTopInset()).isEqualTo(getStatusBarHeight());
        assertThat(child2.getTopInset()).isEqualTo(getStatusBarHeight());

        child1.options.statusBar.drawBehind = new Bool(true);
        assertThat(child1.getTopInset()).isEqualTo(0);
        assertThat(child2.getTopInset()).isEqualTo(getStatusBarHeight());

        assertThat(stackChild.getTopInset()).isEqualTo(getStatusBarHeight() + child4.getTopBar().getHeight());
    }

    @Test
    public void getBottomInset_defaultOptionsAreTakenIntoAccount() {
        Options defaultOptions = new Options();
        defaultOptions.bottomTabsOptions.visible = new Bool(false);

        assertThat(uut.getBottomInset(child1)).isEqualTo(bottomTabs.getHeight());
        uut.setDefaultOptions(defaultOptions);
        assertThat(uut.getBottomInset(child1)).isZero();
    }

    @Test
    public void destroy() {
        uut.destroy();
        verify(tabsAttacher).destroy();
    }

    private void prepareViewsForTests() {
        perform(uut, ViewController::destroy);
        bottomTabs = spy(new BottomTabs(activity) {
            @Override
            public void superCreateItems() {

            }
        });
        createChildren();
        tabs = Arrays.asList(child1, child2, child3, child4, child5);
        presenter = spy(new BottomTabsPresenter(tabs, Options.EMPTY));
        bottomTabPresenter = spy(new BottomTabPresenter(activity, tabs, ImageLoaderMock.mock(), Options.EMPTY));
        tabsAttacher = spy(new BottomTabsAttacher(tabs, presenter, Options.EMPTY));
        uut = createBottomTabs();

        activity.setContentView(new FakeParentController(activity, childRegistry, uut).getView());
    }

    private void createChildren() {
        child1 = spy(new SimpleViewController(activity, childRegistry, "child1", tabOptions));
        child2 = spy(new SimpleViewController(activity, childRegistry, "child2", tabOptions));
        child3 = spy(new SimpleViewController(activity, childRegistry, "child3", tabOptions));
        stackChild = spy(new SimpleViewController(activity, childRegistry, "stackChild", tabOptions));
        child4 = spyOnStack(stackChild);
        child5 = spy(new SimpleViewController(activity, childRegistry, "child5", tabOptions));
        when(child5.handleBack(any())).thenReturn(true);
    }

    private StackController spyOnStack(ViewController initialChild) {
        StackController build = TestUtils.newStackController(activity)
                .setInitialOptions(tabOptions)
                .build();
        StackController stack =  spy(build);
        disablePushAnimation(initialChild);
        stack.ensureViewIsCreated();
        stack.push(initialChild, new CommandListenerAdapter());
        return stack;
    }

    private BottomTabsController createBottomTabs() {
        return new BottomTabsController(activity,
                tabs,
                childRegistry,
                eventEmitter,
                imageLoaderMock,
                "uut",
                initialOptions,
                new Presenter(activity, new Options()),
                tabsAttacher,
                presenter,
                bottomTabPresenter) {
            @Override
            public void ensureViewIsCreated() {
                super.ensureViewIsCreated();
                uut.getView().layout(0, 0, 1000, 1000);
            }

            @NonNull
            @Override
            protected BottomTabsLayout createView() {
                BottomTabsLayout view = super.createView();
                bottomTabs.getLayoutParams().height = 100;
                return view;
            }

            @NonNull
            @Override
            protected BottomTabs createBottomTabs() {
                return bottomTabs;
            }
        };
    }

    private int getStatusBarHeight() {
        return StatusBarUtils.getStatusBarHeight(activity);
    }
}

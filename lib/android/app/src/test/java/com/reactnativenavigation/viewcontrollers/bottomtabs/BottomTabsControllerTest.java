package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.BottomTabPresenter;
import com.reactnativenavigation.presentation.BottomTabsPresenter;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.react.EventEmitter;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.OptionHelper;
import com.reactnativenavigation.utils.StatusBarUtils;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.views.BottomTabs;

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
        bottomTabs = spy(new BottomTabs(activity) {
            @Override
            public void superCreateItems() {

            }
        });
        childRegistry = new ChildControllersRegistry();
        eventEmitter = Mockito.mock(EventEmitter.class);

        child1 = spy(new SimpleViewController(activity, childRegistry, "child1", tabOptions));
        child2 = spy(new SimpleViewController(activity, childRegistry, "child2", tabOptions));
        child3 = spy(new SimpleViewController(activity, childRegistry, "child3", tabOptions));
        child4 = spy(createStack("someStack"));
        child5 = spy(new SimpleViewController(activity, childRegistry, "child5", tabOptions));
        when(child5.handleBack(any())).thenReturn(true);
        tabs = createTabs();
        presenter = spy(new BottomTabsPresenter(tabs, new Options()));
        bottomTabPresenter = spy(new BottomTabPresenter(activity, tabs, ImageLoaderMock.mock(), new Options()));
        tabsAttacher = spy(new BottomTabsAttacher(tabs, presenter));
        uut = createBottomTabs();

        uut.setParentController(Mockito.mock(ParentController.class));
        CoordinatorLayout parent = new CoordinatorLayout(activity);
        parent.addView(uut.getView());
        activity.setContentView(parent);

        StatusBarUtils.saveStatusBarHeight(63);
    }

    @Test
    public void createView_checkProperStructure() {
        assertThat(uut.getView()).isInstanceOf(CoordinatorLayout.class);
        assertThat(uut.getView().getChildAt(0)).isInstanceOf(BottomTabs.class);
        assertThat(((CoordinatorLayout.LayoutParams) uut.getBottomTabs().getLayoutParams()).gravity).isEqualTo(Gravity.BOTTOM);
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
        uut.onViewAppeared();
        assertThat(uut.getView().getChildCount()).isEqualTo(6);
        for (ViewController child : uut.getChildControllers()) {
            assertThat(child.getView().getParent()).isNotNull();
        }
    }

    @Test
    public void setTabs_firstChildIsVisibleOtherAreGone() {
        uut.onViewAppeared();
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
        child1.onViewAppeared();

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
        assertThat(uut.getBottomInset()).isEqualTo(uut.getBottomTabs().getHeight());

        Options o1 = new Options();
        o1.bottomTabsOptions.drawBehind = new Bool(true);
        child1.mergeOptions(o1);
        assertThat(uut.getBottomInset()).isEqualTo(0);

        Options o2 = new Options();
        o2.topBar.title.text = new Text("Some text");
        child1.mergeOptions(o1);
        assertThat(uut.getBottomInset()).isEqualTo(0);
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
        SimpleViewController pushedScreen = new SimpleViewController(activity, childRegistry, "child4.1", childOptions);
        disablePushAnimation(pushedScreen);
        child4 = createStack(pushedScreen);

        tabs = new ArrayList<>(Collections.singletonList(child4));
        tabsAttacher = new BottomTabsAttacher(tabs, presenter);

        initialOptions.bottomTabsOptions.currentTabIndex = new Number(0);
        Options resolvedOptions = new Options();
        uut = new BottomTabsController(activity,
                tabs,
                childRegistry,
                eventEmitter,
                imageLoaderMock,
                "uut",
                initialOptions,
                new Presenter(activity, new Options()),
                tabsAttacher,
                presenter,
                new BottomTabPresenter(activity, tabs, ImageLoaderMock.mock(), new Options())) {
            @Override
            public Options resolveCurrentOptions() {
                return resolvedOptions;
            }

            @NonNull
            @Override
            protected BottomTabs createBottomTabs() {
                return new BottomTabs(activity) {
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
        uut.ensureViewIsCreated();
        uut.selectTab(3);

        SimpleViewController stackChild = new SimpleViewController(activity, childRegistry, "stackChild", new Options());
        SimpleViewController stackChild2 = new SimpleViewController(activity, childRegistry, "stackChild", new Options());

        disablePushAnimation(stackChild, stackChild2);
        hideBackButton(stackChild2);

        child4.push(stackChild, new CommandListenerAdapter());
        assertThat(child4.size()).isOne();
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
    public void getTopInset() {
        assertThat(child1.getTopInset()).isEqualTo(StatusBarUtils.getStatusBarHeight(activity));
        assertThat(child2.getTopInset()).isEqualTo(StatusBarUtils.getStatusBarHeight(activity));

        child1.options.statusBar.drawBehind = new Bool(true);
        assertThat(child1.getTopInset()).isEqualTo(0);
        assertThat(child2.getTopInset()).isEqualTo(StatusBarUtils.getStatusBarHeight(activity));

        SimpleViewController stackChild = new SimpleViewController(activity, childRegistry, "stackChild", new Options());
        disablePushAnimation(stackChild);
        child4.push(stackChild, new CommandListenerAdapter());

        assertThat(stackChild.getTopInset()).isEqualTo(StatusBarUtils.getStatusBarHeight(activity) + child4.getTopBar().getHeight());

        uut.options.statusBar.drawBehind = new Bool(true);
        stackChild.options.topBar.drawBehind = new Bool(true);
        assertThat(uut.getTopInset()).isEqualTo(0);
        assertThat(child4.getTopInset()).isEqualTo(0);
        assertThat(child4.getTopBar().getY()).isEqualTo(StatusBarUtils.getStatusBarHeight(activity));
        assertThat(stackChild.getTopInset()).isEqualTo(StatusBarUtils.getStatusBarHeight(activity));
    }

    @Test
    public void destroy() {
        uut.destroy();
        verify(tabsAttacher).destroy();
    }

    @NonNull
    private List<ViewController> createTabs() {
        return Arrays.asList(child1, child2, child3, child4, child5);
    }

    private StackController createStack(String id) {
        return TestUtils.newStackController(activity)
                .setId(id)
                .setInitialOptions(tabOptions)
                .build();
    }

    private StackController createStack(ViewController initialChild) {
        return TestUtils.newStackController(activity)
                .setInitialOptions(tabOptions)
                .setChildren(initialChild)
                .build();
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
            protected BottomTabs createBottomTabs() {
                return bottomTabs;
            }
        };
    }
}

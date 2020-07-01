package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.BackDrawable;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.Mocks;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TitleBarButtonCreatorMock;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.parse.Alignment;
import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.OrientationOptions;
import com.reactnativenavigation.parse.SubtitleOptions;
import com.reactnativenavigation.parse.TitleOptions;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.RenderChecker;
import com.reactnativenavigation.presentation.StackPresenter;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.utils.TitleBarHelper;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.button.IconResolver;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.titlebar.TitleBarReactView;
import com.reactnativenavigation.views.topbar.TopBar;

import org.json.JSONObject;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowLooper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.reactnativenavigation.utils.CollectionUtils.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@LooperMode(LooperMode.Mode.PAUSED)
public class StackPresenterTest extends BaseTest {

    private static final Options EMPTY_OPTIONS = new Options();
    private StackController parent;
    private StackPresenter uut;
    private ViewController child;
    private ViewController otherChild;
    private Activity activity;
    private TopBar topBar;
    private RenderChecker renderChecker;

    private Button textBtn1 = TitleBarHelper.textualButton("btn1");
    private Button textBtn2 = TitleBarHelper.textualButton("btn2");
    private Button componentBtn1 = TitleBarHelper.reactViewButton("btn1_");
    private Button componentBtn2 = TitleBarHelper.reactViewButton("btn2_");
    private Component titleComponent1 = TitleBarHelper.titleComponent("component1");
    private Component titleComponent2 = TitleBarHelper.titleComponent("component2");
    private TopBarController topBarController;
    private ChildControllersRegistry childRegistry;

    @Override
    public void beforeEach() {
        activity = spy(newActivity());
        TitleBarReactViewCreatorMock titleViewCreator = new TitleBarReactViewCreatorMock() {
            @Override
            public TitleBarReactView create(Activity activity, String componentId, String componentName) {
                return spy(super.create(activity, componentId, componentName));
            }
        };
        renderChecker = spy(new RenderChecker());
        uut = spy(new StackPresenter(activity, titleViewCreator, new TopBarBackgroundViewCreatorMock(), new TitleBarButtonCreatorMock(), new IconResolver(activity, ImageLoaderMock.mock()), renderChecker, new Options()));
        createTopBarController();

        parent = TestUtils.newStackController(activity)
                .setTopBarController(topBarController)
                .setStackPresenter(uut)
                .build();

        childRegistry = new ChildControllersRegistry();
        child = spy(new SimpleViewController(activity, childRegistry, "child1", Options.EMPTY));
        otherChild = spy(new SimpleViewController(activity, childRegistry, "child1", Options.EMPTY));
        activity.setContentView(parent.getView());
    }

    @Test
    public void isRendered() {
        Options o1 = new Options();
        o1.topBar.title.component = component(Alignment.Default);
        o1.topBar.background.component = component(Alignment.Default);
        o1.topBar.buttons.right = new ArrayList<>(singletonList(componentBtn1));
        uut.applyChildOptions(o1, parent, child);

        uut.isRendered(child.getView());
        ArgumentCaptor<Collection<ViewController>> controllers = ArgumentCaptor.forClass(Collection.class);
        verify(renderChecker).areRendered(controllers.capture());
        ArrayList<ViewController> items = new ArrayList<>(controllers.getValue());
        assertThat(items.contains(uut.getComponentButtons(child.getView()).get(0))).isTrue();
        assertThat(items.contains(uut.getTitleComponents().get(child.getView()))).isTrue();
        assertThat(items.contains(uut.getBackgroundComponents().get(child.getView()))).isTrue();
        assertThat(items.size()).isEqualTo(3);
    }

    @Test
    public void applyChildOptions_setTitleComponent() {
        Options options = new Options();
        options.topBar.title.component = component(Alignment.Default);
        uut.applyChildOptions(options, parent, child);
        verify(topBar).setTitleComponent(uut.getTitleComponents().get(child.getView()).getView());
    }

    @Test
    public void applyChildOptions_setTitleComponentCreatesOnce() {
        Options options = new Options();
        options.topBar.title.component = component(Alignment.Default);
        uut.applyChildOptions(options, parent, child);

        uut.applyChildOptions(Options.EMPTY, parent, otherChild);

        TitleBarReactViewController titleController = uut.getTitleComponents().get(child.getView());
        uut.applyChildOptions(options, parent, child);
        assertThat(uut.getTitleComponents().size()).isOne();
        assertThat(uut.getTitleComponents().get(child.getView())).isEqualTo(titleController);
    }

    @Test
    public void applyChildOptions_setTitleComponentAlignment() {
        Options options = new Options();
        options.topBar.title.component = component(Alignment.Center);
        uut.applyChildOptions(options, parent, child);
        ArgumentCaptor<View> captor = ArgumentCaptor.forClass(View.class);
        verify(topBar).setTitleComponent(captor.capture());

        Toolbar.LayoutParams lp = (Toolbar.LayoutParams) captor.getValue().getLayoutParams();
        assertThat(lp.gravity).isEqualTo(Gravity.CENTER);
    }

    @Test
    public void onChildDestroyed_destroyTitleComponent() {
        Options options = new Options();
        options.topBar.title.component = component(Alignment.Default);
        uut.applyChildOptions(options, parent, child);

        TitleBarReactView titleView = uut.getTitleComponents().get(child.getView()).getView();
        uut.onChildDestroyed(child);
        verify(titleView).destroy();
    }

    @Test
    public void mergeOrientation() throws Exception {
        Options options = new Options();
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child);
        verify(uut, times(0)).applyOrientation(any());

        JSONObject orientation = new JSONObject().put("orientation", "landscape");
        options.layout.orientation = OrientationOptions.parse(orientation);
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child);
        verify(uut, times(1)).applyOrientation(options.layout.orientation);
    }

    @Test
    public void mergeButtons() {
        uut.mergeChildOptions(EMPTY_OPTIONS, EMPTY_OPTIONS, parent, child);
        verify(topBarController, times(0)).applyRightButtons(any());
        verify(topBarController, times(0)).setLeftButtons(any());

        Options options = new Options();

        Button button = new Button();
        button.text = new Text("btn");
        options.topBar.buttons.right = new ArrayList<>(Collections.singleton(button));
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child);
        verify(topBarController).mergeRightButtons(any(), any());

        options.topBar.buttons.left = new ArrayList<>();
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child);
        verify(topBarController).setLeftButtons(any());
    }

    @Test
    public void mergeButtons_previousRightButtonsAreDestroyed() {
        Options options = new Options();
        options.topBar.buttons.right = new ArrayList<>(singletonList(componentBtn1));
        uut.applyChildOptions(options, parent, child);
        List<TitleBarButtonController> initialButtons = uut.getComponentButtons(child.getView());
        forEach(initialButtons, ViewController::ensureViewIsCreated);

        options.topBar.buttons.right = new ArrayList<>(singletonList(componentBtn2));
        uut.mergeChildOptions(options, Options.EMPTY, parent, child);
        for (TitleBarButtonController button : initialButtons) {
            assertThat(button.isDestroyed()).isTrue();
        }
    }

    @Test
    public void mergeRightButtons_mergingButtonsOnlyDestroysRightButtons() {
        Options a = new Options();
        a.topBar.buttons.right = new ArrayList<>(singletonList(componentBtn1));
        a.topBar.buttons.left = new ArrayList<>(singletonList(componentBtn2));
        uut.applyChildOptions(a, parent, child);
        List<TitleBarButtonController> initialButtons = uut.getComponentButtons(child.getView());
        forEach(initialButtons, ViewController::ensureViewIsCreated);

        Options b = new Options();
        b.topBar.buttons.right = new ArrayList<>(singletonList(componentBtn2));
        uut.mergeChildOptions(b, Options.EMPTY, parent, child);
        assertThat(initialButtons.get(0).isDestroyed()).isTrue();
        assertThat(initialButtons.get(1).isDestroyed()).isFalse();
    }

    @Test
    public void mergeRightButtons_buttonsAreCreatedOnlyIfNeeded() {
        Options toApply = new Options();
        textBtn1.color = new Colour(Color.GREEN);
        toApply.topBar.buttons.right = new ArrayList<>(asList(textBtn1, componentBtn1));
        uut.applyChildOptions(toApply, parent, child);

        ArgumentCaptor<List<TitleBarButtonController>> captor1 = ArgumentCaptor.forClass(List.class);
        verify(topBarController).applyRightButtons(captor1.capture());
        assertThat(topBar.getTitleBar().getMenu().size()).isEqualTo(2);
        List<TitleBarButtonController> appliedButtons = captor1.getValue();

        Options toMerge = new Options();
        toMerge.topBar.buttons.right = new ArrayList(requireNonNull(map(toApply.topBar.buttons.right, Button::copy)));
        toMerge.topBar.buttons.right.get(0).color = new Colour(Color.RED);
        toMerge.topBar.buttons.right.add(1, componentBtn2);
        uut.mergeChildOptions(toMerge, Options.EMPTY, parent, child);

        assertThat(topBar.getTitleBar().getMenu().size()).isEqualTo(3);
        ArgumentCaptor<List<TitleBarButtonController>> captor2 = ArgumentCaptor.forClass(List.class);
        verify(topBarController).mergeRightButtons(captor2.capture(), any());
        List<TitleBarButtonController> mergedButtons = captor2.getValue();
        assertThat(mergedButtons).hasSize(3);
        assertThat(appliedButtons.get(0)).isNotEqualTo(mergedButtons.get(0));
        assertThat(appliedButtons.get(1)).isEqualTo(mergedButtons.get(2));
    }

    @Test
    public void mergeButtons_mergingLeftButtonsDoesNotDestroyRightButtons() {
        Options a = new Options();
        a.topBar.buttons.right = new ArrayList<>(singletonList(componentBtn1));
        a.topBar.buttons.left = new ArrayList<>(singletonList(componentBtn2));
        uut.applyChildOptions(a, parent, child);
        List<TitleBarButtonController> initialButtons = uut.getComponentButtons(child.getView());
        forEach(initialButtons, ViewController::ensureViewIsCreated);

        Options b = new Options();
        b.topBar.buttons.left = new ArrayList<>(singletonList(componentBtn2));
        uut.mergeChildOptions(b, Options.EMPTY, parent, child);
        assertThat(initialButtons.get(0).isDestroyed()).isFalse();
    }

    @Test
    public void mergeButtons_backButtonIsRemovedIfVisibleFalse() {
        ViewController pushedChild = spy(new SimpleViewController(activity, childRegistry, "child2", new Options()));
        disablePushAnimation(child, pushedChild);
        parent.push(child, new CommandListenerAdapter());

        assertThat(topBar.getTitleBar().getNavigationIcon()).isNull();

        parent.push(pushedChild, new CommandListenerAdapter());
        ShadowLooper.idleMainLooper();
        verify(pushedChild).onViewWillAppear();
        assertThat(topBar.getTitleBar().getNavigationIcon()).isInstanceOf(BackDrawable.class);

        Options backButtonHidden = new Options();
        backButtonHidden.topBar.buttons.back.setHidden();
        uut.mergeChildOptions(backButtonHidden, backButtonHidden, parent, child);

        ShadowLooper.idleMainLooper();
        assertThat(topBar.getTitleBar().getNavigationIcon()).isNull();
    }

    @Test
    public void mergeTopBarOptions() {
        Options options = new Options();
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child);
        assertTopBarOptions(options, 0);

        TitleOptions title = new TitleOptions();
        title.text = new Text("abc");
        title.component.name = new Text("someComponent");
        title.component.componentId = new Text("compId");
        title.color = new Colour(0);
        title.fontSize = new Fraction(1.0f);
        title.fontFamily = Typeface.DEFAULT_BOLD;
        options.topBar.title = title;
        SubtitleOptions subtitleOptions = new SubtitleOptions();
        subtitleOptions.text = new Text("Sub");
        subtitleOptions.color = new Colour(1);
        options.topBar.subtitle = subtitleOptions;
        options.topBar.background.color = new Colour(0);
        options.topBar.testId = new Text("test123");
        options.topBar.animate = new Bool(false);
        options.topBar.visible = new Bool(false);
        options.topBar.drawBehind = new Bool(false);
        options.topBar.hideOnScroll = new Bool(false);
        options.topBar.validate();

        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child);

        assertTopBarOptions(options, 1);

        options.topBar.drawBehind = new Bool(true);
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child);
    }

    @Test
    public void applyTopBarOptions_setTitleComponent() {
        Options applyComponent = new Options();
        applyComponent.topBar.title.component.name = new Text("Component1");
        applyComponent.topBar.title.component.componentId = new Text("Component1id");
        uut.applyChildOptions(applyComponent, parent, child);
        verify(topBarController).setTitleComponent(any());
    }

    @Test
    public void mergeTopBarOptions_settingTitleDestroysComponent() {
        Options componentOptions = new Options();
        componentOptions.topBar.title.component = titleComponent1;
        uut.applyChildOptions(componentOptions, parent, child);
        ArgumentCaptor<TitleBarReactViewController> applyCaptor = ArgumentCaptor.forClass(TitleBarReactViewController.class);
        verify(topBarController).setTitleComponent(applyCaptor.capture());

        Options titleOptions = new Options();
        titleOptions.topBar.title.text = new Text("Some title");
        uut.mergeChildOptions(titleOptions, Options.EMPTY, parent, child);
        assertThat(applyCaptor.getValue().isDestroyed()).isTrue();
    }

    @Test
    public void mergeTopBarOptions_doesNotRecreateTitleComponentIfEquals() {
        Options options = new Options();
        options.topBar.title.component = titleComponent1;
        uut.applyChildOptions(options, parent, child);
        ArgumentCaptor<TitleBarReactViewController> applyCaptor = ArgumentCaptor.forClass(TitleBarReactViewController.class);
        verify(topBarController).setTitleComponent(applyCaptor.capture());

        uut.mergeChildOptions(options, Options.EMPTY, parent, child);
        verify(topBarController, times(2)).setTitleComponent(applyCaptor.getValue());
    }

    @Test
    public void mergeTopBarOptions_previousTitleComponentIsDestroyed() {
        Options options = new Options();
        options.topBar.title.component = titleComponent1;
        uut.applyChildOptions(options, parent, child);
        ArgumentCaptor<TitleBarReactViewController> applyCaptor = ArgumentCaptor.forClass(TitleBarReactViewController.class);
        verify(topBarController).setTitleComponent(applyCaptor.capture());

        Options toMerge = new Options();
        toMerge.topBar.title.component = titleComponent2;
        uut.mergeChildOptions(toMerge, Options.EMPTY, parent, child);
        ArgumentCaptor<TitleBarReactViewController> mergeCaptor = ArgumentCaptor.forClass(TitleBarReactViewController.class);
        verify(topBarController, times(2)).setTitleComponent(mergeCaptor.capture());

        assertThat(applyCaptor.getValue()).isNotEqualTo(mergeCaptor.getValue());
        assertThat(applyCaptor.getValue().isDestroyed()).isTrue();
    }

    @Test
    public void mergeTopTabsOptions() {
        Options options = new Options();
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child);
        verify(topBar, times(0)).applyTopTabsColors(any(), any());
        verify(topBar, times(0)).applyTopTabsFontSize(any());
        verify(topBar, times(0)).setTopTabsVisible(anyBoolean());

        options.topTabs.selectedTabColor = new Colour(1);
        options.topTabs.unselectedTabColor = new Colour(1);
        options.topTabs.fontSize = new Number(1);
        options.topTabs.visible = new Bool(true);
        uut.mergeChildOptions(options, EMPTY_OPTIONS, parent, child);
        verify(topBar, times(1)).applyTopTabsColors(options.topTabs.selectedTabColor, options.topTabs.unselectedTabColor);
        verify(topBar, times(1)).applyTopTabsFontSize(options.topTabs.fontSize);
        verify(topBar, times(1)).setTopTabsVisible(anyBoolean());
    }

    @Test
    public void applyInitialChildLayoutOptions() {
        Options options = new Options();
        options.topBar.visible = new Bool(false);
        options.topBar.animate = new Bool(true);

        uut.applyInitialChildLayoutOptions(options);
        verify(topBarController).hide();
    }

    @Test
    public void mergeOptions_defaultOptionsAreNotApplied() {
        Options defaultOptions = new Options();
        defaultOptions.topBar.background.color = new Colour(10);
        uut.setDefaultOptions(defaultOptions);

        Options childOptions = new Options();
        childOptions.topBar.title.text = new Text("someText");
        uut.mergeChildOptions(childOptions, EMPTY_OPTIONS, parent, child);

        verify(topBar, times(0)).setBackgroundColor(anyInt());
    }

    @Test
    public void applyButtons_buttonColorIsMergedToButtons() {
        Options options = new Options();
        Button rightButton1 = new Button();
        Button rightButton2 = new Button();
        Button leftButton = new Button();

        options.topBar.rightButtonColor = new Colour(10);
        options.topBar.leftButtonColor = new Colour(100);

        options.topBar.buttons.right = new ArrayList<>();
        options.topBar.buttons.right.add(rightButton1);
        options.topBar.buttons.right.add(rightButton2);

        options.topBar.buttons.left = new ArrayList<>();
        options.topBar.buttons.left.add(leftButton);

        uut.applyChildOptions(options, parent, child);
        ArgumentCaptor<List<TitleBarButtonController>> rightCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBarController).applyRightButtons(rightCaptor.capture());
        assertThat(rightCaptor.getValue().get(0).getButton().color.get()).isEqualTo(options.topBar.rightButtonColor.get());
        assertThat(rightCaptor.getValue().get(1).getButton().color.get()).isEqualTo(options.topBar.rightButtonColor.get());
        assertThat(rightCaptor.getValue().get(0)).isNotEqualTo(rightButton1);
        assertThat(rightCaptor.getValue().get(1)).isNotEqualTo(rightButton2);

        ArgumentCaptor<List<TitleBarButtonController>> leftCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBarController).setLeftButtons(leftCaptor.capture());
        assertThat(leftCaptor.getValue().get(0).getButton().color).isEqualTo(options.topBar.leftButtonColor);
        assertThat(leftCaptor.getValue().get(0)).isNotEqualTo(leftButton);
    }

    @Test
    public void applyTopBarOptions_backgroundComponentIsCreatedOnceIfNameAndIdAreEqual() {
        Options o = new Options();
        o.topBar.background.component.name = new Text("comp");
        o.topBar.background.component.componentId = new Text("compId");

        uut.applyChildOptions(o, parent, Mocks.viewController());
        assertThat(uut.getBackgroundComponents().size()).isOne();

        uut.applyChildOptions(o, parent, Mocks.viewController());
        assertThat(uut.getBackgroundComponents().size()).isOne();
    }

    @Test
    public void mergeChildOptions_buttonColorIsResolvedFromAppliedOptions() {
        Options appliedOptions = new Options();
        appliedOptions.topBar.rightButtonColor = new Colour(10);
        appliedOptions.topBar.leftButtonColor = new Colour(100);

        Options options2 = new Options();
        Button rightButton1 = new Button();
        Button rightButton2 = new Button();
        Button leftButton = new Button();

        options2.topBar.buttons.right = new ArrayList<>();
        options2.topBar.buttons.right.add(rightButton1);
        options2.topBar.buttons.right.add(rightButton2);

        options2.topBar.buttons.left = new ArrayList<>();
        options2.topBar.buttons.left.add(leftButton);

        uut.mergeChildOptions(options2, appliedOptions, parent, child);
        ArgumentCaptor<List<TitleBarButtonController>> rightCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBarController).mergeRightButtons(rightCaptor.capture(), any());
        assertThat(rightCaptor.getValue().get(0).getButton().color.get()).isEqualTo(appliedOptions.topBar.rightButtonColor.get());
        assertThat(rightCaptor.getValue().get(1).getButton().color.get()).isEqualTo(appliedOptions.topBar.rightButtonColor.get());
        assertThat(rightCaptor.getValue().get(0)).isNotEqualTo(rightButton1);
        assertThat(rightCaptor.getValue().get(1)).isNotEqualTo(rightButton2);

        ArgumentCaptor<List<TitleBarButtonController>> leftCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBarController).setLeftButtons(leftCaptor.capture());
        assertThat(leftCaptor.getValue().get(0).getButton().color.get()).isEqualTo(appliedOptions.topBar.leftButtonColor.get());
        assertThat(leftCaptor.getValue().get(0)).isNotEqualTo(leftButton);
    }

    @Test
    public void mergeChildOptions_buttonColorIsResolvedFromMergedOptions() {
        Options resolvedOptions = new Options();
        resolvedOptions.topBar.rightButtonColor = new Colour(10);
        resolvedOptions.topBar.leftButtonColor = new Colour(100);

        Options options2 = new Options();
        Button rightButton1 = new Button();
        Button rightButton2 = new Button();
        Button leftButton = new Button();

        options2.topBar.buttons.right = new ArrayList<>();
        options2.topBar.buttons.right.add(rightButton1);
        options2.topBar.buttons.right.add(rightButton2);

        options2.topBar.buttons.left = new ArrayList<>();
        options2.topBar.buttons.left.add(leftButton);

        uut.mergeChildOptions(options2, resolvedOptions, parent, child);
        ArgumentCaptor<List<TitleBarButtonController>> rightCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBarController).mergeRightButtons(rightCaptor.capture(), any());
        assertThat(rightCaptor.getValue().get(0).getButton().color.get()).isEqualTo(resolvedOptions.topBar.rightButtonColor.get());
        assertThat(rightCaptor.getValue().get(1).getButton().color.get()).isEqualTo(resolvedOptions.topBar.rightButtonColor.get());
        assertThat(rightCaptor.getValue().get(0)).isNotEqualTo(rightButton1);
        assertThat(rightCaptor.getValue().get(1)).isNotEqualTo(rightButton2);

        ArgumentCaptor<List<TitleBarButtonController>> leftCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBarController).setLeftButtons(leftCaptor.capture());
        assertThat(leftCaptor.getValue().get(0).getButton().color.get()).isEqualTo(resolvedOptions.topBar.leftButtonColor.get());
        assertThat(leftCaptor.getValue().get(0)).isNotEqualTo(leftButton);
    }

    @Test
    public void getButtonControllers_buttonControllersArePassedToTopBar() {
        Options options = new Options();
        options.topBar.buttons.right = new ArrayList<>(singletonList(textBtn1));
        options.topBar.buttons.left = new ArrayList<>(singletonList(textBtn1));
        uut.applyChildOptions(options, parent, child);

        ArgumentCaptor<List<TitleBarButtonController>> rightCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<TitleBarButtonController>> leftCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBarController).applyRightButtons(rightCaptor.capture());
        verify(topBarController).setLeftButtons(leftCaptor.capture());

        assertThat(rightCaptor.getValue().size()).isOne();
        assertThat(leftCaptor.getValue().size()).isOne();
    }

    @Test
    public void getButtonControllers_storesButtonsByComponent() {
        Options options = new Options();
        options.topBar.buttons.right = new ArrayList<>(singletonList(textBtn1));
        options.topBar.buttons.left = new ArrayList<>(singletonList(textBtn2));
        uut.applyChildOptions(options, parent, child);

        List<TitleBarButtonController> componentButtons = uut.getComponentButtons(child.getView());
        assertThat(componentButtons.size()).isEqualTo(2);
        assertThat(componentButtons.get(0).getButton().text.get()).isEqualTo(textBtn1.text.get());
        assertThat(componentButtons.get(1).getButton().text.get()).isEqualTo(textBtn2.text.get());
    }

    @Test
    public void getButtonControllers_createdOnce() {
        Options options = new Options();
        options.topBar.buttons.right = new ArrayList<>(singletonList(textBtn1));
        options.topBar.buttons.left = new ArrayList<>(singletonList(textBtn2));

        uut.applyChildOptions(options, parent, child);
        List<TitleBarButtonController> buttons1 = uut.getComponentButtons(child.getView());

        uut.applyChildOptions(options, parent, child);
        List<TitleBarButtonController> buttons2 = uut.getComponentButtons(child.getView());
        for (int i = 0; i < 2; i++) {
            assertThat(buttons1.get(i)).isEqualTo(buttons2.get(i));
        }
    }

    @Test
    public void applyButtons_doesNotDestroyOtherComponentButtons() {
        Options options = new Options();
        options.topBar.buttons.right = new ArrayList<>(singletonList(componentBtn1));
        options.topBar.buttons.left = new ArrayList<>(singletonList(componentBtn2));
        uut.applyChildOptions(options, parent, child);
        List<TitleBarButtonController> buttons = uut.getComponentButtons(child.getView());
        forEach(buttons, ViewController::ensureViewIsCreated);

        uut.applyChildOptions(options, parent, otherChild);
        for (TitleBarButtonController button : buttons) {
            assertThat(button.isDestroyed()).isFalse();
        }
    }

    @Test
    public void onChildDestroyed_destroyedButtons() {
        Options options = new Options();
        options.topBar.buttons.right = new ArrayList<>(singletonList(componentBtn1));
        options.topBar.buttons.left = new ArrayList<>(singletonList(componentBtn2));
        uut.applyChildOptions(options, parent, child);
        List<TitleBarButtonController> buttons = uut.getComponentButtons(child.getView());
        forEach(buttons, ViewController::ensureViewIsCreated);

        uut.onChildDestroyed(child);
        for (TitleBarButtonController button : buttons) {
            assertThat(button.isDestroyed()).isTrue();
        }
        assertThat(uut.getComponentButtons(child.getView(), null)).isNull();
    }

    @Test
    public void applyTopInsets_topBarIsDrawnUnderStatusBarIfDrawBehindIsTrue() {
        Options options = new Options();
        options.statusBar.drawBehind = new Bool(true);
        uut.applyTopInsets(parent, child);

        assertThat(topBar.getY()).isEqualTo(0);
    }

    @Test
    public void applyTopInsets_topBarIsDrawnUnderStatusBarIfStatusBarIsHidden() {
        Options options = new Options();
        options.statusBar.visible = new Bool(false);
        uut.applyTopInsets(parent, Mocks.viewController());

        assertThat(topBar.getY()).isEqualTo(0);
    }

    @Test
    public void applyTopInsets_delegatesToChild() {
        uut.applyTopInsets(parent, child);
        verify(child).applyTopInset();
    }

    private void assertTopBarOptions(Options options, int t) {
        if (options.topBar.title.component.hasValue()) {
            verify(topBar, times(0)).setTitle(any());
            verify(topBar, times(0)).setSubtitle(any());
        } else {
            verify(topBar, times(t)).setTitle(any());
            verify(topBar, times(t)).setSubtitle(any());
        }
        verify(topBar, times(t)).setTitleComponent(any());
        verify(topBar, times(t)).setBackgroundColor(anyInt());
        verify(topBar, times(t)).setTitleTextColor(anyInt());
        verify(topBar, times(t)).setTitleFontSize(anyDouble());
        verify(topBar, times(t)).setTitleTypeface(any());
        verify(topBar, times(t)).setSubtitleColor(anyInt());
        verify(topBar, times(t)).setTestId(any());
        verify(topBarController, times(t)).hide();
    }

    private TopBar mockTopBar() {
        TopBar topBar = mock(TopBar.class);
        Toolbar toolbar = new Toolbar(activity);
        toolbar.addView(new ActionMenuView(activity));
        when(topBar.getTitleBar()).then(invocation -> toolbar);
        when(topBar.getContext()).then(invocation -> activity);
        when(topBar.dispatchApplyWindowInsets(any())).then(invocation -> invocation.getArguments()[0]);
        when(topBar.getLayoutParams()).thenReturn(new CoordinatorLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        return topBar;
    }

    private void createTopBarController() {
        topBarController = spy(new TopBarController() {
            @Override
            protected TopBar createTopBar(Context context, StackLayout stackLayout) {
                topBar = spy(super.createTopBar(context, stackLayout));
                topBar.layout(0, 0, 1000, UiUtils.getTopBarHeight(activity));
                return topBar;
            }
        });
    }

    private Component component(Alignment alignment) {
        Component component = new Component();
        component.name = new Text("myComp");
        component.alignment = alignment;
        component.componentId = new Text("compId");
        return component;
    }
}

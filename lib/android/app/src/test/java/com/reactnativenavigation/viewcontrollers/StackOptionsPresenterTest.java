package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.TestComponentLayout;
import com.reactnativenavigation.mocks.TestReactView;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
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
import com.reactnativenavigation.presentation.StackOptionsPresenter;
import com.reactnativenavigation.utils.TitleBarHelper;
import com.reactnativenavigation.views.titlebar.TitleBarReactView;
import com.reactnativenavigation.views.topbar.TopBar;

import org.json.JSONObject;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.reactnativenavigation.utils.CollectionUtils.forEach;
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

public class StackOptionsPresenterTest extends BaseTest {

    private static final Options EMPTY_OPTIONS = new Options();
    private StackOptionsPresenter uut;
    private TestComponentLayout child;
    private TestComponentLayout otherChild;
    private Activity activity;
    private TopBar topBar;

    private Button textBtn1 = TitleBarHelper.textualButton("btn1");
    private Button textBtn2 = TitleBarHelper.textualButton("btn2");
    private Button componentBtn1 = TitleBarHelper.reactViewButton("btn1_");
    private Button componentBtn2 = TitleBarHelper.reactViewButton("btn2_");

    @Override
    public void beforeEach() {
        activity = spy(newActivity());
        //noinspection Convert2Lambda
        TitleBarButtonController.OnClickListener onClickListener = spy(new TitleBarButtonController.OnClickListener() {
            @Override
            public void onPress(String buttonId) {
                Log.i("TopBarTest", "onPress: " + buttonId);
            }
        });

        TitleBarReactViewCreatorMock titleViewCreator = new TitleBarReactViewCreatorMock() {
            @Override
            public TitleBarReactView create(Activity activity, String componentId, String componentName) {
                return spy(super.create(activity, componentId, componentName));
            }
        };
        uut = spy(new StackOptionsPresenter(activity, titleViewCreator, new TopBarButtonCreatorMock(), ImageLoaderMock.mock(), new Options()));
        topBar = mockTopBar();
        uut.bindView(topBar);
        uut.setButtonOnClickListener(onClickListener);
        child = spy(new TestComponentLayout(activity, new TestReactView(activity)));
        otherChild = new TestComponentLayout(activity, new TestReactView(activity));
    }

    @Test
    public void applyChildOptions_setTitleComponent() {
        Options options = new Options();
        options.topBar.title.component = component(Alignment.Default);
        uut.applyChildOptions(options, child);
        verify(topBar).setTitleComponent(uut.getTitleComponents().get(child).getView());
    }

    @Test
    public void applyChildOptions_setTitleComponentCreatesOnce() {
        Options options = new Options();
        options.topBar.title.component = component(Alignment.Default);
        uut.applyChildOptions(options, child);

        uut.applyChildOptions(new Options(), otherChild);

        TitleBarReactViewController titleController = uut.getTitleComponents().get(child);
        uut.applyChildOptions(options, child);
        assertThat(uut.getTitleComponents().size()).isOne();
        assertThat(uut.getTitleComponents().get(child)).isEqualTo(titleController);
    }

    @Test
    public void applyChildOptions_setTitleComponentAlignment() {
        Options options = new Options();
        options.topBar.title.component = component(Alignment.Center);
        uut.applyChildOptions(options, child);
        ArgumentCaptor<View> captor = ArgumentCaptor.forClass(View.class);
        verify(topBar).setTitleComponent(captor.capture());

        Toolbar.LayoutParams lp = (Toolbar.LayoutParams) captor.getValue().getLayoutParams();
        assertThat(lp.gravity).isEqualTo(Gravity.CENTER);
    }

    @Test
    public void onChildDestroyed_destroyTitleComponent() {
        Options options = new Options();
        options.topBar.title.component = component(Alignment.Default);
        uut.applyChildOptions(options, child);

        TitleBarReactView titleView = uut.getTitleComponents().get(child).getView();
        uut.onChildDestroyed(child);
        verify(titleView).destroy();
    }

    @Test
    public void mergeOrientation() throws Exception {
        Options options = new Options();
        uut.mergeChildOptions(options, EMPTY_OPTIONS, child);
        verify(uut, times(0)).applyOrientation(any());

        JSONObject orientation = new JSONObject().put("orientation", "landscape");
        options.layout.orientation = OrientationOptions.parse(orientation);
        uut.mergeChildOptions(options, EMPTY_OPTIONS, child);
        verify(uut, times(1)).applyOrientation(options.layout.orientation);
    }

    @Test
    public void mergeButtons() {
        uut.mergeChildOptions(EMPTY_OPTIONS, EMPTY_OPTIONS, child);
        verify(topBar, times(0)).setRightButtons(any());
        verify(topBar, times(0)).setLeftButtons(any());

        Options options = new Options();
        options.topBar.buttons.right = new ArrayList<>();
        uut.mergeChildOptions(options, EMPTY_OPTIONS, child);
        verify(topBar, times(1)).setRightButtons(any());

        options.topBar.buttons.left = new ArrayList<>();
        uut.mergeChildOptions(options, EMPTY_OPTIONS, child);
        verify(topBar, times(1)).setLeftButtons(any());
    }

    @Test
    public void mergeButtons_previousRightButtonsAreDestroyed() {
        Options options = new Options();
        options.topBar.buttons.right = new ArrayList<>(Collections.singletonList(componentBtn1));
        uut.applyChildOptions(options, child);
        List<TitleBarButtonController> initialButtons = uut.getComponentButtons(child);
        forEach(initialButtons, ViewController::ensureViewIsCreated);

        options.topBar.buttons.right = new ArrayList<>(Collections.singletonList(componentBtn2));
        uut.mergeChildOptions(options, new Options(), child);
        for (TitleBarButtonController button : initialButtons) {
            assertThat(button.isDestroyed()).isTrue();
        }
    }

    @Test
    public void mergeButtons_mergingRightButtonsOnlyDestroysRightButtons() {
        Options a = new Options();
        a.topBar.buttons.right = new ArrayList<>(Collections.singletonList(componentBtn1));
        a.topBar.buttons.left = new ArrayList<>(Collections.singletonList(componentBtn2));
        uut.applyChildOptions(a, child);
        List<TitleBarButtonController> initialButtons = uut.getComponentButtons(child);
        forEach(initialButtons, ViewController::ensureViewIsCreated);

        Options b = new Options();
        b.topBar.buttons.right = new ArrayList<>(Collections.singletonList(componentBtn2));
        uut.mergeChildOptions(b, new Options(), child);
        assertThat(initialButtons.get(0).isDestroyed()).isTrue();
        assertThat(initialButtons.get(1).isDestroyed()).isFalse();
    }

    @Test
    public void mergeButtons_mergingLeftButtonsOnlyDestroysLeftButtons() {
        Options a = new Options();
        a.topBar.buttons.right = new ArrayList<>(Collections.singletonList(componentBtn1));
        a.topBar.buttons.left = new ArrayList<>(Collections.singletonList(componentBtn2));
        uut.applyChildOptions(a, child);
        List<TitleBarButtonController> initialButtons = uut.getComponentButtons(child);
        forEach(initialButtons, ViewController::ensureViewIsCreated);

        Options b = new Options();
        b.topBar.buttons.left = new ArrayList<>(Collections.singletonList(componentBtn2));
        uut.mergeChildOptions(b, new Options(), child);
        assertThat(initialButtons.get(0).isDestroyed()).isFalse();
        assertThat(initialButtons.get(1).isDestroyed()).isTrue();
    }

    @Test
    public void mergeTopBarOptions() {
        Options options = new Options();
        uut.mergeChildOptions(options, EMPTY_OPTIONS, child);
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

        uut.mergeChildOptions(options, EMPTY_OPTIONS, child);

        assertTopBarOptions(options, 1);

        options.topBar.drawBehind = new Bool(true);
        uut.mergeChildOptions(options, EMPTY_OPTIONS, child);
        verify(child, times(1)).drawBehindTopBar();
    }

    @Test
    public void mergeTopTabsOptions() {
        Options options = new Options();
        uut.mergeChildOptions(options, EMPTY_OPTIONS, child);
        verify(topBar, times(0)).applyTopTabsColors(any(), any());
        verify(topBar, times(0)).applyTopTabsFontSize(any());
        verify(topBar, times(0)).setTopTabsVisible(anyBoolean());

        options.topTabs.selectedTabColor = new Colour(1);
        options.topTabs.unselectedTabColor = new Colour(1);
        options.topTabs.fontSize = new Number(1);
        options.topTabs.visible = new Bool(true);
        uut.mergeChildOptions(options, EMPTY_OPTIONS, child);
        verify(topBar, times(1)).applyTopTabsColors(options.topTabs.selectedTabColor, options.topTabs.unselectedTabColor);
        verify(topBar, times(1)).applyTopTabsFontSize(options.topTabs.fontSize);
        verify(topBar, times(1)).setTopTabsVisible(anyBoolean());
    }

    @Test
    public void mergeTopTabOptions() {
        Options options = new Options();
        uut.mergeChildOptions(options, EMPTY_OPTIONS, child);

        verify(topBar, times(0)).setTopTabFontFamily(anyInt(), any());

        options.topTabOptions.tabIndex = 1;
        options.topTabOptions.fontFamily = Typeface.DEFAULT_BOLD;
        uut.mergeChildOptions(options, EMPTY_OPTIONS, child);

        verify(topBar, times(1)).setTopTabFontFamily(1, Typeface.DEFAULT_BOLD);
    }

    @Test
    public void applyLayoutParamsOptions() {
        Options options = new Options();
        options.topBar.visible = new Bool(false);
        options.topBar.animate = new Bool(false);
        View view = Mockito.mock(View.class, Mockito.withSettings().extraInterfaces(com.reactnativenavigation.views.Component.class));

        uut.applyLayoutParamsOptions(options, view);
        verify(topBar).hide();
    }

    @Test
    public void mergeOptions_defaultOptionsAreNotApplied() {
        Options defaultOptions = new Options();
        defaultOptions.topBar.background.color = new Colour(10);
        uut.setDefaultOptions(defaultOptions);

        Options childOptions = new Options();
        childOptions.topBar.title.text = new Text("someText");
        uut.mergeChildOptions(childOptions, EMPTY_OPTIONS, child);

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

        uut.applyChildOptions(options, child);
        ArgumentCaptor<List<TitleBarButtonController>> rightCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBar).setRightButtons(rightCaptor.capture());
        assertThat(rightCaptor.getValue().get(0).getButton().color.get()).isEqualTo(options.topBar.rightButtonColor.get());
        assertThat(rightCaptor.getValue().get(1).getButton().color.get()).isEqualTo(options.topBar.rightButtonColor.get());
        assertThat(rightCaptor.getValue().get(0)).isNotEqualTo(rightButton1);
        assertThat(rightCaptor.getValue().get(1)).isNotEqualTo(rightButton2);

        ArgumentCaptor<List<TitleBarButtonController>> leftCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBar).setLeftButtons(leftCaptor.capture());
        assertThat(leftCaptor.getValue().get(0).getButton().color).isEqualTo(options.topBar.leftButtonColor);
        assertThat(leftCaptor.getValue().get(0)).isNotEqualTo(leftButton);
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

        uut.mergeChildOptions(options2, appliedOptions, child);
        ArgumentCaptor<List<TitleBarButtonController>> rightCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBar, times(1)).setRightButtons(rightCaptor.capture());
        assertThat(rightCaptor.getValue().get(0).getButton().color.get()).isEqualTo(appliedOptions.topBar.rightButtonColor.get());
        assertThat(rightCaptor.getValue().get(1).getButton().color.get()).isEqualTo(appliedOptions.topBar.rightButtonColor.get());
        assertThat(rightCaptor.getValue().get(0)).isNotEqualTo(rightButton1);
        assertThat(rightCaptor.getValue().get(1)).isNotEqualTo(rightButton2);

        ArgumentCaptor<List<TitleBarButtonController>> leftCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBar, times(1)).setLeftButtons(leftCaptor.capture());
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

        uut.mergeChildOptions(options2, resolvedOptions, child);
        ArgumentCaptor<List<TitleBarButtonController>> rightCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBar).setRightButtons(rightCaptor.capture());
        assertThat(rightCaptor.getValue().get(0).getButton().color.get()).isEqualTo(resolvedOptions.topBar.rightButtonColor.get());
        assertThat(rightCaptor.getValue().get(1).getButton().color.get()).isEqualTo(resolvedOptions.topBar.rightButtonColor.get());
        assertThat(rightCaptor.getValue().get(0)).isNotEqualTo(rightButton1);
        assertThat(rightCaptor.getValue().get(1)).isNotEqualTo(rightButton2);

        ArgumentCaptor<List<TitleBarButtonController>> leftCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBar).setLeftButtons(leftCaptor.capture());
        assertThat(leftCaptor.getValue().get(0).getButton().color.get()).isEqualTo(resolvedOptions.topBar.leftButtonColor.get());
        assertThat(leftCaptor.getValue().get(0)).isNotEqualTo(leftButton);
    }

    @Test
    public void getButtonControllers_buttonControllersArePassedToTopBar() {
        Options options = new Options();
        options.topBar.buttons.right = new ArrayList<>(Collections.singletonList(textBtn1));
        options.topBar.buttons.left = new ArrayList<>(Collections.singletonList(textBtn1));
        uut.applyChildOptions(options, child);

        ArgumentCaptor<List<TitleBarButtonController>> rightCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<TitleBarButtonController>> leftCaptor = ArgumentCaptor.forClass(List.class);
        verify(topBar).setRightButtons(rightCaptor.capture());
        verify(topBar).setLeftButtons(leftCaptor.capture());

        assertThat(rightCaptor.getValue().size()).isOne();
        assertThat(leftCaptor.getValue().size()).isOne();
    }

    @Test
    public void getButtonControllers_storesButtonsByComponent() {
        Options options = new Options();
        options.topBar.buttons.right = new ArrayList<>(Collections.singletonList(textBtn1));
        options.topBar.buttons.left = new ArrayList<>(Collections.singletonList(textBtn2));
        uut.applyChildOptions(options, child);

        List<TitleBarButtonController> componentButtons = uut.getComponentButtons(child);
        assertThat(componentButtons.size()).isEqualTo(2);
        assertThat(componentButtons.get(0).getButton().text.get()).isEqualTo(textBtn1.text.get());
        assertThat(componentButtons.get(1).getButton().text.get()).isEqualTo(textBtn2.text.get());
    }

    @Test
    public void getButtonControllers_createdOnce() {
        Options options = new Options();
        options.topBar.buttons.right = new ArrayList<>(Collections.singletonList(textBtn1));
        options.topBar.buttons.left = new ArrayList<>(Collections.singletonList(textBtn2));

        uut.applyChildOptions(options, child);
        List<TitleBarButtonController> buttons1 = uut.getComponentButtons(child);

        uut.applyChildOptions(options, child);
        List<TitleBarButtonController> buttons2 = uut.getComponentButtons(child);
        for (int i = 0; i < 2; i++) {
            assertThat(buttons1.get(i)).isEqualTo(buttons2.get(i));
        }
    }

    @Test
    public void applyButtons_doesNotDestroyOtherComponentButtons() {
        Options options = new Options();
        options.topBar.buttons.right = new ArrayList<>(Collections.singletonList(componentBtn1));
        options.topBar.buttons.left = new ArrayList<>(Collections.singletonList(componentBtn2));
        uut.applyChildOptions(options, child);
        List<TitleBarButtonController> buttons = uut.getComponentButtons(child);
        forEach(buttons, ViewController::ensureViewIsCreated);

        uut.applyChildOptions(options, otherChild);
        for (TitleBarButtonController button : buttons) {
            assertThat(button.isDestroyed()).isFalse();
        }
    }

    @Test
    public void onChildDestroyed_destroyedButtons() {
        Options options = new Options();
        options.topBar.buttons.right = new ArrayList<>(Collections.singletonList(componentBtn1));
        options.topBar.buttons.left = new ArrayList<>(Collections.singletonList(componentBtn2));
        uut.applyChildOptions(options, child);
        List<TitleBarButtonController> buttons = uut.getComponentButtons(child);
        forEach(buttons, ViewController::ensureViewIsCreated);

        uut.onChildDestroyed(child);
        for (TitleBarButtonController button : buttons) {
            assertThat(button.isDestroyed()).isTrue();
        }
        assertThat(uut.getComponentButtons(child, null)).isNull();
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
        verify(topBar, times(t)).hide();
        verify(child, times(t)).drawBelowTopBar(topBar);
        verify(child, times(0)).drawBehindTopBar();
    }

    private TopBar mockTopBar() {
        TopBar topBar = mock(TopBar.class);
        when(topBar.getContext()).then(invocation -> activity);
        return topBar;
    }

    private Component component(Alignment alignment) {
        Component component = new Component();
        component.name = new Text("myComp");
        component.alignment = alignment;
        component.componentId = new Text("compId");
        return component;
    }
}

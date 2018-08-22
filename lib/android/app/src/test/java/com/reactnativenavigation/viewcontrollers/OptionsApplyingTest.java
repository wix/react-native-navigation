package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.RelativeLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.TestComponentLayout;
import com.reactnativenavigation.mocks.TestReactView;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.mocks.TypefaceLoaderMock;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.SubtitleOptions;
import com.reactnativenavigation.parse.TopBarBackgroundOptions;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.presentation.StackOptionsPresenter;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.viewcontrollers.stack.StackControllerBuilder;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.topbar.TopBar;

import org.json.JSONObject;
import org.junit.Test;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.RelativeLayout.BELOW;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OptionsApplyingTest extends BaseTest {
    private Activity activity;
    private StackController stackController;
    private ComponentViewController uut;
    private IReactView view;
    private Options initialNavigationOptions;
    private TopBar topBar;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        initialNavigationOptions = new Options();
        view = spy(new TestComponentLayout(activity, new TestReactView(activity)));
        view.asView().setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        uut = new ComponentViewController(activity,
                new ChildControllersRegistry(),
                "componentId1",
                "componentName",
                (activity1, componentId, componentName) -> view,
                initialNavigationOptions,
                new OptionsPresenter(activity, new Options())
        );
        TopBarController topBarController = new TopBarController() {
            @Override
            protected TopBar createTopBar(Context context, ReactViewCreator buttonCreator, TopBarBackgroundViewController topBarBackgroundViewController, TopBarButtonController.OnClickListener topBarButtonClickListener, StackLayout stackLayout, ImageLoader imageLoader) {
                topBar =
                        spy(super.createTopBar(context, buttonCreator, topBarBackgroundViewController, topBarButtonClickListener, stackLayout, imageLoader));
                return topBar;
            }
        };
        stackController = TestUtils.newStackController(activity)
                .setTopBarController(topBarController)
                .build();
        stackController.ensureViewIsCreated();
        stackController.getView().layout(0, 0, 1000, 1000);
        stackController.getTopBar().layout(0, 0, 1000, 100);
        uut.setParentController(stackController);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void applyNavigationOptionsHandlesNoParentStack() {
        uut.setParentController(null);
        assertThat(uut.getParentController()).isNull();
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        assertThat(uut.getParentController()).isNull();
    }

    @Test
    public void initialOptionsAppliedOnAppear() {
        uut.options.topBar.title.text = new Text("the title");
        StackController stackController =
                new StackControllerBuilder(activity)
                        .setTopBarButtonCreator(new TopBarButtonCreatorMock())
                        .setTopBarBackgroundViewController(new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()))
                        .setTopBarController(new TopBarController())
                        .setId("stackId")
                        .setInitialOptions(new Options())
                        .setStackPresenter(new StackOptionsPresenter(activity, new TitleBarReactViewCreatorMock(), new Options()))
                        .build();
        stackController.ensureViewIsCreated();
        stackController.push(uut, new CommandListenerAdapter());
        assertThat(stackController.getTopBar().getTitle()).isEmpty();

        uut.onViewAppeared();
        assertThat(stackController.getTopBar().getTitle()).isEqualTo("the title");
    }

    @Test
    public void mergeNavigationOptionsUpdatesCurrentOptions() {
        uut.ensureViewIsCreated();
        assertThat(uut.options.topBar.title.text.get("")).isEmpty();
        Options options = new Options();
        options.topBar.title.text = new Text("new title");
        uut.mergeOptions(options);
        assertThat(uut.options.topBar.title.text.get()).isEqualTo("new title");
    }

    @Test
    public void reappliesOptionsOnMerge() {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        assertThat(stackController.getTopBar().getTitle()).isEmpty();

        Options opts = new Options();
        opts.topBar.title.text = new Text("the new title");
        uut.mergeOptions(opts);

        assertThat(stackController.getTopBar().getTitle()).isEqualTo("the new title");
    }

    @Test
    public void appliesTopBackBackgroundColor() {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();

        Options opts = new Options();
        opts.topBar.background.color = new Colour(Color.RED);
        uut.mergeOptions(opts);

        assertThat(((ColorDrawable) stackController.getTopBar().getBackground()).getColor()).isEqualTo(Color.RED);
    }

    @Test
    public void appliesTopBarTextColor() {
        assertThat(uut.initialOptions).isSameAs(initialNavigationOptions);
        stackController.push(uut, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                Options opts = new Options();
                opts.topBar.title.text = new Text("the title");
                opts.topBar.title.color = new Colour(Color.RED);
                uut.mergeOptions(opts);

                assertThat(stackController.getTopBar().getTitleTextView()).isNotEqualTo(null);
                assertThat(stackController.getTopBar().getTitleTextView().getCurrentTextColor()).isEqualTo(Color.RED);
            }
        });
    }

    @SuppressWarnings("MagicNumber")
    @Test
    public void appliesTopBarTextSize() {
        assertThat(uut.initialOptions).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBar.title.text = new Text("the title");
        uut.ensureViewIsCreated();
        uut.onViewAppeared();

        Options opts = new Options();
        opts.topBar.title.text = new Text("the title");
        opts.topBar.title.fontSize = new Fraction(18);
        uut.mergeOptions(opts);

        assertThat(stackController.getTopBar().getTitleTextView()).isNotEqualTo(null);
        assertThat(stackController.getTopBar().getTitleTextView().getTextSize()).isEqualTo(18);
    }

    @Test
    public void appliesTopBarVisible() {
        assertThat(uut.initialOptions).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBar.title.text = new Text("the title");
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        assertThat(stackController.getTopBar().getVisibility()).isNotEqualTo(View.GONE);

        Options opts = new Options();
        opts.topBar.visible = new Bool(false);
        opts.topBar.animate = new Bool(false);
        uut.mergeOptions(opts);

        assertThat(stackController.getTopBar().getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void appliesDrawUnder() {
        uut.options.topBar.title.text = new Text("the title");
        uut.options.topBar.drawBehind = new Bool(false);
        uut.ensureViewIsCreated();
        stackController.ensureViewIsCreated();
        stackController.push(uut, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.onViewAppeared();
                RelativeLayout.LayoutParams uutLayoutParams = (RelativeLayout.LayoutParams) uut.getComponent().asView().getLayoutParams();
                assertThat(uutLayoutParams.topMargin).isNotEqualTo(0);

                Options opts = new Options();
                opts.topBar.drawBehind = new Bool(true);
                uut.mergeOptions(opts);

                uutLayoutParams = (RelativeLayout.LayoutParams) (uut.getComponent().asView()).getLayoutParams();
                assertThat(uutLayoutParams.getRule(BELOW)).isNotEqualTo(stackController.getTopBar().getId());
            }
        });
    }

    @Test
    public void appliesTopBarComponent() throws Exception {
        disablePushAnimation(uut);
        JSONObject json = new JSONObject();
        json.put("component", new JSONObject().put("name","someComponent").put("componentId", "id"));
        uut.options.topBar.background = TopBarBackgroundOptions.parse(json);
        uut.ensureViewIsCreated();
        stackController.push(uut, new CommandListenerAdapter());
        uut.onViewAppeared();

        verify(topBar, times(1)).setBackgroundComponent(any());
    }

    @Test
    public void appliesSubtitle() throws Exception {
        JSONObject json = new JSONObject();
        json.put("text", "sub");
        uut.options.topBar.subtitle = SubtitleOptions.parse(new TypefaceLoaderMock(), json);
        uut.ensureViewIsCreated();
        stackController.push(uut, new CommandListenerAdapter());
        uut.onViewAppeared();

        assertThat(stackController.getTopBar().getTitleBar().getSubtitle()).isEqualTo("sub");
    }
}

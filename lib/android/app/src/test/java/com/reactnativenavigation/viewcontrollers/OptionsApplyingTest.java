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
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.ComponentPresenter;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.topbar.TopBar;

import org.junit.Test;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;

public class OptionsApplyingTest extends BaseTest {
    private Activity activity;
    private StackController stack;
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
                new Presenter(activity, new Options()),
                new ComponentPresenter(Options.EMPTY)
        ) {
            @Override
            public boolean isViewShown() {
                return true;
            }
        };
        TopBarController topBarController = new TopBarController() {
            @Override
            protected TopBar createTopBar(Context context, StackLayout stackLayout) {
                topBar = spy(super.createTopBar(context, stackLayout));
                return topBar;
            }
        };
        stack = TestUtils.newStackController(activity)
                .setTopBarController(topBarController)
                .build();
        stack.ensureViewIsCreated();
        stack.getView().layout(0, 0, 1000, 1000);
        stack.getTopBar().layout(0, 0, 1000, 100);
        activity.setContentView(stack.getView());
        disablePushAnimation(uut);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void applyNavigationOptionsHandlesNoParentStack() {
        uut.setParentController(null);
        assertThat(uut.getParentController()).isNull();
        uut.ensureViewIsCreated();
        uut.onViewWillAppear();
        assertThat(uut.getParentController()).isNull();
    }

    @Test
    public void initialOptionsAppliedOnAppear() {
        uut.options.topBar.title.text = new Text("the title");
        StackController stackController = TestUtils.newStackController(activity).build();
        stackController.ensureViewIsCreated();
        stackController.push(uut, new CommandListenerAdapter());
        assertThat(stackController.getTopBar().getTitle()).isEmpty();

        uut.onViewWillAppear();
        assertThat(stackController.getTopBar().getTitle()).isEqualTo("the title");
    }

    @Test
    public void mergeNavigationOptionsUpdatesCurrentOptions() {
        assertThat(uut.options.topBar.title.text.get("")).isEmpty();
        Options options = new Options();
        options.topBar.title.text = new Text("new title");
        uut.mergeOptions(options);
        assertThat(uut.options.topBar.title.text.get()).isEqualTo("new title");
    }

    @Test
    public void reappliesOptionsOnMerge() {
        assertThat(stack.getTopBar().getTitle()).isEmpty();
        stack.push(uut, new CommandListenerAdapter());

        Options opts = new Options();
        opts.topBar.title.text = new Text("the new title");
        uut.mergeOptions(opts);

        assertThat(stack.getTopBar().getTitle()).isEqualTo("the new title");
    }

    @Test
    public void appliesTopBackBackgroundColor() {
        uut.options.topBar.background.color = new Colour(Color.RED);
        stack.push(uut, new CommandListenerAdapter());
        assertThat(((ColorDrawable) stack.getTopBar().getBackground()).getColor()).isEqualTo(Color.RED);
    }

    @Test
    public void appliesTopBarVisible() {
        stack.push(uut, new CommandListenerAdapter());

        assertThat(uut.initialOptions).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBar.title.text = new Text("the title");
        assertThat(stack.getTopBar().getVisibility()).isNotEqualTo(View.GONE);

        Options opts = new Options();
        opts.topBar.visible = new Bool(false);
        opts.topBar.animate = new Bool(false);
        uut.mergeOptions(opts);

        assertThat(stack.getTopBar().getVisibility()).isEqualTo(View.GONE);
    }
}

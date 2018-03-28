package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.RelativeLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.MockPromise;
import com.reactnativenavigation.mocks.TestComponentLayout;
import com.reactnativenavigation.mocks.TestReactView;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.TopBarBackgroundOptions;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.topbar.TopBarBackgroundView;

import org.json.JSONObject;
import org.junit.Test;

import javax.annotation.Nullable;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.RelativeLayout.BELOW;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;

public class OptionsApplyingTest extends BaseTest {
    private Activity activity;
    private StackController stackController;
    private ComponentViewController uut;
    private IReactView view;
    private Options initialNavigationOptions;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        initialNavigationOptions = new Options();
        view = spy(new TestComponentLayout(activity, new TestReactView(activity)));
        view.asView().setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        uut = new ComponentViewController(activity,
                "componentId1",
                "componentName",
                (activity1, componentId, componentName) -> view,
                initialNavigationOptions
        );
        stackController = new StackController(activity, new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewCreatorMock(), new TopBarController(), "stack", new Options());
        stackController.ensureViewIsCreated();
        uut.setParentController(stackController);
    }

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
        uut.options.topBarOptions.title.text = new Text("the title");
        StackController stackController = new StackController(activity, new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewCreatorMock(), new TopBarController(), "stackId", new Options());
        stackController.animatePush(uut, new MockPromise() {});
        assertThat(stackController.getTopBar().getTitle()).isEmpty();

        uut.onViewAppeared();
        assertThat(stackController.getTopBar().getTitle()).isEqualTo("the title");
    }

    @Test
    public void mergeNavigationOptionsUpdatesCurrentOptions() {
        uut.ensureViewIsCreated();
        assertThat(uut.options.topBarOptions.title.text.get("")).isEmpty();
        Options options = new Options();
        options.topBarOptions.title.text = new Text("new title");
        uut.mergeOptions(options);
        assertThat(uut.options.topBarOptions.title.text.get()).isEqualTo("new title");
    }

    @Test
    public void reappliesOptionsOnMerge() {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        assertThat(stackController.getTopBar().getTitle()).isEmpty();

        Options opts = new Options();
        opts.topBarOptions.title.text = new Text("the new title");
        uut.mergeOptions(opts);

        assertThat(stackController.getTopBar().getTitle()).isEqualTo("the new title");
    }

    @Test
    public void appliesTopBackBackgroundColor() {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();

        Options opts = new Options();
        opts.topBarOptions.background.color = new com.reactnativenavigation.parse.params.Color(Color.RED);
        uut.mergeOptions(opts);

        assertThat(((ColorDrawable) stackController.getTopBar().getTitleBar().getBackground()).getColor()).isEqualTo(Color.RED);
    }

    @Test
    public void appliesTopBarTextColor() {
        assertThat(uut.initialOptions).isSameAs(initialNavigationOptions);
        stackController.animatePush(uut, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                Options opts = new Options();
                opts.topBarOptions.title.text = new Text("the title");
                opts.topBarOptions.title.color = new com.reactnativenavigation.parse.params.Color(Color.RED);
                uut.mergeOptions(opts);

                assertThat(stackController.getTopBar().getTitleTextView()).isNotEqualTo(null);
                assertThat(stackController.getTopBar().getTitleTextView().getCurrentTextColor()).isEqualTo(Color.RED);
            }
        });
    }

    @Test
    public void appliesTopBarTextSize() {
        assertThat(uut.initialOptions).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title.text = new Text("the title");
        uut.ensureViewIsCreated();
        uut.onViewAppeared();

        Options opts = new Options();
        opts.topBarOptions.title.text = new Text("the title");
        opts.topBarOptions.title.fontSize = new Fraction(18);
        uut.mergeOptions(opts);

        assertThat(stackController.getTopBar().getTitleTextView()).isNotEqualTo(null);
        assertThat(stackController.getTopBar().getTitleTextView().getTextSize()).isEqualTo(18);
    }

    @Test
    public void appliesTopBarVisible() {
        assertThat(uut.initialOptions).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title.text = new Text("the title");
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        assertThat(stackController.getTopBar().getVisibility()).isNotEqualTo(View.GONE);

        Options opts = new Options();
        opts.topBarOptions.visible = new Bool(false);
        opts.topBarOptions.animate = new Bool(false);
        uut.mergeOptions(opts);

        assertThat(stackController.getTopBar().getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void appliesDrawUnder() {
        uut.options.topBarOptions.title.text = new Text("the title");
        uut.options.topBarOptions.drawBehind = new Bool(false);
        uut.ensureViewIsCreated();
        stackController.animatePush(uut, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                uut.onViewAppeared();
                RelativeLayout.LayoutParams uutLayoutParams = (RelativeLayout.LayoutParams) uut.getComponent().asView().getLayoutParams();
                assertThat(uutLayoutParams.getRule(BELOW)).isNotEqualTo(0);

                Options opts = new Options();
                opts.topBarOptions.drawBehind = new Bool(true);
                uut.mergeOptions(opts);

                uutLayoutParams = (RelativeLayout.LayoutParams) (uut.getComponent().asView()).getLayoutParams();
                assertThat(uutLayoutParams.getRule(BELOW)).isNotEqualTo(stackController.getTopBar().getId());
            }
        });
    }

    @Test
    public void appliesTopBarComponent() throws Exception {
        JSONObject json = new JSONObject();
        json.put("component", "someComponent");
        uut.options.topBarOptions.background = TopBarBackgroundOptions.parse(json);
        uut.ensureViewIsCreated();
        stackController.push(uut, new MockPromise());
        uut.onViewAppeared();

        assertThat(((ColorDrawable) stackController.getTopBar().getTitleBar().getBackground()).getColor()).isEqualTo(Color.TRANSPARENT);
        assertThat(ViewUtils.findChildrenByClassRecursive(stackController.getTopBar(), TopBarBackgroundView.class)).isNotNull();
    }
}

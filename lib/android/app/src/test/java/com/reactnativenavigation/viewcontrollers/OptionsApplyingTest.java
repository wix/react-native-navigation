package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.MockPromise;
import com.reactnativenavigation.mocks.TestComponentLayout;
import com.reactnativenavigation.mocks.TestReactView;
import com.reactnativenavigation.parse.Fraction;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.Text;

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
    private ComponentViewController.IReactView view;
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
        stackController = new StackController(activity, "stack");
        stackController.ensureViewIsCreated();
        uut.setParentController(stackController);
    }

    @Test
    public void applyNavigationOptionsHandlesNoParentStack() throws Exception {
        uut.setParentController(null);
        assertThat(uut.getParentStackController()).isNull();
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        assertThat(uut.getParentStackController()).isNull();
    }

    @Test
    public void initialOptionsAppliedOnAppear() throws Exception {
        assertThat(uut.getOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = new Text("the title");
        StackController stackController = new StackController(activity, "stackId");
        stackController.animatePush(uut, new MockPromise() {});
        assertThat(stackController.getTopBar().getTitle()).isEmpty();

        uut.onViewAppeared();
        assertThat(stackController.getTopBar().getTitle()).isEqualTo("the title");
    }

    @Test
    public void mergeNavigationOptionsUpdatesCurrentOptions() throws Exception {
        uut.ensureViewIsCreated();
        assertThat(uut.getOptions().topBarOptions.title.get("")).isEmpty();
        Options options = new Options();
        options.topBarOptions.title = new Text("new title");
        uut.mergeOptions(options);
        assertThat(uut.getOptions().topBarOptions.title.get()).isEqualTo("new title");
        assertThat(uut.getOptions()).isSameAs(initialNavigationOptions);
    }

    @Test
    public void reappliesOptionsOnMerge() throws Exception {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        assertThat(stackController.getTopBar().getTitle()).isEmpty();

        Options opts = new Options();
        opts.topBarOptions.title = new Text("the new title");
        uut.mergeOptions(opts);

        assertThat(stackController.getTopBar().getTitle()).isEqualTo("the new title");
    }

    @Test
    public void appliesTopBackBackgroundColor() throws Exception {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();

        Options opts = new Options();
        opts.topBarOptions.backgroundColor = new com.reactnativenavigation.parse.Color(Color.RED);
        uut.mergeOptions(opts);

        assertThat(((ColorDrawable) stackController.getTopBar().getTitleBar().getBackground()).getColor()).isEqualTo(Color.RED);
    }

    @Test
    public void appliesTopBarTextColor() throws Exception {
        assertThat(uut.getOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = new Text("the title");
        stackController.animatePush(uut, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                Options opts = new Options();
                opts.topBarOptions.title = new Text("the title");
                opts.topBarOptions.textColor = new com.reactnativenavigation.parse.Color(Color.RED);
                uut.mergeOptions(opts);

                assertThat(stackController.getTopBar().getTitleTextView()).isNotEqualTo(null);
                assertThat(stackController.getTopBar().getTitleTextView().getCurrentTextColor()).isEqualTo(Color.RED);
            }
        });
    }

    @Test
    public void appliesTopBarTextSize() throws Exception {
        assertThat(uut.getOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = new Text("the title");
        uut.ensureViewIsCreated();
        uut.onViewAppeared();

        Options opts = new Options();
        opts.topBarOptions.title = new Text("the title");
        opts.topBarOptions.textFontSize = new Fraction(18);
        uut.mergeOptions(opts);

        assertThat(stackController.getTopBar().getTitleTextView()).isNotEqualTo(null);
        assertThat(stackController.getTopBar().getTitleTextView().getTextSize()).isEqualTo(18);
    }

    @Test
    public void appliesTopBarHidden() throws Exception {
        assertThat(uut.getOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = new Text("the title");
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        assertThat(stackController.getTopBar().getVisibility()).isNotEqualTo(View.GONE);

        Options opts = new Options();
        opts.topBarOptions.hidden = Options.BooleanOptions.True;
        uut.mergeOptions(opts);

        assertThat(stackController.getTopBar().getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void appliesDrawUnder() throws Exception {
        assertThat(uut.getOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = new Text("the title");
        initialNavigationOptions.topBarOptions.drawBehind = Options.BooleanOptions.False;
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        stackController.animatePush(uut, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                RelativeLayout.LayoutParams uutLayoutParams = (RelativeLayout.LayoutParams) ((ViewGroup) uut.getComponent().asView()).getChildAt(0).getLayoutParams();
                assertThat(uutLayoutParams.getRule(BELOW)).isNotEqualTo(0);

                Options opts = new Options();
                opts.topBarOptions.drawBehind = Options.BooleanOptions.True;
                uut.mergeOptions(opts);

                uutLayoutParams = (RelativeLayout.LayoutParams) (uut.getComponent().asView()).getLayoutParams();
                assertThat(uutLayoutParams.getRule(BELOW)).isNotEqualTo(stackController.getTopBar().getId());
            }
        });
    }
}

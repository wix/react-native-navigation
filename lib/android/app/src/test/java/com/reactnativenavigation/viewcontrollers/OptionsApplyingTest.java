package com.reactnativenavigation.viewcontrollers;

import android.app.*;
import android.graphics.Color;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;

import com.reactnativenavigation.*;
import com.reactnativenavigation.mocks.*;
import com.reactnativenavigation.parse.*;

import org.junit.*;

import static android.widget.RelativeLayout.*;
import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.Mockito.*;

public class OptionsApplyingTest extends BaseTest {
    private Activity activity;
    private ComponentViewController uut;
    private ComponentViewController.IReactView view;
    private Options initialNavigationOptions;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        initialNavigationOptions = new Options();
        view = spy(new TestComponentLayout(activity));
        uut = new ComponentViewController(activity,
                "componentId1",
                "componentName",
                (activity1, componentId, componentName) -> view,
                initialNavigationOptions
        );
        uut.ensureViewIsCreated();
    }

    @Test
    public void applyNavigationOptionsHandlesNoParentStack() throws Exception {
        assertThat(uut.getParentStackController()).isNull();
        uut.onViewAppeared();
        assertThat(uut.getParentStackController()).isNull();
    }

    @Test
    public void initialOptionsAppliedOnAppear() throws Exception {
        assertThat(uut.getOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = "the title";
        StackController stackController = new StackController(activity, "stackId");
        stackController.push(uut);
        assertThat(uut.getTopBar().getTitle()).isEmpty();

        uut.onViewAppeared();
        assertThat(uut.getTopBar().getTitle()).isEqualTo("the title");
    }

    @Test
    public void mergeNavigationOptionsUpdatesCurrentOptions() throws Exception {
        assertThat(uut.getOptions().topBarOptions.title).isEmpty();
        Options options = new Options();
        options.topBarOptions.title = "new title";
        uut.mergeOptions(options);
        assertThat(uut.getOptions().topBarOptions.title).isEqualTo("new title");
        assertThat(uut.getOptions()).isSameAs(initialNavigationOptions);
    }

    @Test
    public void reappliesOptionsOnMerge() throws Exception {
        uut.onViewAppeared();
        assertThat(uut.getTopBar().getTitle()).isEmpty();

        Options opts = new Options();
        opts.topBarOptions.title = "the new title";
        uut.mergeOptions(opts);

        assertThat(uut.getTopBar().getTitle()).isEqualTo("the new title");
    }

    @Test
    public void appliesTopBackBackgroundColor() throws Exception {
        uut.onViewAppeared();
        //TODO: FIX TEST
        assertThat(((ColorDrawable) uut.getTopBar().getTitleBar().getBackground()).getColor()).isNotEqualTo(Color.RED);

        Options opts = new Options();
        opts.topBarOptions.backgroundColor = Color.RED;
        uut.mergeOptions(opts);

        assertThat(((ColorDrawable) uut.getTopBar().getTitleBar().getBackground()).getColor()).isEqualTo(Color.RED);
    }

    @Test
    public void appliesTopBarTextColor() throws Exception {
        assertThat(uut.getOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = "the title";
        uut.onViewAppeared();
        assertThat(uut.getTopBar().getTitleTextView().getCurrentTextColor()).isNotEqualTo(Color.RED);

        Options opts = new Options();
        opts.topBarOptions.title = "the title";
        opts.topBarOptions.textColor = Color.RED;
        uut.mergeOptions(opts);

        assertThat(uut.getTopBar().getTitleTextView()).isNotEqualTo(null);
        assertThat(uut.getTopBar().getTitleTextView().getCurrentTextColor()).isEqualTo(Color.RED);
    }

    @Test
    public void appliesTopBarTextSize() throws Exception {
        assertThat(uut.getOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = "the title";
        uut.onViewAppeared();
        assertThat(uut.getTopBar().getTitleTextView().getTextSize()).isNotEqualTo(18);

        Options opts = new Options();
        opts.topBarOptions.title = "the title";
        opts.topBarOptions.textFontSize = 18;
        uut.mergeOptions(opts);

        assertThat(uut.getTopBar().getTitleTextView()).isNotEqualTo(null);
        assertThat(uut.getTopBar().getTitleTextView().getTextSize()).isEqualTo(18);
    }

    @Test
    public void appliesTopBarHidden() throws Exception {
        assertThat(uut.getOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = "the title";
        uut.onViewAppeared();
        assertThat(uut.getTopBar().getVisibility()).isNotEqualTo(View.GONE);

        Options opts = new Options();
        opts.topBarOptions.hidden = Options.BooleanOptions.True;
        uut.mergeOptions(opts);

        assertThat(uut.getTopBar().getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void appliesDrawUnder() throws Exception {
        assertThat(uut.getOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = "the title";
        initialNavigationOptions.topBarOptions.drawBehind = Options.BooleanOptions.False;
        uut.onViewAppeared();
        RelativeLayout.LayoutParams uutLayoutParams = (RelativeLayout.LayoutParams) ((ViewGroup) uut.getComponent().asView()).getChildAt(1).getLayoutParams();
        assertThat(uutLayoutParams.getRule(BELOW)).isNotEqualTo(0);

        Options opts = new Options();
        opts.topBarOptions.drawBehind = Options.BooleanOptions.True;
        uut.mergeOptions(opts);

        uutLayoutParams = (RelativeLayout.LayoutParams) ((ViewGroup) uut.getComponent().asView()).getChildAt(1).getLayoutParams();
        assertThat(uutLayoutParams.getRule(BELOW)).isEqualTo(0);
    }
}

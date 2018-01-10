package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TestContainerLayout;
import com.reactnativenavigation.parse.NavigationOptions;

import org.junit.Test;

import static android.widget.RelativeLayout.BELOW;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;

public class OptionsApplyingTest extends BaseTest {
    private Activity activity;
    private ContainerViewController uut;
    private ContainerViewController.IReactView view;
    private NavigationOptions initialNavigationOptions;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        initialNavigationOptions = new NavigationOptions();
        view = spy(new TestContainerLayout(activity));
        uut = new ContainerViewController(activity,
                "containerId1",
                "containerName",
                (activity1, containerId, containerName) -> view,
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
        assertThat(uut.getNavigationOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = "the title";
        StackController stackController = new StackController(activity, "stackId");
        stackController.push(uut);
        assertThat(uut.getTopBar().getTitle()).isEmpty();

        uut.onViewAppeared();
        assertThat(uut.getTopBar().getTitle()).isEqualTo("the title");
    }

    @Test
    public void mergeNavigationOptionsUpdatesCurrentOptions() throws Exception {
        assertThat(uut.getNavigationOptions().topBarOptions.title).isEmpty();
        NavigationOptions options = new NavigationOptions();
        options.topBarOptions.title = "new title";
        uut.mergeNavigationOptions(options);
        assertThat(uut.getNavigationOptions().topBarOptions.title).isEqualTo("new title");
        assertThat(uut.getNavigationOptions()).isSameAs(initialNavigationOptions);
    }

    @Test
    public void reappliesOptionsOnMerge() throws Exception {
        uut.onViewAppeared();
        assertThat(uut.getTopBar().getTitle()).isEmpty();

        NavigationOptions opts = new NavigationOptions();
        opts.topBarOptions.title = "the new title";
        uut.mergeNavigationOptions(opts);

        assertThat(uut.getTopBar().getTitle()).isEqualTo("the new title");
    }

    @Test
    public void appliesTopBackBackgroundColor() throws Exception {
        uut.onViewAppeared();
        //TODO: FIX TEST
        assertThat(((ColorDrawable) uut.getTopBar().getTitleBar().getBackground()).getColor()).isNotEqualTo(Color.RED);

        NavigationOptions opts = new NavigationOptions();
        opts.topBarOptions.backgroundColor = Color.RED;
        uut.mergeNavigationOptions(opts);

        assertThat(((ColorDrawable) uut.getTopBar().getTitleBar().getBackground()).getColor()).isEqualTo(Color.RED);
    }

    @Test
    public void appliesTopBarTextColor() throws Exception {
        assertThat(uut.getNavigationOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = "the title";
        uut.onViewAppeared();
        assertThat(uut.getTopBar().getTitleTextView().getCurrentTextColor()).isNotEqualTo(Color.RED);

        NavigationOptions opts = new NavigationOptions();
        opts.topBarOptions.title = "the title";
        opts.topBarOptions.textColor = Color.RED;
        uut.mergeNavigationOptions(opts);

        assertThat(uut.getTopBar().getTitleTextView()).isNotEqualTo(null);
        assertThat(uut.getTopBar().getTitleTextView().getCurrentTextColor()).isEqualTo(Color.RED);
    }

    @Test
    public void appliesTopBarTextSize() throws Exception {
        assertThat(uut.getNavigationOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = "the title";
        uut.onViewAppeared();
        assertThat(uut.getTopBar().getTitleTextView().getTextSize()).isNotEqualTo(18);

        NavigationOptions opts = new NavigationOptions();
        opts.topBarOptions.title = "the title";
        opts.topBarOptions.textFontSize = 18;
        uut.mergeNavigationOptions(opts);

        assertThat(uut.getTopBar().getTitleTextView()).isNotEqualTo(null);
        assertThat(uut.getTopBar().getTitleTextView().getTextSize()).isEqualTo(18);
    }

    @Test
    public void appliesTopBarHidden() throws Exception {
        assertThat(uut.getNavigationOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = "the title";
        uut.onViewAppeared();
        assertThat(uut.getTopBar().getVisibility()).isNotEqualTo(View.GONE);

        NavigationOptions opts = new NavigationOptions();
        opts.topBarOptions.hidden = NavigationOptions.BooleanOptions.True;
        uut.mergeNavigationOptions(opts);

        assertThat(uut.getTopBar().getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void appliesDrawUnder() throws Exception {
        assertThat(uut.getNavigationOptions()).isSameAs(initialNavigationOptions);
        initialNavigationOptions.topBarOptions.title = "the title";
        initialNavigationOptions.topBarOptions.drawBehind = NavigationOptions.BooleanOptions.False;
        uut.onViewAppeared();
        RelativeLayout.LayoutParams uutLayoutParams = (RelativeLayout.LayoutParams) ((ViewGroup) uut.getContainer().asView()).getChildAt(1).getLayoutParams();
        assertThat(uutLayoutParams.getRule(BELOW)).isNotEqualTo(0);

        NavigationOptions opts = new NavigationOptions();
        opts.topBarOptions.drawBehind = NavigationOptions.BooleanOptions.True;
        uut.mergeNavigationOptions(opts);

        uutLayoutParams = (RelativeLayout.LayoutParams) ((ViewGroup) uut.getContainer().asView()).getChildAt(1).getLayoutParams();
        assertThat(uutLayoutParams.getRule(BELOW)).isEqualTo(0);
    }
}
package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.MockPromise;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.Text;
import com.reactnativenavigation.views.ReactComponent;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ParentControllerTest extends BaseTest {

    public static final String INITIAL_TITLE = "initial title";
    private Activity activity;
    private List<ViewController> children;
    private ParentController uut;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        children = new ArrayList<>();
        Options initialOptions = new Options();
        initialOptions.topBarOptions.title = new Text(INITIAL_TITLE);
        uut = spy(new ParentController(activity, "uut", initialOptions) {

            @NonNull
            @Override
            protected ViewGroup createView() {
                FrameLayout layout = new FrameLayout(activity);
                for (ViewController child : children) {
                    child.setParentController(this);
                    layout.addView(child.getView());
                }
                return layout;
            }

            @NonNull
            @Override
            public Collection<ViewController> getChildControllers() {
                return children;
            }
        });
    }

    @Test
    public void holdsViewGroup() throws Exception {
        assertThat(uut.getView()).isInstanceOf(ViewGroup.class);
    }

    @Test
    public void mustHaveChildControllers() throws Exception {
        assertThat(uut.getChildControllers()).isNotNull();
    }

    @Test
    public void findControllerById_ChildById() throws Exception {
        SimpleViewController child1 = new SimpleViewController(activity, "child1", new Options());
        SimpleViewController child2 = new SimpleViewController(activity, "child2", new Options());
        children.add(child1);
        children.add(child2);

        assertThat(uut.findControllerById("uut")).isEqualTo(uut);
        assertThat(uut.findControllerById("child1")).isEqualTo(child1);
    }

    @Test
    public void findControllerById_Recursive() throws Exception {
        StackController stackController = new StackController(activity, "stack", new Options());
        SimpleViewController child1 = new SimpleViewController(activity, "child1", new Options());
        SimpleViewController child2 = new SimpleViewController(activity, "child2", new Options());
        stackController.animatePush(child1, new MockPromise());
        stackController.animatePush(child2, new MockPromise());
        children.add(stackController);

        assertThat(uut.findControllerById("child2")).isEqualTo(child2);
    }

    @Test
    public void destroy_DestroysChildren() throws Exception {
        ViewController child1 = spy(new SimpleViewController(activity, "child1", new Options()));
        children.add(child1);

        verify(child1, times(0)).destroy();
        uut.destroy();
        verify(child1, times(1)).destroy();
    }

    @Test
    public void optionsAreClearedWhenChildIsAppeared() throws Exception {
        StackController stackController = spy(new StackController(activity, "stack", new Options()));
        SimpleViewController child1 = new SimpleViewController(activity, "child1", new Options());
        stackController.animatePush(child1, new MockPromise());

        child1.onViewAppeared();
        verify(stackController, times(1)).clearOptions();
    }

    @Test
    public void mergeOptions_optionsAreMergedWhenChildAppears() throws Exception {
        Options options = new Options();
        options.topBarOptions.title = new Text("new title");
        ViewController child1 = spy(new SimpleViewController(activity, "child1", options));
        children.add(child1);
        uut.ensureViewIsCreated();

        child1.ensureViewIsCreated();
        child1.onViewAppeared();
        ArgumentCaptor<Options> optionsCaptor = ArgumentCaptor.forClass(Options.class);
        ArgumentCaptor<ReactComponent> viewCaptor = ArgumentCaptor.forClass(ReactComponent.class);
        verify(uut, times(1)).applyOptions(optionsCaptor.capture(), viewCaptor.capture());
        assertThat(optionsCaptor.getValue().topBarOptions.title.get()).isEqualTo("new title");
        assertThat(viewCaptor.getValue()).isEqualTo(child1.getView());
    }

    @Test
    public void mergeOptions_initialParentOptionsAreNotMutatedWhenChildAppears() throws Exception {
        Options options = new Options();
        options.topBarOptions.title = new Text("new title");
        ViewController child1 = spy(new SimpleViewController(activity, "child1", options));
        children.add(child1);

        uut.ensureViewIsCreated();

        child1.ensureViewIsCreated();
        child1.onViewAppeared();
        assertThat(uut.initialOptions.topBarOptions.title.get()).isEqualTo(INITIAL_TITLE);
    }
}

package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import androidx.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.stack.StackController;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ParentControllerTest extends BaseTest {

    private static final String INITIAL_TITLE = "initial title";
    private Activity activity;
    private ChildControllersRegistry childRegistry;
    private List<ViewController> children;
    private ParentController uut;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        children = new ArrayList<>();
        Options initialOptions = new Options();
        initialOptions.topBar.title.text = new Text(INITIAL_TITLE);
        Presenter presenter = new Presenter(activity, new Options());
        uut = spy(new ParentController(activity, childRegistry, "uut", presenter, initialOptions) {

            @Override
            protected ViewController getCurrentChild() {
                return children.get(0);
            }

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

            @Override
            public void sendOnNavigationButtonPressed(String buttonId) {

            }

            @NonNull
            @Override
            public Collection<ViewController> getChildControllers() {
                return children;
            }
        });
    }

    @Test
    public void holdsViewGroup() {
        assertThat(uut.getView()).isInstanceOf(ViewGroup.class);
    }

    @Test
    public void mustHaveChildControllers() {
        assertThat(uut.getChildControllers()).isNotNull();
    }

    @Test
    public void findControllerById_ChildById() {
        SimpleViewController child1 = new SimpleViewController(activity, childRegistry, "child1", new Options());
        SimpleViewController child2 = new SimpleViewController(activity, childRegistry, "child2", new Options());
        children.add(child1);
        children.add(child2);

        assertThat(uut.findController("uut")).isEqualTo(uut);
        assertThat(uut.findController("child1")).isEqualTo(child1);
    }

    @Test
    public void findControllerById_Recursive() {
        StackController stackController = TestUtils.newStackController(activity).build();
        stackController.ensureViewIsCreated();
        SimpleViewController child1 = new SimpleViewController(activity, childRegistry, "child1", new Options());
        SimpleViewController child2 = new SimpleViewController(activity, childRegistry, "child2", new Options());
        stackController.push(child1, new CommandListenerAdapter());
        stackController.push(child2, new CommandListenerAdapter());
        children.add(stackController);

        assertThat(uut.findController("child2")).isEqualTo(child2);
    }

    @Test
    public void destroy_DestroysChildren() {
        ViewController child1 = spy(new SimpleViewController(activity, childRegistry, "child1", new Options()));
        children.add(child1);

        verify(child1, times(0)).destroy();
        uut.destroy();
        verify(child1, times(1)).destroy();
    }

    @Test
    public void optionsAreClearedWhenChildIsAppeared() {
        StackController stackController = spy(TestUtils.newStackController(activity).build());
        stackController.ensureViewIsCreated();
        SimpleViewController child1 = new SimpleViewController(activity, childRegistry, "child1", new Options());
        stackController.push(child1, new CommandListenerAdapter());

        child1.onViewWillAppear();
        verify(stackController, times(1)).clearOptions();
    }

    @Test
    public void mergeOptions_optionsAreMergedWhenChildAppears() {
        Options options = new Options();
        options.topBar.title.text = new Text("new title");
        ViewController child1 = spy(new SimpleViewController(activity, childRegistry, "child1", options));
        children.add(child1);
        uut.ensureViewIsCreated();

        child1.ensureViewIsCreated();
        child1.onViewWillAppear();
        ArgumentCaptor<Options> optionsCaptor = ArgumentCaptor.forClass(Options.class);
        verify(uut, times(1)).clearOptions();
        verify(uut, times(1)).applyChildOptions(optionsCaptor.capture(), eq(child1));
        assertThat(optionsCaptor.getValue().topBar.title.text.get()).isEqualTo("new title");
    }

    @Test
    public void mergeOptions_initialParentOptionsAreNotMutatedWhenChildAppears() {
        Options options = new Options();
        options.topBar.title.text = new Text("new title");
        ViewController child1 = spy(new SimpleViewController(activity, childRegistry, "child1", options));
        children.add(child1);

        uut.ensureViewIsCreated();

        child1.ensureViewIsCreated();
        child1.onViewWillAppear();
        assertThat(uut.initialOptions.topBar.title.text.get()).isEqualTo(INITIAL_TITLE);
    }

    @Test
    public void resolveCurrentOptions_returnOptionsIfNoChildren() {
        assertThat(uut.getChildControllers().size()).isZero();
        assertThat(uut.resolveCurrentOptions()).isEqualTo(uut.initialOptions);
    }

    @Test
    public void resolveCurrentOptions_mergesWithCurrentChild() {
        ViewController child1 = Mockito.mock(ViewController.class);
        when(child1.getView()).thenReturn(new FrameLayout(activity));
        Options copiedChildOptions = spy(new Options());
        Options childOptions = spy(new Options() {
            @Override
            public Options copy() {
                return copiedChildOptions;
            }
        });
        when(child1.resolveCurrentOptions()).thenReturn(childOptions);

        children.add(child1);

        uut.ensureViewIsCreated();
        assertThat(uut.getCurrentChild()).isEqualTo(child1);
        uut.resolveCurrentOptions();
        verify(child1).resolveCurrentOptions();
        verify(copiedChildOptions).withDefaultOptions(uut.initialOptions);
    }

    @Test
    public void resolveCurrentOptions_withDefaultOptions() {
        SimpleViewController child1 = new SimpleViewController(activity, childRegistry, "child1", new Options());
        children.add(child1);
        uut.ensureViewIsCreated();

        Options defaultOptions = new Options();
        Options currentOptions = spy(new Options());
        ParentController spy = spy(uut);
        Mockito.when(spy.resolveCurrentOptions()).thenReturn(currentOptions);
        spy.resolveCurrentOptions(defaultOptions);
        verify(currentOptions).withDefaultOptions(defaultOptions);
    }

    @Test
    public void applyTopInset() {
        children.addAll(createChildren());
        uut.applyTopInset();
        forEach(children, c-> verify(c).applyTopInset());
    }

    @Test
    public void getTopInset() {
        assertThat(uut.getTopInset()).isZero();
    }

    @Test
    public void getTopInsetForChild() {
        ParentController parent = Mockito.mock(ParentController.class);
        when(parent.getTopInset(any())).thenReturn(123);
        uut.setParentController(parent);

        assertThat(uut.getTopInset(Mockito.mock(ViewController.class))).isEqualTo(123);
    }

    @Test
    public void applyBottomInset() {
        children.addAll(createChildren());
        uut.applyBottomInset();
        forEach(children, c-> verify(c).applyBottomInset());
    }

    @Test
    public void getBottomInsetForChild() {
        ParentController parent = Mockito.mock(ParentController.class);
        when(parent.getBottomInset(any())).thenReturn(123);
        uut.setParentController(parent);

        assertThat(uut.getBottomInset(Mockito.mock(ViewController.class))).isEqualTo(123);
    }

    private List<ViewController> createChildren() {
        return Arrays.asList(
                spy(new SimpleViewController(activity, childRegistry, "child1", new Options())),
                spy(new SimpleViewController(activity, childRegistry, "child2", new Options()))
        );
    }
}

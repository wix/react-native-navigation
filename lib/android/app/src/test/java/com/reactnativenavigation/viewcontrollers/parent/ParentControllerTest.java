package com.reactnativenavigation.viewcontrollers.parent;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ParentControllerTest extends BaseTest {

    private static final String INITIAL_TITLE = "initial title";
    private Activity activity;
    private ChildControllersRegistry childRegistry;
    private List<ViewController<?>> children;
    private ParentController<?> uut;
    private Presenter presenter;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        children = new ArrayList<>();
        Options initialOptions = new Options();
        initialOptions.topBar.title.text = new Text(INITIAL_TITLE);
        presenter = spy(new Presenter(activity, new Options()));
        uut = spy(new ParentController<ViewGroup>(activity, childRegistry, "uut", presenter, initialOptions) {

            @Override
            public ViewController<?> getCurrentChild() {
                return children.get(0);
            }

            @NonNull
            @Override
            public ViewGroup createView() {
                FrameLayout layout = new FrameLayout(activity);
                for (ViewController<?> child : children) {
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
            public Collection<ViewController<?>> getChildControllers() {
                return children;
            }
        });
    }

    @Test
    public void onConfigurationChange_shouldCallConfigurationChangeForPresenterAndChildren() {
        children.add(spy(new SimpleViewController(activity, childRegistry, "child1", new Options())));
        children.add(spy(new SimpleViewController(activity, childRegistry, "child2", new Options())));
        ParentController<?> spyUUT = spy(uut);
        spyUUT.onConfigurationChanged(mockConfiguration);
        verify(presenter).onConfigurationChanged(any(),any());
        for (ViewController<?> controller : children) {
            verify(controller).onConfigurationChanged(any());
        }
    }

    @Test
    public void onViewDidAppearShouldCallCurrentChildDidAppear(){
        SimpleViewController child1 = spy(new SimpleViewController(activity, childRegistry, "child1", new Options()));
        SimpleViewController child2 = spy(new SimpleViewController(activity, childRegistry, "child2", new Options()));
        children.add(child1);
        children.add(child2);

        uut.onViewDidAppear();

        verify(child1).onViewDidAppear();
        verify(child2,never()).onViewDidAppear();
    }

    @Test
    public void onViewDisappearShouldCallCurrentChildDisAppear(){
        SimpleViewController child1 = spy(new SimpleViewController(activity, childRegistry, "child1", new Options()));
        SimpleViewController child2 = spy(new SimpleViewController(activity, childRegistry, "child2", new Options()));
        children.add(child1);
        children.add(child2);

        uut.onViewDisappear();

        verify(child1).onViewDisappear();
        verify(child2,never()).onViewDisappear();
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
        ViewController<?> child1 = spy(new SimpleViewController(activity, childRegistry, "child1", new Options()));
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
        ViewController<?> child1 = spy(new SimpleViewController(activity, childRegistry, "child1", options));
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
        ViewController<?> child1 = spy(new SimpleViewController(activity, childRegistry, "child1", options));
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
        ParentController<?> spy = spy(uut);
        Mockito.when(spy.resolveCurrentOptions()).thenReturn(currentOptions);
        spy.resolveCurrentOptions(defaultOptions);
        verify(currentOptions).withDefaultOptions(defaultOptions);
    }

    @Test
    public void applyTopInset() {
        children.addAll(createChildren());
        uut.applyTopInset();
        forEach(children, c -> verify(c).applyTopInset());
    }

    @Test
    public void getTopInset() {
        assertThat(uut.getTopInset()).isZero();
    }

    @Test
    public void getTopInsetForChild() {
        ParentController<?> parent = Mockito.mock(ParentController.class);
        Mockito.when(parent.resolveChildOptions(uut)).thenReturn(Options.EMPTY);

        when(parent.getTopInset(any())).thenReturn(123);
        uut.setParentController(parent);

        assertThat(uut.getTopInset(Mockito.mock(ViewController.class))).isEqualTo(123);
    }

    @Test
    public void applyBottomInset() {
        children.addAll(createChildren());
        uut.applyBottomInset();
        forEach(children, c -> verify(c).applyBottomInset());
    }

    @Test
    public void getBottomInsetForChild() {
        ParentController<?> parent = Mockito.mock(ParentController.class);
        Mockito.when(parent.resolveChildOptions(uut)).thenReturn(Options.EMPTY);

        when(parent.getBottomInset(any())).thenReturn(123);
        uut.setParentController(parent);

        assertThat(uut.getBottomInset(Mockito.mock(ViewController.class))).isEqualTo(123);
    }

    private List<ViewController<?>> createChildren() {
        return Arrays.asList(
                spy(new SimpleViewController(activity, childRegistry, "child1", new Options())),
                spy(new SimpleViewController(activity, childRegistry, "child2", new Options()))
        );
    }
}

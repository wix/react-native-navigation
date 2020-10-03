package com.reactnativenavigation.viewcontrollers.stack;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.options.FabOptions;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.views.stack.fab.Fab;
import com.reactnativenavigation.views.stack.fab.FabMenu;
import com.reactnativenavigation.views.stack.StackLayout;

import org.junit.Test;

import androidx.annotation.NonNull;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FloatingActionButtonTest extends BaseTest {

    private final static int CHILD_FAB_COUNT = 3;

    private StackController stackController;
    private SimpleViewController childFab;
    private SimpleViewController childNoFab;
    private Activity activity;
    private ChildControllersRegistry childRegistry;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        stackController = TestUtils.newStackController(activity)
                .setFabPresenter(createFabPresenter())
                .build();
        stackController.ensureViewIsCreated();
        Options options = getOptionsWithFab();
        childFab = new SimpleViewController(activity, childRegistry, "child1", options);
        childNoFab = new SimpleViewController(activity, childRegistry, "child2", new Options());
    }

    @NonNull
    private Options getOptionsWithFab() {
        Options options = new Options();
        FabOptions fabOptions = new FabOptions();
        fabOptions.id = new Text("FAB");
        options.fabOptions = fabOptions;
        return options;
    }

    @NonNull
    private Options getOptionsWithFabActions() {
        Options options = new Options();
        FabOptions fabOptions = new FabOptions();
        fabOptions.id = new Text("FAB");
        for (int i = 0; i < CHILD_FAB_COUNT; i++) {
            FabOptions childOptions = new FabOptions();
            childOptions.id = new Text("fab" + i);
            fabOptions.actionsArray.add(childOptions);
        }
        options.fabOptions = fabOptions;
        return options;
    }

    private boolean hasFab() {
        StackLayout stackLayout = stackController.getStackLayout();
        for (int i = 0; i < stackLayout.getChildCount(); i++) {
            if (stackLayout.getChildAt(i) instanceof Fab) {
                return true;
            }
            if (stackLayout.getChildAt(i) instanceof FabMenu) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void showOnPush() {
        stackController.push(childFab, new CommandListenerAdapter());
        childFab.onViewWillAppear();
        assertThat(hasFab()).isTrue();
    }

    @Test
    public void hideOnPush() {
        stackController.push(childFab, new CommandListenerAdapter());
        childFab.onViewWillAppear();
        assertThat(hasFab()).isTrue();
        stackController.push(childNoFab, new CommandListenerAdapter());
        childNoFab.onViewWillAppear();
        assertThat(hasFab()).isFalse();
    }

    @Test
    public void hideOnPop() {
        disablePushAnimation(childNoFab, childFab);
        stackController.push(childNoFab, new CommandListenerAdapter());
        stackController.push(childFab, new CommandListenerAdapter());
        childFab.onViewWillAppear();
        assertThat(hasFab()).isTrue();
        stackController.pop(Options.EMPTY, new CommandListenerAdapter());
        childNoFab.onViewWillAppear();
        assertThat(hasFab()).isFalse();
    }

    @Test
    public void showOnPop() {
        disablePushAnimation(childFab, childNoFab);
        stackController.push(childFab, new CommandListenerAdapter());
        stackController.push(childNoFab, new CommandListenerAdapter());
        childNoFab.onViewWillAppear();
        assertThat(hasFab()).isFalse();
        stackController.pop(Options.EMPTY, new CommandListenerAdapter());
        childFab.onViewWillAppear();
        assertThat(hasFab()).isTrue();
    }

    @Test
    public void hasChildren() {
        childFab = new SimpleViewController(activity, childRegistry, "child1", getOptionsWithFabActions());
        stackController.push(childFab, new CommandListenerAdapter());
        childFab.onViewWillAppear();
        assertThat(hasFab()).isTrue();
        assertThat(containsActions()).isTrue();
    }

    private boolean containsActions() {
        StackLayout stackLayout = stackController.getStackLayout();
        for (int i = 0; i < stackLayout.getChildCount(); i++) {
            View child = stackLayout.getChildAt(i);
            if (child instanceof FabMenu) {
                return ((ViewGroup) child).getChildCount() == CHILD_FAB_COUNT + 2;
            }
        }
        return false;
    }

    private FabPresenter createFabPresenter() {
        return new FabPresenter() {
            @Override
            public void animateHide(Runnable onAnimationEnd) {
                onAnimationEnd.run();
            }
        };
    }
}

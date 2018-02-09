package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.MockPromise;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.FabMenuOptions;
import com.reactnativenavigation.parse.FabOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.Text;
import com.reactnativenavigation.views.Fab;
import com.reactnativenavigation.views.FabMenu;
import com.reactnativenavigation.views.StackLayout;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FloatingActionButtonMenuTest extends BaseTest {

    private StackController stackController;
    private SimpleViewController childFab;
    private SimpleViewController childNoFab;

    @Override
    public void beforeEach() {
        super.beforeEach();
        Activity activity = newActivity();
        stackController = new StackController(activity, "stackController", new Options());
        Options options = getOptionsWithFab();
        childFab = new SimpleViewController(activity, "child1", options);
        childNoFab = new SimpleViewController(activity, "child2", new Options());
    }

    @NonNull
    private Options getOptionsWithFab() {
        Options options = new Options();
        FabMenuOptions fabOptions = new FabMenuOptions();
        fabOptions.id = new Text("FAB");
        options.fabMenuOptions = fabOptions;
        return options;
    }

    private boolean hasFab() {
        StackLayout stackLayout = stackController.getStackLayout();
        for (int i = 0; i < stackLayout.getChildCount(); i++) {
            if (stackLayout.getChildAt(i) instanceof FabMenu) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void showOnPush() throws Exception {
        stackController.push(childFab, new MockPromise());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
    }

    @Test
    public void hideOnPush() throws Exception {
        stackController.push(childFab, new MockPromise());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
        stackController.push(childNoFab, new MockPromise());
        childNoFab.onViewAppeared();
        assertThat(hasFab()).isFalse();
    }

    @Test
    public void hideOnPop() throws Exception {
        stackController.push(childNoFab, new MockPromise());
        stackController.push(childFab, new MockPromise());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
        stackController.pop(new MockPromise());
        childNoFab.onViewAppeared();
        assertThat(hasFab()).isFalse();
    }

    @Test
    public void showOnPop() throws Exception {
        stackController.push(childFab, new MockPromise());
        stackController.push(childNoFab, new MockPromise());
        childNoFab.onViewAppeared();
        assertThat(hasFab()).isFalse();
        stackController.pop(new MockPromise());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
    }
}

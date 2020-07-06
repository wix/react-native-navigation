package com.reactnativenavigation.viewcontrollers.viewcontroller;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;

import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class YellowBoxDelegateTest extends BaseTest {
    private YellowBoxDelegate uut;
    private YellowBoxHelper yellowBoxHelper;
    private View yellowBox;
    private ViewGroup parent;

    @Override
    public void beforeEach() {
        Activity context = newActivity();
        yellowBox = new View(context);
        parent = new FrameLayout(context);
        yellowBoxHelper = Mockito.mock(YellowBoxHelper.class);
        uut = new YellowBoxDelegate(context, yellowBoxHelper);
        parent.addView(new View(context)); // We assume view at index 0 is not a yellow box
        parent.addView(yellowBox);
    }

    @Test
    public void onYellowBoxAdded_removedFromParent() {
        uut.onYellowBoxAdded(parent);
        assertThat(yellowBox.getParent()).isNull();
    }

    @Test
    public void onYellowBoxAdded_storesRefToYellowBoxAndParent() {
        uut.onYellowBoxAdded(parent);
        assertThat(uut.getYellowBoxes()).contains(yellowBox);
        assertThat(uut.getParent()).isEqualTo(parent);
    }

    @Test
    public void onReactViewDestroy_yellowBoxIsAddedBackToParent() {
        uut.onYellowBoxAdded(parent);
        uut.destroy();
        assertThat(yellowBox.getParent()).isEqualTo(parent);
    }

    @Test
    public void onChildViewAdded() {
        uut.onChildViewAdded(parent, yellowBox);
        dispatchPreDraw(yellowBox);
        verify(yellowBoxHelper).isYellowBox(parent, yellowBox);
    }

    @Test
    public void onYellowBoxAdded_notHandledIfDelegateIsDestroyed() {
        uut.onYellowBoxAdded(parent);
        uut.destroy();

        uut.onYellowBoxAdded(parent);
        assertThat(yellowBox.getParent()).isEqualTo(parent);
    }
}

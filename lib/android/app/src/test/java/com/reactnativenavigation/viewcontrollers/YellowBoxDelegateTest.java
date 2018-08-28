package com.reactnativenavigation.viewcontrollers;

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
        uut = new YellowBoxDelegate(yellowBoxHelper);
        parent.addView(yellowBox);
    }

    @Test
    public void onYellowBoxAdded_removedFromParent() {
        uut.onYellowBoxAdded(parent, yellowBox);
        assertThat(yellowBox.getParent()).isNull();
    }

    @Test
    public void onYellowBoxAdded_storesRefToYellowBoxAndParent() {
        uut.onYellowBoxAdded(parent, yellowBox);
        assertThat(uut.getYellowBox()).isEqualTo(yellowBox);
        assertThat(uut.getParent()).isEqualTo(parent);
    }

    @Test
    public void onReactViewDestroy_yellowBoxIsAddedBackToParent() {
        uut.onYellowBoxAdded(parent, yellowBox);
        uut.destroy();
        assertThat(yellowBox.getParent()).isEqualTo(parent);
    }

    @Test
    public void onChildViewAdded() {
        uut.onChildViewAdded(parent, yellowBox);
        verify(yellowBoxHelper).isYellowBox(parent, yellowBox);
    }

    @Test
    public void onYellowBoxAdded_notHandledIfDelegateIsDestroyed() {
        uut.onYellowBoxAdded(parent, yellowBox);
        uut.destroy();

        uut.onYellowBoxAdded(parent, yellowBox);
        assertThat(yellowBox.getParent()).isEqualTo(parent);
    }
}

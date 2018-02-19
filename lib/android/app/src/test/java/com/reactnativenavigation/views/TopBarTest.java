package com.reactnativenavigation.views;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.anim.TopBarAnimator;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopBarTest extends BaseTest {

    private TopBar uut;
    private TopBarAnimator animator;

    @Override
    public void beforeEach() {
        StackLayout parent = new StackLayout(newActivity());
        uut = new TopBar(newActivity(), buttonId -> {}, parent);
        animator = spy(new TopBarAnimator(uut));
        uut.setAnimator(animator);
        parent.addView(uut);
    }

    @Test
    public void title() throws Exception {
        assertThat(uut.getTitle()).isEmpty();
        uut.setTitle("new title");
        assertThat(uut.getTitle()).isEqualTo("new title");
    }

    @Test
    public void hide_animateHideUnlessSpecifiedOtherwise() throws Exception {
        uut.hide(new NullBool());
        verify(animator, times(1)).hide();
    }

    @Test
    public void show_animateShowUnlessSpecifiedOtherwise() throws Exception {
        uut.hide(new Bool(false));
        uut.show(new NullBool());
        verify(animator, times(1)).show();
    }
}

package com.reactnativenavigation.views.element;

import android.animation.Animator;
import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.Transition;
import com.reactnativenavigation.parse.Transitions;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ElementTransitionManagerTest extends BaseTest {
    private static final int DURATION = 250;
    private ElementTransitionManager uut;
    private Transition validTransition;
    private Transition invalidTransition;
    private Element from1;
    private Element to1;

    @Override
    public void beforeEach() {
        uut = new ElementTransitionManager();
        Activity activity = newActivity();
        from1 = new Element(activity); from1.setElementId("from1Id");
        to1 = new Element(activity); from1.setElementId("to1Id");
        validTransition = createTransition(from1.getElementId(), to1.getElementId());
        invalidTransition = createTransition(from1.getElementId(), "nonexistentElement");
    }

    private Transition createTransition(String fromId, String toId) {
        Transition transition = new Transition();
        transition.duration = new Number(DURATION);
        transition.fromId = new Text(fromId);
        transition.toId = new Text(toId);
        return transition;
    }

    @Test
    public void createElementTransitions_returnsOnEmptyTransitions() {
        Collection<? extends Animator> result = uut.createTransitions(
                new Transitions(),
                Collections.singletonList(from1),
                Collections.singletonList(to1)
        );
        assertThat(result).isEmpty();
    }

    @Test
    public void createElementTransitions_returnsIfNoElements() {
        Collection<? extends Animator> result = uut.createTransitions(
                new Transitions(Collections.singletonList(validTransition)),
                Collections.EMPTY_LIST,
                Collections.EMPTY_LIST
        );
        assertThat(result).isEmpty();
    }

    @Test
    public void createElementTransitions_returnsIfNoMatchingElements() {
        Transition invalidTransition = new Transition();
        invalidTransition.fromId = new Text("from1Id");
        invalidTransition.toId = new Text("nonExistentElement");
        Transitions transitions = new Transitions(Collections.singletonList(invalidTransition));

        Collection<? extends Animator> result = uut.createTransitions(
                transitions,
                Collections.singletonList(from1),
                Collections.singletonList(to1)
        );
        assertThat(result).isEmpty();
    }

    @Test
    public void createElementTransitions_createsAnimatorsForValidTransitions() {
        Collection<? extends Animator> result = uut.createTransitions(
                new Transitions(Arrays.asList(validTransition, invalidTransition)),
                Collections.singletonList(from1),
                Collections.singletonList(to1)
        );
        assertThat(result.size()).isOne();
    }
}

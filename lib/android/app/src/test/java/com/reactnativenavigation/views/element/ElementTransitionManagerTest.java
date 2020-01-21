package com.reactnativenavigation.views.element;

import android.animation.Animator;
import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.SharedElementTransitionOptions;
import com.reactnativenavigation.parse.SharedElements;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.views.element.animators.shared.SharedPropertyAnimatorCreator;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.reactnativenavigation.views.element.TransitionTestUtils.createElement;
import static com.reactnativenavigation.views.element.TransitionTestUtils.createTransition;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ElementTransitionManagerTest extends BaseTest {
    private ElementTransitionManager uut;
    private SharedElementTransitionOptions validTransition;
    private SharedElementTransitionOptions invalidTransition;
    private Element from1;
    private Element to1;
    private TransitionValidator validator;
    private TransitionAnimatorCreator animatorCreator;

    @Override
    public void beforeEach() {
        validator = spy(new TransitionValidator());
        animatorCreator = spy(new TransitionAnimatorCreator() {
            @Override
            protected List<SharedPropertyAnimatorCreator> getAnimators(Element from, Element to) {
                return Collections.EMPTY_LIST;
            }
        });
        uut = new ElementTransitionManager(validator, animatorCreator);
        Activity activity = newActivity();
        from1 = createElement(activity, "from1Id");
        to1 = createElement(activity, "to1Id");
        validTransition = createTransition(from1, to1);
        invalidTransition = createTransition(from1, to1); invalidTransition.toId = new Text("nonexistentElement");
    }

    @Test
    public void createElementTransitions_returnsOnEmptyTransitions() {
        Collection<? extends Animator> result = uut.createTransitions(
                new SharedElements(),
                Collections.singletonList(from1),
                Collections.singletonList(to1)
        );
        assertThat(result).isEmpty();
    }

    @Test
    public void createElementTransitions_returnsIfNoElements() {
        Collection<? extends Animator> result = uut.createTransitions(
                new SharedElements(Collections.singletonList(validTransition)),
                Collections.EMPTY_LIST,
                Collections.EMPTY_LIST
        );
        assertThat(result).isEmpty();
    }

    @Test
    public void createElementTransitions_returnsIfNoMatchingElements() {
        SharedElementTransitionOptions invalidTransition = new SharedElementTransitionOptions();
        invalidTransition.fromId = new Text("from1Id");
        invalidTransition.toId = new Text("nonExistentElement");
        SharedElements transitions = new SharedElements(Collections.singletonList(invalidTransition));

        Collection<? extends Animator> result = uut.createTransitions(
                transitions,
                Collections.singletonList(from1),
                Collections.singletonList(to1)
        );
        verify(validator).validate(eq(invalidTransition), any(), any());
        assertThat(result).isEmpty();
    }

    @Test
    public void createElementTransitions_delegatesAnimatorCreationToCreator() {
        uut.createTransitions(
                new SharedElements(Arrays.asList(validTransition, invalidTransition)),
                Collections.singletonList(from1),
                Collections.singletonList(to1)
        );
        verify(animatorCreator).create(any(Map.class), any(Map.class));
    }
}

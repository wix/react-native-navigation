package com.reactnativenavigation.views.element;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.SharedElementTransitionOptions;
import com.reactnativenavigation.views.element.animators.shared.SharedPropertyAnimatorCreator;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static com.reactnativenavigation.utils.CollectionUtils.keyBy;
import static com.reactnativenavigation.views.element.TransitionTestUtils.createElement;
import static com.reactnativenavigation.views.element.TransitionTestUtils.createTransition;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransitionAnimatorCreatorTest extends BaseTest {
    private TransitionAnimatorCreator uut;
    private SharedElementTransitionOptions t1;
    private SharedElementTransitionOptions t2;
    private Element e1;
    private Element e2;
    private Element e3;
    private Element e4;
    private SharedPropertyAnimatorCreator animator1 = Mockito.mock(SharedPropertyAnimatorCreator.class);
    private SharedPropertyAnimatorCreator animator2 = Mockito.mock(SharedPropertyAnimatorCreator.class);
    private SharedPropertyAnimatorCreator animator3 = Mockito.mock(SharedPropertyAnimatorCreator.class);

    @Override
    public void beforeEach() {
        when(animator1.shouldAnimateProperty()).thenReturn(true);
        when(animator2.shouldAnimateProperty()).thenReturn(true);
        when(animator3.shouldAnimateProperty()).thenReturn(false);
        uut = new TransitionAnimatorCreator() {
            @Override
            protected List<SharedPropertyAnimatorCreator> getAnimators(Element from, Element to) {
                return Arrays.asList(animator1, animator2, animator3);
            }
        };
        Activity activity = newActivity();
        e1 = createElement(activity); e2 = createElement(activity); e3 = createElement(activity); e4 = createElement(activity);
        t1 = createTransition(e1, e2);
        t2 = createTransition(e3, e4);
    }

    @Test
    public void createAnimatorsForEachTransition() {
        TransitionAnimatorCreator spy = spy(uut);
        spy.create(
                keyBy(Arrays.asList(e1, e3), Element::getElementId),
                keyBy(Arrays.asList(e2, e4), Element::getElementId)
        );
        verify(spy).create(e1, e2);
        verify(spy).create(e3, e4);
    }

    @Test
    public void create_animatorsAreCreated() {
        List<SharedElementTransitionOptions> transitions = Arrays.asList(t1, t2);
        uut.create(
                keyBy(Arrays.asList(e1, e3), Element::getElementId),
                keyBy(Arrays.asList(e2, e4), Element::getElementId)
        );
        verify(animator1, times(2)).shouldAnimateProperty();
        verify(animator2, times(2)).shouldAnimateProperty();
        verify(animator3, times(2)).shouldAnimateProperty();
        for (SharedElementTransitionOptions transition : transitions) {
            verify(animator1).create(transition);
            verify(animator2).create(transition);
        }
        verifyNoMoreInteractions(animator3);
    }
}

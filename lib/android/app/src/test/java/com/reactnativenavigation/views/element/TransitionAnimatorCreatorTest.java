package com.reactnativenavigation.views.element;

import android.app.Activity;
import android.view.View;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.Transition;
import com.reactnativenavigation.views.element.animators.PropertyAnimatorCreator;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static com.reactnativenavigation.utils.CollectionUtils.map;
import static com.reactnativenavigation.views.element.TransitionTestUtils.createElement;
import static com.reactnativenavigation.views.element.TransitionTestUtils.createTransition;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class TransitionAnimatorCreatorTest extends BaseTest {
    private TransitionAnimatorCreator uut;
    private Transition t1;
    private Transition t2;
    private Element e1;
    private Element e2;
    private Element e3;
    private Element e4;
    private PropertyAnimatorCreator animator1 = Mockito.mock(PropertyAnimatorCreator.class);
    private PropertyAnimatorCreator animator2 = Mockito.mock(PropertyAnimatorCreator.class);
    private PropertyAnimatorCreator animator3 = Mockito.mock(PropertyAnimatorCreator.class);

    @Override
    public void beforeEach() {
        when(animator1.shouldAnimateProperty()).thenReturn(true);
        when(animator2.shouldAnimateProperty()).thenReturn(true);
        when(animator3.shouldAnimateProperty()).thenReturn(false);
        uut = new TransitionAnimatorCreator() {
            @Override
            protected List<PropertyAnimatorCreator> getAnimators(Element from, Element to) {
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
                Arrays.asList(t1, t2),
                map(Arrays.asList(e1, e3), Element::getElementId),
                map(Arrays.asList(e2, e4), Element::getElementId)
        );
        verify(spy).create(t1, e1, e2);
        verify(spy).create(t2, e3, e4);
    }

    @Test
    public void create_animatorsAreCreated() {
        List<Transition> transitions = Arrays.asList(t1, t2);
        uut.create(
                transitions,
                map(Arrays.asList(e1, e3), Element::getElementId),
                map(Arrays.asList(e2, e4), Element::getElementId)
        );
        verify(animator1, times(2)).shouldAnimateProperty();
        verify(animator2, times(2)).shouldAnimateProperty();
        verify(animator3, times(2)).shouldAnimateProperty();
        for (Transition transition : transitions) {
            verify(animator1).create(transition);
            verify(animator2).create(transition);
        }
        verifyNoMoreInteractions(animator3);
    }
}

package com.reactnativenavigation.views.element;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.Transition;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;

import org.junit.Test;

import java.util.Collections;

import static com.reactnativenavigation.utils.CollectionUtils.keyBy;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class TransitionValidatorTest extends BaseTest {
    public static final String NONEXISTENT_ELEMENT = "nonexistentElement";
    private TransitionValidator uut;
    private Element from1;
    private Element to1;
    private Transition transition;
    private Transition invalidFromElementTransition;
    private Transition invalidToElementTransition;

    @Override
    public void beforeEach() {
        uut = new TransitionValidator();
        Activity activity = newActivity();
        from1 = new Element(activity); from1.setElementId("e1");
        to1 = new Element(activity); to1.setElementId("e2");

        transition = createTransition(from1.getElementId(), to1.getElementId());
        invalidFromElementTransition = createTransition(NONEXISTENT_ELEMENT, to1.getElementId());
        invalidToElementTransition = createTransition(from1.getElementId(), NONEXISTENT_ELEMENT);
    }

    private Transition createTransition(String fromId, String toId) {
        Transition transition = new Transition();
        transition.duration = new Number(100);
        transition.fromId = new Text(fromId);
        transition.toId = new Text(toId);
        return transition;
    }

    @Test
    public void validate_falseIfNullToId() {
        transition.toId = new NullText();
        boolean result = uut.validate(
                transition,
                keyBy(Collections.singletonList(from1), Element::getElementId),
                keyBy(Collections.singletonList(from1), Element::getElementId)
        );
        assertThat(result).isFalse();
    }

    @Test
    public void validate_falseIfNullFromId() {
        transition.fromId = new NullText();
        boolean result = uut.validate(
                transition,
                keyBy(Collections.singletonList(from1), Element::getElementId),
                keyBy(Collections.singletonList(from1), Element::getElementId)
        );
        assertThat(result).isFalse();
    }

    @Test
    public void validate_falseIfFromElementRequiredByTransitionDontExist() {
        boolean result = uut.validate(
                invalidFromElementTransition,
                keyBy(Collections.singletonList(from1), Element::getElementId),
                keyBy(Collections.singletonList(to1), Element::getElementId)
        );
        assertThat(result).isFalse();
    }

    @Test
    public void validate_falseIfToElementRequiredByTransitionDontExist() {
        boolean result = uut.validate(
                invalidToElementTransition,
                keyBy(Collections.singletonList(from1), Element::getElementId),
                keyBy(Collections.singletonList(to1), Element::getElementId)
        );
        assertThat(result).isFalse();
    }

    @Test
    public void validate_trueIfElementsRequiredByTransitionExist() {
        boolean result = uut.validate(
                transition,
                keyBy(Collections.singletonList(from1), Element::getElementId),
                keyBy(Collections.singletonList(to1), Element::getElementId)
        );
        assertThat(result).isTrue();
    }

    @Test
    public void validate_hasDuration() {
        transition.duration = new NullNumber();
        boolean result = uut.validate(
                transition,
                keyBy(Collections.singletonList(from1), Element::getElementId),
                keyBy(Collections.singletonList(to1), Element::getElementId)
        );
        assertThat(result).isFalse();
    }
}

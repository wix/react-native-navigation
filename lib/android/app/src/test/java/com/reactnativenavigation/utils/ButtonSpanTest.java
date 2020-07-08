package com.reactnativenavigation.utils;

import android.graphics.Color;
import android.graphics.Paint;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonSpan;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.options.params.Colour;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonSpan.DISABLED_COLOR;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class ButtonSpanTest extends BaseTest {
    private ButtonSpan uut;
    private ButtonOptions button;

    @Override
    public void beforeEach() {
        button = createButton();
        uut = new ButtonSpan(button);
    }

    @Test
    public void apply_colorIsNotHandled() {
        Paint paint = new Paint();
        uut.apply(paint);

        assertThat(paint.getColor()).isNotEqualTo(button.color.get());
    }

    @Test
    public void apply_disabledColor() {
        button.enabled = new Bool(false);

        Paint paint = new Paint();
        uut.apply(paint);

        assertThat(paint.getColor()).isEqualTo(DISABLED_COLOR);
    }

    @NotNull
    private ButtonOptions createButton() {
        ButtonOptions button = new ButtonOptions();
        button.color = new Colour(Color.RED);
        return button;
    }
}

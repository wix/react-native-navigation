package com.reactnativenavigation.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TypefaceLoaderMock;
import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.Fraction;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonSpan;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;

@Config(qualifiers = "xhdpi")
public class ButtonSpanTest extends BaseTest {
    private ButtonSpan uut;
    private ButtonOptions button;
    private Activity activity;

    @Override
    public void beforeEach() {
        button = createButton();
        activity = newActivity();
        uut = new ButtonSpan(activity, button, new TypefaceLoaderMock());
    }

    @Test
    public void apply_colorIsNotHandled() {
        Paint paint = new Paint();
        uut.apply(paint);

        assertThat(paint.getColor()).isNotEqualTo(button.color.get());
    }

    @Test
    public void apply_fontSizeIsAppliedInDp() {
        button.fontSize = new Fraction(14);
        Paint paint = new Paint();
        uut.apply(paint);

        assertThat(paint.getTextSize()).isEqualTo(UiUtils.dpToPx(activity, 14));
    }

    @NotNull
    private ButtonOptions createButton() {
        ButtonOptions button = new ButtonOptions();
        button.color = new ThemeColour(new Colour(Color.RED));
        return button;
    }
}

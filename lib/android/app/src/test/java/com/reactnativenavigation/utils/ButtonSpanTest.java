package com.reactnativenavigation.utils;

import static org.assertj.core.api.Java6Assertions.assertThat;

import android.graphics.Color;
import android.graphics.Paint;

import com.reactnativenavigation.BaseRobolectricTest;
import com.reactnativenavigation.mocks.TypefaceLoaderMock;
import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.Fraction;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonSpan;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.robolectric.annotation.Config;

@Config(qualifiers = "xhdpi")
public class ButtonSpanTest extends BaseRobolectricTest {
    private ButtonSpan uut;
    private ButtonOptions button;

    @Override
    public void beforeEach() {
        super.beforeEach();
        button = createButton();
        uut = new ButtonSpan(getContext(), button, new TypefaceLoaderMock());
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

        assertThat(paint.getTextSize()).isEqualTo(UiUtils.dpToPx(getContext(), 14));
    }

    @NotNull
    private ButtonOptions createButton() {
        ButtonOptions button = new ButtonOptions();
        button.color = new ThemeColour(new Colour(Color.RED));
        return button;
    }
}

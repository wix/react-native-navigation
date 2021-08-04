package com.reactnativenavigation.viewcontrollers.button;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.options.BackButton;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.IconResolver;
import com.reactnativenavigation.mocks.BackDrawable;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.react.Constants;
import com.reactnativenavigation.utils.Functions.Func1;
import com.reactnativenavigation.utils.ImageLoader;

import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NavigationIconResolverTest extends BaseTest {
    private static final String ICON_URI = "someIconUri";
    private IconResolver uut;
    private ImageLoader imageLoader;
    private Activity context;

    @Override
    public void beforeEach() {
        imageLoader = ImageLoaderMock.mock();
        context = newActivity();
        uut = new IconResolver(context, imageLoader);
    }

    @Test
    public void create_iconButton() {
        @SuppressWarnings("Convert2Lambda") Func1<Drawable> onSuccess = spy(new Func1<Drawable>() {
            @Override
            public void run(Drawable icon) {

            }
        });
        uut.resolve(iconButton(), onSuccess);
        verify(imageLoader).loadIcon(eq(context), eq(ICON_URI), any());
        verify(onSuccess).run(any(Drawable.class));
    }

    @Test
    public void create_backButton() {
        @SuppressWarnings("Convert2Lambda") Func1<Drawable> onSuccess = spy(new Func1<Drawable>() {
            @Override
            public void run(Drawable param) {

            }
        });
        uut.resolve(backButton(), onSuccess);
        verify(onSuccess).run(any(BackDrawable.class));
    }

    private ButtonOptions iconButton() {
        ButtonOptions button = new ButtonOptions();
        button.id = "iconBtnId";
        button.icon = new Text(ICON_URI);
        button.color = new ThemeColour(new Colour(Color.RED));
        return button;
    }

    private BackButton backButton() {
        BackButton button = BackButton.parse(null, null);
        button.id = Constants.BACK_BUTTON_ID;
        return button;
    }
}

package com.reactnativenavigation.viewcontrollers.button;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.react.Constants;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.Task;

import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class NavigationIconResolverTest extends BaseTest {
    private static final String ICON_URI = "someIconUri";
    private NavigationIconResolver uut;
    private ImageLoader imageLoader;
    private Context context;

    @Override
    public void beforeEach() {
        imageLoader = ImageLoaderMock.mock();
        context = newActivity();
        uut = new NavigationIconResolver(context, imageLoader);
    }

    @Test
    public void create_iconButton() {
        @SuppressWarnings("Convert2Lambda") Task<Drawable> onSuccess = spy(new Task<Drawable>() {
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
        @SuppressWarnings("Convert2Lambda") Task<Drawable> onSuccess = spy(new Task<Drawable>() {
            @Override
            public void run(Drawable param) {

            }
        });
        uut.resolve(backButton(), onSuccess);
        verifyZeroInteractions(imageLoader);
        verify(onSuccess).run(any());
    }

    private Button iconButton() {
        Button button = new Button();
        button.id = "iconBtnId";
        button.icon = new Text(ICON_URI);
        button.color = new Colour(Color.RED);
        return button;
    }

    private Button backButton() {
        Button button = new Button();
        button.id = Constants.BACK_BUTTON_ID;
        return button;
    }
}

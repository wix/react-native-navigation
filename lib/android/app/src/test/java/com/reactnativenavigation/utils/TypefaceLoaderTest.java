package com.reactnativenavigation.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.reactnativenavigation.*;

import org.junit.*;
import org.mockito.*;

import static org.assertj.core.api.Java6Assertions.*;

public class TypefaceLoaderTest extends BaseTest {
    private TypefaceLoader uut;
    @Override
    public void beforeEach() {
        Context context = Mockito.mock(Context.class);
        uut = Mockito.spy(new TypefaceLoader(context));
    }

    @Test
    public void loadTypefaceNoAssets() {
        Mockito.doReturn(null).when(uut).getTypefaceFromAssets("Helvetica-Bold");

        Typeface typeface = uut.getTypeFace("Helvetica-Bold");
        assertThat(typeface).isNotNull();
        assertThat(typeface.getStyle()).isEqualTo(Typeface.BOLD);
    }

    @Test
    public void loadTypefaceWithAssets() {
        Mockito.doReturn(Typeface.create("Helvetica-Italic", Typeface.ITALIC)).when(uut).getTypefaceFromAssets("Helvetica-Italic");

        Typeface typeface = uut.getTypeFace("Helvetica-Italic");
        assertThat(typeface).isNotNull();
        assertThat(typeface.getStyle()).isEqualTo(Typeface.ITALIC);
    }

    @Test
    public void loadTypefaceWrongName() {
        Mockito.doReturn(null).when(uut).getTypefaceFromAssets("Some-name");

        Typeface typeface = uut.getTypeFace("Some-name");
        assertThat(typeface).isNotNull();
        assertThat(typeface.getStyle()).isEqualTo(Typeface.NORMAL);
    }

    @Test
    public void loadTypefaceNull() {
        Context context = Mockito.mock(Context.class);
        TypefaceLoader mockedLoader = Mockito.spy(new TypefaceLoader(context));
        Mockito.doReturn(null).when(mockedLoader).getTypefaceFromAssets(null);

        Typeface typeface = uut.getTypeFace(null);
        assertThat(typeface).isNull();
    }
}



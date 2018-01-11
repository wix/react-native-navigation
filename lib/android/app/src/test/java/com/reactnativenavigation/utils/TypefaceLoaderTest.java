package com.reactnativenavigation.utils;

import android.content.*;
import android.graphics.*;
import android.test.mock.*;

import com.reactnativenavigation.*;

import org.junit.*;
import org.mockito.*;

import static org.assertj.core.api.Java6Assertions.*;

public class TypefaceLoaderTest extends BaseTest {

    @Test
    public void loadTypefaceNoAssets() {
        Context context = new MockContext();
        TypefaceLoader mockedLoader = Mockito.spy(new TypefaceLoader(context));
        Mockito.doReturn(null).when(mockedLoader).getTypefaceFromAssets("Helvetica-Bold");

        Typeface typeface = mockedLoader.getTypeFace("Helvetica-Bold");
        assertThat(typeface).isNotNull();
        assertThat(typeface.getStyle()).isEqualTo(Typeface.BOLD);
    }

    @Test
    public void loadTypefaceWithAssets() {
        Context context = new MockContext();
        TypefaceLoader mockedLoader = Mockito.spy(new TypefaceLoader(context));
        Mockito.doReturn(Typeface.create("Helvetica-Italic", Typeface.ITALIC)).when(mockedLoader).getTypefaceFromAssets("Helvetica-Italic");

        Typeface typeface = mockedLoader.getTypeFace("Helvetica-Italic");
        assertThat(typeface).isNotNull();
        assertThat(typeface.getStyle()).isEqualTo(Typeface.ITALIC);
    }

    @Test
    public void loadTypefaceWrongName() {
        Context context = new MockContext();
        TypefaceLoader mockedLoader = Mockito.spy(new TypefaceLoader(context));
        Mockito.doReturn(null).when(mockedLoader).getTypefaceFromAssets("Some-name");

        Typeface typeface = mockedLoader.getTypeFace("Some-name");
        assertThat(typeface).isNotNull();
        assertThat(typeface.getStyle()).isEqualTo(Typeface.NORMAL);
    }

    @Test
    public void loadTypefaceNull() {
        Context context = new MockContext();
        TypefaceLoader mockedLoader = Mockito.spy(new TypefaceLoader(context));
        Mockito.doReturn(null).when(mockedLoader).getTypefaceFromAssets(null);

        Typeface typeface = mockedLoader.getTypeFace(null);
        assertThat(typeface).isNull();
    }
}



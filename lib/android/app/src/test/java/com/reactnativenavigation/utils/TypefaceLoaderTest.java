package com.reactnativenavigation.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.test.mock.MockContext;

import com.reactnativenavigation.BaseTest;

import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class TypefaceLoaderTest extends BaseTest {

	@Test
	public void loadTypefaceNoAssets() {
		Context context = new MockContext();
		TypefaceLoader mockedLoader = Mockito.spy(new TypefaceLoader(context));
		Mockito.doReturn(null).when(mockedLoader).getTypefaceFromAssets(context, "Helvetica-Bold");

		Typeface typeface = mockedLoader.getTypeFace("Helvetica-Bold");
		assertThat(typeface).isNotNull();
		assertThat(typeface.getStyle()).isEqualTo(Typeface.BOLD);
	}

	@Test
	public void loadTypefaceWithAssets() {
        Context context = new MockContext();
        TypefaceLoader mockedLoader = Mockito.spy(new TypefaceLoader(context));
		Mockito.doReturn(Typeface.create("Helvetica-Italic", Typeface.ITALIC)).when(mockedLoader).getTypefaceFromAssets(context, "Helvetica-Italic");

		Typeface typeface = mockedLoader.getTypeFace("Helvetica-Italic");
		assertThat(typeface).isNotNull();
		assertThat(typeface.getStyle()).isEqualTo(Typeface.ITALIC);
	}

	@Test
	public void loadTypefaceWrongName() {
        Context context = new MockContext();
        TypefaceLoader mockedLoader = Mockito.spy(new TypefaceLoader(context));
		Mockito.doReturn(null).when(mockedLoader).getTypefaceFromAssets(context, "Some-name");

		Typeface typeface = mockedLoader.getTypeFace("Some-name");
		assertThat(typeface).isNotNull();
		assertThat(typeface.getStyle()).isEqualTo(Typeface.NORMAL);
	}

	@Test
	public void loadTypefaceNull() {
        Context context = new MockContext();
        TypefaceLoader mockedLoader = Mockito.spy(new TypefaceLoader(context));
		Mockito.doReturn(null).when(mockedLoader).getTypefaceFromAssets(context, null);

		Typeface typeface = mockedLoader.getTypeFace(null);
		assertThat(typeface).isNull();
	}
}



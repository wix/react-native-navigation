package com.reactnativenavigation.mocks;

import android.content.Context;
import android.graphics.Typeface;
import com.reactnativenavigation.options.parsers.TypefaceLoader;
import org.mockito.Mockito;

import java.util.Map;

public class TypefaceLoaderMock extends TypefaceLoader {
    private Map<String, Typeface> mockTypefaces;

    public TypefaceLoaderMock() {
        super(Mockito.mock(Context.class));
    }

    public TypefaceLoaderMock(Map<String, Typeface> mockTypefaces) {
        this();
        this.mockTypefaces = mockTypefaces;
    }

    @Override
    public Typeface getDefaultTypeFace() {
        return Typeface.DEFAULT;
    }

    @Override
    public Typeface getTypeFace(String fontFamilyName, String fontStyle, String fontWeight, Typeface defaultTypeFace) {
        if (mockTypefaces != null && mockTypefaces.containsKey(fontFamilyName)) {
            return mockTypefaces.get(fontFamilyName);
        }
        return defaultTypeFace;
    }
}
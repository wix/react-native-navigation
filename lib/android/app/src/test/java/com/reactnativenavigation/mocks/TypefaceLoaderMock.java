package com.reactnativenavigation.mocks;

import android.content.Context;

import com.reactnativenavigation.options.parsers.TypefaceLoader;

import org.mockito.Mockito;

public class TypefaceLoaderMock extends TypefaceLoader {

    public TypefaceLoaderMock() {
        super(Mockito.mock(Context.class));
    }
}

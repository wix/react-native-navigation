package com.reactnativenavigation.mocks

import android.content.Context
import android.graphics.Typeface
import com.reactnativenavigation.options.parsers.TypefaceLoader
import org.mockito.Mockito

class TypefaceLoaderMock() : TypefaceLoader(Mockito.mock(Context::class.java)) {
    private var mockTypefaces: Map<String, Typeface>? = null

    constructor(mockTypefaces: Map<String, Typeface>?) : this() {
        this.mockTypefaces = mockTypefaces
    }

    override fun getTypeFace(fontFamilyName: String?, fontStyle: String?, fontWeight: String?, defaultTypeFace: Typeface?): Typeface? {
        return mockTypefaces?.getOrDefault(fontFamilyName, defaultTypeFace) ?: defaultTypeFace
    }
}
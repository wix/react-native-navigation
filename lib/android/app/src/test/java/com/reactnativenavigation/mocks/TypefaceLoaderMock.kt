package com.reactnativenavigation.mocks

import android.graphics.Typeface
import com.nhaarman.mockitokotlin2.mock
import com.reactnativenavigation.options.parsers.TypefaceLoader

class TypefaceLoaderMock() : TypefaceLoader(mock()) {
    private var mockTypefaces: Map<String, Typeface>? = null

    constructor(mockTypefaces: Map<String, Typeface>?) : this() {
        this.mockTypefaces = mockTypefaces
    }

    override fun getTypeFace(fontFamilyName: String?, fontStyle: String?, fontWeight: String?, defaultTypeFace: Typeface?): Typeface? {
        return mockTypefaces?.getOrDefault(fontFamilyName, defaultTypeFace) ?: defaultTypeFace
    }
}
package com.reactnativenavigation.mocks

import android.graphics.Typeface
import com.reactnativenavigation.options.parsers.TypefaceLoader
import org.mockito.kotlin.mock

class TypefaceLoaderMock() : TypefaceLoader(mock()) {
    private var mockTypefaces: Map<String, Typeface>? = null

    constructor(mockTypefaces: Map<String, Typeface>?) : this() {
        this.mockTypefaces = mockTypefaces

    }

    override val defaultTypeFace: Typeface
        get() = Typeface.DEFAULT

    override fun getTypeFace(fontFamilyName: String?, fontStyle: String?, fontWeight: String?, defaultTypeFace: Typeface?): Typeface? {
        return mockTypefaces?.getOrDefault(fontFamilyName, defaultTypeFace) ?: defaultTypeFace
    }
}
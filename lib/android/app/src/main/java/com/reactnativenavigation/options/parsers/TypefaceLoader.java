package com.reactnativenavigation.options.parsers;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.facebook.react.views.text.ReactTypefaceUtils;

import androidx.annotation.Nullable;

public class TypefaceLoader {

    private Context context;

    public TypefaceLoader(Context context) {
        this.context = context;
    }

    @Nullable
	public Typeface getTypeFace(String fontFamilyName, String fontStyle, String fontWeight) {
		if (TextUtils.isEmpty(fontFamilyName)) return null;
		return ReactTypefaceUtils.applyStyles(
				null,
				ReactTypefaceUtils.parseFontStyle(fontStyle),
				ReactTypefaceUtils.parseFontWeight(fontWeight),
				fontFamilyName,
				context.getAssets()
		);
	}
}

package com.reactnativenavigation.options.parsers;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

import com.facebook.react.views.text.ReactTypefaceUtils;

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


package com.reactnativenavigation.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypefaceLoader {
	private static final Map<String, Typeface> typefaceCache = new HashMap<>();
    private Context context;

    public TypefaceLoader(Context context) {
        this.context = context;
    }

    @Nullable
	public Typeface getTypeFace(String fontFamilyName) {
		if (TextUtils.isEmpty(fontFamilyName)) return null;
		if (typefaceCache.containsKey(fontFamilyName)) return typefaceCache.get(fontFamilyName);

		Typeface result = load(fontFamilyName);
		typefaceCache.put(fontFamilyName, result);
		return result;
	}

	private Typeface load(String fontFamilyName) {
		Typeface typeface = getTypefaceFromAssets(fontFamilyName);
		if (typeface != null) return typeface;

		int style = getStyle(fontFamilyName);
		return Typeface.create(fontFamilyName, style);
	}

	private int getStyle(String fontFamilyName) {
		int style = Typeface.NORMAL;
		if (fontFamilyName.toLowerCase().contains("bold")) {
			style = Typeface.BOLD;
		} else if (fontFamilyName.toLowerCase().contains("italic")) {
			style = Typeface.ITALIC;
		}
		return style;
	}

	@Nullable
    public Typeface getTypefaceFromAssets(String fontFamilyName) {
		try {
			if (context != null) {
				AssetManager assets = context.getAssets();
				List<String> fonts = Arrays.asList(assets.list("fonts"));
				if (fonts.contains(fontFamilyName + ".ttf")) {
					return Typeface.createFromAsset(assets, "fonts/" + fontFamilyName + ".ttf");
				}

				if (fonts.contains(fontFamilyName + ".otf")) {
					return Typeface.createFromAsset(assets, "fonts/" + fontFamilyName + ".otf");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}


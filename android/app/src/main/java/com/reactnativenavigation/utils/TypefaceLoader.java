package com.reactnativenavigation.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.reactnativenavigation.NavigationApplication;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypefaceLoader {
    private static final Map<String, Typeface> typefaceRegistry = new HashMap<>();

    private String fontFamilyName;

    public TypefaceLoader(String fontFamilyName) {
        this.fontFamilyName = fontFamilyName;
    }

    public void load(@NonNull TextView view) {
        Typeface result = getTypeFace();
        view.setTypeface(result);
    }

    public Typeface getTypeFace() {
        if (typefaceRegistry.containsKey(fontFamilyName)) {
            return typefaceRegistry.get(fontFamilyName);
        }
        Typeface result = load(fontFamilyName);
        typefaceRegistry.put(fontFamilyName, result);
        return result;
    }

    private Typeface load(String fontFamilyName) {
        AssetManager assets = NavigationApplication.instance.getAssets();
        try {
            List<String> fonts = Arrays.asList(assets.list("fonts"));
            if (fonts.contains(fontFamilyName + ".ttf")) {
                return Typeface.createFromAsset(assets, "fonts/" + fontFamilyName + ".ttf");
            }

            if (fonts.contains(fontFamilyName + ".otf")) {
                return Typeface.createFromAsset(assets, "fonts/" + fontFamilyName + ".otf");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Typeface.create(fontFamilyName, Typeface.NORMAL);
    }
}

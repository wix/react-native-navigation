package com.reactnativenavigation.parse;

import android.support.annotation.NonNull;

import com.reactnativenavigation.utils.TypefaceLoader;

import org.json.JSONObject;

public class Options implements DEFAULT_VALUES {

    public enum BooleanOptions {
		True,
		False,
		NoValue;

		static BooleanOptions parse(String value) {
			if (value != null && !value.equals("")) {
				return Boolean.valueOf(value) ? True : False;
			}
			return NoValue;
		}
	}

    @NonNull
    public static Options parse(TypefaceLoader typefaceManager, JSONObject json) {
        return parse(typefaceManager, json, new Options());
    }

	@NonNull
	public static Options parse(TypefaceLoader typefaceManager, JSONObject json, @NonNull Options defaultOptions) {
		Options result = new Options();
		if (json == null) return result;

		result.topBarOptions = TopBarOptions.parse(typefaceManager, json.optJSONObject("topBar"));
		result.topTabsOptions = TopTabsOptions.parse(json.optJSONObject("topTabs"));
        result.topTabOptions = TopTabOptions.parse(typefaceManager, json.optJSONObject("topTab"));
		result.bottomTabsOptions = BottomTabsOptions.parse(json.optJSONObject("bottomTabs"));

		return result.withDefaultOptions(defaultOptions);
	}

    @NonNull public TopBarOptions topBarOptions = new TopBarOptions();
    @NonNull public TopTabsOptions topTabsOptions = new TopTabsOptions();
    @NonNull public TopTabOptions topTabOptions = new TopTabOptions();
    @NonNull public BottomTabsOptions bottomTabsOptions = new BottomTabsOptions();

	public void mergeWith(final Options other) {
        topBarOptions.mergeWith(other.topBarOptions);
        topTabsOptions.mergeWith(other.topTabsOptions);
        bottomTabsOptions.mergeWith(other.bottomTabsOptions);
    }

    Options withDefaultOptions(final Options other) {
        topBarOptions.mergeWithDefault(other.topBarOptions);
        topTabsOptions.mergeWithDefault(other.topTabsOptions);
        bottomTabsOptions.mergeWithDefault(other.bottomTabsOptions);
        return this;
    }
}

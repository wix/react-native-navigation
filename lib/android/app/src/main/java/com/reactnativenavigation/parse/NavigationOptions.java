package com.reactnativenavigation.parse;

import android.support.annotation.NonNull;
import android.widget.Button;

import org.json.JSONObject;

import java.util.ArrayList;

public class NavigationOptions implements DEFAULT_VALUES {

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
    public static NavigationOptions parse(JSONObject json) {
        return parse(json, new NavigationOptions());
    }

	@NonNull
	public static NavigationOptions parse(JSONObject json, @NonNull NavigationOptions defaultOptions) {
		NavigationOptions result = new NavigationOptions();
		if (json == null) return result;

		result.topBarOptions = TopBarOptions.parse(json.optJSONObject("topBar"));
		result.topTabsOptions = TopTabsOptions.parse(json.optJSONObject("topTabs"));
        result.topTabOptions = TopTabOptions.parse(json.optJSONObject("topTab"));
		result.bottomTabsOptions = BottomTabsOptions.parse(json.optJSONObject("bottomTabs"));

		return result.withDefaultOptions(defaultOptions);
	}

    @NonNull public TopBarOptions topBarOptions = new TopBarOptions();
    @NonNull public TopTabsOptions topTabsOptions = new TopTabsOptions();
    @NonNull public TopTabOptions topTabOptions = new TopTabOptions();
    @NonNull public BottomTabsOptions bottomTabsOptions = new BottomTabsOptions();
    public ArrayList<Button> leftButtons;
    public ArrayList<Button> rightButtons;

	public void mergeWith(final NavigationOptions other) {
        topBarOptions.mergeWith(other.topBarOptions);
        topTabsOptions.mergeWith(other.topTabsOptions);
        bottomTabsOptions.mergeWith(other.bottomTabsOptions);

        if(other.leftButtons != null) {
            leftButtons = other.leftButtons;
        }

        if(other.rightButtons != null) {
            rightButtons = other.rightButtons;
        }
    }

    NavigationOptions withDefaultOptions(final NavigationOptions other) {
        topBarOptions.mergeWithDefault(other.topBarOptions);
        topTabsOptions.mergeWithDefault(other.topTabsOptions);
        bottomTabsOptions.mergeWithDefault(other.bottomTabsOptions);
        return this;
    }
}

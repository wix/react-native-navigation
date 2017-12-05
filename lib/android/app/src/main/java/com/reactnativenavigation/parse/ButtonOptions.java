package com.reactnativenavigation.parse;


import android.support.annotation.ColorInt;

import org.json.JSONObject;

import static com.reactnativenavigation.parse.OverlayOptions.NO_COLOR;

public class ButtonOptions {

	public static ButtonOptions parse(JSONObject json) {
		ButtonOptions options = new ButtonOptions();
		if (json == null) return new ButtonOptions();

		//TODO: parse
		options.text = json.optString("text");
		options.action = json.optString("action");
		options.visible = json.optBoolean("visible", true);
		options.textColor = json.optInt("textColor", NO_COLOR);

		return options;
	}

	private String text = "";
	private String action;
	private boolean visible = false;
	@ColorInt
	private int textColor;

	public String getText() {
		return text;
	}

	public String getAction() {
		return action;
	}

	public boolean isVisible() {
		return visible;
	}

	public int getTextColor() {
		return textColor;
	}
}

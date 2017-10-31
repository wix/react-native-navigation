package com.reactnativenavigation.parse;


import org.json.JSONObject;

public class ButtonOptions {

	public static ButtonOptions parse(JSONObject json) {
		ButtonOptions options = new ButtonOptions();
		if (json == null) return new ButtonOptions();

		//TODO: parse
		options.text = json.optString("text");
		options.action = json.optString("action");
		options.visible = json.optBoolean("visible", true);

		return options;
	}

	private String text;
	private String action;
	private boolean visible = false;

	public String getText() {
		return text;
	}

	public String getAction() {
		return action;
	}

	public boolean isVisible() {
		return visible;
	}
}

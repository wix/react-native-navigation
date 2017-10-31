package com.reactnativenavigation.parse;


import org.json.JSONObject;

public class OverlayOptions {

	public static OverlayOptions parse(JSONObject json) {
		OverlayOptions options = new OverlayOptions();
		if (json == null) return options;

		//TODO: parse
		options.title = json.optString("title");
		options.text = json.optString("text");
		options.positiveButton = ButtonOptions.parse(json.optJSONObject("positiveButton"));
		options.negativeButton = ButtonOptions.parse(json.optJSONObject("negativeButton"));

		return options;
	}

	private String title = "";
	private String text = "";
	private ButtonOptions positiveButton;
	private ButtonOptions negativeButton;

	public String getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}

	public ButtonOptions getPositiveButton() {
		return positiveButton;
	}

	public ButtonOptions getNegativeButton() {
		return negativeButton;
	}
}

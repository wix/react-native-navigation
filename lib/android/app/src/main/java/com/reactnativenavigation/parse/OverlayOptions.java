package com.reactnativenavigation.parse;


import org.json.JSONObject;

public class OverlayOptions {

	public static OverlayOptions parse(JSONObject json) {
		if (json == null) return new OverlayOptions();

		OverlayOptions options = new OverlayOptions();

		//TODO: parse
		options.title = json.optString("title");
		options.text = json.optString("text");

		return options;
	}

	private String title = "";
	private String text = "";

	public String getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}
}

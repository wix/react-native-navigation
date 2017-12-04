package com.reactnativenavigation.parse;


import android.support.annotation.ColorInt;

import com.reactnativenavigation.viewcontrollers.ViewController;

import org.json.JSONObject;

public class OverlayOptions {

	public static final int NO_COLOR = -1;

	public static OverlayOptions parse(JSONObject json) {
		OverlayOptions options = new OverlayOptions();
		if (json == null) return options;

		options.title = json.optString("title");
		options.text = json.optString("text");
		options.positiveButton = ButtonOptions.parse(json.optJSONObject("positiveButton"));
		options.negativeButton = ButtonOptions.parse(json.optJSONObject("negativeButton"));
		options.button = ButtonOptions.parse(json.optJSONObject("button"));
		options.textColor = json.optInt("textColor", NO_COLOR);
		options.duration = json.optString("duration");
		options.backgroundColor = json.optInt("backgroundColor", NO_COLOR);

		return options;
	}

	public static OverlayOptions create(ViewController containerView) {
		OverlayOptions options = new OverlayOptions();
		if (containerView == null) return options;

		options.customView = containerView;
		return options;
	}

	private String title = "";
	private String text = "";
	private ButtonOptions positiveButton;
	private ButtonOptions negativeButton;
	private ButtonOptions button;
	@ColorInt
	private int textColor;
	private String duration;
	@ColorInt
	private int backgroundColor;

	private ViewController customView;

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

	public ViewController getCustomView() {
		return customView;
	}

	public ButtonOptions getButton() {
		return button;
	}

	public int getTextColor() {
		return textColor;
	}

	public String getDuration() {
		return duration;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}
}

package com.reactnativenavigation.parse;


import com.reactnativenavigation.viewcontrollers.ContainerViewController;
import com.reactnativenavigation.viewcontrollers.ViewController;

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
}

package com.reactnativenavigation.viewcontrollers.overlay;


import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.reactnativenavigation.parse.OverlayOptions;
import com.reactnativenavigation.viewcontrollers.ViewController;

import static com.reactnativenavigation.parse.OverlayOptions.NO_COLOR;

public class SnackbarOverlay implements OverlayInterface {

	private Snackbar snackbar;
	private OverlayOptions options;

	@Override
	public SnackbarOverlay create(ViewController viewController, OverlayOptions options) {
		this.options = options;

		init(viewController);
		setAction();
		setStyle();

		return this;
	}

	private void init(ViewController viewController) {
		snackbar = Snackbar.make(viewController.getView(),
				options.getTextColor() == NO_COLOR ? options.getText() : getStyledText(),
				getDuration(options.getDuration()));
	}

	private Spanned getStyledText() {
		SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(options.getText());
		spannableStringBuilder.setSpan(new ForegroundColorSpan(options.getTextColor()), 0,
				spannableStringBuilder.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		return spannableStringBuilder;
	}

	@Override
	public void show() {
		snackbar.show();
	}

	@Override
	public void dismiss() {
		snackbar.dismiss();
	}

	private int getDuration(String duration) {
		switch (duration) {
			case "short":
				return Snackbar.LENGTH_SHORT;
			case "long":
				return Snackbar.LENGTH_LONG;
			case "indefinite":
				return Snackbar.LENGTH_INDEFINITE;
			default:
				return Snackbar.LENGTH_SHORT;
		}
	}

	private void setAction() {
		if (options.getButton() != null) {
			snackbar.setAction(options.getButton().getText(), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//TODO: perform action
				}
			});
		}
	}

	private void setStyle() {
		if (options.getButton() != null && options.getButton().getTextColor() != NO_COLOR) {
			snackbar.setActionTextColor(options.getButton().getTextColor());
		}
		if (options.getBackgroundColor() != NO_COLOR) {
			snackbar.getView().setBackgroundColor(options.getBackgroundColor());
		}
	}
}

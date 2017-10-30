package com.reactnativenavigation.presentation;


import android.content.Context;

import com.reactnativenavigation.parse.OverlayOptions;
import com.reactnativenavigation.viewcontrollers.overlay.AlertOverlay;
import com.reactnativenavigation.viewcontrollers.overlay.CustomOverlay;
import com.reactnativenavigation.viewcontrollers.overlay.FabOverlay;
import com.reactnativenavigation.viewcontrollers.overlay.OverlayInterface;
import com.reactnativenavigation.viewcontrollers.overlay.SnackbarOverlay;

public class OverlayPresenter {

	private enum Overlay {
		AlertDialog("alert", new AlertOverlay()),
		Snackbar("snackbar", new SnackbarOverlay()),
		Fab("fab", new FabOverlay()),
		CustomDialog("custom", new CustomOverlay());

		private String name;
		private OverlayInterface overlay;

		Overlay(String name, OverlayInterface overlay) {
			this.name = name;
			this.overlay = overlay;
		}

		public static Overlay getOverlay(String type) {
			for (Overlay overlay : values()) {
				if (overlay.name.equals(type)) {
					return overlay;
				}
			}
			return CustomDialog;
		}

		public OverlayInterface getOverlay() {
			return overlay;
		}
	}

	private Overlay overlay;

	public OverlayPresenter(Context context, String overlay, OverlayOptions options) {
		this.overlay = Overlay.getOverlay(overlay);

		this.overlay.getOverlay().init(context, options);
	}


	public void show() {
		overlay.getOverlay().show();
	}
}

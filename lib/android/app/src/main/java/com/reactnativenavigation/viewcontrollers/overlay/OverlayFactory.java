package com.reactnativenavigation.viewcontrollers.overlay;


import com.reactnativenavigation.parse.OverlayOptions;
import com.reactnativenavigation.viewcontrollers.ViewController;

public class OverlayFactory {

	public enum Overlay {
		AlertDialog("alert", new AlertOverlay()),
		Snackbar("snackbar", new SnackbarOverlay());

		private String name;
		private OverlayInterface overlayInstance;

		Overlay(String name, OverlayInterface overlayInstance) {
			this.name = name;
			this.overlayInstance = overlayInstance;
		}

		public static Overlay create(String type) {
			for (Overlay overlay : values()) {
				if (overlay.name.equals(type)) {
					return overlay;
				}
			}
			return AlertDialog;
		}
	}

	public static OverlayInterface create(String type, ViewController viewController, OverlayOptions options) {
		return Overlay.create(type).overlayInstance.create(viewController, options);
	}

}

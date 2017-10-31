package com.reactnativenavigation.viewcontrollers.overlay;


import android.content.Context;

import com.reactnativenavigation.parse.OverlayOptions;

public class OverlayFabric {

	private enum Overlay {
		AlertDialog("alert", new AlertOverlay()),
		Snackbar("snackbar", new SnackbarOverlay()),
		Fab("fab", new FabOverlay()),
		CustomDialog("custom", new CustomOverlay());

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
			return CustomDialog;
		}
	}

	public static OverlayInterface create(String type, Context context, OverlayOptions options) {
		return Overlay.create(type).overlayInstance.create(context, options);
	}

}

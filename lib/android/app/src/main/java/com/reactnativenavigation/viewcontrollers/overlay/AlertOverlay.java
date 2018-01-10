package com.reactnativenavigation.viewcontrollers.overlay;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.reactnativenavigation.parse.OverlayOptions;
import com.reactnativenavigation.viewcontrollers.ViewController;

public class AlertOverlay implements OverlayInterface {

	private AlertDialog dialog;

	@Override
	public AlertOverlay create(ViewController viewController, final OverlayOptions options) {
		AlertDialog.Builder builder = new AlertDialog.Builder(viewController.getActivity());

		builder.setTitle(options.getTitle());
		builder.setMessage(options.getText());
		if (options.getPositiveButton().isVisible()) {
			builder.setPositiveButton(options.getPositiveButton().getText(), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//TODO: perform action options.getPositiveButton().getAction();
					dialog.dismiss();
				}
			});
		}
		if (options.getNegativeButton().isVisible()) {
			builder.setNegativeButton(options.getNegativeButton().getText(), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//TODO: perform action options.getNegativeButton().getAction();
					dialog.dismiss();
				}
			});
		}
		dialog = builder.create();

		return this;
	}

	@Override
	public void show() {
		dialog.show();
	}

	@Override
	public void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}

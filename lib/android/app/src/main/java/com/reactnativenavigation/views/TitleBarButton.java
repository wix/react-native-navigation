package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.reactnativenavigation.parse.Button;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.utils.ImageUtils;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.ContainerViewController;

import java.util.ArrayList;

//https://github.com/wix/react-native-navigation/blob/d089ec845b724df1f4d5ba6424159f3e55043975/android/app/src/main/java/com/reactnativenavigation/views/TitleBarButton.java

public class TitleBarButton implements MenuItem.OnMenuItemClickListener {
	private Toolbar toolbar;
	private final Button button;
	private ContainerViewController.ContainerView containerView;
	private Drawable icon;

	TitleBarButton(ContainerViewController.ContainerView containerView, Toolbar toolbar, Button button) {
		this.containerView = containerView;
		this.toolbar = toolbar;
		this.button = button;
	}

	public void addToMenu(Context context, final Menu menu) {
		MenuItem menuItem = menu.add(button.title);
		menuItem.setShowAsAction(button.showAsAction);
		menuItem.setEnabled(button.disabled != NavigationOptions.BooleanOptions.True);
		menuItem.setOnMenuItemClickListener(this);

		if (hasIcon()) {
			setIcon(context, menuItem);
		} else {
			setTextColor();
		}
	}

	private void setIcon(Context context, final MenuItem menuItem) {
		if (button.icon != null) {
			ImageUtils.tryLoadIcon(context, button.icon, new ImageUtils.ImageLoadingListener() {
				@Override
				public void onComplete(@NonNull Drawable drawable) {
					icon = drawable;
					UiUtils.runOnMainThread(new Runnable() {
						@Override
						public void run() {
							menuItem.setIcon(icon);
							setIconColor();
						}
					});
				}

				@Override
				public void onError(Throwable error) {
					//TODO: handle
					error.printStackTrace();
				}
			});
		}
	}

	private void setIconColor() {
		if (button.disabled == NavigationOptions.BooleanOptions.False || button.disabled == NavigationOptions.BooleanOptions.NoValue) {
			UiUtils.tintDrawable(icon, button.buttonColor);
			return;
		}

		if (button.disableIconTint == NavigationOptions.BooleanOptions.True) {
			UiUtils.tintDrawable(icon, button.buttonColor);
		} else {
			UiUtils.tintDrawable(icon, Color.LTGRAY);
		}
	}

	private void setTextColor() {
		UiUtils.runOnPreDrawOnce(this.toolbar, new Runnable() {
			@Override
			public void run() {
				ArrayList<View> outViews = findActualTextViewInMenuByLabel();
				setTextColorForFoundButtonViews(outViews);
			}
		});
	}

	@Override
	public boolean onMenuItemClick(MenuItem menuItem) {
		this.containerView.sendOnNavigationButtonPressed(this.containerView.getContainerId(), this.button.id);
		return true;
	}

	@NonNull
	private ArrayList<View> findActualTextViewInMenuByLabel() {
		ArrayList<View> outViews = new ArrayList<>();
		this.toolbar.findViewsWithText(outViews, button.title, View.FIND_VIEWS_WITH_TEXT);
		return outViews;
	}

	private void setTextColorForFoundButtonViews(ArrayList<View> buttons) {
		for (View button : buttons) {
			((TextView) button).setTextColor(this.button.buttonColor);
		}
	}

	private boolean hasIcon() {
		return button.icon != null;
	}
}

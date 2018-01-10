package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.reactnativenavigation.parse.Button;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.ImageUtils;
import com.reactnativenavigation.utils.UiUtils;

import java.util.ArrayList;

public class TitleBarButton implements MenuItem.OnMenuItemClickListener {
    public interface OnClickListener {
        void onPress(String buttonId);
    }

	private Toolbar toolbar;
	private final Button button;
	private Drawable icon;
    private OnClickListener onPressListener;

    TitleBarButton(Toolbar toolbar, Button button, OnClickListener onPressListener) {
        this.onPressListener = onPressListener;
		this.toolbar = toolbar;
		this.button = button;
	}

	void addToMenu(Context context, final Menu menu) {
		MenuItem menuItem = menu.add(button.title);
		menuItem.setShowAsAction(button.showAsAction);
		menuItem.setEnabled(button.disabled != Options.BooleanOptions.True);
		menuItem.setOnMenuItemClickListener(this);

		if (hasIcon()) {
			applyIcon(context, menuItem);
		} else {
			setTextColor();
			setFontSize(menuItem);
		}
	}

	void applyNavigationIcon(Context context) {
		if (!hasIcon()) {
			Log.w("RNN", "Left button needs to have an icon");
			return;
		}

		ImageUtils.tryLoadIcon(context, button.icon, new ImageUtils.ImageLoadingListener() {
			@Override
			public void onComplete(@NonNull Drawable drawable) {
				icon = drawable;
				UiUtils.runOnMainThread(() -> {
                    setIconColor();
                    setNavigationClickListener();
                    toolbar.setNavigationIcon(icon);
                });
			}

			@Override
			public void onError(Throwable error) {
				//TODO: handle
				error.printStackTrace();
			}
		});
	}

	private void applyIcon(Context context, final MenuItem menuItem) {
		ImageUtils.tryLoadIcon(context, button.icon, new ImageUtils.ImageLoadingListener() {
			@Override
			public void onComplete(@NonNull Drawable drawable) {
				icon = drawable;
				UiUtils.runOnMainThread(() -> {
                    menuItem.setIcon(icon);
                    setIconColor();
                });
			}

			@Override
			public void onError(Throwable error) {
				//TODO: handle
				error.printStackTrace();
			}
		});
	}

	private void setIconColor() {
		if (button.disabled == Options.BooleanOptions.False || button.disabled == Options.BooleanOptions.NoValue) {
			UiUtils.tintDrawable(icon, button.buttonColor);
			return;
		}

		if (button.disableIconTint == Options.BooleanOptions.True) {
			UiUtils.tintDrawable(icon, button.buttonColor);
		} else {
			UiUtils.tintDrawable(icon, Color.LTGRAY);
		}
	}

	private void setTextColor() {
		UiUtils.runOnPreDrawOnce(this.toolbar, () -> {
            ArrayList<View> outViews = findActualTextViewInMenuByLabel();
            setTextColorForFoundButtonViews(outViews);
        });
	}

	private void setFontSize(MenuItem menuItem) {
		SpannableString spanString = new SpannableString(button.title);
		spanString.setSpan(new AbsoluteSizeSpan(button.buttonFontSize, true), 0, button.title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		menuItem.setTitleCondensed(spanString);
	}

	private void setNavigationClickListener() {
		toolbar.setNavigationOnClickListener(view -> onPressListener.onPress(button.id));
	}

	@Override
	public boolean onMenuItemClick(MenuItem menuItem) {
		onPressListener.onPress(button.id);
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

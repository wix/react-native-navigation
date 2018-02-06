package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.reactnativenavigation.parse.Button;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.Text;
import com.reactnativenavigation.utils.ArraryUtils;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

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
		MenuItem menuItem = menu.add(button.title.get(""));
		menuItem.setShowAsAction(button.showAsAction);
		menuItem.setEnabled(button.disabled != Options.BooleanOptions.True);
		menuItem.setOnMenuItemClickListener(this);

		if (hasIcon()) {
			applyIcon(context, menuItem);
		} else {
			setTextColor();
			setFontSize(menuItem);
		}

        setTestId(button.testId);
    }

    void applyNavigationIcon(Context context) {
		if (!hasIcon()) {
			Log.w("RNN", "Left button needs to have an icon");
			return;
		}

		new ImageLoader().loadIcon(context, button.icon.get(), new ImageLoader.ImageLoadingListener() {
			@Override
			public void onComplete(@NonNull Drawable drawable) {
				icon = drawable;
                setIconColor();
                setNavigationClickListener();
                toolbar.setNavigationIcon(icon);
                setLeftButtonTestId();
            }

			@Override
			public void onError(Throwable error) {
				error.printStackTrace();
			}
		});
	}

    private void setLeftButtonTestId() {
        if (!button.testId.hasValue()) return;
        toolbar.post(() -> {
            ImageButton leftButton = ViewUtils.findChildByClass(toolbar, ImageButton.class);
            if (leftButton != null) {
                leftButton.setTag(button.testId.get());
            }
        });
    }

    private void applyIcon(Context context, final MenuItem menuItem) {
        new ImageLoader().loadIcon(context, button.icon.get(), new ImageLoader.ImageLoadingListener() {
			@Override
			public void onComplete(@NonNull Drawable drawable) {
				icon = drawable;
                menuItem.setIcon(icon);
                setIconColor();
			}

			@Override
			public void onError(Throwable error) {
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
            ArrayList<View> outViews = findActualTextViewInMenuByText();
            setTextColorForFoundButtonViews(outViews);
        });
	}

	private void setFontSize(MenuItem menuItem) {
		SpannableString spanString = new SpannableString(button.title.get());
		spanString.setSpan(new AbsoluteSizeSpan(button.buttonFontSize, true), 0, button.title.get().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
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

	private void setTextColorForFoundButtonViews(ArrayList<View> buttons) {
		for (View button : buttons) {
			((TextView) button).setTextColor(this.button.buttonColor);
		}
	}

	private boolean hasIcon() {
		return button.icon.hasValue();
	}

    private void setTestId(Text testId) {
        if (!testId.hasValue()) return;
        UiUtils.runOnPreDrawOnce(this.toolbar, () -> {
            ActionMenuView buttonsLayout = ViewUtils.findChildByClass(toolbar, ActionMenuView.class);
            List<TextView> buttons = ViewUtils.findChildrenByClass(buttonsLayout, TextView.class);
            for (TextView view : buttons) {
                if (button.title.hasValue() && button.title.get().equals(view.getText())) {
                    view.setTag(testId.get());
                } else if (button.icon.hasValue() && ArraryUtils.containes(view.getCompoundDrawables(), icon)) {
                    view.setTag(testId.get());
                }
            }
        });
    }

    @NonNull
    private ArrayList<View> findActualTextViewInMenuByText() {
        ArrayList<View> outViews = new ArrayList<>();
        this.toolbar.findViewsWithText(outViews, button.title.get(), View.FIND_VIEWS_WITH_TEXT);
        return outViews;
    }
}

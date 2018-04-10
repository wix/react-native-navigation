package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.ArrayUtils;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.ImageLoadingListenerAdapter;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.titlebar.TitleBarReactButtonView;

import java.util.ArrayList;
import java.util.List;

public class TopBarButtonController extends ViewController<TitleBarReactButtonView> implements MenuItem.OnMenuItemClickListener {
    public interface OnClickListener {
        void onPress(String buttonId);
    }

    private final Button button;
    private final ReactViewCreator viewCreator;
    private TopBarButtonController.OnClickListener onPressListener;
    private Drawable icon;

    public TopBarButtonController(Activity activity, Button button, ReactViewCreator viewCreator, TopBarButtonController.OnClickListener onClickListener) {
        super(activity, button.id, new Options());
        this.button = button;
        this.viewCreator = viewCreator;
        this.onPressListener = onClickListener;
    }

    @Override
    public void onViewAppeared() {
        view.sendComponentStart();
    }

    @Override
    public void onViewDisappear() {
        view.sendComponentStop();
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        getView().sendOnNavigationButtonPressed(buttonId);
    }

    @NonNull
    @Override
    protected TitleBarReactButtonView createView() {
        view = (TitleBarReactButtonView) viewCreator.create(getActivity(), button.component.componentId.get(), button.component.name.get());
        return (TitleBarReactButtonView) view.asView();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        onPressListener.onPress(button.id);
        return true;
    }

    public void applyNavigationIcon(Toolbar toolbar) {
        if (!button.hasIcon()) {
            Log.w("RNN", "Left button needs to have an icon");
            return;
        }

        new ImageLoader().loadIcon(toolbar.getContext(), button.icon.get(), new ImageLoader.ImageLoadingListener() {
            @Override
            public void onComplete(@NonNull Drawable drawable) {
                icon = drawable;
                setIconColor(drawable);
                toolbar.setNavigationOnClickListener(view -> onPressListener.onPress(button.id));
                toolbar.setNavigationIcon(icon);
                setLeftButtonTestId(toolbar);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    private void setLeftButtonTestId(Toolbar toolbar) {
        if (!button.testId.hasValue()) return;
        toolbar.post(() -> {
            ImageButton leftButton = ViewUtils.findChildByClass(toolbar, ImageButton.class);
            if (leftButton != null) {
                leftButton.setTag(button.testId.get());
            }
        });
    }

    public void addToMenu(Toolbar toolbar, int position) {
        MenuItem menuItem = toolbar.getMenu().add(0, position, position, button.title.get(""));
        menuItem.setShowAsAction(button.showAsAction);
        menuItem.setEnabled(button.enabled.isTrueOrUndefined());
        menuItem.setOnMenuItemClickListener(this);
        if (button.hasComponent()) {
            menuItem.setActionView(getView());
        } else {
            if (button.hasIcon()) {
                loadIcon(new ImageLoadingListenerAdapter() {
                    @Override
                    public void onComplete(@NonNull Drawable icon) {
                        TopBarButtonController.this.icon = icon;
                        setIconColor(icon);
                        menuItem.setIcon(icon);
                    }
                });
            } else {
                setTextColor(toolbar);
                setFontSize(menuItem);
            }
        }
        setTestId(toolbar, button.testId);
    }

    private void loadIcon(ImageLoader.ImageLoadingListener callbacks) {
        new ImageLoader().loadIcon(getActivity(), button.icon.get(), callbacks);
    }

    private void setIconColor(Drawable icon) {
        if (button.enabled.isTrueOrUndefined() && button.buttonColor.hasValue()) {
            UiUtils.tintDrawable(icon, button.buttonColor.get());
            return;
        }
        if (button.disableIconTint.isTrue() && button.buttonColor.hasValue()) {
            UiUtils.tintDrawable(icon, button.buttonColor.get());
        } else {
            UiUtils.tintDrawable(icon, Color.LTGRAY);
        }
    }

    private void setTextColor(Toolbar toolbar) {
        UiUtils.runOnPreDrawOnce(toolbar, () -> {
            ArrayList<View> outViews = findActualTextViewInMenuByText(toolbar);
            setTextColorForFoundButtonViews(outViews);
        });
    }

    @NonNull
    private ArrayList<View> findActualTextViewInMenuByText(Toolbar toolbar) {
        ArrayList<View> outViews = new ArrayList<>();
        toolbar.findViewsWithText(outViews, button.title.get(), View.FIND_VIEWS_WITH_TEXT);
        return outViews;
    }

    private void setTextColorForFoundButtonViews(ArrayList<View> buttons) {
        for (View button : buttons) {
            if (this.button.buttonColor.hasValue())
                ((TextView) button).setTextColor(this.button.buttonColor.get());
        }
    }

    private void setFontSize(MenuItem menuItem) {
        SpannableString spanString = new SpannableString(button.title.get());
        if (this.button.buttonFontSize.hasValue())
            spanString.setSpan(new AbsoluteSizeSpan(button.buttonFontSize.get(), true),
                    0, button.title.get().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        menuItem.setTitleCondensed(spanString);
    }

    private void setTestId(Toolbar toolbar, Text testId) {
        if (!testId.hasValue()) return;
        UiUtils.runOnPreDrawOnce(toolbar, () -> {
            ActionMenuView buttonsLayout = ViewUtils.findChildByClass(toolbar, ActionMenuView.class);
            List<TextView> buttons = ViewUtils.findChildrenByClass(buttonsLayout, TextView.class);
            for (TextView view : buttons) {
                if (button.title.hasValue() && button.title.get().equals(view.getText().toString())) {
                    view.setTag(testId.get());
                } else if (button.icon.hasValue() && ArrayUtils.contains(view.getCompoundDrawables(), icon)) {
                    view.setTag(testId.get());
                }
            }
        });
    }


}

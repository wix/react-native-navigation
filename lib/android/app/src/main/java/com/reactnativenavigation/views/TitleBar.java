package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;

import java.util.ArrayList;

@SuppressLint("ViewConstructor")
public class TitleBar extends Toolbar {
    private final ReactViewCreator buttonCreator;
    private final TopBarButtonController.OnClickListener onClickListener;

    public TitleBar(Context context, ReactViewCreator buttonCreator, TopBarButtonController.OnClickListener onClickListener) {
        super(context);
        this.buttonCreator = buttonCreator;
        this.onClickListener = onClickListener;
    }

    public String getTitle() {
        return super.getTitle() == null ? "" : (String) super.getTitle();
    }

    void setTitleTextColor(Color color) {
        if (color.hasValue()) setTitleTextColor(color.get());
    }

    void setBackgroundColor(Color color) {
        if (color.hasValue()) setBackgroundColor(color.get());
    }

    void setTitleFontSize(Fraction size) {
        TextView titleTextView = getTitleTextView();
        if (titleTextView != null && size.hasValue()) {
            titleTextView.setTextSize(size.get());
        }
    }

    void setTitleTypeface(Typeface typeface) {
        TextView titleTextView = getTitleTextView();
        if (titleTextView != null) {
            titleTextView.setTypeface(typeface);
        }
    }

    TextView getTitleTextView() {
        return findTextView(this);
    }

    void clear() {
        setTitle(null);
        setNavigationIcon(null);
        getMenu().clear();
    }

    void setButtons(ArrayList<Button> leftButtons, ArrayList<Button> rightButtons) {
        setLeftButtons(leftButtons);
        setRightButtons(rightButtons);
    }

    private void setLeftButtons(ArrayList<Button> leftButtons) {
        if (leftButtons == null) return;
        if (leftButtons.isEmpty()) {
            setNavigationIcon(null);
            return;
        }

        if (leftButtons.size() > 1) {
            Log.w("RNN", "Use a custom TopBar to have more than one left button");
        }

        Button leftButton = leftButtons.get(0);
        setLeftButton(leftButton);
    }

    private void setLeftButton(final Button button) {
        TopBarButtonController buttonController = new TopBarButtonController((Activity) getContext(), button, buttonCreator, onClickListener);
        buttonController.applyNavigationIcon(this);
    }

    private void setRightButtons(ArrayList<Button> rightButtons) {
        if (rightButtons.isEmpty()) return;
        getMenu().clear();

        for (Button button : rightButtons) {
            final TopBarButtonController controller = new TopBarButtonController((Activity) getContext(), button, buttonCreator, onClickListener);
            controller.addToMenu(this);
        }
    }

    @Nullable
    private TextView findTextView(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View view = root.getChildAt(i);
            if (view instanceof ViewGroup) {
                view = findTextView((ViewGroup) view);
            }
            if (view instanceof TextView) {
                return (TextView) view;
            }
        }
        return null;
    }
}

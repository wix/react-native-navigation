package com.reactnativenavigation.views.titlebar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reactnativenavigation.parse.TitleOptions;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.TitleBarReactViewController;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("ViewConstructor")
public class TitleBar extends Toolbar {
    private final ReactViewCreator buttonCreator;
    private TitleBarReactViewController reactViewController;
    private final TopBarButtonController.OnClickListener onClickListener;
    private final List<TopBarButtonController> rightButtonControllers = new ArrayList<>();
    private TopBarButtonController leftButtonController;

    public TitleBar(Context context, ReactViewCreator buttonCreator, TitleBarReactViewController reactViewController, TopBarButtonController.OnClickListener onClickListener) {
        super(context);
        this.buttonCreator = buttonCreator;
        this.reactViewController = reactViewController;
        this.onClickListener = onClickListener;
        getMenu();
        setContentDescription("titleBar");
    }

    public String getTitle() {
        return super.getTitle() == null ? "" : (String) super.getTitle();
    }

    public void setTitleTextColor(Color color) {
        if (color.hasValue()) setTitleTextColor(color.get());
    }

    public void setComponent(String componentName, TitleOptions.Alignment alignment) {
        clearTitle();
        reactViewController.setComponent(componentName);
        addView(reactViewController.getView(), getComponentLayoutParams(alignment));
    }

    public void setBackgroundColor(Color color) {
        if (color.hasValue()) setBackgroundColor(color.get());
    }

    public void setTitleFontSize(Fraction size) {
        TextView titleTextView = getTitleTextView();
        if (titleTextView != null && size.hasValue()) {
            titleTextView.setTextSize(size.get());
        }
    }

    public void setTitleTypeface(Typeface typeface) {
        TextView titleTextView = getTitleTextView();
        if (titleTextView != null) {
            titleTextView.setTypeface(typeface);
        }
    }

    public TextView getTitleTextView() {
        return findTextView(this);
    }

    public void clear() {
        clearTitle();
        clearRightButtons();
        clearLeftButton();
        clearComponent();
    }

    private void clearTitle() {
        setTitle(null);
    }

    private void clearComponent() {
        reactViewController.destroy();
        reactViewController = new TitleBarReactViewController(reactViewController);
    }

    private void clearLeftButton() {
        setNavigationIcon(null);
        if (leftButtonController != null) {
            leftButtonController.destroy();
            leftButtonController = null;
        }
    }

    private void clearRightButtons() {
        for (TopBarButtonController button : rightButtonControllers) {
            button.destroy();
        }
        rightButtonControllers.clear();
        getMenu().clear();
    }

    public void setLeftButtons(List<Button> leftButtons) {
        if (leftButtons == null) return;
        if (leftButtons.isEmpty()) {
            clearLeftButton();
            return;
        }
        if (leftButtons.size() > 1) {
            Log.w("RNN", "Use a custom TopBar to have more than one left button");
        }
        setLeftButton(leftButtons.get(0));
    }

    private void setLeftButton(final Button button) {
        TopBarButtonController controller = createButtonController(button);
        leftButtonController = controller;
        controller.applyNavigationIcon(this);
    }

    public void setRightButtons(List<Button> rightButtons) {
        if (rightButtons == null) return;
        clearRightButtons();
        for (int i = 0; i < rightButtons.size(); i++) {
            TopBarButtonController controller = createButtonController(rightButtons.get(i));
            rightButtonControllers.add(controller);
            controller.addToMenu(this, rightButtons.size() - i - 1);
        }
    }

    public TopBarButtonController createButtonController(Button button) {
        return new TopBarButtonController((Activity) getContext(), button, buttonCreator, onClickListener);
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

    public Toolbar.LayoutParams getComponentLayoutParams(TitleOptions.Alignment alignment) {
        if (alignment == TitleOptions.Alignment.Fill) {
            return new LayoutParams(MATCH_PARENT, getHeight());
        } else {
            LayoutParams lp = new LayoutParams(WRAP_CONTENT, getHeight());
            lp.gravity = Gravity.CENTER;
            return lp;
        }
    }
}

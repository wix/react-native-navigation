package com.reactnativenavigation.viewcontrollers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.ArrayUtils;
import com.reactnativenavigation.utils.ButtonOptionsPresenter;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.ImageLoadingListenerAdapter;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.button.NavigationIconResolver;
import com.reactnativenavigation.views.titlebar.TitleBarReactButtonView;

import java.util.Collections;
import java.util.List;

public class TopBarButtonController extends ViewController<TitleBarReactButtonView> implements MenuItem.OnMenuItemClickListener {
    public interface OnClickListener {
        void onPress(String buttonId);
    }

    private final NavigationIconResolver navigationIconResolver;
    private final ImageLoader imageLoader;
    private ButtonOptionsPresenter optionsPresenter;
    private final Button button;
    private final ReactViewCreator viewCreator;
    private TopBarButtonController.OnClickListener onPressListener;
    private Drawable icon;

    public TopBarButtonController(Activity activity,
                                  NavigationIconResolver navigationIconResolver,
                                  ImageLoader imageLoader,
                                  ButtonOptionsPresenter optionsPresenter,
                                  Button button,
                                  ReactViewCreator viewCreator,
                                  OnClickListener onClickListener) {
        super(activity, button.id, new Options());
        this.navigationIconResolver = navigationIconResolver;
        this.imageLoader = imageLoader;
        this.optionsPresenter = optionsPresenter;
        this.button = button;
        this.viewCreator = viewCreator;
        this.onPressListener = onClickListener;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onViewAppeared() {
        view.sendComponentStart();
    }

    @SuppressLint("MissingSuperCall")
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

    @Override
    protected void onYellowBoxAdded(View yellowBox) {
        ((ViewManager) yellowBox.getParent()).removeView(yellowBox);
    }

    public void applyNavigationIcon(Toolbar toolbar) {
        navigationIconResolver.resolve(button, icon -> {
            setIconColor(icon);
            toolbar.setNavigationOnClickListener(view -> onPressListener.onPress(button.id));
            toolbar.setNavigationIcon(icon);
            setLeftButtonTestId(toolbar);
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
        MenuItem menuItem = toolbar.getMenu().add(0, position, position, button.text.get(""));
        if (button.showAsAction.hasValue()) menuItem.setShowAsAction(button.showAsAction.get());
        menuItem.setEnabled(button.enabled.isTrueOrUndefined());
        menuItem.setOnMenuItemClickListener(this);
        if (button.hasComponent()) {
            menuItem.setActionView(getView());
        } else {
            if (button.hasIcon()) {
                loadIcon(new ImageLoadingListenerAdapter() {
                    @Override
                    public void onComplete(@NonNull List<Drawable> icons) {
                        Drawable icon = icons.get(0);
                        TopBarButtonController.this.icon = icon;
                        setIconColor(icon);
                        menuItem.setIcon(icon);
                    }
                });
            } else {
                optionsPresenter.setTextColor();
                if (button.fontSize.hasValue()) optionsPresenter.setFontSize(menuItem);
                optionsPresenter.setTypeFace(button.fontFamily);
            }
        }
        setTestId(toolbar, button.testId);
    }

    private void loadIcon(ImageLoader.ImagesLoadingListener callback) {
        imageLoader.loadIcons(getActivity(), Collections.singletonList(button.icon.get()), callback);
    }

    private void setIconColor(Drawable icon) {
        if (button.disableIconTint.isTrue()) return;
        if (button.enabled.isTrueOrUndefined() && button.color.hasValue()) {
            optionsPresenter.tint(icon, button.color.get());
        } else if (button.enabled.isFalse()) {
            optionsPresenter.tint(icon, button.disabledColor.get(Color.LTGRAY));
        }
    }

    private void setTestId(Toolbar toolbar, Text testId) {
        if (!testId.hasValue()) return;
        UiUtils.runOnPreDrawOnce(toolbar, () -> {
            ActionMenuView buttonsLayout = ViewUtils.findChildByClass(toolbar, ActionMenuView.class);
            List<TextView> buttons = ViewUtils.findChildrenByClass(buttonsLayout, TextView.class);
            for (TextView view : buttons) {
                if (button.text.hasValue() && button.text.get().equals(view.getText().toString())) {
                    view.setTag(testId.get());
                } else if (button.icon.hasValue() && ArrayUtils.contains(view.getCompoundDrawables(), icon)) {
                    view.setTag(testId.get());
                }
            }
        });
    }
}

package com.reactnativenavigation.views.titlebar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reactnativenavigation.parse.Alignment;
import com.reactnativenavigation.parse.BackButton;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.utils.ButtonOptionsPresenter;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.viewcontrollers.button.NavigationIconResolver;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

@SuppressLint("ViewConstructor")
public class TitleBar extends Toolbar {
    public static final int DEFAULT_LEFT_MARGIN = 16;

    private final ReactViewCreator buttonCreator;
    private final TopBarButtonController.OnClickListener onClickListener;
    private final List<TopBarButtonController> rightButtonControllers = new ArrayList<>();
    private TopBarButtonController leftButtonController;
    private ImageLoader imageLoader;
    private View component;

    public TitleBar(Context context, ReactViewCreator buttonCreator, TopBarButtonController.OnClickListener onClickListener, ImageLoader imageLoader) {
        super(context);
        this.buttonCreator = buttonCreator;
        this.imageLoader = imageLoader;
        this.onClickListener = onClickListener;
        getMenu();
        setContentDescription("titleBar");
    }

    @Override
    public void setTitle(CharSequence title) {
        clearComponent();
        super.setTitle(title);
    }

    public String getTitle() {
        return super.getTitle() == null ? "" : (String) super.getTitle();
    }

    public void setTitleTextColor(Colour color) {
        if (color.hasValue()) setTitleTextColor(color.get());
    }

    public void setComponent(View component) {
        clearTitle();
        clearSubtitle();
        this.component = component;
        addView(component);
    }

    public void setBackgroundColor(Colour color) {
        if (color.hasValue()) setBackgroundColor(color.get());
    }

    public void setTitleFontSize(double size) {
        TextView titleTextView = findTitleTextView();
        if (titleTextView != null) titleTextView.setTextSize((float) size);
    }

    public void setTitleTypeface(Typeface typeface) {
        TextView titleTextView = findTitleTextView();
        if (titleTextView != null) titleTextView.setTypeface(typeface);
    }

    public void setTitleAlignment(Alignment alignment) {
        TextView title = findTitleTextView();
        if (title == null) return;
        alignTextView(alignment, title);
    }

    public void setSubtitleTypeface(Typeface typeface) {
        TextView subtitleTextView = findSubtitleTextView();
        if (subtitleTextView != null) subtitleTextView.setTypeface(typeface);
    }

    public void setSubtitleFontSize(double size) {
        TextView subtitleTextView = findSubtitleTextView();
        if (subtitleTextView != null) subtitleTextView.setTextSize((float) size);
    }

    public void setSubtitleAlignment(Alignment alignment) {
        TextView subtitle = findSubtitleTextView();
        if (subtitle == null) return;
        alignTextView(alignment, subtitle);
    }

    private void alignTextView(Alignment alignment, TextView view) {
        view.post(() -> {
            if (alignment == Alignment.Center) {
                view.setX((getWidth() - view.getWidth()) / 2);
            } else if (leftButtonController != null) {
                view.setX(getContentInsetStartWithNavigation());
            } else {
                view.setX(UiUtils.dpToPx(getContext(), DEFAULT_LEFT_MARGIN));
            }
        });
    }

    @Nullable
    public TextView findTitleTextView() {
        List<TextView> children = ViewUtils.findChildrenByClass(this, TextView.class, textView -> textView.getText().equals(getTitle()));
        return children.isEmpty() ? null : children.get(0);
    }

    @Nullable
    public TextView findSubtitleTextView() {
        List<TextView> children = ViewUtils.findChildrenByClass(this, TextView.class, textView -> textView.getText().equals(getSubtitle()));
        return children.isEmpty() ? null : children.get(0);
    }

    public void clear() {
        clearTitle();
        clearSubtitle();
        clearRightButtons();
        clearLeftButton();
        clearComponent();
    }

    private void clearTitle() {
        setTitle(null);
    }

    private void clearSubtitle() {
        setSubtitle(null);
    }

    private void clearComponent() {
        if (component != null) {
            removeView(component);
            component = null;
        }
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
        if (getMenu().size() > 0) getMenu().clear();
    }

    public void setBackButton(BackButton button) {
        setLeftButton(button);
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
        return new TopBarButtonController((Activity) getContext(),
                new NavigationIconResolver(getContext(), imageLoader),
                imageLoader,
                new ButtonOptionsPresenter(this, button),
                button,
                buttonCreator,
                onClickListener
        );
    }

    public void setHeight(int height) {
        if (height == getLayoutParams().height) return;
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = (int) UiUtils.dpToPx(getContext(), height);
        setLayoutParams(lp);
    }
}

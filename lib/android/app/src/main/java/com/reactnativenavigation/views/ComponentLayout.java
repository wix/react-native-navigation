package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.ComponentOptionsPresenter;
import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.views.touch.OverlayTouchDelegate;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.RelativeLayout.BELOW;

@SuppressLint("ViewConstructor")
public class ComponentLayout extends FrameLayout implements ReactComponent, TopBarButtonController.OnClickListener {

    private IReactView reactView;
    private final OverlayTouchDelegate touchDelegate;

    public ComponentLayout(Context context, IReactView reactView) {
		super(context);
		this.reactView = reactView;
        addView(reactView.asView(), MATCH_PARENT, MATCH_PARENT);
        setContentDescription("ComponentLayout");
        touchDelegate = new OverlayTouchDelegate(reactView);
    }

    @Override
    public boolean isReady() {
        return reactView.isReady();
    }

    @Override
    public View asView() {
        return this;
    }

    @Override
    public void destroy() {
        reactView.destroy();
    }

	@Override
	public void sendComponentStart() {
		reactView.sendComponentStart();
	}

	@Override
	public void sendComponentStop() {
		reactView.sendComponentStop();
	}

    @Override
    public void applyOptions(Options options) {
        new ComponentOptionsPresenter(this).present(options);
        touchDelegate.setInterceptTouchOutside(options.overlayOptions.interceptTouchOutside.isTrue());
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        reactView.sendOnNavigationButtonPressed(buttonId);
    }

    @Override
    public ScrollEventListener getScrollEventListener() {
        return reactView.getScrollEventListener();
    }

    @Override
    public void dispatchTouchEventToJs(MotionEvent event) {
        reactView.dispatchTouchEventToJs(event);
    }

    @Override
    public void drawBehindTopBar() {
        if (getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            layoutParams.removeRule(BELOW);
            setLayoutParams(layoutParams);
        }
    }

    @Override
    public void drawBelowTopBar(TopBar topBar) {
        if (getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            layoutParams.addRule(BELOW, topBar.getId());
            setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onPress(String buttonId) {
        reactView.sendOnNavigationButtonPressed(buttonId);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return touchDelegate.onInterceptTouchEvent(ev);
    }
}

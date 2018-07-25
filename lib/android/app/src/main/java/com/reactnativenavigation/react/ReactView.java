package com.reactnativenavigation.react;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.view.MotionEvent;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.JSTouchDispatcher;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.views.element.Element;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class ReactView extends ReactRootView implements IReactView {

	private final ReactInstanceManager reactInstanceManager;
	private final String componentId;
	private final String componentName;
	private boolean isAttachedToReactInstance = false;
    private final JSTouchDispatcher jsTouchDispatcher;
    private ArrayList<Element> elements = new ArrayList<>();

    public ReactView(final Context context, ReactInstanceManager reactInstanceManager, String componentId, String componentName) {
		super(context);
		this.reactInstanceManager = reactInstanceManager;
		this.componentId = componentId;
		this.componentName = componentName;
		jsTouchDispatcher = new JSTouchDispatcher(this);
		start();
	}

	private void start() {
		setEventListener(reactRootView -> {
            reactRootView.setEventListener(null);
            isAttachedToReactInstance = true;
        });
		final Bundle opts = new Bundle();
		opts.putString("componentId", componentId);
		startReactApplication(reactInstanceManager, componentName, opts);
	}

	@Override
	public boolean isReady() {
		return isAttachedToReactInstance;
	}

	@Override
	public ReactView asView() {
		return this;
	}

	@Override
	public void destroy() {
		unmountReactApplication();
	}

	@Override
	public void sendComponentStart() {
        ReactContext currentReactContext = reactInstanceManager.getCurrentReactContext();
        if (currentReactContext != null) {
            new EventEmitter(currentReactContext).componentDidAppear(componentId, componentName);
        }
	}

	@Override
	public void sendComponentStop() {
        ReactContext currentReactContext = reactInstanceManager.getCurrentReactContext();
        if (currentReactContext != null) {
            new EventEmitter(currentReactContext).componentDidDisappear(componentId, componentName);
        }
	}

    @Override
	public void sendOnNavigationButtonPressed(String buttonId) {
        ReactContext currentReactContext = reactInstanceManager.getCurrentReactContext();
        if (currentReactContext != null) {
            new EventEmitter(currentReactContext).emitOnNavigationButtonPressed(componentId, buttonId);
        }
	}

    @Override
    public ScrollEventListener getScrollEventListener() {
        return new ScrollEventListener(getEventDispatcher());
    }

    @Override
    public void dispatchTouchEventToJs(MotionEvent event) {
        jsTouchDispatcher.handleTouchEvent(event, getEventDispatcher());
    }

    @Override
    public boolean isRendered() {
        return getChildCount() >= 1;
    }

    public EventDispatcher getEventDispatcher() {
        ReactContext reactContext = reactInstanceManager.getCurrentReactContext();
        return reactContext == null ? null : reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public String getComponentName() {
        return componentName;
    }

    public void registerElement(Element element) {
        elements.add(element);
    }

    public void unregisterElement(Element element) {
        elements.remove(element);
    }

    @Override
    public List<Element> getElements() {
        return elements;
    }
}

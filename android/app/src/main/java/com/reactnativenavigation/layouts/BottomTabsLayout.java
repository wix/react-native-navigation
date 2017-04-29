package com.reactnativenavigation.layouts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.events.EventBus;
import com.reactnativenavigation.events.ScreenChangedEvent;
import com.reactnativenavigation.params.ActivityParams;
import com.reactnativenavigation.params.ContextualMenuParams;
import com.reactnativenavigation.params.FabParams;
import com.reactnativenavigation.params.LightBoxParams;
import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.params.SideMenuParams;
import com.reactnativenavigation.params.SlidingOverlayParams;
import com.reactnativenavigation.params.SnackbarParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.screens.ScreenStack;
import com.reactnativenavigation.views.BottomTabs;
import com.reactnativenavigation.views.LightBox;
import com.reactnativenavigation.views.SideMenu;
import com.reactnativenavigation.views.SideMenu.Side;
import com.reactnativenavigation.views.SnackbarAndFabContainer;
import com.reactnativenavigation.views.slidingOverlay.SlidingOverlay;
import com.reactnativenavigation.views.slidingOverlay.SlidingOverlaysQueue;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class BottomTabsLayout extends BaseLayout implements AHBottomNavigation.OnTabSelectedListener {

    private ActivityParams params;
    private SnackbarAndFabContainer snackbarAndFabContainer;
    private BottomTabs bottomTabs;
    private ScreenStack[] screenStacks;
    private final SideMenuParams leftSideMenuParams;
    private final SideMenuParams rightSideMenuParams;
    private final SlidingOverlaysQueue slidingOverlaysQueue = new SlidingOverlaysQueue();
    private @Nullable SideMenu sideMenu;
    private int currentStackIndex = 0;
    private LightBox lightBox;

    public BottomTabsLayout(AppCompatActivity activity, ActivityParams params) {
        super(activity);
        this.params = params;
        leftSideMenuParams = params.leftSideMenuParams;
        rightSideMenuParams = params.rightSideMenuParams;
        screenStacks = new ScreenStack[params.tabParams.size()];
        createLayout();
    }

    private void createLayout() {
        createSideMenu();
        createBottomTabs();
        addBottomTabs();
        addScreenStacks();
        createSnackbarContainer();
        showInitialScreenStack();
    }

    private void createSideMenu() {
        if (leftSideMenuParams == null && rightSideMenuParams == null) {
            return;
        }
        sideMenu = new SideMenu(getContext(), leftSideMenuParams, rightSideMenuParams);
        RelativeLayout.LayoutParams lp = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        addView(sideMenu, lp);
    }

    private void addScreenStacks() {
        for (int i = screenStacks.length - 1; i >= 0; i--) {
            createAndAddScreens(i);
        }
    }

    private void createAndAddScreens(int position) {
        ScreenParams screenParams = params.tabParams.get(position);
        ScreenStack newStack = new ScreenStack(getActivity(), getScreenStackParent(), screenParams.getNavigatorId(), this);
        newStack.pushInitialScreen(screenParams, createScreenLayoutParams(screenParams));
        screenStacks[position] = newStack;
    }

    private RelativeLayout getScreenStackParent() {
        return sideMenu == null ? this : sideMenu.getContentContainer();
    }

    @NonNull
    private LayoutParams createScreenLayoutParams(ScreenParams params) {
        LayoutParams lp = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        if (params.styleParams.drawScreenAboveBottomTabs) {
            lp.addRule(RelativeLayout.ABOVE, bottomTabs.getId());
        }
        return lp;
    }

    private void createBottomTabs() {
        bottomTabs = new BottomTabs(getContext());
        bottomTabs.addTabs(params.tabParams, this);
    }

    private void addBottomTabs() {
        LayoutParams lp = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        lp.addRule(ALIGN_PARENT_BOTTOM);
        getScreenStackParent().addView(bottomTabs, lp);
    }

    private void createSnackbarContainer() {
        snackbarAndFabContainer = new SnackbarAndFabContainer(getContext(), this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        lp.addRule(ABOVE, bottomTabs.getId());
        snackbarAndFabContainer.setClickable(false);
        getScreenStackParent().addView(snackbarAndFabContainer, lp);
    }

    private void showInitialScreenStack() {
        showStackAndUpdateStyle(screenStacks[0]);
        EventBus.instance.post(new ScreenChangedEvent(screenStacks[0].peek().getScreenParams()));
    }

    @Override
    public View asView() {
        return this;
    }

    @Override
    public boolean onBackPressed() {
        if (getCurrentScreenStack().handleBackPressInJs()) {
            return true;
        }

        if (getCurrentScreenStack().canPop()) {
            getCurrentScreenStack().pop(true);
            setBottomTabsStyleFromCurrentScreen();
            EventBus.instance.post(new ScreenChangedEvent(getCurrentScreenStack().peek().getScreenParams()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setTopBarVisible(String screenInstanceId, boolean hidden, boolean animated) {
        for (int i = 0; i < bottomTabs.getItemsCount(); i++) {
            screenStacks[i].setScreenTopBarVisible(screenInstanceId, hidden, animated);
        }
    }

    public void setBottomTabsVisible(boolean hidden, boolean animated) {
        bottomTabs.setVisibility(hidden, animated);
    }

    @Override
    public void setTitleBarTitle(String screenInstanceId, String title) {
        for (int i = 0; i < bottomTabs.getItemsCount(); i++) {
            screenStacks[i].setScreenTitleBarTitle(screenInstanceId, title);
        }
    }

    @Override
    public void setTitleBarSubtitle(String screenInstanceId, String subtitle) {
        for (int i = 0; i < bottomTabs.getItemsCount(); i++) {
            screenStacks[i].setScreenTitleBarSubtitle(screenInstanceId, subtitle);
        }
    }

    @Override
    public void setTitleBarRightButtons(String screenInstanceId, String navigatorEventId, List<TitleBarButtonParams> titleBarButtons) {
        for (int i = 0; i < bottomTabs.getItemsCount(); i++) {
            screenStacks[i].setScreenTitleBarRightButtons(screenInstanceId, navigatorEventId, titleBarButtons);
        }
    }

    @Override
    public void setTitleBarLeftButton(String screenInstanceId, String navigatorEventId, TitleBarLeftButtonParams titleBarLeftButtonParams) {
        for (int i = 0; i < bottomTabs.getItemsCount(); i++) {
            screenStacks[i].setScreenTitleBarLeftButton(screenInstanceId, navigatorEventId, titleBarLeftButtonParams);
        }
    }

    @Override
    public void setFab(String screenInstanceId, String navigatorEventId, FabParams fabParams) {
        for (int i = 0; i < bottomTabs.getItemsCount(); i++) {
            screenStacks[i].setFab(screenInstanceId, fabParams);
        }
    }

    @Override
    public void updateScreenStyle(String screenInstanceId, Bundle styleParams) {
        for (int i = 0; i < bottomTabs.getItemsCount(); i++) {
            screenStacks[i].updateScreenStyle(screenInstanceId, styleParams);
        }
    }

    @Override
    public void selectTopTabByTabIndex(String screenInstanceId, int index) {
        for (int i = 0; i < bottomTabs.getItemsCount(); i++) {
            screenStacks[i].selectTopTabByTabIndex(screenInstanceId, index);
        }
    }

    @Override
    public void selectTopTabByScreen(String screenInstanceId) {
        for (int i = 0; i < bottomTabs.getItemsCount(); i++) {
            screenStacks[i].selectTopTabByScreen(screenInstanceId);
        }
    }

    @Override
    public void toggleSideMenuVisible(boolean animated, Side side) {
        if (sideMenu != null) {
            sideMenu.toggleVisible(animated, side);
        }
    }

    @Override
    public void setSideMenuVisible(boolean animated, boolean visible, Side side) {
        if (sideMenu != null) {
            sideMenu.setVisible(visible, animated, side);
        }
    }

    @Override
    public void showSnackbar(SnackbarParams params) {
        final String eventId = getCurrentScreenStack().peek().getNavigatorEventId();
        snackbarAndFabContainer.showSnackbar(eventId, params);
    }

    @Override
    public void dismissSnackbar() {
        snackbarAndFabContainer.dismissSnackbar();
    }

    @Override
    public void showLightBox(LightBoxParams params) {
        if (lightBox == null) {
            lightBox = new LightBox(getActivity(), new Runnable() {
                @Override
                public void run() {
                    lightBox = null;
                }
            }, params);
            lightBox.show();
        }
    }

    @Override
    public void dismissLightBox() {
        if (lightBox != null) {
            lightBox.hide();
            lightBox = null;
        }
    }

    @Override
    public void showSlidingOverlay(final SlidingOverlayParams params) {
        slidingOverlaysQueue.add(new SlidingOverlay(this, params));
    }

    @Override
    public void hideSlidingOverlay() {
        slidingOverlaysQueue.remove();
    }

    @Override
    public void onModalDismissed() {
        EventBus.instance.post(new ScreenChangedEvent(getCurrentScreenStack().peek().getScreenParams()));
    }

    @Override
    public boolean containsNavigator(String navigatorId) {
        // Unused
        return false;
    }

    @Override
    public void showContextualMenu(String screenInstanceId, ContextualMenuParams params, Callback onButtonClicked) {
        getCurrentScreenStack().peek().showContextualMenu(params, onButtonClicked);
    }

    @Override
    public void dismissContextualMenu(String screenInstanceId) {
        getCurrentScreenStack().peek().dismissContextualMenu();
    }

    @Override
    public Screen getCurrentScreen() {
        return getCurrentScreenStack().peek();
    }

    public void selectBottomTabByTabIndex(Integer index) {
        bottomTabs.setCurrentItem(index);
    }

    public void selectBottomTabByNavigatorId(String navigatorId) {
        bottomTabs.setCurrentItem(getScreenStackIndex(navigatorId));
    }

    @Override
    public void push(ScreenParams params) {
        ScreenStack screenStack = getScreenStack(params.getNavigatorId());
        screenStack.push(params, createScreenLayoutParams(params));
        if (isCurrentStack(screenStack)) {
            bottomTabs.setStyleFromScreen(params.styleParams);
            EventBus.instance.post(new ScreenChangedEvent(params));
        }
    }

    @Override
    public void pop(ScreenParams params) {
        getCurrentScreenStack().pop(params.animateScreenTransitions, new ScreenStack.OnScreenPop() {
            @Override
            public void onScreenPopAnimationEnd() {
                setBottomTabsStyleFromCurrentScreen();
                EventBus.instance.post(new ScreenChangedEvent(getCurrentScreenStack().peek().getScreenParams()));
            }
        });
    }

    @Override
    public void popToRoot(ScreenParams params) {
        getCurrentScreenStack().popToRoot(params.animateScreenTransitions, new ScreenStack.OnScreenPop() {
            @Override
            public void onScreenPopAnimationEnd() {
                setBottomTabsStyleFromCurrentScreen();
                EventBus.instance.post(new ScreenChangedEvent(getCurrentScreenStack().peek().getScreenParams()));
            }
        });
    }

    @Override
    public void newStack(final ScreenParams params) {
        ScreenStack screenStack = getScreenStack(params.getNavigatorId());
        screenStack.newStack(params, createScreenLayoutParams(params));
        if (isCurrentStack(screenStack)) {
            bottomTabs.setStyleFromScreen(params.styleParams);
            EventBus.instance.post(new ScreenChangedEvent(params));
        }
    }

    @Override
    public void destroy() {
        snackbarAndFabContainer.destroy();
        for (ScreenStack screenStack : screenStacks) {
            screenStack.destroy();
        }
        if (sideMenu != null) {
            sideMenu.destroy();
        }
        if (lightBox != null) {
            lightBox.destroy();
            lightBox = null;
        }
        slidingOverlaysQueue.destroy();
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        if (wasSelected) {
            sendTabReselectedEventToJs();
            return false;
        }

        final int unselectedTabIndex = currentStackIndex;
        hideCurrentStack();
        showNewStack(position);
        EventBus.instance.post(new ScreenChangedEvent(getCurrentScreenStack().peek().getScreenParams()));
        sendTabSelectedEventToJs(position, unselectedTabIndex);
        return true;
    }

    private void sendTabSelectedEventToJs(int selectedTabIndex, int unselectedTabIndex) {
        String navigatorEventId = getCurrentScreenStack().peek().getNavigatorEventId();
        WritableMap data = createTabSelectedEventData(selectedTabIndex, unselectedTabIndex);
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("bottomTabSelected", navigatorEventId, data);

        data = createTabSelectedEventData(selectedTabIndex, unselectedTabIndex);
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("bottomTabSelected", data);
    }

    private WritableMap createTabSelectedEventData(int selectedTabIndex, int unselectedTabIndex) {
        WritableMap data = Arguments.createMap();
        data.putInt("selectedTabIndex", selectedTabIndex);
        data.putInt("unselectedTabIndex", unselectedTabIndex);
        return data;
    }

    private void sendTabReselectedEventToJs() {
        WritableMap data = Arguments.createMap();
        String navigatorEventId = getCurrentScreenStack().peek().getNavigatorEventId();
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("bottomTabReselected", navigatorEventId, data);
    }

    private void showNewStack(int position) {
        showStackAndUpdateStyle(screenStacks[position]);
        currentStackIndex = position;
    }

    private void showStackAndUpdateStyle(ScreenStack newStack) {
        newStack.show();
        bottomTabs.setStyleFromScreen(newStack.getCurrentScreenStyleParams());
    }

    private void hideCurrentStack() {
        ScreenStack currentScreenStack = getCurrentScreenStack();
        currentScreenStack.hide();
    }

    private ScreenStack getCurrentScreenStack() {
        return screenStacks[currentStackIndex];
    }

    private @NonNull ScreenStack getScreenStack(String navigatorId) {
        int index = getScreenStackIndex(navigatorId);
        return screenStacks[index];
    }

    public void setBottomTabBadgeByIndex(Integer index, String badge) {
        bottomTabs.setNotification(badge, index);
    }

    public void setBottomTabBadgeByNavigatorId(String navigatorId, String badge) {
        bottomTabs.setNotification(badge, getScreenStackIndex(navigatorId));
    }

    private int getScreenStackIndex(String navigatorId) throws ScreenStackNotFoundException {
        for (int i = 0; i < screenStacks.length; i++) {
            if (screenStacks[i].getNavigatorId().equals(navigatorId)) {
                return i;
            }
        }
        throw new ScreenStackNotFoundException("Stack " + navigatorId + " not found");
    }

    private class ScreenStackNotFoundException extends RuntimeException {
        ScreenStackNotFoundException(String navigatorId) {
            super(navigatorId);
        }
    }

    private boolean isCurrentStack(ScreenStack screenStack) {
        return getCurrentScreenStack() == screenStack;
    }

    private void setBottomTabsStyleFromCurrentScreen() {
        bottomTabs.setStyleFromScreen(getCurrentScreenStack().getCurrentScreenStyleParams());
    }

    @Override
    public boolean onTitleBarBackButtonClick() {
        if (getCurrentScreenStack().canPop()) {
            getCurrentScreenStack().pop(true, new ScreenStack.OnScreenPop() {
                @Override
                public void onScreenPopAnimationEnd() {
                    setBottomTabsStyleFromCurrentScreen();
                    EventBus.instance.post(new ScreenChangedEvent(getCurrentScreenStack().peek().getScreenParams()));
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public void onSideMenuButtonClick() {
        final String navigatorEventId = getCurrentScreenStack().peek().getNavigatorEventId();
        NavigationApplication.instance.getEventEmitter().sendNavigatorEvent("sideMenu", navigatorEventId);
        if (sideMenu != null) {
            sideMenu.openDrawer(Side.Left);
        }
    }
}
